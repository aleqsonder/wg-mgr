package wg.mgr.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wg.mgr.backend.dto.ContactRequest;
import wg.mgr.backend.dto.UserWithContactsRequest;
import wg.mgr.backend.dto.UserWithContactsResponse;
import wg.mgr.backend.exception.ConflictVpnException;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.model.ContactType;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.model.VpnUserContact;
import wg.mgr.backend.repository.ContactTypeRepository;
import wg.mgr.backend.repository.VpnUserContactRepository;
import wg.mgr.backend.repository.VpnUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VpnUserServiceTest {

    @Mock
    private VpnUserRepository vpnUserRepository;

    @Mock
    private ContactTypeRepository contactTypeRepository;

    @Mock
    private VpnUserContactRepository vpnUserContactRepository;

    @InjectMocks
    private VpnUserService vpnUserService;

    private VpnUser sampleUser;
    private ContactType sampleContactType;

    @BeforeEach
    void setUp() {
        sampleUser = new VpnUser();
        sampleUser.setId(1L);
        sampleUser.setUsername("alice");
        sampleUser.setVpnUserContacts(new ArrayList<>());

        sampleContactType = new ContactType();
        sampleContactType.setId(1L);
        sampleContactType.setTypename("telegram");
    }

    // ─────────────────────────────────────────────
    // add()
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("add() — успешное создание пользователя без контактов")
    void add_success_noContacts() {
        UserWithContactsRequest request = new UserWithContactsRequest("alice", List.of());

        when(vpnUserRepository.existsByUsername("alice")).thenReturn(false);
        when(vpnUserRepository.save(any(VpnUser.class))).thenReturn(sampleUser);

        UserWithContactsResponse response = vpnUserService.add(request);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("alice");
        assertThat(response.contacts()).isEmpty();
        verify(vpnUserRepository).save(any(VpnUser.class));
    }

    @Test
    @DisplayName("add() — успешное создание пользователя с контактами")
    void add_success_withContacts() {
        ContactRequest contactRequest = new ContactRequest("telegram", "@alice");
        UserWithContactsRequest request = new UserWithContactsRequest("alice", List.of(contactRequest));

        VpnUserContact savedContact = new VpnUserContact();
        savedContact.setContactType(sampleContactType);
        savedContact.setContent("@alice");

        when(vpnUserRepository.existsByUsername("alice")).thenReturn(false);
        when(vpnUserRepository.save(any(VpnUser.class))).thenReturn(sampleUser);
        when(contactTypeRepository.findByTypename("telegram")).thenReturn(Optional.of(sampleContactType));
        when(vpnUserContactRepository.save(any(VpnUserContact.class))).thenReturn(savedContact);

        UserWithContactsResponse response = vpnUserService.add(request);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("alice");
        verify(vpnUserContactRepository).save(any(VpnUserContact.class));
    }

    @Test
    @DisplayName("add() — ConflictVpnException при дублировании username")
    void add_throwsConflict_whenUsernameExists() {
        UserWithContactsRequest request = new UserWithContactsRequest("alice", List.of());
        when(vpnUserRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> vpnUserService.add(request))
                .isInstanceOf(ConflictVpnException.class)
                .hasMessageContaining("alice");

        verify(vpnUserRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // edit()
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("edit() — успешное переименование пользователя")
    void edit_success() {
        UserWithContactsRequest update = new UserWithContactsRequest("bob", List.of());
        VpnUser updatedUser = new VpnUser();
        updatedUser.setId(1L);
        updatedUser.setUsername("bob");
        updatedUser.setVpnUserContacts(new ArrayList<>());

        when(vpnUserRepository.existsByUsername("bob")).thenReturn(false);
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(vpnUserRepository.save(any(VpnUser.class))).thenReturn(updatedUser);

        UserWithContactsResponse response = vpnUserService.edit(1L, update);

        assertThat(response.username()).isEqualTo("bob");
        verify(vpnUserRepository).save(sampleUser);
    }

    @Test
    @DisplayName("edit() — ConflictVpnException при занятом новом username")
    void edit_throwsConflict_whenNewUsernameExists() {
        UserWithContactsRequest update = new UserWithContactsRequest("bob", List.of());
        when(vpnUserRepository.existsByUsername("bob")).thenReturn(true);

        assertThatThrownBy(() -> vpnUserService.edit(1L, update))
                .isInstanceOf(ConflictVpnException.class);

        verify(vpnUserRepository, never()).findById(any());
    }

    @Test
    @DisplayName("edit() — NotFoundVpnException когда пользователь не существует")
    void edit_throwsNotFound_whenUserMissing() {
        UserWithContactsRequest update = new UserWithContactsRequest("bob", List.of());
        when(vpnUserRepository.existsByUsername("bob")).thenReturn(false);
        when(vpnUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vpnUserService.edit(99L, update))
                .isInstanceOf(NotFoundVpnException.class)
                .hasMessageContaining("99");
    }

    // ─────────────────────────────────────────────
    // getById()
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getById() — успешное получение пользователя")
    void getById_success() {
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserWithContactsResponse response = vpnUserService.getById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("alice");
    }

    @Test
    @DisplayName("getById() — NotFoundVpnException при отсутствующем ID")
    void getById_throwsNotFound_whenMissing() {
        when(vpnUserRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vpnUserService.getById(42L))
                .isInstanceOf(NotFoundVpnException.class)
                .hasMessageContaining("42");
    }

    // ─────────────────────────────────────────────
    // getAll()
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAll() — возвращает список всех пользователей")
    void getAll_returnsAllUsers() {
        VpnUser second = new VpnUser();
        second.setId(2L);
        second.setUsername("bob");
        second.setVpnUserContacts(new ArrayList<>());

        when(vpnUserRepository.findAll()).thenReturn(List.of(sampleUser, second));

        List<UserWithContactsResponse> result = vpnUserService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserWithContactsResponse::username)
                .containsExactlyInAnyOrder("alice", "bob");
    }

    @Test
    @DisplayName("getAll() — возвращает пустой список если пользователей нет")
    void getAll_returnsEmptyList_whenNoUsers() {
        when(vpnUserRepository.findAll()).thenReturn(List.of());

        List<UserWithContactsResponse> result = vpnUserService.getAll();

        assertThat(result).isEmpty();
    }

    // ─────────────────────────────────────────────
    // delete()
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("delete() — успешное удаление")
    void delete_success() {
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        doNothing().when(vpnUserRepository).delete(sampleUser);

        assertThatCode(() -> vpnUserService.delete(1L)).doesNotThrowAnyException();

        verify(vpnUserRepository).delete(sampleUser);
    }

    @Test
    @DisplayName("delete() — NotFoundVpnException при отсутствующем пользователе")
    void delete_throwsNotFound_whenMissing() {
        when(vpnUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vpnUserService.delete(99L))
                .isInstanceOf(NotFoundVpnException.class)
                .hasMessageContaining("99");

        verify(vpnUserRepository, never()).delete(any());
    }
}

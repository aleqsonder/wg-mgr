package wg.mgr.backend.modular;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wg.mgr.backend.dto.ContactRequest;
import wg.mgr.backend.dto.UserWithContactsRequest;
import wg.mgr.backend.exception.ConflictVpnException;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.exception.ServerVpnException;
import wg.mgr.backend.model.ContactType;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.repository.ContactTypeRepository;
import wg.mgr.backend.repository.VpnUserContactRepository;
import wg.mgr.backend.repository.VpnUserRepository;
import wg.mgr.backend.service.VpnUserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VpnUserServiceTest {

    @Mock
    private VpnUserRepository vpnUserRepository;
    @Mock
    private ContactTypeRepository contactTypeRepository;
    @Mock
    private VpnUserContactRepository vpnUserContactRepository;

    @InjectMocks
    private VpnUserService vpnUserService;

    // ---------- add() ----------

    @Test
    void add_shouldThrowConflict_whenUserAlreadyExists() {
        UserWithContactsRequest req = new UserWithContactsRequest("john", List.of());
        when(vpnUserRepository.existsByUsername("john")).thenReturn(true);
        assertThrows(ConflictVpnException.class, () -> vpnUserService.add(req));
    }

    @Test
    void add_shouldThrowServerError_whenContactTypeNotFound() {
        UserWithContactsRequest req = new UserWithContactsRequest(
                "john",
                List.of(new ContactRequest("EMAIL22", "test@mail.com"))
        );
        when(vpnUserRepository.existsByUsername("john")).thenReturn(false);
        VpnUser vpnUser = new VpnUser();
        vpnUser.setId(1L);
        vpnUser.setUsername("john");
        when(vpnUserRepository.save(any())).thenReturn(vpnUser);
        when(contactTypeRepository.findByTypename("EMAIL22")).thenReturn(Optional.empty());
        assertThrows(ServerVpnException.class, () -> vpnUserService.add(req));
    }

    @Test
    void add_shouldCreateUserWithContacts() {
        UserWithContactsRequest req = new UserWithContactsRequest(
                "john",
                List.of(new ContactRequest("EMAIL", "test@mail.com"))
        );
        VpnUser savedUser = new VpnUser();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        ContactType emailType = new ContactType();
        emailType.setId(10L);
        emailType.setTypename("EMAIL");

        when(vpnUserRepository.existsByUsername("john")).thenReturn(false);
        when(vpnUserRepository.save(any())).thenReturn(savedUser);
        when(contactTypeRepository.findByTypename("EMAIL")).thenReturn(Optional.of(emailType));
        when(vpnUserContactRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = vpnUserService.add(req);

        assertEquals(1L, result.id());
        assertEquals("john", result.username());
        assertEquals(1, result.contacts().size());
        assertEquals("EMAIL", result.contacts().getFirst().contactType());
        assertEquals("test@mail.com", result.contacts().getFirst().content());
    }

    // ---------- edit() ----------

    @Test
    void edit_shouldThrowConflict_whenUsernameAlreadyExists() {
        UserWithContactsRequest update = new UserWithContactsRequest(
                "john",
                List.of(new ContactRequest("EMAIL", "test@mail.com"))
        );
        when(vpnUserRepository.existsByUsername("john")).thenReturn(true);
        assertThrows(ConflictVpnException.class, () -> vpnUserService.edit(1L, update));
    }

    @Test
    void edit_shouldThrowNotFound_whenUserDoesNotExist() {
        UserWithContactsRequest update = new UserWithContactsRequest(
                "newName",
                List.of()
        );

        when(vpnUserRepository.existsByUsername("newName")).thenReturn(false);
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundVpnException.class, () -> vpnUserService.edit(1L, update));
    }

    @Test
    void edit_shouldUpdateUser() {
        VpnUser oldUser = new VpnUser();
        oldUser.setId(1L);
        oldUser.setUsername("oldName");
        UserWithContactsRequest update = new UserWithContactsRequest(
                "newName",
                List.of()
        );

        when(vpnUserRepository.existsByUsername("newName")).thenReturn(false);
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.of(oldUser));
        when(vpnUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = vpnUserService.edit(1L, update);
        assertEquals(update.username(), result.username());
        assertEquals(oldUser.getId(), result.id());
    }

    // ---------- getById() ----------

    @Test
    void getById_shouldThrowNotFound() {
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundVpnException.class, () -> vpnUserService.getById(1L));
    }

    @Test
    void getById_shouldReturnUser() {
        VpnUser existingUser = new VpnUser();
        existingUser.setId(1L);
        existingUser.setUsername("existingName");

        when(vpnUserRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        var result =  vpnUserService.getById(1L);

        assertEquals(existingUser.getId(), result.id());
        assertEquals("existingName", result.username());
    }

    // ---------- delete() ----------

    @Test
    void delete_shouldRemoveUser() {
        VpnUser user = new VpnUser();
        user.setId(1L);
        user.setUsername("john");

        when(vpnUserRepository.findById(1L)).thenReturn(Optional.of(user));

        vpnUserService.delete(1L);

        verify(vpnUserRepository).delete(user);
    }

    @Test
    void delete_shouldThrowNotFound() {
        when(vpnUserRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundVpnException.class, () -> vpnUserService.delete(1L));
    }


}

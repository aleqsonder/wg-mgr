package wg.mgr.backend.modular;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.repository.ContactTypeRepository;
import wg.mgr.backend.repository.VpnUserContactRepository;
import wg.mgr.backend.repository.VpnUserRepository;
import wg.mgr.backend.service.VpnUserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

package wg.mgr.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wg.mgr.backend.exception.ConflictVpnException;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.repository.VpnUserRepository;

@Slf4j
@Service
public class VpnUserService {

    private final VpnUserRepository vpnUserRepository;

    public VpnUserService(VpnUserRepository vpnUserRepository) {
        this.vpnUserRepository = vpnUserRepository;
    }

    public VpnUser add(VpnUser vpnUser) {
        if (vpnUserRepository.existsByUsername(vpnUser.getUsername())) {
            throw new ConflictVpnException("User with username " + vpnUser.getUsername() + " already exists");
        }
        return vpnUserRepository.save(vpnUser);
    }

    public VpnUser edit(Long vpnUserId, VpnUser update) {
        if (vpnUserRepository.existsByUsername(update.getUsername())) {
            throw new ConflictVpnException("User with username " + update.getUsername() + " already exists");
        }
        VpnUser userToUpdate = vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
        userToUpdate.setUsername(update.getUsername());
        return vpnUserRepository.save(userToUpdate);
    }

    public VpnUser getById(Long vpnUserId) {
        return vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
    }

    public void delete(Long vpnUserId) {
        VpnUser userToDelete = vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
        vpnUserRepository.delete(userToDelete);
    }
}

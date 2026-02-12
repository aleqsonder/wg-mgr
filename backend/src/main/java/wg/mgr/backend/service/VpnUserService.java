package wg.mgr.backend.service;

import org.springframework.stereotype.Service;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.repository.VpnUserRepository;

import java.util.Optional;

@Service
public class VpnUserService {

    private final VpnUserRepository vpnUserRepository;

    public VpnUserService(VpnUserRepository vpnUserRepository) {
        this.vpnUserRepository = vpnUserRepository;
    }

    public VpnUser add(VpnUser vpnUser) {
        // may be validation and some logic
        return vpnUserRepository.save(vpnUser);
    }

    public VpnUser edit(Long vpnUserId, VpnUser vpnUser) {
        // may be validation and some logic
        Optional<VpnUser> maybeUser = vpnUserRepository.findById(vpnUserId);
        if (maybeUser.isEmpty()) {
            // TODO custom exception later
            throw new RuntimeException("User with id " + vpnUserId + " not found");
        } else {
            VpnUser userToUpdate = maybeUser.get();
            userToUpdate.setUsername(vpnUser.getUsername());
            return vpnUserRepository.save(userToUpdate);
        }
    }

    public VpnUser getById(Long vpnUserId) {
        Optional<VpnUser> maybeUser = vpnUserRepository.findById(vpnUserId);
        if (maybeUser.isPresent()) {
            return maybeUser.get();
        } else {
            // TODO custom exception later
            throw new RuntimeException("User with id " + vpnUserId + " not found");
        }
    }

    public void delete(Long vpnUserId) {
        Optional<VpnUser> maybeUser = vpnUserRepository.findById(vpnUserId);
        if (maybeUser.isPresent()) {
            vpnUserRepository.deleteById(vpnUserId);
        } else {
            // TODO custom exception later
            throw new RuntimeException("User with id " + vpnUserId + " not found");
        }
    }
}

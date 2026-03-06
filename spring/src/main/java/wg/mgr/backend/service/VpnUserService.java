package wg.mgr.backend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wg.mgr.backend.dto.ContactRequest;
import wg.mgr.backend.dto.ContactResponse;
import wg.mgr.backend.dto.UserWithContactsRequest;
import wg.mgr.backend.dto.UserWithContactsResponse;
import wg.mgr.backend.exception.ConflictVpnException;
import wg.mgr.backend.exception.NotFoundVpnException;
import wg.mgr.backend.exception.ServerVpnException;
import wg.mgr.backend.model.ContactType;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.model.VpnUserContact;
import wg.mgr.backend.model.VpnUserContactId;
import wg.mgr.backend.repository.ContactTypeRepository;
import wg.mgr.backend.repository.VpnUserContactRepository;
import wg.mgr.backend.repository.VpnUserRepository;

import java.util.List;

@Slf4j
@Service
public class VpnUserService {

    private final VpnUserRepository vpnUserRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final VpnUserContactRepository vpnUserContactRepository;

    public VpnUserService(
            VpnUserRepository vpnUserRepository,
            ContactTypeRepository contactTypeRepository,
            VpnUserContactRepository vpnUserContactRepository
    ) {
        this.vpnUserRepository = vpnUserRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.vpnUserContactRepository = vpnUserContactRepository;
    }

    private UserWithContactsResponse vpnUserToResponse(VpnUser vpnUser) {
        return new UserWithContactsResponse(
                vpnUser.getId(),
                vpnUser.getUsername(),
                vpnUser.getVpnUserContacts().stream().map(vpnUserContact ->
                        new ContactResponse(
                                vpnUserContact.getContactType().getTypename(),
                                vpnUserContact.getContent())
                ).toList()
        );
    }

    @Transactional
    public UserWithContactsResponse add(UserWithContactsRequest userWithContacts) {

        if (vpnUserRepository.existsByUsername(userWithContacts.username())) {
            throw new ConflictVpnException("User with username " + userWithContacts.username() + " already exists");
        }
        VpnUser vpnUser = new VpnUser();
        vpnUser.setUsername(userWithContacts.username());
        vpnUser = vpnUserRepository.save(vpnUser);

        for (ContactRequest contactRequest : userWithContacts.contacts()) {
            ContactType contactType = contactTypeRepository.findByTypename(contactRequest.contactType())
                    .orElseThrow(() -> new ServerVpnException("This type of contact no longer exists."));

            VpnUserContact vpnUserContact = new VpnUserContact();
            vpnUserContact.setVpnUser(vpnUser);
            vpnUserContact.setContactType(contactType);
            vpnUserContact.setContent(contactRequest.content());

            VpnUserContactId vpnUserContactId = new VpnUserContactId();
            vpnUserContactId.setVpnUserId(vpnUser.getId());
            vpnUserContactId.setContactTypeId(contactType.getId());

            vpnUserContact.setId(vpnUserContactId);
            vpnUserContact = vpnUserContactRepository.save(vpnUserContact);
            vpnUser.getVpnUserContacts().add(vpnUserContact);
        }
        return vpnUserToResponse(vpnUser);
    }

    public UserWithContactsResponse edit(Long vpnUserId, UserWithContactsRequest update) {
        if (vpnUserRepository.existsByUsername(update.username())) {
            throw new ConflictVpnException("User with username " + update.username() + " already exists");
        }
        VpnUser userToUpdate = vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
        userToUpdate.setUsername(update.username());
        VpnUser updated =  vpnUserRepository.save(userToUpdate);
        return vpnUserToResponse(updated);
    }

    public UserWithContactsResponse getById(Long vpnUserId) {
        VpnUser foundUser = vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
        return vpnUserToResponse(foundUser);
    }

    public List<UserWithContactsResponse> getAll() {
        List<VpnUser> vpnUsers = vpnUserRepository.findAll();
        return vpnUsers.stream().map(this::vpnUserToResponse).toList();
    }

    public void delete(Long vpnUserId) {
        VpnUser userToDelete = vpnUserRepository.findById(vpnUserId).orElseThrow(
                () -> new NotFoundVpnException("User with id " + vpnUserId + " not found")
        );
        vpnUserRepository.delete(userToDelete);
    }
}

package wg.mgr.backend.service;

import org.springframework.stereotype.Service;
import wg.mgr.backend.model.Client;
import wg.mgr.backend.repository.UserRepository;

@Service
public class UserService {

    // we but all business logic for client here
    // methods of Service can be called fom other services or from Controllers

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Client add(Client client) {
        // may be validation and some logic
        return userRepository.save(client);
    }

    public Client edit(Long id, Client client) {
        // may be validation and some logic
        // update logic
        return userRepository.save(client);
    }

    public Client getById(Long clientId) {
        // may be validation and some logic
        // add isPresent check
        return userRepository.findById(clientId).get();
    }

    public void delete(Long clientId) {
        // may be validation and some logic
        userRepository.deleteById(clientId);
    }

}

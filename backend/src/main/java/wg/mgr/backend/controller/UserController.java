package wg.mgr.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wg.mgr.backend.exception.RequestMessage;
import wg.mgr.backend.model.Client;
import wg.mgr.backend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    // all api
    // auto exception-handler can be included for this class (use ControllerAdvice)

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Client> addUser(@RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(client));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Client> addUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Client> editUser(@PathVariable Long userId,  @RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.edit(userId, client));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<RequestMessage> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new RequestMessage(
                204,
                "Successfully deleted user with id: " + userId
        ));
    }

}

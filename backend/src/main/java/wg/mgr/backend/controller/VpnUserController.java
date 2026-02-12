package wg.mgr.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wg.mgr.backend.exception.ResponseMessage;
import wg.mgr.backend.model.VpnUser;
import wg.mgr.backend.service.VpnUserService;

@RestController
@RequestMapping("/users")
public class VpnUserController {

    private final VpnUserService vpnUserService;

    public VpnUserController(VpnUserService vpnUserService) {
        this.vpnUserService = vpnUserService;
    }

    @PostMapping
    public ResponseEntity<VpnUser> addVpnUser(@RequestBody VpnUser vpnUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vpnUserService.add(vpnUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<VpnUser> addVpnUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(vpnUserService.getById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<VpnUser> editUser(@PathVariable Long userId,  @RequestBody VpnUser vpnUser) {
        return ResponseEntity.status(HttpStatus.OK).body(vpnUserService.edit(userId, vpnUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable Long userId) {
        vpnUserService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseMessage(
                204,
                "Successfully deleted user with id: " + userId
        ));
    }

}

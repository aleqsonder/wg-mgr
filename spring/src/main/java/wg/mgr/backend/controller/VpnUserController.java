package wg.mgr.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wg.mgr.backend.dto.UserWithContactsRequest;
import wg.mgr.backend.dto.UserWithContactsResponse;
import wg.mgr.backend.service.VpnUserService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class VpnUserController {

    private final VpnUserService vpnUserService;

    public VpnUserController(VpnUserService vpnUserService) {
        this.vpnUserService = vpnUserService;
    }

    @PostMapping
    public ResponseEntity<UserWithContactsResponse> addVpnUser(
            @RequestBody
            @Valid
            UserWithContactsRequest vpnUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vpnUserService.add(vpnUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserWithContactsResponse> getVpnUserById(
            @PathVariable
            @Min(value = 0, message = "userId must be >= 0")
            Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(vpnUserService.getById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserWithContactsResponse>> getVpnUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(vpnUserService.getAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserWithContactsResponse> editUser(
            @PathVariable
            @Min(value = 0, message = "userId must be >= 0")
            Long userId,
            @RequestBody
            @Valid
            UserWithContactsRequest vpnUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(vpnUserService.edit(userId, vpnUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable
            @Min(value = 0, message = "userId must be >= 0")
            Long userId
    ) {
        vpnUserService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}

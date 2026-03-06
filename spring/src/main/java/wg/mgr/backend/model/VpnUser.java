package wg.mgr.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "vpn_users")
public class VpnUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "vpnUser")
    private Set<Device> devices = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vpnUser")
    private Set<VpnUserContact> vpnUserContacts = new LinkedHashSet<>();


}
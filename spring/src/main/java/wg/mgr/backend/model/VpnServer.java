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
@Table(name = "vpn_servers")
public class VpnServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_name", nullable = false, length = 32)
    private String serverName;

    @Column(name = "ipv4", nullable = false, length = 15)
    private String ipv4;

    @JsonIgnore
    @OneToMany(mappedBy = "vpnServer")
    private Set<Device> devices = new LinkedHashSet<>();


}
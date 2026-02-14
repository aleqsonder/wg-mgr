package wg.mgr.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vpn_server_id", nullable = false)
    private VpnServer vpnServer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vpn_user_id", nullable = false)
    private VpnUser vpnUser;

    @Column(name = "device_name", nullable = false, length = 64)
    private String deviceName;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @Column(name = "ipv4", nullable = false, length = 15)
    private String ipv4;

    @Column(name = "private_key", nullable = false, length = Integer.MAX_VALUE)
    private String privateKey;

    @Column(name = "public_key", nullable = false, length = Integer.MAX_VALUE)
    private String publicKey;

    @Column(name = "preshared_key", nullable = false, length = Integer.MAX_VALUE)
    private String presharedKey;


}
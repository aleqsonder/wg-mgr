package wg.mgr.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vpn_user_contacts")
public class VpnUserContact {
    @EmbeddedId
    private VpnUserContactId id;

    @MapsId("vpnUserId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vpn_user_id", nullable = false)
    private VpnUser vpnUser;

    @MapsId("contactTypeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactType contactType;

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;


}
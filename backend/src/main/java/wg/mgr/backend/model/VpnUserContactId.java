package wg.mgr.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class VpnUserContactId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3611598089320037380L;
    @Column(name = "vpn_user_id", nullable = false)
    private Integer vpnUserId;

    @Column(name = "contact_type_id", nullable = false)
    private Integer contactTypeId;


}
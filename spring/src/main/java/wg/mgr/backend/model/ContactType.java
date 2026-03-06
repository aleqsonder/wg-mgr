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
@Table(name = "contact_types")
public class ContactType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "typename", nullable = false, length = 32)
    private String typename;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @JsonIgnore
    @OneToMany(mappedBy = "contactType")
    private Set<VpnUserContact> vpnUserContacts = new LinkedHashSet<>();


}
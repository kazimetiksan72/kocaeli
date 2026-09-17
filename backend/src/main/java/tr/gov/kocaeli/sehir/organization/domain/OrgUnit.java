package tr.gov.kocaeli.sehir.organization.domain;
import jakarta.persistence.*; import lombok.Getter; import java.util.UUID;
@Entity @Table(name="org_unit") @Getter public class OrgUnit { @Id private UUID id; private String code; private String name; private String type; @Column(name="parent_id") private UUID parentId; private boolean active; @Version private long version; protected OrgUnit(){} }


package tr.gov.kocaeli.sehir.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.*;

@Entity @Table(name="app_user") @Getter
public class AppUser {
    @Id private UUID id;
    private String username;
    @Column(name="full_name") private String fullName;
    private String email;
    @Column(name="unit_id") private UUID unitId;
    private boolean active;
    @JdbcTypeCode(SqlTypes.ARRAY) @Column(columnDefinition="text[]") private String[] roles;
    @Version private long version;
    protected AppUser() {}
    public Set<String> roleSet() { return Set.copyOf(Arrays.asList(roles)); }
}


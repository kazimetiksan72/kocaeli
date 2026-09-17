package tr.gov.kocaeli.sehir.location.domain;
import jakarta.persistence.*;import lombok.Getter;import java.util.UUID;
@Entity @Getter public class Neighborhood{@Id private UUID id;@Column(name="district_id")private UUID districtId;private String code;private String name;protected Neighborhood(){}}


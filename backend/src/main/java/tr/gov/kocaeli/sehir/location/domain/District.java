package tr.gov.kocaeli.sehir.location.domain;
import jakarta.persistence.*;import lombok.Getter;import java.util.UUID;
@Entity @Getter public class District{@Id private UUID id;private String code;private String name;protected District(){}}


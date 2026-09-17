package tr.gov.kocaeli.sehir.request.domain;
import jakarta.persistence.*;import java.time.*;import java.util.UUID;
@Entity @Table(name="request_transition")public class RequestTransition{@Id private UUID id;@Column(name="request_id")private UUID requestId;@Column(name="from_status")private String fromStatus;@Column(name="to_status")private String toStatus;private String note;@Column(name="changed_by")private UUID changedBy;@Column(name="changed_at")private Instant changedAt;protected RequestTransition(){}public RequestTransition(UUID requestId,RequestStatus from,RequestStatus to,String note,UUID actor){this.id=UUID.randomUUID();this.requestId=requestId;this.fromStatus=from==null?null:from.name();this.toStatus=to.name();this.note=note;this.changedBy=actor;this.changedAt=Instant.now();}}


package tr.gov.kocaeli.sehir.request.domain;
import jakarta.persistence.*;import lombok.Getter;import org.locationtech.jts.geom.Geometry;import java.time.*;import java.util.UUID;
@Entity @Table(name="city_request") @Getter
public class CityRequest{
 @Id private UUID id;@Column(name="request_no")private String requestNo;private String title;private String description;@Column(name="request_type")private String requestType;private String channel;private String priority;
 @Column(name="applicant_name")private String applicantName;@Column(name="applicant_contact")private String applicantContact;@Column(name="responsible_unit_id")private UUID responsibleUnitId;@Column(name="assignee_id")private UUID assigneeId;
 @Column(name="district_id")private UUID districtId;@Column(name="neighborhood_id")private UUID neighborhoodId;private String address;@Column(columnDefinition="geometry(Geometry,4326)")private Geometry geometry;
 @Column(name="external_reference")private String externalReference;@Enumerated(EnumType.STRING)private RequestStatus status;@Column(name="created_by")private UUID createdBy;@Column(name="updated_by")private UUID updatedBy;
 @Column(name="created_at")private Instant createdAt;@Column(name="updated_at")private Instant updatedAt;@Column(name="deleted_at")private Instant deletedAt;@Version private long version;
 protected CityRequest(){}
 public static CityRequest create(String no,String title,String description,String type,String channel,String priority,String applicant,String contact,UUID unit,UUID district,UUID neighborhood,String address,Geometry geometry,String external,UUID actor){var r=new CityRequest();r.id=UUID.randomUUID();r.requestNo=no;r.title=title;r.description=description;r.requestType=type;r.channel=channel;r.priority=priority;r.applicantName=applicant;r.applicantContact=contact;r.responsibleUnitId=unit;r.districtId=district;r.neighborhoodId=neighborhood;r.address=address;r.geometry=geometry;r.externalReference=external;r.status=RequestStatus.NEW;r.createdBy=actor;r.updatedBy=actor;r.createdAt=Instant.now();r.updatedAt=r.createdAt;return r;}
 public RequestStatus transition(RequestStatus next,UUID actor){if(!status.canTransitionTo(next))throw new IllegalStateException(status+" durumundan "+next+" durumuna geçilemez");var old=status;status=next;updatedBy=actor;updatedAt=Instant.now();return old;}
 public void assign(UUID unit,UUID assignee,UUID actor){responsibleUnitId=unit;assigneeId=assignee;updatedBy=actor;updatedAt=Instant.now();if(status==RequestStatus.PRE_REVIEW)status=RequestStatus.ASSIGNED;}
 public void archive(){deletedAt=Instant.now();}public void restore(){deletedAt=null;}
}


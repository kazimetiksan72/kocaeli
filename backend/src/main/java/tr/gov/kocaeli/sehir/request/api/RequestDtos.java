package tr.gov.kocaeli.sehir.request.api;
import jakarta.validation.constraints.*;import tr.gov.kocaeli.sehir.request.domain.*;import java.time.*;import java.util.*;
public final class RequestDtos{private RequestDtos(){}
 public record Create(@NotBlank @Size(max=250)String title,@NotBlank String description,@NotBlank String requestType,@NotBlank String channel,@NotBlank String priority,String applicantName,String applicantContact,@NotNull UUID responsibleUnitId,@NotNull UUID districtId,@NotNull UUID neighborhoodId,@Size(max=500)String address,@NotBlank String geometryWkt,String externalReference){}
 public record Assign(@NotNull UUID unitId,UUID assigneeId){}
 public record Transition(@NotNull RequestStatus status,String note){}
 public record View(UUID id,String requestNo,String title,String description,String requestType,String channel,String priority,String applicantName,String applicantContact,UUID responsibleUnitId,UUID assigneeId,UUID districtId,UUID neighborhoodId,String address,String geometryWkt,String externalReference,RequestStatus status,Instant createdAt,Instant updatedAt,long version){}
 public record PageView<T>(List<T>content,int page,int size,long totalElements,int totalPages){}
}


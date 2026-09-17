package tr.gov.kocaeli.sehir.request.api;
import jakarta.validation.Valid;import org.springframework.data.domain.*;import org.springframework.data.web.PageableDefault;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import tr.gov.kocaeli.sehir.request.application.RequestService;import tr.gov.kocaeli.sehir.request.domain.RequestStatus;import java.net.URI;import java.util.*;
@RestController @RequestMapping("/api/v1/requests") public class RequestController{private final RequestService service;public RequestController(RequestService service){this.service=service;}
 @GetMapping RequestDtos.PageView<RequestDtos.View>list(@RequestParam(required=false)RequestStatus status,@RequestParam(required=false)String q,@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC)Pageable pageable){return service.list(status,q,pageable);}
 @GetMapping("/{id}")RequestDtos.View get(@PathVariable UUID id){return service.get(id);}
 @GetMapping("/{id}/duplicate-candidates")List<RequestDtos.View>duplicates(@PathVariable UUID id){return service.duplicates(id);}
 @PostMapping @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','REQUEST_OPERATOR')")ResponseEntity<RequestDtos.View>create(@Valid @RequestBody RequestDtos.Create body){var v=service.create(body);return ResponseEntity.created(URI.create("/api/v1/requests/"+v.id())).body(v);}
 @PostMapping("/{id}/assign")@PreAuthorize("hasAnyRole('SYSTEM_ADMIN','UNIT_MANAGER','REQUEST_OPERATOR')")RequestDtos.View assign(@PathVariable UUID id,@Valid @RequestBody RequestDtos.Assign body){return service.assign(id,body);}
 @PostMapping("/{id}/transitions")@PreAuthorize("hasAnyRole('SYSTEM_ADMIN','UNIT_MANAGER','REQUEST_OPERATOR','TECHNICAL_REVIEWER','APPROVER')")RequestDtos.View transition(@PathVariable UUID id,@Valid @RequestBody RequestDtos.Transition body){return service.transition(id,body);}
 @DeleteMapping("/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)@PreAuthorize("hasAnyRole('SYSTEM_ADMIN','UNIT_MANAGER')")void archive(@PathVariable UUID id){service.archive(id);}
}

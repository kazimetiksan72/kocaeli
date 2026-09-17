package tr.gov.kocaeli.sehir.organization.api;
import tr.gov.kocaeli.sehir.organization.domain.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/organization") public class OrganizationController {private final OrgUnitRepository units;public OrganizationController(OrgUnitRepository units){this.units=units;}@GetMapping("/tree")List<UnitView> tree(){return units.findAllByOrderByName().stream().map(u->new UnitView(u.getId(),u.getCode(),u.getName(),u.getType(),u.getParentId(),u.isActive())).toList();}public record UnitView(UUID id,String code,String name,String type,UUID parentId,boolean active){}}


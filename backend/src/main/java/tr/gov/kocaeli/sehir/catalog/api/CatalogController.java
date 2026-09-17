package tr.gov.kocaeli.sehir.catalog.api;
import tr.gov.kocaeli.sehir.catalog.domain.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/catalogs")public class CatalogController{private final CatalogRepository repo;public CatalogController(CatalogRepository repo){this.repo=repo;}@GetMapping("/{category}")List<View>list(@PathVariable String category){return repo.findByCategoryAndActiveTrueOrderBySortOrder(category.toUpperCase(Locale.ROOT)).stream().map(i->new View(i.getId(),i.getCode(),i.getName(),i.getDescription())).toList();}public record View(UUID id,String code,String name,String description){}}


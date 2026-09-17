package tr.gov.kocaeli.sehir.catalog.domain;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface CatalogRepository extends JpaRepository<CatalogItem,UUID>{List<CatalogItem>findByCategoryAndActiveTrueOrderBySortOrder(String category);}


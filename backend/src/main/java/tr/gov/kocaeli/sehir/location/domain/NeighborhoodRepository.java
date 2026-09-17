package tr.gov.kocaeli.sehir.location.domain;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface NeighborhoodRepository extends JpaRepository<Neighborhood,UUID>{List<Neighborhood>findByDistrictIdOrderByName(UUID districtId);}


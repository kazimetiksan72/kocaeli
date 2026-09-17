package tr.gov.kocaeli.sehir.location.domain;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface DistrictRepository extends JpaRepository<District,UUID>{List<District>findAllByOrderByName();}


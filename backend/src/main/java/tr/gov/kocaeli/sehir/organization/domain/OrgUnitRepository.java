package tr.gov.kocaeli.sehir.organization.domain;
import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
public interface OrgUnitRepository extends JpaRepository<OrgUnit,UUID>{List<OrgUnit> findAllByOrderByName(); @Query(value="select descendant_id from org_unit_closure where ancestor_id=:id",nativeQuery=true)Set<UUID> descendants(@Param("id")UUID id);}


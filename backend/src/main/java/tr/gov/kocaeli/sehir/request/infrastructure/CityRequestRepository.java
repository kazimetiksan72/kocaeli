package tr.gov.kocaeli.sehir.request.infrastructure;
import tr.gov.kocaeli.sehir.request.domain.*;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;import java.util.*;
public interface CityRequestRepository extends JpaRepository<CityRequest,UUID>{
 @Query("select r from CityRequest r where r.deletedAt is null and r.responsibleUnitId in :units and (:status is null or r.status=:status) and (:q is null or lower(r.title) like lower(concat('%',:q,'%')) or lower(r.requestNo) like lower(concat('%',:q,'%'))) ")Page<CityRequest>search(@Param("units")Set<UUID>units,@Param("status")RequestStatus status,@Param("q")String q,Pageable pageable);
 @Query("select r from CityRequest r where r.id=:id and r.deletedAt is null and r.responsibleUnitId in :units")Optional<CityRequest>findScoped(@Param("id")UUID id,@Param("units")Set<UUID>units);
 @Query(value="select * from city_request r where r.deleted_at is null and r.id<>:id and r.responsible_unit_id in (:units) and (ST_DWithin(r.geometry::geography,(select geometry::geography from city_request where id=:id),250) or r.neighborhood_id=(select neighborhood_id from city_request where id=:id) or similarity(r.title,(select title from city_request where id=:id))>0.35 or (r.external_reference is not null and r.external_reference=(select external_reference from city_request where id=:id))) order by r.created_at desc limit 10",nativeQuery=true)List<CityRequest>duplicates(@Param("id")UUID id,@Param("units")Set<UUID>units);
}


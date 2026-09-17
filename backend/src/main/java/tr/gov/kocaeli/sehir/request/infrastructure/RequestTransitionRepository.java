package tr.gov.kocaeli.sehir.request.infrastructure;
import tr.gov.kocaeli.sehir.request.domain.RequestTransition;import org.springframework.data.jpa.repository.JpaRepository;import java.util.UUID;
public interface RequestTransitionRepository extends JpaRepository<RequestTransition,UUID>{}


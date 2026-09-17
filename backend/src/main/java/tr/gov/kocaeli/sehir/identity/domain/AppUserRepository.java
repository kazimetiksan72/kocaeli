package tr.gov.kocaeli.sehir.identity.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AppUserRepository extends JpaRepository<AppUser, UUID> { Optional<AppUser> findByUsernameAndActiveTrue(String username); List<AppUser> findAllByActiveTrueOrderByFullName(); }


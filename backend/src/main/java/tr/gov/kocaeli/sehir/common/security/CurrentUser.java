package tr.gov.kocaeli.sehir.common.security;
import java.util.*;
public record CurrentUser(UUID id, String username, String fullName, UUID unitId, Set<String> roles) {
    public boolean isAdmin() { return roles.contains("SYSTEM_ADMIN"); }
}


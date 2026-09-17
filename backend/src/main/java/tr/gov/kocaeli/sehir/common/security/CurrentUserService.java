package tr.gov.kocaeli.sehir.common.security;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component
public class CurrentUserService {
    public CurrentUser require() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CurrentUser user)) throw new IllegalStateException("Oturum bulunamadı");
        return user;
    }
}


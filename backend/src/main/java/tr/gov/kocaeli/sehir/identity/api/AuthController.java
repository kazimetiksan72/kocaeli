package tr.gov.kocaeli.sehir.identity.api;
import tr.gov.kocaeli.sehir.common.security.*; import tr.gov.kocaeli.sehir.identity.domain.*; import jakarta.validation.constraints.NotBlank; import org.springframework.beans.factory.annotation.Value; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AppUserRepository users; private final TokenService tokens; private final boolean mockEnabled;
 public AuthController(AppUserRepository users,TokenService tokens,@Value("${app.auth.mock-enabled}")boolean mockEnabled){this.users=users;this.tokens=tokens;this.mockEnabled=mockEnabled;}
 @GetMapping("/mock-users") List<UserView> mockUsers(){guard();return users.findAllByActiveTrueOrderByFullName().stream().map(UserView::of).toList();}
 @PostMapping("/mock-login") TokenResponse login(@RequestBody LoginRequest req){guard();var u=users.findByUsernameAndActiveTrue(req.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Kullanıcı bulunamadı"));var current=new CurrentUser(u.getId(),u.getUsername(),u.getFullName(),u.getUnitId(),u.roleSet());return new TokenResponse(tokens.access(current),tokens.refresh(current),UserView.of(u));}
 private void guard(){if(!mockEnabled)throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
 public record LoginRequest(@NotBlank String username){} public record TokenResponse(String accessToken,String refreshToken,UserView user){}
 public record UserView(UUID id,String username,String fullName,UUID unitId,Set<String> roles){static UserView of(AppUser u){return new UserView(u.getId(),u.getUsername(),u.getFullName(),u.getUnitId(),u.roleSet());}}
}

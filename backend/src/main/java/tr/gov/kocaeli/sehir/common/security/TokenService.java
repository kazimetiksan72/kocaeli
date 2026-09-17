package tr.gov.kocaeli.sehir.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;

@Service
public class TokenService {
    private final ObjectMapper mapper; private final byte[] secret; private final long accessMinutes; private final long refreshDays;
    public TokenService(ObjectMapper mapper, @Value("${app.auth.jwt-secret}") String secret,
                        @Value("${app.auth.access-minutes}") long accessMinutes, @Value("${app.auth.refresh-days}") long refreshDays) {
        if (secret.length() < 32) throw new IllegalArgumentException("JWT secret en az 32 karakter olmalıdır");
        this.mapper=mapper; this.secret=secret.getBytes(StandardCharsets.UTF_8); this.accessMinutes=accessMinutes; this.refreshDays=refreshDays;
    }
    public String access(CurrentUser u) { return create(u, "access", Instant.now().plus(Duration.ofMinutes(accessMinutes))); }
    public String refresh(CurrentUser u) { return create(u, "refresh", Instant.now().plus(Duration.ofDays(refreshDays))); }
    private String create(CurrentUser u, String type, Instant expires) {
        try {
            String header=b64(mapper.writeValueAsBytes(Map.of("alg","HS256","typ","JWT")));
            var claims=new LinkedHashMap<String,Object>(); claims.put("sub",u.id().toString()); claims.put("username",u.username()); claims.put("name",u.fullName());
            claims.put("unitId",u.unitId().toString()); claims.put("roles",u.roles()); claims.put("type",type); claims.put("exp",expires.getEpochSecond());
            String body=b64(mapper.writeValueAsBytes(claims)); String unsigned=header+"."+body;
            return unsigned+"."+b64(sign(unsigned));
        } catch(Exception e) { throw new IllegalStateException("Token üretilemedi",e); }
    }
    @SuppressWarnings("unchecked") public CurrentUser verifyAccess(String token) {
        try {
            String[] p=token.split("\\."); if(p.length!=3 || !MessageDigest.isEqual(sign(p[0]+"."+p[1]), Base64.getUrlDecoder().decode(p[2]))) throw new SecurityException();
            Map<String,Object> c=mapper.readValue(Base64.getUrlDecoder().decode(p[1]),Map.class);
            if(!"access".equals(c.get("type")) || ((Number)c.get("exp")).longValue()<Instant.now().getEpochSecond()) throw new SecurityException();
            return new CurrentUser(UUID.fromString((String)c.get("sub")),(String)c.get("username"),(String)c.get("name"),UUID.fromString((String)c.get("unitId")),new HashSet<>((List<String>)c.get("roles")));
        } catch(Exception e) { throw new SecurityException("Geçersiz token"); }
    }
    private byte[] sign(String value) throws Exception { Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret,"HmacSHA256")); return mac.doFinal(value.getBytes(StandardCharsets.UTF_8)); }
    private String b64(byte[] b){return Base64.getUrlEncoder().withoutPadding().encodeToString(b);}
}

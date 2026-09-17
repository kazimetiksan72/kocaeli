package tr.gov.kocaeli.sehir.common.security;
import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.http.HttpHeaders; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final TokenService tokens; public JwtFilter(TokenService tokens){this.tokens=tokens;}
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String h=req.getHeader(HttpHeaders.AUTHORIZATION);
        if(h!=null&&h.startsWith("Bearer ")) try { var u=tokens.verifyAccess(h.substring(7)); var authorities=u.roles().stream().map(r->new SimpleGrantedAuthority("ROLE_"+r)).toList(); SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,null,authorities)); } catch(SecurityException ignored){}
        chain.doFilter(req,res);
    }
}


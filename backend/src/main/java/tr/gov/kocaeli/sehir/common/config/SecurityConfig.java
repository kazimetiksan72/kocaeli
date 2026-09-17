package tr.gov.kocaeli.sehir.common.config;
import tr.gov.kocaeli.sehir.common.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*; import org.springframework.http.HttpMethod; import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; import org.springframework.web.cors.*;
import java.util.*;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean SecurityFilterChain security(HttpSecurity http,JwtFilter jwt)throws Exception{return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).headers(h->h.contentSecurityPolicy(c->c.policyDirectives("default-src 'self'; frame-ancestors 'none'")).frameOptions(f->f.deny())).authorizeHttpRequests(a->a.requestMatchers("/actuator/health/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/api/v1/auth/**").permitAll().requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();}
 @Bean CorsConfigurationSource cors(@Value("${app.cors.allowed-origins}")String origins){var c=new CorsConfiguration();c.setAllowedOrigins(Arrays.asList(origins.split(",")));c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type","Idempotency-Key","X-Trace-Id"));c.setExposedHeaders(List.of("X-Trace-Id"));c.setAllowCredentials(true);var s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;}
}


package tr.gov.kocaeli.sehir.request;
import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.test.context.DynamicPropertyRegistry;import org.springframework.test.context.DynamicPropertySource;import org.testcontainers.containers.PostgreSQLContainer;import org.testcontainers.junit.jupiter.*;import org.testcontainers.utility.DockerImageName;import static org.assertj.core.api.Assertions.*;
@SpringBootTest @Testcontainers(disabledWithoutDocker=true)
class PostgisMigrationTest{
 @Container static final PostgreSQLContainer<?>DB=new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:15-3.5").asCompatibleSubstituteFor("postgres")).withDatabaseName("sehir_test").withUsername("test").withPassword("test");
 @DynamicPropertySource static void properties(DynamicPropertyRegistry r){r.add("spring.datasource.url",DB::getJdbcUrl);r.add("spring.datasource.username",DB::getUsername);r.add("spring.datasource.password",DB::getPassword);}
 @Autowired JdbcTemplate jdbc;
 @Test void migrationsAndPostgisAreOperational(){assertThat(jdbc.queryForObject("select count(*) from app_user",Long.class)).isEqualTo(4);assertThat(jdbc.queryForObject("select ST_AsText(ST_GeomFromText('POINT(29.9 40.7)',4326))",String.class)).isEqualTo("POINT(29.9 40.7)");}
}

package tr.gov.kocaeli.sehir.reporting.api;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.gov.kocaeli.sehir.common.security.CurrentUserService;
import tr.gov.kocaeli.sehir.organization.application.UnitScopeService;
import java.util.*;

@RestController @RequestMapping("/api/v1")
public class ReportingController {
    private final JdbcClient jdbc; private final UnitScopeService scope; private final CurrentUserService current;
    public ReportingController(JdbcClient jdbc, UnitScopeService scope, CurrentUserService current) { this.jdbc=jdbc; this.scope=scope; this.current=current; }

    @GetMapping("/dashboard") Map<String,Object> dashboard() {
        var units=scope.allowedUnits();
        long open=jdbc.sql("select count(*) from city_request where deleted_at is null and responsible_unit_id in (:u) and status not in ('REJECTED','DUPLICATE_CLOSED','CONVERTED_TO_PROJECT','CONVERTED_TO_ACTIVITY')").param("u",units).query(Long.class).single();
        var byStatus=jdbc.sql("select status,count(*) total from city_request where deleted_at is null and responsible_unit_id in (:u) group by status order by status").param("u",units).query((rs,n)->Map.of("status",rs.getString(1),"count",rs.getLong(2))).list();
        return Map.of("openRequests",open,"byStatus",byStatus,"generatedAt",java.time.Instant.now());
    }
    @GetMapping("/projects") List<LinkedHashMap<String,Object>> projects() {
        return jdbc.sql("select id,project_code,name,project_type,priority,status,created_at from project_candidate where responsible_unit_id in (:u) order by created_at desc").param("u",scope.allowedUnits()).query((rs,n)->row(rs,"project")).list();
    }
    @GetMapping("/activities") List<LinkedHashMap<String,Object>> activities() {
        return jdbc.sql("select id,activity_no,title,activity_type,status,created_at from activity where responsible_unit_id in (:u) order by created_at desc").param("u",scope.allowedUnits()).query((rs,n)->row(rs,"activity")).list();
    }
    @GetMapping("/notifications") List<LinkedHashMap<String,Object>> notifications() {
        return jdbc.sql("select id,type,title,body,link,read_at,created_at from notification where user_id=:u order by created_at desc limit 100").param("u",current.require().id()).query((rs,n)->row(rs,"notification")).list();
    }
    @PostMapping("/notifications/{id}/read") void read(@PathVariable UUID id) { jdbc.sql("update notification set read_at=coalesce(read_at,now()) where id=:id and user_id=:u").param("id",id).param("u",current.require().id()).update(); }
    @GetMapping("/audit") @PreAuthorize("hasRole('SYSTEM_ADMIN')") List<LinkedHashMap<String,Object>> audit() {
        return jdbc.sql("select id,action,entity_type,entity_id,trace_id,occurred_at from audit_log order by occurred_at desc limit 200").query((rs,n)->row(rs,"audit")).list();
    }
    private LinkedHashMap<String,Object> row(java.sql.ResultSet rs,String type)throws java.sql.SQLException {
        var m=new LinkedHashMap<String,Object>();m.put("id",rs.getObject("id",UUID.class));
        switch(type){
            case"project"->{m.put("projectCode",rs.getString("project_code"));m.put("name",rs.getString("name"));m.put("projectType",rs.getString("project_type"));m.put("priority",rs.getString("priority"));m.put("status",rs.getString("status"));m.put("createdAt",rs.getObject("created_at"));}
            case"activity"->{m.put("activityNo",rs.getString("activity_no"));m.put("title",rs.getString("title"));m.put("activityType",rs.getString("activity_type"));m.put("status",rs.getString("status"));m.put("createdAt",rs.getObject("created_at"));}
            case"notification"->{m.put("type",rs.getString("type"));m.put("title",rs.getString("title"));m.put("body",rs.getString("body"));m.put("link",rs.getString("link"));m.put("readAt",rs.getObject("read_at"));m.put("createdAt",rs.getObject("created_at"));}
            case"audit"->{m.put("action",rs.getString("action"));m.put("entityType",rs.getString("entity_type"));m.put("entityId",rs.getObject("entity_id",UUID.class));m.put("traceId",rs.getString("trace_id"));m.put("occurredAt",rs.getObject("occurred_at"));}
            default->throw new IllegalArgumentException(type);
        }return m;
    }
}

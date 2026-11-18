package com.example.clouddemo;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HomeController {
    private final JdbcTemplate jdbc;

    public HomeController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/")
    public String home(Model model) {
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS app_visits (
              id INTEGER PRIMARY KEY,
              visit_count BIGINT NOT NULL
            )
            """);
        Long count = jdbc.queryForObject("""
            INSERT INTO app_visits (id, visit_count)
            VALUES (1, 1)
            ON CONFLICT (id)
            DO UPDATE SET visit_count = app_visits.visit_count + 1
            RETURNING visit_count
            """, Long.class);
        model.addAttribute("visitCount", count);
        return "index";
    }

    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok", "service", "spring-boot"));
    }

    @GetMapping("/ready")
    @ResponseBody
    public ResponseEntity<Map<String, String>> ready() {
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok(Map.of("status", "ready", "database", "connected"));
        } catch (Exception exception) {
            return ResponseEntity.status(503)
                    .body(Map.of("status", "not-ready", "database", "unavailable"));
        }
    }
}

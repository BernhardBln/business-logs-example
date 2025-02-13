package de.bstreit.examples.businesslogsexample.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;

import static de.bstreit.examples.businesslogsexample.services.ProcessContextData.*;

@Component
public class BusinessLogger {

    private final JdbcTemplate jdbcTemplate;
    private final String applicationName;

    public BusinessLogger(JdbcTemplate jdbcTemplate,
                          @Value("${spring.application.name}")
                          String applicationName) {
        this.jdbcTemplate = jdbcTemplate;
        this.applicationName = applicationName;
    }


    public void log(String message, Map<String, Object> metadata) {
        log(ProcessContextData.getTriggerId(), ProcessContextData.getProcessId(), ProcessContextData.getProcess(), message, metadata);
    }

    // stores to database and participates in running transactions
    public void log(TriggerId triggerId, ProcessId processId, ProcessName processName, String message, Map<String, Object> metadata) {
        final var additionalFields = new HashMap<>(metadata);
        additionalFields.put("business_log", "true");
        additionalFields.put("log.level", "INFO");

        var logline = "{ " +
                      jsonLineWithTrailingComma(TRIGGER_ID_KEY, triggerId.getId().toString()) +
                      jsonLineWithTrailingComma(PROCESS_ID_KEY, processId.getId().toString()) +
                      jsonLineWithTrailingComma(PROCESS_NAME_KEY, processName.getName()) +
                      jsonLineWithTrailingComma("@timestamp", ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)) +
                      jsonLineWithTrailingComma("message", message) +
                      jsonLineWithTrailingComma("service.name", applicationName) +
                      formatMetadata(additionalFields) +
                      " }";

        System.out.println("Storing business log to DB: " + logline);

        jdbcTemplate.update(con -> {
            final var st = con.prepareStatement("INSERT INTO business_log (log_line) values (to_json(?::json));");
            st.setString(1, logline);
            return st;
        });
    }

    private String jsonLineWithTrailingComma(String key, String value) {
        return String.format("\"%s\":\"%s\",", key, escape(value));
    }
    private String jsonLine(String key, String value) {
        return String.format("\"%s\":\"%s\"", key, escape(value));
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String formatMetadata(Map<String, Object> metadata) {
        return metadata.entrySet().stream()
            .map(e -> jsonLine(e.getKey(), e.getValue().toString()))
            .collect(Collectors.joining(", "));
    }
}

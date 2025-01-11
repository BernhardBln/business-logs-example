package de.bstreit.examples.businesslogsexample.services;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;

@Component
public class BusinessLogger {

  public void log(String message, Map<String, Object> metadata) {
    log(ProcessContextData.getTriggerId(), ProcessContextData.getProcessId(), ProcessContextData.getProcess(), message, metadata);
  }

  // stores to database and participates in running transactions
  public void log(TriggerId triggerId, ProcessId processId, ProcessName processName, String message, Map<String, Object> metadata) {
    // should store to database, but here we just log to sysout
    var logline =
        "TriggerId: %s, processId: %s, process: %s, message: %s, metadata: %s"
            .formatted(triggerId, processId, processName.getName(), message, formatMetadata(metadata));

    System.out.println("Store business log to DB: " + logline);
  }

  private static String formatMetadata(Map<String, Object> metadata) {
    return metadata.entrySet().stream()
        .map(e -> e.getKey() + ": " + e.getValue())
        .collect(Collectors.joining(", "));
  }
}

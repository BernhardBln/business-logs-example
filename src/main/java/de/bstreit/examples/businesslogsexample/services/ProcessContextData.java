package de.bstreit.examples.businesslogsexample.services;

import org.slf4j.MDC;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;

public class ProcessContextData {

  public static void set(TriggerId triggerId, ProcessId processId, ProcessName processName) {
    MDC.put("triggerId", triggerId.getId().toString());
    MDC.put("processId", processId.getId().toString());
    MDC.put("process", processName.getName());
  }

  public static TriggerId getTriggerId() {
    return TriggerId.from(MDC.get("triggerId"));
  }

  public static ProcessId getProcessId() {
    return ProcessId.from(MDC.get("processId"));
  }

  public static ProcessName getProcess() {
    return ProcessName.fromName(MDC.get("process"));
  }

  public static void clear() {
    try {
      MDC.remove("triggerId");
    } finally {
      try {
        MDC.remove("processId");
      } finally {
        MDC.remove("process");
      }
    }
  }
}

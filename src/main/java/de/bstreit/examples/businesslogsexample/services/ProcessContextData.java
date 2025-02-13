package de.bstreit.examples.businesslogsexample.services;

import org.slf4j.MDC;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;

public class ProcessContextData {

    public static final String TRIGGER_ID_KEY = "trigger.id";
    public static final String PROCESS_ID_KEY = "process.id";
    public static final String PROCESS_NAME_KEY = "process.name";

    public static void set(TriggerId triggerId, ProcessId processId, ProcessName processName) {
        MDC.put(TRIGGER_ID_KEY, triggerId.getId().toString());
        MDC.put(PROCESS_ID_KEY, processId.getId().toString());
        MDC.put(PROCESS_NAME_KEY, processName.getName());
    }

    public static TriggerId getTriggerId() {
        return TriggerId.from(MDC.get(TRIGGER_ID_KEY));
    }

    public static ProcessId getProcessId() {
        return ProcessId.from(MDC.get(PROCESS_ID_KEY));
    }

    public static ProcessName getProcess() {
        return ProcessName.fromName(MDC.get(PROCESS_NAME_KEY));
    }

    public static void clear() {
        try {
            MDC.remove(TRIGGER_ID_KEY);
        } finally {
            try {
                MDC.remove(PROCESS_ID_KEY);
            } finally {
                MDC.remove(PROCESS_NAME_KEY);
            }
        }
    }
}

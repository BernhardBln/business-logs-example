package de.bstreit.examples.businesslogsexample.processes;

import java.util.Map;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;

public interface BusinessProcess {
    ProcessName getProcessName();
    void run(TriggerId triggerId, ProcessId processId, Map<String,Object> additionalContext);
}

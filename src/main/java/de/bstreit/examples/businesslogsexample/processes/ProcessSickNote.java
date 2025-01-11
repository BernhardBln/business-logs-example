package de.bstreit.examples.businesslogsexample.processes;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ProcessSickNote implements BusinessProcess {

  @Override
  public ProcessName getProcessName() {
    return ProcessName.PROCESS_SICK_NOTE;
  }

  @Transactional
  @Override
  public void run(TriggerId triggerId, ProcessId processId, Map<String, Object> additionalContext) {
    // ...
  }
}

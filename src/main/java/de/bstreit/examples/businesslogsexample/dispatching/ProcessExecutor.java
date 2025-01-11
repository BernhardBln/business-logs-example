package de.bstreit.examples.businesslogsexample.dispatching;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.BusinessProcess;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;
import de.bstreit.examples.businesslogsexample.services.ProcessContextData;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessExecutor {

  private final Map<ProcessName, BusinessProcess> businessProcesses;

  @Autowired
  public ProcessExecutor(List<BusinessProcess> businessProcesses) {
    this.businessProcesses =
        businessProcesses.stream().collect(toMap(BusinessProcess::getProcessName, identity()));

    if (this.businessProcesses.size() != ProcessName.values().length) {
      throw new IllegalStateException();
    }
  }

  void runProcess(
      TriggerId triggerId,
      ProcessId processId,
      ProcessName processName,
      Map<String, Object> additionalContext) {

    ProcessContextData.set(triggerId, processId, processName);

    try {

      log.trace("Running process.");
      businessProcesses.get(processName).run(triggerId, processId, additionalContext);
      log.trace("Process exited normally.");

    } catch (Exception e) {
      log.error("Process exited with error.", e);

    } finally {

      ProcessContextData.clear();
    }
  }
}

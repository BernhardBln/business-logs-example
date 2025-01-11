package de.bstreit.examples.businesslogsexample.dispatching;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.BusinessProcess;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;
import de.bstreit.examples.businesslogsexample.services.BusinessLogger;
import de.bstreit.examples.businesslogsexample.services.ProcessContextData;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessScheduler {

  private final BusinessLogger businessLogger;
  private final ObjectFactory<ProcessExecutor> processExecutorProvider;

  @Autowired
  public ProcessScheduler(
      BusinessLogger businessLogger, ObjectFactory<ProcessExecutor> processExecutorProvider) {
    this.businessLogger = businessLogger;
    this.processExecutorProvider = processExecutorProvider;
  }

  public void runNext(ProcessName processName, Map<String, Object> additionalContext) {
    var triggerId = ProcessContextData.getTriggerId();
    var nextProcessId = ProcessId.random();

    if (ProcessContextData.getProcessId() == null || ProcessContextData.getProcess() == null) {
      throw new IllegalStateException("Missing process information!");

    } else {
      businessLogger.log(
          "Follow-up process '%s' triggered.".formatted(processName.getName()),
          Map.of(
              "followUpProcessId",
              nextProcessId,
              "followUpProcess",
              processName.getName(),
              // this should be done differently:
              "userEmail",
              additionalContext.get("e-mail-sender")));
    }

    CompletableFuture.runAsync(
        () ->
            processExecutorProvider
                .getObject()
                .runProcess(triggerId, nextProcessId, processName, additionalContext));
  }

  public void runNext(
      ProcessName processName, TriggerId triggerId, Map<String, Object> additionalContext) {

    var processId = ProcessId.random();

    businessLogger.log(
        triggerId,
        processId,
        processName,
        "Business process '%s' triggered.".formatted(processName.getName()),
        Map.of());

    CompletableFuture.runAsync(
        () ->
            processExecutorProvider
                .getObject()
                .runProcess(triggerId, processId, processName, additionalContext));
  }
}

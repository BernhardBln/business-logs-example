package de.bstreit.examples.businesslogsexample.eventhandling;

import java.util.Map;

import org.springframework.stereotype.Component;

import de.bstreit.examples.businesslogsexample.dispatching.ProcessExecutor;
import de.bstreit.examples.businesslogsexample.dispatching.ProcessScheduler;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.processes.ProcessName;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NewEmailListener {

  private final ProcessScheduler processScheduler;

  @PostConstruct
  void simulateNewEmail() {
    processScheduler.runNext(
        ProcessName.RECEIVE_AND_STORE_E_MAIL,
        TriggerId.random(),
        Map.of(
            "e-mail-sender", "employeeXX@company.com",
            "e-mail-header-yyy", "header value",
            "attachments", "list of attachments"));
  }
}

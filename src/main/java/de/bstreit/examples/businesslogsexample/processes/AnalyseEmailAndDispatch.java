package de.bstreit.examples.businesslogsexample.processes;

import de.bstreit.examples.businesslogsexample.dispatching.ProcessExecutor;
import de.bstreit.examples.businesslogsexample.dispatching.ProcessScheduler;
import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.services.BusinessLogger;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyseEmailAndDispatch implements BusinessProcess {

  private final BusinessLogger businessLogger;
  private final ProcessScheduler processScheduler;

  @Override
  public ProcessName getProcessName() {
    return ProcessName.ANALYSE_EMAIL_AND_DISPATCH;
  }

  @Transactional
  @Override
  public void run(TriggerId triggerId, ProcessId processId, Map<String, Object> additionalContext) {

    var sender = additionalContext.get("e-mail-sender").toString();
    var attachmentIds = additionalContext.get("attachment-ids");

    Types type = analyseEmail(sender, attachmentIds);

    switch (type) {
      case SICK_NOTE -> processSickNote(sender);
      case VACATION_REQUEST -> processVacationRequest(sender, attachmentIds);
    }

  }

  private void processSickNote(String sender) {
    log.info("Dispatched to Sick Note Service.");
    processScheduler.runNext(ProcessName.PROCESS_SICK_NOTE, Map.of("e-mail-sender", sender));
  }

  private void processVacationRequest(String sender, Object attachmentIds) {
    // read vacation dates from attachment ids and dispatch
    // ...
  }

  private Types analyseEmail(String sender, Object attachmentIds) {
    // analyse files and return correct type
    log.info("Analysing E-Mail attachment.");
    log.debug("Starting virus scan.");
    // scan file
    log.debug("Virus scan completed, no issues found.");

    // if (...) {
    businessLogger.log(
        "Analysed E-Mail from employee '%s' and found sick note.".formatted(sender),
        Map.of("userEmail", sender));
    return Types.SICK_NOTE;
    // } else if (...) {
    //    businessLogger.log(
    //        "Analysed E-Mail from employee '%s' and found vacation request.".formatted(sender),
    //        Map.of("userEmail", sender));
    //    return Types.VACATION_REQUEST;
    // }
  }

  enum Types {
    SICK_NOTE,
    VACATION_REQUEST,
  }
}

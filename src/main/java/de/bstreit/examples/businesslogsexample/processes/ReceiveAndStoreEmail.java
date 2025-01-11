package de.bstreit.examples.businesslogsexample.processes;

import de.bstreit.examples.businesslogsexample.dispatching.ProcessScheduler;
import de.bstreit.examples.businesslogsexample.ids.AttachmentId;
import de.bstreit.examples.businesslogsexample.ids.ProcessId;
import de.bstreit.examples.businesslogsexample.ids.TriggerId;
import de.bstreit.examples.businesslogsexample.services.BusinessLogger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiveAndStoreEmail implements BusinessProcess {

  private final BusinessLogger businessLogger;
  private final ProcessScheduler processScheduler;

  @Override
  public ProcessName getProcessName() {
    return ProcessName.RECEIVE_AND_STORE_E_MAIL;
  }

  @Transactional
  @Override
  public void run(TriggerId triggerId, ProcessId processId, Map<String, Object> additionalContext) {

    var receptionTime = ZonedDateTime.now();
    var sender = additionalContext.get("e-mail-sender").toString();

    List<AttachmentId> attachmentIds = storeAttachments(additionalContext.get("attachments"));
    storeMetadata(triggerId, sender, receptionTime, attachmentIds);

    log.info("Received and stored E-Mail with attachments.");
    businessLogger.log(
        "Received E-Mail at %s from sender '%s' and stored to inbox."
            .formatted(receptionTime, sender),
        Map.of("userEmail", sender));

    verifyEmailHeader(additionalContext);

    processScheduler.runNext(ProcessName.ANALYSE_EMAIL_AND_DISPATCH, Map.of("attachment-ids", attachmentIds, "e-mail-sender", sender));
  }

  private void verifyEmailHeader(Map<String, Object> additionalContext) {
    if (!additionalContext.containsKey("e-mail-header-xxx")) {
      log.warn("Missing E-Mail header xxx.");
    }
  }

  private List<AttachmentId> storeAttachments(Object attachments) {
    // store somewhere
    return List.of();
  }

  private void storeMetadata(
      TriggerId triggerId, String sender, ZonedDateTime now, List<AttachmentId> attachmentIds) {
    // store metadata to database
  }
}

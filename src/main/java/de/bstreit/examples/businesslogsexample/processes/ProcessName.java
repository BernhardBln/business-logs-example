package de.bstreit.examples.businesslogsexample.processes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProcessName {
  RECEIVE_AND_STORE_E_MAIL("receive-and-store-e-mail"),
  ANALYSE_EMAIL_AND_DISPATCH("analyse-e-mail-and-dispatch"),
  PROCESS_SICK_NOTE("process-sick-note");

  @Getter private final String name;

  public static ProcessName fromName(String name) {
    for (ProcessName processName : ProcessName.values()) {
      if (processName.name.equals(name)) {
        return processName;
      }
    }
    ;
    throw new IllegalArgumentException("Unknown process name: " + name);
  }
}

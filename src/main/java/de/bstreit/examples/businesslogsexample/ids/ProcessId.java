package de.bstreit.examples.businesslogsexample.ids;

import java.util.UUID;

import lombok.NonNull;
import lombok.Value;

@Value
public class ProcessId {
  UUID id;

  public static ProcessId random() {
    return new ProcessId(UUID.randomUUID());
  }

  public static ProcessId from(@NonNull String triggerId) {
    return new ProcessId(UUID.fromString(triggerId));
  }
}

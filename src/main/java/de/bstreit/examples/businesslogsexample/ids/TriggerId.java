package de.bstreit.examples.businesslogsexample.ids;


import java.util.Map;
import java.util.UUID;

import lombok.NonNull;
import lombok.Value;

@Value
public class TriggerId {
    UUID id;

    public static TriggerId from(@NonNull String triggerId) {
        return new TriggerId(UUID.fromString(triggerId));
    }

    public static TriggerId random() {
        return new TriggerId(UUID.randomUUID());
    }
}

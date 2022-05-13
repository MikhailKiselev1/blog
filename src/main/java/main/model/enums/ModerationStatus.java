package main.model.enums;

public enum ModerationStatus {
    NEW("new"),
    ACCEPTED("accept"),
    DECLINED("decline");

    private String value;

    private ModerationStatus(String value) {
        this.value = value;
    }

    public static ModerationStatus fromString(String value) {
        if (value != null) {
            for (ModerationStatus ms : ModerationStatus.values()) {
                if (value.equalsIgnoreCase(ms.value)) {
                    return ms;
                }
            }
        }
        throw new IllegalArgumentException("No such value " + value);
    }
}

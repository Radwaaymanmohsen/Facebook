package mytry;

public enum PrivacyLevel {
    GENERAL("GENERAL"),
    RESTRICTED("RESTRICTED");

    private final String value;

    PrivacyLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PrivacyLevel fromString(String value) {
        for (PrivacyLevel privacyLevel : PrivacyLevel.values()) {
            if (privacyLevel.value.equalsIgnoreCase(value)) {
                return privacyLevel;
            }
        }
        throw new IllegalArgumentException("Invalid PrivacyLevel value: " + value);
    }
}

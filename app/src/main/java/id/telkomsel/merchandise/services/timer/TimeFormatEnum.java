package id.telkomsel.merchandise.services.timer;

public enum TimeFormatEnum {
    MILLIS,
    SECONDS,
    MINUTES,
    HOUR,
    DAY;

    public String canonicalForm() {
        return this.name();
    }

    public static id.telkomsel.merchandise.services.timer.TimeFormatEnum fromCanonicalForm(String canonical) {
        return valueOf(id.telkomsel.merchandise.services.timer.TimeFormatEnum.class, canonical);
    }
}

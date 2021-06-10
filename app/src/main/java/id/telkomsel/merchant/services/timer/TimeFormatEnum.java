package id.telkomsel.merchant.services.timer;

public enum TimeFormatEnum {
    MILLIS,
    SECONDS,
    MINUTES,
    HOUR,
    DAY;

    public String canonicalForm() {
        return this.name();
    }

    public static id.telkomsel.merchant.services.timer.TimeFormatEnum fromCanonicalForm(String canonical) {
        return valueOf(id.telkomsel.merchant.services.timer.TimeFormatEnum.class, canonical);
    }
}

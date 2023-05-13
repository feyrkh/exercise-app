package com.hellocurious.site.model.tracking;

public enum DistanceTrackingType {
    NONE("", ""), MILES("mile", "miles"), KMS("km", "kms"), LAPS("lap", "laps");

    private final String singular;
    private final String plural;

    private DistanceTrackingType(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String getUnit(Number amt) {
        if (Math.abs(amt.doubleValue() - 1.0) < 0.0001) {
            return singular;
        } else {
            return plural;
        }
    }
}

package com.hellocurious.site.model.tracking;

public enum WeightTrackingType {
    NONE("", ""), LBS("lb", "lbs"), KGS("kg", "kgs");

    private String singular;
    private String plural;

    private WeightTrackingType(String singular, String plural) {
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

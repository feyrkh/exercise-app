package com.hellocurious.site.model.tracking;

public enum RepTrackingType {
    NONE("", ""), REPS("rep", "reps");

    private String singular;
    private String plural;

    private RepTrackingType(String singular, String plural) {
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

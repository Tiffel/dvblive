package de.hackathondd.dvblive.domain;

public class Haltestelle {
    private String triasCode;
    private String latitude;
    private String longitude;

    public Haltestelle(String triasCode, String latitude, String longitude) {
        this.triasCode = triasCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTriasCode() {
        return triasCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}

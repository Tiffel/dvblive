package de.hackathondd.dvblive.domain;

import java.util.HashSet;
import java.util.Set;

public class Haltestelle {
    private String triasCode;
    private String latitude;
    private String longitude;
    private Set<Journey> journeys = new HashSet<>();

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

    public Set<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
    }
}

package de.hackathondd.dvblive.application;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.Journey;

public class HaltestelleTo {
    private String triasCode;
    private String latitude;
    private String longitude;
    private Set<Journey> journeys = new HashSet<>();

    public HaltestelleTo(Haltestelle haltestelle, String linienfilter) {
        this.triasCode = haltestelle.getTriasCode();
        this.latitude = haltestelle.getLatitude();
        this.longitude = haltestelle.getLongitude();
        this.journeys = haltestelle.getJourneys().stream()
                .filter(journey -> journey.getLinie().equals(linienfilter)).collect(Collectors.toSet());
        ;
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
}

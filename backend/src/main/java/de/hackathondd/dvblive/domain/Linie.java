package de.hackathondd.dvblive.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Linie {
    private String nummer;
    private String startEndHaltestelle;
    private String triasNummer;
    private String triasStartHaltestelleCode;
    private String triasEndHaltestelleCode;

    public Linie(String nummer, String startEndHaltestelle, String triasNummer, String triasStartHaltestelleCode,
            String triasEndHaltestelleCode) {
        this.nummer = nummer;
        this.startEndHaltestelle = startEndHaltestelle;
        this.triasNummer = triasNummer;
        this.triasStartHaltestelleCode = triasStartHaltestelleCode;
        this.triasEndHaltestelleCode = triasEndHaltestelleCode;
    }

    public String getNummer() {
        return nummer;
    }

    public String getStartEndHaltestelle() {
        return startEndHaltestelle;
    }

    public String getTriasNummer() {
        return triasNummer;
    }

    public String getTriasStartHaltestelleCode() {
        return triasStartHaltestelleCode;
    }

    public String getTriasEndHaltestelleCode() {
        return triasEndHaltestelleCode;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(triasNummer)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Linie)) {
            return false;
        }
        Linie other = (Linie) obj;
        return this.triasNummer.equals(other.triasNummer);
    }
}

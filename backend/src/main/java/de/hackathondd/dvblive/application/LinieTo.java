package de.hackathondd.dvblive.application;

import java.util.List;

import de.hackathondd.dvblive.domain.Linie;

public class LinieTo {
    private String nummer;
    private String startEndHaltestelle;
    private String triasNummer;
    private String triasStartHaltestelleCode;
    private String triasEndHaltestelleCode;
    private List<HaltestelleTo> haltestellen;

    public LinieTo(Linie linie,
            List<HaltestelleTo> haltestellen) {
        this.nummer = linie.getNummer();
        this.startEndHaltestelle = linie.getStartEndHaltestelle();
        this.triasNummer = linie.getTriasNummer();
        this.triasStartHaltestelleCode = linie.getStartEndHaltestelle();
        this.triasEndHaltestelleCode = linie.getTriasEndHaltestelleCode();
        this.haltestellen = haltestellen;
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

    public List<HaltestelleTo> getHaltestellen() {
        return haltestellen;
    }
}

package de.hackathondd.dvblive.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Linie {
    private int nummer;
    private String start;
    private String end;

    public Linie(int nummer) {
        this.nummer = nummer;
    }

    public int getNummer() {
        return nummer;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(nummer)
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
        return this.nummer == other.nummer;
    }
}

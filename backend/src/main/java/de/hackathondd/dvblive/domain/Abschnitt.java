package de.hackathondd.dvblive.domain;

import java.time.Duration;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Abschnitt {
    private String start;
    private String end;
    private Duration maxVerspaetung;
    private Position startPosition;
    private Position endPosition;

    public Abschnitt(String start, String end, Duration maxVerspaetung, Position startPosition, Position endPosition) {
        this.start = start;
        this.end = end;
        this.maxVerspaetung = maxVerspaetung;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Duration getMaxVerspaetung() {
        return maxVerspaetung;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(start)
                .append(end)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Abschnitt)) {
            return false;
        }
        Abschnitt other = (Abschnitt) obj;
        return this.start.equals(other.start) && this.end.equals(other.end);
    }

    public static class Position {
        private String latitude;
        private String longitude;

        public Position(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }
}

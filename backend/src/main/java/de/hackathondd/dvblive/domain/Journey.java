package de.hackathondd.dvblive.domain;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Journey {
    private String id;
    private String linie;
    private ZonedDateTime timetabledTime;
    private ZonedDateTime estimatedTime;

    public Journey(String id, String linie, String timetabledTime, String estimatedTime) {
        this.id = id;
        this.linie = linie;
        this.timetabledTime = ZonedDateTime.parse(timetabledTime);
        this.estimatedTime = ZonedDateTime.parse(estimatedTime);
    }

    public String getId() {
        return id;
    }

    public String getLinie() {
        return linie;
    }

    public ZonedDateTime getTimetabledTime() {
        return timetabledTime;
    }

    public ZonedDateTime getEstimatedTime() {
        return estimatedTime;
    }

    public Duration getDifference() {
        return Duration.between(timetabledTime, estimatedTime).abs();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Journey)) {
            return false;
        }
        Journey other = (Journey) obj;
        return this.id.equals(other.id);
    }
}

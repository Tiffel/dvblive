package de.hackathondd.dvblive.domain.inmemorydb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hackathondd.dvblive.domain.Linie;
import org.springframework.stereotype.Repository;

@Repository
public class LinienRepository {
    private Map<String, Linie> linien = new HashMap<>();

    public boolean exists(String triasNummer) {
        return linien.containsKey(triasNummer);
    }

    public Linie getLinie(String triasNummer) {
        return linien.get(triasNummer);
    }

    public void createOrUpdateLinie(Linie linie) {
        linien.put(linie.getTriasNummer(), linie);
    }

    public Set<Linie> getAll() {
        return linien.values().stream().collect(Collectors.toSet());
    }
}

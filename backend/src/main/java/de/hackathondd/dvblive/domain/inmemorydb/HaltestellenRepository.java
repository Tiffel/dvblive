package de.hackathondd.dvblive.domain.inmemorydb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hackathondd.dvblive.domain.Haltestelle;
import org.springframework.stereotype.Repository;

@Repository
public class HaltestellenRepository {
    private Map<String, Haltestelle> haltestellen = new HashMap<>();

    public boolean exists(String triasCode) {
        return haltestellen.containsKey(triasCode);
    }

    public Haltestelle getHaltestelle(String triasCode) {
        return haltestellen.get(triasCode);
    }

    public Set<Haltestelle> getAll() {
        return haltestellen.values().stream().collect(Collectors.toSet());
    }

    public void createOrupdateHaltestelle(Haltestelle haltestelle) {
        haltestellen.put(haltestelle.getTriasCode(), haltestelle);
    }
}

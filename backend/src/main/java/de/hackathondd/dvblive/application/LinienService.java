package de.hackathondd.dvblive.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.Linie;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import de.hackathondd.dvblive.domain.inmemorydb.LinienRepository;
import org.springframework.stereotype.Service;

@Service
public class LinienService {
    private final LinienRepository linienRepository;
    private final HaltestellenRepository haltestellenRepository;

    public LinienService(LinienRepository linienRepository,
            HaltestellenRepository haltestellenRepository) {
        this.linienRepository = linienRepository;
        this.haltestellenRepository = haltestellenRepository;
    }

    public LinieTo getLinie(String triasNummer) {
        Linie linie = linienRepository.getLinie(triasNummer);
        List<Haltestelle> haltestellen = linie.getHaltestellenTriasCode()
                .stream().map(s -> haltestellenRepository.getHaltestelle(s)).collect(Collectors.toList());
        return new LinieTo(linie, haltestellen);
    }

    public Set<LinieTo> getAll() {
        Set<Linie> linien = linienRepository.getAll();
        Set<LinieTo> linienTos = new HashSet<>();
        for (Linie linie : linien) {
            List<Haltestelle> haltestellen = linie.getHaltestellenTriasCode()
                    .stream().map(s -> haltestellenRepository.getHaltestelle(s)).collect(Collectors.toList());
            linienTos.add(new LinieTo(linie, haltestellen));
        }
        return linienTos;
    }
}

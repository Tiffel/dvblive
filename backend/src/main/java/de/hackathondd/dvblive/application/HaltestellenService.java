package de.hackathondd.dvblive.application;

import java.util.Set;

import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import org.springframework.stereotype.Service;

@Service
public class HaltestellenService {
    private final VvoQueryService vvoQueryService;
    private final HaltestellenRepository haltestellenRepository;

    public HaltestellenService(VvoQueryService vvoQueryService,
            HaltestellenRepository haltestellenRepository) {
        this.vvoQueryService = vvoQueryService;
        this.haltestellenRepository = haltestellenRepository;
    }

    public void updateAll() throws Exception {
        Set<Haltestelle> haltestellen = haltestellenRepository.getAll();
        for (Haltestelle haltestelle : haltestellen) {
            vvoQueryService.updateJourneys(haltestelle);
        }
    }
}

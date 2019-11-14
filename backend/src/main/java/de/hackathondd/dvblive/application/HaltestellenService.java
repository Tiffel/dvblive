package de.hackathondd.dvblive.application;

import java.util.Set;
import java.util.Date;

import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HaltestellenService {
    Logger logger = LoggerFactory.getLogger(HaltestellenService.class);
    private final VvoQueryService vvoQueryService;
    private final HaltestellenRepository haltestellenRepository;

    public HaltestellenService(VvoQueryService vvoQueryService,
            HaltestellenRepository haltestellenRepository) {
        this.vvoQueryService = vvoQueryService;
        this.haltestellenRepository = haltestellenRepository;
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 40000)
    public void updateAll() throws Exception {
        logger.info("update haltestellen");
        Date start_time = new Date();
        Set<Haltestelle> haltestellen = haltestellenRepository.getAll();
        for (Haltestelle haltestelle : haltestellen) {
            vvoQueryService.updateJourneys(haltestelle);
        }
        logger.info("done update haltestellen");
        Date end_time = new Date();
        logger.info("in " + ((end_time.getTime() - start_time.getTime()) / 1000) + " seconds");
    }
}

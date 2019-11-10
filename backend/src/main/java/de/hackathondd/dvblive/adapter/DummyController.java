package de.hackathondd.dvblive.adapter;

import java.util.Set;

import de.hackathondd.dvblive.application.VvoQueryService;
import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.Linie;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import de.hackathondd.dvblive.domain.inmemorydb.LinienRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ein Dummy Controller, um manuell abfragen zu triggern, die später regelmäßig automatisch laufen
 */
@RestController(value = "dummy")
public class DummyController {
    private final VvoQueryService vvoQueryService;
    private final LinienRepository linienRepository;
    private final HaltestellenRepository haltestellenRepository;

    public DummyController(VvoQueryService vvoQueryService,
            LinienRepository linienRepository,
            HaltestellenRepository haltestellenRepository) {
        this.vvoQueryService = vvoQueryService;
        this.linienRepository = linienRepository;
        this.haltestellenRepository = haltestellenRepository;
    }

    @GetMapping(value = "/initdata")
    public Set<Linie> initLinienUndHaltestellen() throws Exception {
        return vvoQueryService.initLinienUndHaltestellen();
    }

    @GetMapping(value = "/linien")
    public Set<Linie> linien() throws Exception {
        return linienRepository.getAll();
    }

    @GetMapping(value = "/haltestellen")
    public Set<Haltestelle> haltestellen() throws Exception {
        return haltestellenRepository.getAll();
    }
}

package de.hackathondd.dvblive.adapter;

import java.util.Collection;
import java.util.Set;

import de.hackathondd.dvblive.application.AbschnittService;
import de.hackathondd.dvblive.application.HaltestellenService;
import de.hackathondd.dvblive.application.LinieTo;
import de.hackathondd.dvblive.application.LinienService;
import de.hackathondd.dvblive.application.VvoQueryService;
import de.hackathondd.dvblive.domain.Abschnitt;
import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.Linie;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import de.hackathondd.dvblive.domain.inmemorydb.LinienRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ein Dummy Controller, um manuell abfragen zu triggern, die später regelmäßig automatisch laufen
 */
@RestController(value = "dummy")
public class DummyController {
    private final VvoQueryService vvoQueryService;
    private final LinienService linienService;
    private final LinienRepository linienRepository;
    private final HaltestellenRepository haltestellenRepository;
    private final HaltestellenService haltestellenService;
    private final AbschnittService abschnittService;

    public DummyController(VvoQueryService vvoQueryService,
            LinienService linienService, LinienRepository linienRepository,
            HaltestellenRepository haltestellenRepository,
            HaltestellenService haltestellenService,
            AbschnittService abschnittService) {
        this.vvoQueryService = vvoQueryService;
        this.linienService = linienService;
        this.linienRepository = linienRepository;
        this.haltestellenRepository = haltestellenRepository;
        this.haltestellenService = haltestellenService;
        this.abschnittService = abschnittService;
    }

    @GetMapping(value = "/initdata")
    public Set<Linie> initLinienUndHaltestellen() throws Exception {
        return vvoQueryService.initLinienUndHaltestellen();
    }

    @GetMapping(value = "/linie")
    public Set<Linie> linie() throws Exception {
        return linienRepository.getAll();
    }

    @GetMapping(value = "/linie/{id}")
    public Linie linien(@PathVariable String id) throws Exception {
        return linienRepository.getLinie(id);
    }

    @GetMapping(value = "/linieMitHaltestellen")
    public Set<LinieTo> linieMitHaltestellen() throws Exception {
        return linienService.getAll();
    }

    @GetMapping(value = "/linieMitHaltestellen/{id}")
    public LinieTo linieMitHaltestellen(@PathVariable String id) throws Exception {
        return linienService.getLinie(id);
    }

    @GetMapping(value = "/haltestelle")
    public Set<Haltestelle> haltestellen() throws Exception {
        return haltestellenRepository.getAll();
    }

    @GetMapping(value = "/haltestelle/update")
    public void haltestellenUpdate() throws Exception {
        haltestellenService.updateAll();
        ;
    }

    @GetMapping(value = "/abschnitte")
    public Collection<Abschnitt> abschnitte() {
        return abschnittService.alle();
    }
}

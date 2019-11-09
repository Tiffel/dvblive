package de.hackathondd.dvblive.adapter;

import java.util.Set;

import de.hackathondd.dvblive.application.VvoQueryService;
import de.hackathondd.dvblive.domain.Linie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ein Dummy Controller, um manuell abfragen zu triggern, die später regelmäßig automatisch laufen
 */
@RestController(value = "dummy")
public class DummyController {
    private VvoQueryService vvoQueryService;

    public DummyController(VvoQueryService vvoQueryService) {
        this.vvoQueryService = vvoQueryService;
    }

    @GetMapping(value = "/locationInformationRequest")
    public String test() {
        return vvoQueryService.locationInformationRequest();
    }

    @GetMapping(value = "/linien")
    public Set<Linie> linien() throws Exception {
        return vvoQueryService.alleLinien();
    }
}

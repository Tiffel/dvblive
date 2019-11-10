package de.hackathondd.dvblive.application;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hackathondd.dvblive.domain.Abschnitt;
import de.hackathondd.dvblive.domain.Journey;
import org.springframework.stereotype.Service;

@Service
public class AbschnittService {
    private final LinienService linienService;

    public AbschnittService(LinienService linienService) {
        this.linienService = linienService;
    }

    public Collection<Abschnitt> alle() {
        Map<String, Abschnitt> abschnitte = new HashMap<>();
        for (LinieTo linie : linienService.getAll()) {
            List<HaltestelleTo> haltestelleTos = linie.getHaltestellen();
            for (int i = 0; i < linie.getHaltestellen().size() - 1; i++) {
                HaltestelleTo current = haltestelleTos.get(i);
                HaltestelleTo next = haltestelleTos.get(i + 1);
                Duration maxVerspaetung = next.getJourneys().stream().map(Journey::getDifference)
                        .max(Duration::compareTo).orElseGet(() -> Duration.ZERO);
                Abschnitt abschnitt = new Abschnitt(current.getTriasCode(), next.getTriasCode(), maxVerspaetung,
                        new Abschnitt.Position(current.getLatitude(), current.getLongitude()),
                        new Abschnitt.Position(next.getLatitude(), next.getLongitude()));
                Abschnitt existierender = abschnitte.get(current.getTriasCode() + next.getTriasCode());

                if (existierender != null) {
                    existierender.addLinie(linie.getNummer());
                    if (abschnitt.getMaxVerspaetung().minus(existierender.getMaxVerspaetung()).isNegative()) {
                        //der schon vorhandene ist größer
                    } else {
                        //der schon vorhandene ist kleiner, also den neuen nehmen
                        abschnitt.addLinien(existierender.getLinien());
                        abschnitte.put(current.getTriasCode() + next.getTriasCode(), abschnitt);
                    }
                } else {
                    abschnitt.addLinie(linie.getNummer());
                    abschnitte.put(current.getTriasCode() + next.getTriasCode(), abschnitt);
                }

            }
        }
        return abschnitte.values();
    }

    public Collection<Abschnitt> jeLinie(String triasNummer) {
        Map<String, Abschnitt> abschnitte = new HashMap<>();
        LinieTo linie = linienService.getLinie(triasNummer);
        List<HaltestelleTo> haltestelleTos = linie.getHaltestellen();
        for (int i = 0; i < linie.getHaltestellen().size() - 1; i++) {
            HaltestelleTo current = haltestelleTos.get(i);
            HaltestelleTo next = haltestelleTos.get(i + 1);
            Duration maxVerspaetung = next.getJourneys().stream().map(Journey::getDifference)
                    .max(Duration::compareTo).orElseGet(() -> Duration.ZERO);
            Abschnitt abschnitt = new Abschnitt(current.getTriasCode(), next.getTriasCode(), maxVerspaetung,
                    new Abschnitt.Position(current.getLatitude(), current.getLongitude()),
                    new Abschnitt.Position(next.getLatitude(), next.getLongitude()));
            Abschnitt existierender = abschnitte.get(current.getTriasCode() + next.getTriasCode());

            if (existierender != null) {
                existierender.addLinie(linie.getNummer());
                if (abschnitt.getMaxVerspaetung().minus(existierender.getMaxVerspaetung()).isNegative()) {
                    //der schon vorhandene ist größer
                } else {
                    //der schon vorhandene ist kleiner, also den neuen nehmen
                    abschnitt.addLinien(existierender.getLinien());
                    abschnitte.put(current.getTriasCode() + next.getTriasCode(), abschnitt);
                }
            } else {
                abschnitt.addLinie(linie.getNummer());
                abschnitte.put(current.getTriasCode() + next.getTriasCode(), abschnitt);
            }
        }
        return abschnitte.values();
    }
}

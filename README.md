# Intro
Diese Anwendung ist im Rahmen von [opendatacamp2019](http://www.dresden.de/odcdresden19) entstanden.

# Warnung
Dieser Code ist schlecht. Bitte nicht kopieren oder schon garnicht versuchen, daran zu lernen! Alles auf eigene Gefahr

# Anwendung starten
## Frontend
```
frontend$ docker run --name dvblive -v $(pwd):/usr/share/nginx/html:ro -d -p 8000:80 nginx
```
In `dynamic.js` kann der json request gegen statische dateien, die auch eingecheckt sind, getauscht werden. Damit kann ohne Backend entwickelt werden.

## Backend
Vorraussetzungen
* Java 11
* Maven 3
```
backend$ mvn spring-boot:run
```
Es gibt keine echte Persistenz, es wird alles InMemory gehalten.
* Alle 60 minuten werden die initialdaten aktualisiert
* alle 30 Sekunden nach Beendigung der letzen aktualisierung werden die Haltestellendaten aktualisiert

# Datenquelle
https://register.opendata.sachsen.de/catalog/471/datasets/13

## Valider Request gegen die Livedaten API des VVO
```
curl -H 'Content-Type: text/xml' -d '@TripRequest.xml' http://efa.vvo-online.de:8080/std3/trias -o output-abfahrt.xml
```
see https://github.com/VDVde/TRIAS/issues/1

## Beispiele
Im Verzeichnis backend\xml gibt es Beispiel Requests und Responses

# Fragen und Weiterentwickelung
* Wir werden diese Anwendung nicht aktiv weiter entwickeln.
* Wenn es Fragen zu der Schnittstelle gibt, versuchen wir gerne diese zu beantworten. Einfach einen Issue einstellen
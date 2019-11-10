# Intro
Diese Anwendung ist im Rahmen von [opendatacamp2019](http://www.dresden.de/odcdresden19) entstanden.

# Warnung
Dieser Code ist schlecht. Bitte nicht kopieren oder schon garnicht versuchen, daran zu lernen! Alles auf eigene Gefahr

# Anwendung starten
## Frontend
```
frontend$ docker run --name dvblive -v $(pwd):/usr/share/nginx/html:ro -d -p 8000:80 nginx
```

## Backend
Vorraussetzungen
* Java 11
* Maven 3
```
backend$ mvn spring-boot:run
```

# Datenquelle
https://register.opendata.sachsen.de/catalog/471/datasets/13

## Valider Request gegen die Livedaten API des VVO
```
curl -H 'Content-Type: text/xml' -d '@TripRequest.xml' http://efa.vvo-online.de:8080/std3/trias -o output-abfahrt.xml
```
see https://github.com/VDVde/TRIAS/issues/1


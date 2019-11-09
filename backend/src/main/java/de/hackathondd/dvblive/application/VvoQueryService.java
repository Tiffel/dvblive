package de.hackathondd.dvblive.application;

import java.time.LocalDateTime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VvoQueryService {
    public static final String URL = "http://efa.vvo-online.de:8080/std3/trias";
    public static final String LOCATION_INFORMATION_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Trias xmlns:siri=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3"
            + ".org/2001/XMLSchema-instance\" version=\"1.2\" xmlns=\"http://www.vdv.de/trias\"\n"
            + "       xsi:schemaLocation=\"http://www.vdv.de/trias file:///C:/Develop/dvblive/trias-xsd-v1"
            + ".2/Trias.xsd\">\n"
            + "    <ServiceRequest>\n"
            + "        <siri:RequestTimestamp>{requestTime}</siri:RequestTimestamp>\n"
            + "        <siri:RequestorRef>OpenService</siri:RequestorRef>\n"
            + "        <RequestPayload>\n"
            + "            <LocationInformationRequest>\n"
            + "                <LocationRef>\n"
            + "                    <LocationName>\n"
            + "                        <Text>{LocationName}</Text>\n"
            + "                    </LocationName>\n"
            + "                </LocationRef>\n"
            + "                <Restrictions>\n"
            + "                    <Type>stop</Type>\n"
            + "                    <IncludePtModes>true</IncludePtModes>\n"
            + "                </Restrictions>\n"
            + "            </LocationInformationRequest>\n"
            + "        </RequestPayload>\n"
            + "    </ServiceRequest>\n"
            + "</Trias>";

    static HttpHeaders textXmlHeaders;
    static {
        textXmlHeaders = new HttpHeaders();
        textXmlHeaders.setContentType(MediaType.TEXT_XML);
    }


    private final RestTemplate restTemplate;

    public VvoQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String locationInformationRequest() {
        String location = "Dresden, Zwickauer Straße";
        String requestBody = LOCATION_INFORMATION_REQUEST
                .replace("{LocationName}", location)
                .replace("{requestTime}", LocalDateTime.now().toString());

        HttpEntity<String> request = new HttpEntity<>(requestBody, textXmlHeaders);

        ResponseEntity<String> response = restTemplate
                .exchange(URL, HttpMethod.POST, request, String.class);
        String result = response.getBody();

        return result;
    }
}

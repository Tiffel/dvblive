package de.hackathondd.dvblive.application;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import de.hackathondd.dvblive.domain.Linie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
    public static final String STOP_EVENT_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Trias version=\"1.2\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "  xsi:schemaLocation=\"http://www.vdv.de/trias ../../trias-xsd-v1.2/trias.xsd\">\n"
            + "  <ServiceRequest>\n"
            + "    <siri:RequestTimestamp>{requestTime}</siri:RequestTimestamp>\n"
            + "    <siri:RequestorRef>OpenService</siri:RequestorRef>\n"
            + "    <RequestPayload>\n"
            + "      <StopEventRequest>\n"
            + "        <Location>\n"
            + "          <LocationRef>\n"
            + "            <StopPointRef>{stopPointRef}</StopPointRef>\n"
            + "          </LocationRef>\n"
            + "          <DepArrTime>{DepArrTime}</DepArrTime>\n"
            + "        </Location>\n"
            + "        <Params>\n"
            + "          <NumberOfResults>16</NumberOfResults>\n"
            + "          <StopEventType>departure</StopEventType>\n"
            + "          <IncludePreviousCalls>false</IncludePreviousCalls>\n"
            + "          <IncludeOnwardCalls>false</IncludeOnwardCalls>\n"
            + "          <IncludeRealtimeData>true</IncludeRealtimeData>\n"
            + "        </Params>\n"
            + "      </StopEventRequest>\n"
            + "    </RequestPayload>\n"
            + "  </ServiceRequest>\n"
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

    private static String convertNodeToString(Node node) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter sw = new StringWriter();
            t.transform(new DOMSource(node), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String locationInformationRequest() {
        String location = "Dresden, Zwickauer Straße";
        String requestBody = LOCATION_INFORMATION_REQUEST
                .replace("{requestTime}", LocalDateTime.now().toString())
                .replace("{LocationName}", location);

        HttpEntity<String> request = new HttpEntity<>(requestBody, textXmlHeaders);

        ResponseEntity<String> response = restTemplate
                .exchange(URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    public Set<Linie> alleLinien() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "//PublishedLineName/Text[../../Mode/PtMode = 'tram']/text()";

        List<String> wichtigeHaltestellen = List
                .of("de:14612:13", "de:14612:5", "de:14612:7"); //Albertplatz, Pirnaischer Platz, Straßburger Platz
        String genericRequestBody = STOP_EVENT_REQUEST
                .replace("{requestTime}", LocalDateTime.now().toString())
                .replace("{DepArrTime}", "2019-11-09T12:00:00Z");

        Set<Linie> linien = new HashSet<>();

        for (String haltestelle : wichtigeHaltestellen) {
            String requestBody = genericRequestBody.replace("{stopPointRef}", haltestelle);
            HttpEntity<String> request = new HttpEntity<>(requestBody, textXmlHeaders);
            ResponseEntity<String> response = restTemplate
                    .exchange(URL, HttpMethod.POST, request, String.class);
            Document xml = parseXml(response.getBody());
            try {
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xml, XPathConstants.NODESET);
                for (String string : nodeListToStringList(nodeList)) {
                    //TODO: parse string to linie mit mehr eigenschaften
                    Linie linie = new Linie(Integer.parseInt(string));
                    linien.add(linie);
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
        return linien;
    }

    private List<String> nodeListToStringList(NodeList nodeList) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            strings.add(convertNodeToString(node));
        }
        return strings;
    }

    private Document parseXml(String input) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(input)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

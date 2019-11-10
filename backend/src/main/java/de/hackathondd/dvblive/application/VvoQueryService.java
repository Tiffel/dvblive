package de.hackathondd.dvblive.application;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import de.hackathondd.dvblive.domain.Haltestelle;
import de.hackathondd.dvblive.domain.Linie;
import de.hackathondd.dvblive.domain.inmemorydb.HaltestellenRepository;
import de.hackathondd.dvblive.domain.inmemorydb.LinienRepository;
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
            + "<Trias version=\"1.2\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\"\n"
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "  xsi:schemaLocation=\"http://www.vdv.de/trias ../../trias-xsd-v1.2/trias.xsd\">\n"
            + "  <ServiceRequest>\n"
            + "    <siri:RequestTimestamp>{requestTime}</siri:RequestTimestamp>\n"
            + "    <siri:RequestorRef>OpenService</siri:RequestorRef>\n"
            + "    <RequestPayload>\n"
            + "      <LocationInformationRequest>\n"
            + "        <LocationRef>\n"
            + "          <StopPointRef>{StopPointRef}</StopPointRef>\n"
            + "        </LocationRef>\n"
            + "        <Restrictions>\n"
            + "          <Type>stop</Type>\n"
            + "          <IncludePtModes>true</IncludePtModes>\n"
            + "        </Restrictions>\n"
            + "      </LocationInformationRequest>\n"
            + "    </RequestPayload>\n"
            + "  </ServiceRequest>\n"
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
    public static final String TRIP_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Trias version=\"1.2\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "       xsi:schemaLocation=\"http://www.vdv.de/trias file:///C:/Develop/dvblive/trias-xsd-v1.2/Trias"
            + ".xsd\">\n"
            + "  <ServiceRequest>\n"
            + "    <siri:RequestTimestamp>{requestTime}</siri:RequestTimestamp>\n"
            + "    <siri:RequestorRef>OpenService</siri:RequestorRef>\n"
            + "    <RequestPayload>\n"
            + "      <TripRequest>\n"
            + "        <Origin>\n"
            + "          <LocationRef>\n"
            + "            <StopPointRef>{originStopPointRef}</StopPointRef>\n"
            + "          </LocationRef>\n"
            + "          <DepArrTime>{DepArrTime}</DepArrTime>\n"
            + "        </Origin>\n"
            + "        <Destination>\n"
            + "          <LocationRef>\n"
            + "            <StopPointRef>{destinationStopPointRef}</StopPointRef>\n"
            + "          </LocationRef>\n"
            + "        </Destination>\n"
            + "        <Params>\n"
            + "          <IncludeTrackSections>true</IncludeTrackSections>\n"
            + "          <IncludeLegProjection>true</IncludeLegProjection>\n"
            + "          <IncludeIntermediateStops>true</IncludeIntermediateStops>\n"
            + "        </Params>\n"
            + "      </TripRequest>\n"
            + "    </RequestPayload>\n"
            + "  </ServiceRequest>\n"
            + "</Trias>";

    static HttpHeaders textXmlHeaders;

    static {
        textXmlHeaders = new HttpHeaders();
        textXmlHeaders.setContentType(MediaType.TEXT_XML);
    }

    private final RestTemplate restTemplate;
    private final HaltestellenRepository haltestellenRepository;
    private final LinienRepository linienRepository;

    public VvoQueryService(RestTemplate restTemplate,
            HaltestellenRepository haltestellenRepository,
            LinienRepository linienRepository) {
        this.restTemplate = restTemplate;
        this.haltestellenRepository = haltestellenRepository;
        this.linienRepository = linienRepository;
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

    private static List<String> nodeListToStringList(NodeList nodeList) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            strings.add(convertNodeToString(node));
        }
        return strings;
    }

    private static Document parseXml(String input) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(input)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<Linie> alleLinien() throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression liniennummerExpression = xPath.compile(
                "//*[local-name(.)='ServiceSection'][*[local-name(.)='Mode']/*[local-name(.)"
                        + "='PtMode']='tram']/*[local-name(.)='PublishedLineName']/*[local-name(.)='Text']/text()");
        XPathExpression startEndhaltestelleExpression = xPath.compile(
                "//*[local-name(.)='ServiceSection'][*[local-name(.)='Mode']/*[local-name(.)"
                        + "='PtMode']='tram']/*[local-name(.)='RouteDescription']/*[local-name(.)='Text']/text()");
        XPathExpression triasLiniennummerExpression = xPath.compile(
                "//*[local-name(.)='ServiceSection'][*[local-name(.)='Mode']/*[local-name(.)"
                        + "='PtMode']='tram']/*[local-name(.)='LineRef']/text()");
        XPathExpression triasStartHaltestelleCodeExpression = xPath.compile(
                "//*[local-name(.)='ServiceSection'][*[local-name(.)='Mode']/*[local-name(.)='PtMode']='tram']/."
                        + "./*[local-name(.)='OriginStopPointRef']/text()");
        XPathExpression triasEndHaltestelleCodeExpression = xPath.compile(
                "//*[local-name(.)='ServiceSection'][*[local-name(.)='Mode']/*[local-name(.)='PtMode']='tram']/."
                        + "./*[local-name(.)='DestinationStopPointRef']/text()");

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
                NodeList liniennummerNodelist = (NodeList) liniennummerExpression.evaluate(xml, XPathConstants.NODESET);
                NodeList startEndhaltestelleNodelist = (NodeList) startEndhaltestelleExpression
                        .evaluate(xml, XPathConstants.NODESET);
                NodeList triasLiniennummerNodelist = (NodeList) triasLiniennummerExpression
                        .evaluate(xml, XPathConstants.NODESET);
                NodeList triasStartHaltestelleCodeNodelist = (NodeList) triasStartHaltestelleCodeExpression
                        .evaluate(xml, XPathConstants.NODESET);
                NodeList triasEndHaltestelleCodeNodelist = (NodeList) triasEndHaltestelleCodeExpression
                        .evaluate(xml, XPathConstants.NODESET);

                List<String> stringList0 = nodeListToStringList(liniennummerNodelist);
                List<String> stringList1 = nodeListToStringList(startEndhaltestelleNodelist);
                List<String> stringList2 = nodeListToStringList(triasLiniennummerNodelist);
                List<String> stringList3 = nodeListToStringList(triasStartHaltestelleCodeNodelist);
                List<String> stringList4 = nodeListToStringList(triasEndHaltestelleCodeNodelist);
                for (int i = 0; i < stringList0.size(); i++) {
                    Linie linie = new Linie(
                            stringList0.get(i),
                            stringList1.get(i),
                            stringList2.get(i),
                            stringList3.get(i).replaceAll("([a-z]*:[0-9]*:[0-9]*):.*", "$1"),
                            stringList4.get(i).replaceAll("([a-z]*:[0-9]*:[0-9]*):.*", "$1")
                    );
                    linien.add(linie);
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
        for (Linie linie : linien) {
            List<String> haltestelleTriasCodes = haltestellenIds(linie);
            for (String haltestelleTriasCode : haltestelleTriasCodes) {
                if (!haltestellenRepository.exists(haltestelleTriasCode)) {
                    Haltestelle haltestelle = forHaltestelleId(haltestelleTriasCode);
                    haltestellenRepository.createOrupdateHaltestelle(haltestelle);
                }
                linie.addHaltestelle(haltestellenRepository.getHaltestelle(haltestelleTriasCode));
            }
        }
        linien.stream().forEach(linie -> linienRepository.createOrUpdateLinie(linie));
        return linienRepository.getAll();
    }

    private Haltestelle forHaltestelleId(String id) throws XPathExpressionException {
        String requestBody = LOCATION_INFORMATION_REQUEST
                .replace("{requestTime}", LocalDateTime.now().toString())
                .replace("{StopPointRef}", id);
        HttpEntity<String> request = new HttpEntity<>(requestBody, textXmlHeaders);

        ResponseEntity<String> response = restTemplate
                .exchange(URL, HttpMethod.POST, request, String.class);

        Document xml = parseXml(response.getBody());
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression longExpression = xPath
                .compile("//*[local-name(.)='GeoPosition']/*[local-name(.)='Longitude']/text()");
        NodeList longNodeList = (NodeList) longExpression.evaluate(xml, XPathConstants.NODESET);
        XPathExpression latExpression = xPath
                .compile("//*[local-name(.)='GeoPosition']/*[local-name(.)='Latitude']/text()");
        NodeList latNodeList = (NodeList) latExpression.evaluate(xml, XPathConstants.NODESET);
        return new Haltestelle(id, convertNodeToString(longNodeList.item(0)), convertNodeToString(latNodeList.item(0)));
    }

    private List<String> haltestellenIds(Linie linie) throws Exception {
        String requestBody = TRIP_REQUEST
                .replace("{requestTime}", LocalDateTime.now().toString())
                .replace("{DepArrTime}", "2019-11-09T12:00:00Z")
                .replace("{originStopPointRef}", linie.getTriasStartHaltestelleCode())
                .replace("{destinationStopPointRef}", linie.getTriasEndHaltestelleCode());
        HttpEntity<String> request = new HttpEntity<>(requestBody, textXmlHeaders);

        ResponseEntity<String> response = restTemplate
                .exchange(URL, HttpMethod.POST, request, String.class);
        Document xml = parseXml(response.getBody());

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExpression = xPath.compile(
                "//*[local-name(.)='TripResult'][1]/descendant::*[local-name(.)='TimedLeg']/*/*[local-name(.)"
                        + "='StopPointRef']/text()");
        NodeList nodeList = (NodeList) xPathExpression.evaluate(xml, XPathConstants.NODESET);
        List<String> stringList = nodeListToStringList(nodeList);
        return stringList.stream().map(s -> s.replaceAll("([a-z]*:[0-9]*:[0-9]*):.*", "$1"))
                .collect(Collectors.toList());
    }
}

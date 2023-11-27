package com.huawei.huaweicli.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.huawei.huaweicli.DTO.DiagnosticsSnapshot;
import com.huawei.huaweicli.StaticContext;

public class RequestHandler {

    private static final RestTemplate template = new RestTemplate();

    public static String getSession(String login, String pass) {
        String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<request>" +
                "<Username>" + login + "</Username>" +
                "<Password>" + pass + "</Password>" +
                "</request>";
        HttpEntity<String> request = new HttpEntity<>(body);
        ResponseEntity<String> response = template.exchange(StaticContext.getHost() + "api/user/login", HttpMethod.POST,
                request, String.class);
        if (response.getBody() == null || response.getBody().contains("error")) {
            return null;
        }
        HttpHeaders headers = response.getHeaders();
        return Objects.requireNonNull(headers.getFirst(HttpHeaders.SET_COOKIE)).split("=")[1];
    }

    public static String getDiagnosticsData() {
        ResponseEntity<String> response = null;
        while (response == null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Cookie", "SessionID_R3=" + StaticContext.getSession() + "; " + "cookie=maintenance%2C%2Cdiagnosis");
                HttpEntity<String> request = new HttpEntity<>(headers);
                response = template.exchange(StaticContext.getHost() + "api/device/diagnosis", HttpMethod.GET, request, String.class);
            } catch (Exception e) {
                String session = getSession(StaticContext.getLogin(), StaticContext.getPassword());
                if (session == null) {
                    throw new RuntimeException();
                }
                StaticContext.changeSession(session);
            }
        }

        return response.getBody();
    }

    public static DiagnosticsSnapshot getDataSnapshot() {
        String data = getDiagnosticsData();
        DocumentBuilder db = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        }
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(data));

        Document doc = null;
        try {
            doc = db.parse(is);
        } catch (SAXException | IOException e) {
            throw new RuntimeException();
        }
        NodeList nodes = doc.getElementsByTagName("response");

        Element element = (Element) nodes.item(0);

        NodeList rsrp = element.getElementsByTagName("rsrp");
        Element rsrpElement = (Element) rsrp.item(0);

        NodeList rsrq = element.getElementsByTagName("rsrq");
        Element rsrqElement = (Element) rsrq.item(0);

        NodeList rssi = element.getElementsByTagName("rssi");
        Element rssiElement = (Element) rssi.item(0);

        NodeList rscp = element.getElementsByTagName("rscp");
        Element rscpElement = (Element) rscp.item(0);

        return new DiagnosticsSnapshot(Integer.valueOf(getCharacterDataFromElement(rsrpElement)),
                Integer.valueOf(getCharacterDataFromElement(rsrqElement)),
                Integer.valueOf(getCharacterDataFromElement(rssiElement)),
                Integer.valueOf(getCharacterDataFromElement(rscpElement)));
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

}

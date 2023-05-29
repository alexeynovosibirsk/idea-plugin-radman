package com.nazarov.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String createFileName(String findParameter) {
        final String patternFormat = "ddMM-HHMMSS";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat).withZone(ZoneId.systemDefault());
        String timeStamp = formatter.format(Instant.now());
        StringBuilder stringBuilder = new StringBuilder(findParameter);
        stringBuilder.append("-");
        stringBuilder.append(timeStamp);
        stringBuilder.append(".rad");

        return stringBuilder.toString();
    }

    public static String getData(String findParameter, boolean url, boolean url_resolved, boolean name, boolean homepage,
                                 boolean countrycode, boolean language, boolean codec, boolean bitrate, boolean votes) throws IOException {
        //TODO: decide which limit is resonable, or how to switch it
        String limit = "50";
        String prefix = "https://nl1.api.radio-browser.info/json/stations/search?limit=10&name=";
        String genre = findParameter;
        String urlString = prefix + genre + "&hidebroken=true&order=clickcount&reverse=true";
        String result = parseJson(urlString, url, url_resolved, name, homepage, countrycode, language, codec, bitrate, votes);

        return result;
    }

    public static String parseJson(String stringUrl, boolean urlb, boolean url_resolvedb, boolean nameb, boolean homepageb,
                                   boolean countrycodeb, boolean languageb, boolean codecb, boolean bitrateb, boolean votesb) throws IOException {
        List<String> resultList = new ArrayList<>();
        String fieldName = "Unknown";
        String spltr = " | ";

        String homepage = "";
        String name = "";
        String url = "";
        String countrycode = "";
        String language = "";
        String codec = "";
        String bitrate = "";
        String votes = "";

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new URL(stringUrl));
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                fieldName = parser.getCurrentName();
                parser.nextToken();

                if ("url".equals(fieldName)) {
                    if (urlb) {
                        url = parser.getValueAsString() + spltr;
                    }
                } else if ("url_resolved".equals(fieldName)) {
                    if (url_resolvedb) {
                        url = parser.getValueAsString() + spltr;
                    }
                } else if ("name".equals(fieldName)) {
                    if (nameb) {
                        name = parser.getValueAsString() + spltr;
                    }
                } else if ("homepage".equals(fieldName)) {
                    if (homepageb) {
                        homepage = parser.getValueAsString() + spltr;
                    }
                } else if ("countrycode".equals(fieldName)) {
                    if (countrycodeb) {
                        countrycode = parser.getValueAsString() + spltr;
                    }
                } else if ("language".equals(fieldName)) {
                    if (languageb) {
                        language = "Lang:" + parser.getValueAsString() + spltr;
                    }
                } else if ("codec".equals(fieldName)) {
                    if (codecb) {
                        codec = "Codec:" + parser.getValueAsString() + spltr;
                    }
                } else if ("bitrate".equals(fieldName)) {
                    if (bitrateb) {
                        bitrate = "Bitrate:" + parser.getValueAsString() + spltr;
                    }
                } else if ("votes".equals(fieldName)) {
                    if (votesb) {
                        votes = "Votes:" + parser.getValueAsString() + spltr;
                    }
                    if ((!url.isBlank())) {
                        resultList.add(url + name + homepage + countrycode + language + codec + bitrate + votes);
                    }
                }
            }
        }
        //TODO: make as link on the plugins panel
        resultList.add("http://all.api.radio-browser.info/  <-- All codes of radio-browser here.");

        StringBuilder sb = new StringBuilder();
        for (
                String s : resultList) {
            sb.append(s + "\n");
        }

        return sb.toString();
    }

    private String webClient(String urlAsString) {

        WebClient client = WebClient.create();
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(urlAsString)
                .retrieve();
        String responseBody = responseSpec.bodyToMono(String.class).block();

        return responseBody;
    }

}

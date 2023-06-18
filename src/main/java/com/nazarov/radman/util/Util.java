package com.nazarov.radman.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.nazarov.radman.model.CommunityRadioBrowser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static int stationsFound;

    public static String createFileName(String findParameter) {
        LocalTime localTime = LocalTime.now();
        String timeStamp = localTime.toString()
                .replace(":", "")
                .split("\\.")[0];

        return findParameter + "-" + timeStamp + ".rad";
    }

    public static String getData(String findParameter, String limit, CommunityRadioBrowser crb) throws IOException {

        String prefix = "https://nl1.api.radio-browser.info/json/stations/search?limit=";
        String genre = "&name=" + findParameter;
        String urlString = prefix + limit +  genre + "&hidebroken=true&order=clickcount&reverse=true";

        return parseJson(urlString, crb);
    }

    public static String parseJson(String stringUrl, CommunityRadioBrowser crb) throws IOException {

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
                    if (crb.isUrl()) {
                        url = parser.getValueAsString() + " ";
                    }
                } else if ("url_resolved".equals(fieldName)) {
                    if (crb.isUrl_resolved()) {
                        url = parser.getValueAsString() + " ";
                    }
                } else if ("name".equals(fieldName)) {
                    if (crb.isName()) {
                        name =  spltr + parser.getValueAsString();
                    }
                } else if ("homepage".equals(fieldName)) {
                    if (crb.isHomepage()) {
                        homepage = spltr + parser.getValueAsString();
                    }
                } else if ("countrycode".equals(fieldName)) {
                    if (crb.isCountrycode()) {
                        countrycode = spltr + parser.getValueAsString();
                    }
                } else if ("language".equals(fieldName)) {
                    if (crb.isLanguage()) {
                        language = spltr + "Lang:" + parser.getValueAsString();
                    }
                } else if ("codec".equals(fieldName)) {
                    if (crb.isCodec()) {
                        codec = spltr + "Codec:" + parser.getValueAsString();
                    }
                } else if ("bitrate".equals(fieldName)) {
                    if (crb.isBitrate()) {
                        bitrate = spltr + "Bitrate:" + parser.getValueAsString();
                    }
                } else if ("votes".equals(fieldName)) {
                    if (crb.isVotes()) {
                        votes = spltr + "Votes:" + parser.getValueAsString();
                    }
                    if ((!url.isBlank())) {
                        resultList.add(url + name + homepage + countrycode + language + codec + bitrate + votes);
                    }
                }
            }
        }
        stationsFound = 0;

        StringBuilder sb = new StringBuilder();
        for (
                String s : resultList) {
            sb.append(s + "\n");
            stationsFound++;
        }

        return sb.toString();
    }

    public static int getStationsFound() {
        return stationsFound;
    }
}

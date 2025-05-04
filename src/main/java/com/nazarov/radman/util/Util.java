package com.nazarov.radman.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.PermanentInstallationID;
import com.nazarov.radman.model.CommunityRadioBrowser;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Util {
    private static int stationsFound;

    public static void main(String[] args) {
        try {
            System.out.println(Util.getDataFromRadioBrowser("jazz", "3", new CommunityRadioBrowser()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonParser getParser(String urlString) throws Exception {
        URL url = new URI(urlString).toURL();
        JsonFactory factory = new JsonFactory();
        return factory.createParser(url);
    }

    public static String getDataFromRadioBrowser(String findParameter, String limit, CommunityRadioBrowser crb) throws IOException {
        JsonParser parser = null;
        List<String> urlPrefix = new ArrayList<>(Arrays.asList("https://de1", "https://de2", "https://fi1"));
        String params = ".api.radio-browser.info/json/stations/search?limit=" + limit + "&name=" + findParameter + "&hidebroken=true&order=clickcount&reverse=true";
        String urlString = urlPrefix.get(0) + params;
        String url = "http://na3arov.ru/genre?genre=" + findParameter;
        sendStatistic(url);


        try {
            parser = getParser(urlString);
        } catch (Exception e) {
            urlString = urlPrefix.get(1) + params;
            try {
                parser = getParser(urlString);
            } catch (Exception ex) {
                urlString = urlPrefix.get(2) + params;
                try {
                    parser = getParser(urlString);
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        }

        return parseJson(crb, parser);
    }

    public static String parseJson(CommunityRadioBrowser crb, JsonParser parser) throws IOException {
        stationsFound = 0;
        StringBuilder sb = new StringBuilder();
        String spltr = " | ";
        String homepage = "";
        String name = "";
        String url = "";
        String countrycode = "";
        String bitrate = "";

        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.currentName();
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
                        name = spltr + parser.getValueAsString();
                    }
                } else if ("homepage".equals(fieldName)) {
                    if (crb.isHomepage()) {
                        homepage = spltr + parser.getValueAsString();
                    }
                } else if ("countrycode".equals(fieldName)) {
                    if (crb.isCountrycode()) {
                        countrycode = spltr + parser.getValueAsString();
                    }
                } else if ("bitrate".equals(fieldName)) {
                    if (crb.isBitrate()) {
                        bitrate = spltr + "Bitrate:" + parser.getValueAsString();
                    }
                    if ((!url.isBlank())) {
                        sb.append(url).append(name).append(homepage).append(countrycode).append(bitrate).append("\n");
                        stationsFound++;
                    }
                }
            }
        }

        return sb.toString();
    }

    public static int getStationsFound() {
        return stationsFound;
    }

    public static Runnable runOnce() {
        return new Runnable() {
            @Override
            public void run() {
                ApplicationInfo info = ApplicationInfo.getInstance();
                String fullAppName = info.getFullApplicationName().replaceAll(" ", "-");
                String uuid = PermanentInstallationID.get().replaceAll("-", "");
                String url = "http://na3arov.ru/ide?uuid=" + uuid + "&vendor=" + fullAppName;
                sendStatistic(url);
            }
        };
    }

    public static void sendStatistic(String url) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        CompletableFuture<HttpResponse<String>> futureResp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            futureResp.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}




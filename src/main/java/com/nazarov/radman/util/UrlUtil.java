package com.nazarov.radman.util;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UrlUtil {

    Map<String, String> resultMap = new HashMap<>();

//    TODO: will be used to check links
    public Map<String, String> getHeader(String radioUrl) {
        URLConnection urlConnection = null;
        try {
            URL url = new URL(radioUrl);
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.println(headerName + " " + value);
                resultMap.put(headerName, value);
            }
        }

        return resultMap;
    }

    public static URL makeUrl(String string) {
        URL url;
        try {
            url = new URL(string);
        } catch (
                MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return url;
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

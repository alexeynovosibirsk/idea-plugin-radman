package com.nazarov.radman.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckHeader {

    private static final Map<String, String> resultMap = new HashMap<>();
    private static final int CONNECT_TIMEOUT = 2000;
    private static final int READ_TIMEOUT = 1700;


    public static void main(String[] args) {
        System.out.println("Result: " + CheckHeader.isAudioStream("http://online.radiojazz.ua/RadioJazz"));
    }

    //TODO: реализовать просмотр хедеров
    public static void printHeader(String url) {
        if (isAudioStream(url)) {

            for (Map.Entry<String, String> localMap : resultMap.entrySet()) {
                String headerName = localMap.getKey();
                String value = localMap.getValue();
                System.out.println(headerName + " = " + value);
            }
        }
    }

    //TODO: монструозную вложенность переработать
    public static boolean isAudioStream(String url) {
        getHeader(url);

        if (resultMap.size() > 10) {
            String contentType;

            try {
                contentType = resultMap.get("Content-Type").toLowerCase();
            } catch (NullPointerException npe) {
                try {
                    contentType = resultMap.get("content-type").toLowerCase();
                } catch (NullPointerException npex) {
                    return false;
                }
            }

            return contentType.contains("audio");
        }

        return false;
    }

    public static Map<String, String> getHeader(String radioUrl) {
        if (!resultMap.isEmpty()) {
            resultMap.clear();
        }

        Map<String, List<String>> headers = new HashMap<>();
        URLConnection urlConnection = null;
        URL url = UrlUtil.makeUrl(radioUrl);

        if (url != null) {
            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (urlConnection != null) {
                urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                headers = urlConnection.getHeaderFields();
            }

            if (!headers.isEmpty()) {
                Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
                for (Map.Entry<String, List<String>> entry : entrySet) {
                    String headerName = entry.getKey();
                    List<String> headerValues = entry.getValue();
                    for (String value : headerValues) {
                        if (value.contains("404")) {
                            resultMap.put(headerName, value);
                            return resultMap;
                        }
                        resultMap.put(headerName, value);
                    }
                }
            }
        }

        return resultMap;
    }

}

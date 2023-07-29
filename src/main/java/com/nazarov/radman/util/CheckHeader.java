package com.nazarov.radman.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckHeader {

    public static void main(String[] args) {
        CheckHeader checkUrl = new CheckHeader();
        boolean b = checkUrl.isAudoStreamUrl("http://edge126.rdsnet.ro:84/profm/music-fm.mp3");
        System.out.println(">>>" + b);
    }

    private static final Map<String, String> resultMap = new HashMap<>();

    //TODO: реализовать просмотр хедеров
    public static void printHeader(String url) {
        if (isAudoStreamUrl(url)) {

            for (Map.Entry<String, String> localMap : resultMap.entrySet()) {
                String headerName = localMap.getKey();
                String value = localMap.getValue();
                System.out.println(headerName + " = " + value);
            }
        }
    }

    public static boolean isAudoStreamUrl(String url) {
        getHeader(url);
        if ((resultMap.size() < 10) || !resultMap.get("Content-Type").contains("audio")) {

            return false;
        }

        return true;
    }

    public static Map<String, String> getHeader(String radioUrl) {

        URLConnection urlConnection = null;
        URL url = UrlUtil.makeUrl(radioUrl);

        if (url != null) {
            try {
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
                    if (value.contains("404")) {
                        resultMap.put(headerName, value);
                        return resultMap;
                    }
                    resultMap.put(headerName, value);
                }
            }
        }

        return resultMap;
    }

}

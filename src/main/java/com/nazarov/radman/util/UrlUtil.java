package com.nazarov.radman.util;

import com.nazarov.radman.message.ShowMsg;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.URL;

public class UrlUtil {

    public static void main(String[] args) {
        String url = "http://n13.radiojar.com/n4dzb3znn3quv.mp3?rj-ttl=5&rj-tok=AAABiUPmj9EAh_CcxCzF2NKckQ";
        System.out.println(urlValidator(url));
    }

    public static boolean urlValidator(String string) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        return urlValidator.isValid(string);
    }

    public static URL makeUrl(String string) {

            URL url = null;

            try {
                if (urlValidator(string)) {
                    url = new URL(string);
                } else {
                    ShowMsg.UrlIsNotValid();
                }

            } catch (Exception e) {
                ShowMsg.MessagedError("MalformedURLException. There is an error while getting URL from String.");
            }

            return url;
    }

    //TODO: Use when getting url streams from some new resources
//    private String webClient(String urlAsString) {
//
//        WebClient client = WebClient.create();
//        WebClient.ResponseSpec responseSpec = client.get()
//                .uri(urlAsString)
//                .retrieve();
//        String responseBody = responseSpec.bodyToMono(String.class).block();
//
//        return responseBody;
//    }
}

package com.nazarov.radman.util;

import com.nazarov.radman.message.ShowMsg;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

public class UrlUtil {

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
                ShowMsg.UnknownError();
            }

            return url;
    }

    //TODO: Use when getting url streams from some new resources
    private String webClient(String urlAsString) {

        WebClient client = WebClient.create();
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(urlAsString)
                .retrieve();
        String responseBody = responseSpec.bodyToMono(String.class).block();

        return responseBody;
    }
}

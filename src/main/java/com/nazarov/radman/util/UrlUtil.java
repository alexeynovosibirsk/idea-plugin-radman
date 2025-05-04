package com.nazarov.radman.util;

import com.nazarov.radman.message.ShowMsg;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.URI;
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
                    url = new URI(string).toURL();
                } else {
                    ShowMsg.dialog(ShowMsg.URL_IS_NOT_VALID + url, ShowMsg.URL_IS_NOT_VALID_TITLE);
                }
            } catch (Exception e) {
                ShowMsg.messagedError(ShowMsg.MALFORMED_URL_EXCEPTION);
            }

            return url;
    }

}

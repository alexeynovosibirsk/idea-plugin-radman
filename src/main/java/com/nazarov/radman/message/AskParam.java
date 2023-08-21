package com.nazarov.radman.message;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.nazarov.radman.validate.CharacterValidator;
import com.nazarov.radman.validate.IntegerValidator;
import icons.Icons;

public class AskParam {

    public static int askBitrate() {
        IntegerValidator integerValidator = new IntegerValidator();
        String input = showParams("Input bitrate:", "Delete Url With Bitrate Lower Than", "", integerValidator);
        int bitrateValue = 0;
        if(input != null) {
            bitrateValue = Integer.parseInt(input);
        }

        return bitrateValue;
    }

    public static String askLanguage() {
        CharacterValidator characterValidator = new CharacterValidator();
        String input = showParams("Input language:", "Delete Url That Use Particulary Language", "", characterValidator);
        String language = null;
        if(input != null) {
            language = input;
        }

        return language;
    }

    public static String askExceptLanguage() {
        CharacterValidator characterValidator = new CharacterValidator();
        String input = showParams("Input languages via whitespace:", "Delete Url Except using Languages", "russian american english", characterValidator);
        String language = null;
        if(input != null) {
            language = input;
        }

        return language;
    }


    private static String showParams(String message,
                                    String title,
                                    String value,
                                    InputValidator validator ) {

        return Messages.showInputDialog(message, title, Icons.Headphones_icon, value, validator);
    }

}

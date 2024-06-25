package com.nazarov.radman.message;

import com.intellij.openapi.ui.Messages;
import icons.Icons;

public class ShowMsg {
    public static final String MALFORMED_URL_EXCEPTION = "MalformedURLException. There is an error while getting URL from String.";
    public static final String RESULT = "Radlist completed:";
    public static final String REQUEST_COMPLETED = "Request Completed.";
    public static final String URL_IS_NOT_VALID = "Url is not valid or this is not an url: ";
    public static final String URL_IS_NOT_VALID_TITLE = "Url Is Not Valid!";
    public static final String NOTHING_IS_PLAYED = "Nothing is played. No receiving stream.";
    public static final String NOTHING_IS_PLAYED_TITLE = "Nothing to stop";
    public static final String HIGHLIGHT_THE_LINK = "In order to open an url link you have to highlight the link via cursor!";
    public static final String HIGHLIGHT_THE_LINK_TITLE = "Highlight The Link";
    public static final String QUERY_PARAMETER_IS_MISSED = "Please fill the genre";
    public static final String QUERY_PARAMETER_IS_MISSED_TITLE = "Query Parameter Is Missed!";
    public static final String ERROR_TITLE = "The Error!";
    public static final String NOTHING_TO_PLAY = "There is nothing to play";

    public static void dialog(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }
    public static void messagedError(String msg) {
            error(msg, ERROR_TITLE);
    }
    private static void error(String msg, String title) {
        Messages.showErrorDialog(msg, title);
    }

    public static void debug(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }

    public static void debug(String msg) {
        debug(msg, "Debug message:");
    }

}

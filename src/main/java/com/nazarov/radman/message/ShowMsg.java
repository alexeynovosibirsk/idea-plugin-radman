package com.nazarov.radman.message;

import com.intellij.openapi.ui.Messages;
import icons.Icons;

public class ShowMsg {

    public static void UrlIsNotValidAndWillBeDeleted() {
        error("Url is not streaming audio and will be deleted", "Not Streaming Audio");    }
    public static void UrlIsNotValid() {
        error("Url is not valid or this is not an url", "Url Is Not Valid!");
    }
    public static void UrlIsNotValid(String msg) {
        error (msg, "Url Is Not Valid!");
    }

    public static void UnknownError() {
        error("There is an error while getting URL from String. ", "Unknown Error!");
    }
    public static void UrlIsOk() { dialog("Url is Ok", "Streaming Audio"); }
    public static void NothingIsPlayed() {
        dialog("Nothing is played", "Not Found Radio for Stopping!");
    }
    public static void HighlightTheLink() {
        dialog("In order to open an url link you have to highlight the link via cursor!", "Highlight The Link"); }
    public static void RequestCompleted(String msg) {
        dialog(msg, "Request Completed.");
    }
    public static void QueryParameterMissed() {
        dialog("Please fill the genre", "Query Parameter Missed!");
    }

    private static void error(String msg, String title) {
        Messages.showErrorDialog(msg, title);
    }
    private static void dialog(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }

    public static void debug(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }


}

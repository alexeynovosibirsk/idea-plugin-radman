package com.nazarov.radman.message;

import com.intellij.openapi.ui.Messages;
import icons.Icons;

public class ShowMsg {

    public static void UrlIsNotValid() {
        dialog("Url is not valid or this is not an url", "Url Is Not Valid!");  }
    public static void Result(String result) { dialog(result, "Radlist completed:"); }
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

    public static void MessagedError(String msg) {
        if (msg != null) {
            error(msg, "The Error!");
        } else {
            error("There is some error... ", "Unknown Error!");
        }
    }

    private static void error(String msg, String title) {
        Messages.showErrorDialog(msg, title);
    }

    private static void dialog(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }

    public static void inProgressMsg(String s, String m) {
        Messages.showInfoMessage(s, m);
    }

    public static void debug(String msg, String title) {
        Messages.showMessageDialog(msg, title, Icons.Headphones_icon);
    }

    public static void debug(String msg) {
        debug(msg, "Debug message:");
    }

}

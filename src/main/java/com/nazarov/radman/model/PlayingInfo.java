package com.nazarov.radman.model;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.LogicalPosition;
import java.net.URL;

/**
 * Information about current playing objects:
 * url streaming audio radio station url
 * playingFile *.rad file from playing station
 * radioStationInfo all string except audio stream url
 * station description and psi file.
 */
public class PlayingInfo {

    private static PlayingInfo INSTANCE = null;
    private static URL url;
    private static String playingFile;
    private static String radioStationInfo;
    private static Caret primaryCaret;
    private static LogicalPosition cursorPosition;


    private PlayingInfo() {
    }
    public static PlayingInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayingInfo();
        }
        return INSTANCE;
    }

    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }
    public String getPlayingFile() {
        return playingFile;
    }
    public void setPlayingFile(String string) {
        playingFile = string;
    }
    public String getRadioStationInfo() { return radioStationInfo; }
    public void setRadioStationInfo(String string) { radioStationInfo = string; }
    public static Caret getPrimaryCaret() { return primaryCaret; }
    public static void setPrimaryCaret(Caret caret) {
        primaryCaret = caret;
        cursorPosition = primaryCaret.getLogicalPosition();
    }
    public static LogicalPosition getCursorPosition() { return cursorPosition; }

}

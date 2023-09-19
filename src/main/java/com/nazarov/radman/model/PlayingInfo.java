package com.nazarov.radman.model;

import java.net.URL;

/**
 * Information about url, station description and psi file.
 */

public class PlayingInfo {

    private static PlayingInfo INSTANCE = null;
    private static URL url;
    private static String playingFile;
    private static String nowPlayingInfo;

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

    public void setPlayingFile(String playingFile) {
        this.playingFile = playingFile;
    }

    public String getNowPlayingInfo() {
        return nowPlayingInfo;
    }

    public void setNowPlayingInfo(String nowPlayingInfo) {
        this.nowPlayingInfo = nowPlayingInfo;
    }
}

package com.nazarov.radman.util.audio;

import com.nazarov.radman.model.PlayingInfo;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StationPlayer {

    private static InputStream is = null;
    private static Future<?> future = null;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static boolean isPlayed = false;
    private static final PlayingInfo playingInfo = PlayingInfo.getInstance();

    public static boolean getStatus() {
        return isPlayed;
    }

    public static void play() {
        isPlayed = true;
        future = executorService.submit(() ->
                urlPlayer(open(playingInfo.getUrl())));
    }

    public static void stopPlay() {
        inputStreamClose();
        if (future != null) {
            future.cancel(true);
        }
        isPlayed = false;
    }

    private static InputStream open(URL url) {
        try {
            is = url.openStream();
        } catch (IOException e) {
            System.out.println("Unable to open " + url + ": " + e.getCause());
        }
        return is;
    }

    private static void inputStreamClose() {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("Unable to close input steam:" + e.getCause());
            }
        }
    }

    public static void urlPlayer(InputStream url) {
        try {
            AACPlayer.decodeAAC(url);
        } catch (Exception e) {
            try {
                AdvancedPlayer advancedPlayer = new AdvancedPlayer(url);
                advancedPlayer.play();
            } catch (JavaLayerException ex) {
//            ex.printStackTrace();
            }
        }
    }

}
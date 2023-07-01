package com.nazarov.radman.util.audio;

import com.nazarov.radman.action.PlayAction;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

public class StationPlayer extends Thread {
    Thread thread = new Thread();
    private boolean isPlayed = false;
    private static StationPlayer INSTANCE = null;

    private StationPlayer() {
    }

    public static StationPlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StationPlayer();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        urlPlayer(PlayAction.getUrl());
    }

    public boolean getStatus() {
        return isPlayed;
    }

    public void play() {
        isPlayed = true;
        thread = new StationPlayer();
        thread.start();
    }

    public void stopPlay() {
        thread.stop();
        isPlayed = false;
    }

    public void urlPlayer(URL url) {
        try {
            AACPlayer.decodeAAC(url.openStream());
        } catch (Exception e) {
            try {
                AdvancedPlayer advancedPlayer = new AdvancedPlayer(url.openStream());
                advancedPlayer.play();

            } catch (JavaLayerException | ConnectException | FileNotFoundException ex) {
                ex.printStackTrace();
                e.printStackTrace();

            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

}
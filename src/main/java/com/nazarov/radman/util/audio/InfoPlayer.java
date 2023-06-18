package com.nazarov.radman.util.audio;



import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InfoPlayer {

    InputStream is = null;

    public InfoPlayer() {
        play(haha());
    }

    private InputStream haha() {
        InfoPlayer i = new InfoPlayer();
        Class c = i.getClass();
        URL f = c.getResource("sounds/urlisnotvalid.mp3");

        try {
        is = f.openStream();

    } catch (
    IOException e) {
        e.printStackTrace();
    }
        return is;
}

    private void play(InputStream inputStream) {

        try {
            AdvancedPlayer advancedPlayer = new AdvancedPlayer(inputStream);
            advancedPlayer.play();

        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}

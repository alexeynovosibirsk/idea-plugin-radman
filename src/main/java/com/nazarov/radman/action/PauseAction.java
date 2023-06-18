package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.nazarov.radman.RadMan;
import com.nazarov.radman.util.audio.StationPlayer;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class PauseAction extends AnAction {
    StationPlayer stationPlayer;

    @Override
    public void actionPerformed(AnActionEvent e) {
        stationPlayer = StationPlayer.getInstance();
        boolean played = stationPlayer.getStatus();
        URL url = PlayAction.getUrl();
        if (url == null) {
            Messages.showMessageDialog("Nothing is played", "Not Found Radio for Stopping!", Icons.Headphones_icon);
        }
        if (played) {
            stationPlayer.stopPlay();
            RadMan.setNowPlayingFile("nothing...");
        } else {
            String playingFile = PlayAction.getPlayingFile();
            RadMan.setNowPlayingFile(playingFile);

            PlayAction.setUrl(url);
            stationPlayer.play();
        }

    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        stationPlayer = StationPlayer.getInstance();
        boolean played = stationPlayer.getStatus();
        // Set visibility
        //   e.getPresentation().setEnabledAndVisible(played);
    }

}

package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.panel.PlayPanel;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class PauseAction extends AnAction {
    //AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        StationPlayer stationPlayer = StationPlayer.getInstance();
        boolean played = stationPlayer.getStatus();
        URL url = PlayAction.getUrl();
        if (url == null) {
            ShowMsg.NothingIsPlayed();
        }
        if (played) {
            stationPlayer.stopPlay();
            PlayPanel.setNowPlayingFile("nothing...");
            PlayPanel.setNowPlayingUrl("");
        } else {
            String playingFile = PlayAction.getPlayingFile();
            PlayPanel.setNowPlayingFile(playingFile);
            String playingUrl = PlayAction.getNowPlayingUrl();
            PlayPanel.setNowPlayingUrl(playingUrl);

            PlayAction.setUrl(url);
            stationPlayer.play();
        }
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad"));
    }

}

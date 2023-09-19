package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.model.PlayingInfo;
import com.nazarov.radman.panel.PlayPanel;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.Metadata;
import com.nazarov.radman.util.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class PauseAction extends AnAction {
    //AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PlayingInfo playingInfo = PlayingInfo.getInstance();
        Metadata metadata = Metadata.getInstance();
        StationPlayer stationPlayer = StationPlayer.getInstance();
        boolean played = stationPlayer.getStatus();
        URL url = playingInfo.getUrl();
        if (url == null) {
            ShowMsg.dialog(ShowMsg.NOTHING_IS_PLAYED, ShowMsg.NOTHING_IS_PLAYED_TITLE);
        }
        if (played) {
            stationPlayer.stopPlay();
            //TODO: Think about to invoke the below objects without setters
            PlayPanel.setNowPlayingFile(PlayPanel.NOTHING_IS_PLAYED);
            PlayPanel.setNowPlayingUrl(PlayPanel.CLEAR);
            PlayPanel.setMetadata(PlayPanel.CLEAR);
            metadata.stopMetadata();
        } else {
            String playingFile = playingInfo.getPlayingFile();
            PlayPanel.setNowPlayingFile(playingFile);
            String playingUrl = playingInfo.getNowPlayingInfo();
            PlayPanel.setNowPlayingUrl(playingUrl);

            playingInfo.setUrl(url);
            stationPlayer.play();

            metadata.startMetadata();
        }
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad"));
    }

}

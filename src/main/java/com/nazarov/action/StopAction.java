package com.nazarov.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

public class StopAction extends AnAction {
    StationPlayer stationPlayer;

    @Override
    public void actionPerformed(AnActionEvent e) {
        stationPlayer = StationPlayer.getInstance();
        stationPlayer.stopPlay();
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        stationPlayer = StationPlayer.getInstance();
        boolean played = stationPlayer.getStatus();
        // Set visibility
        e.getPresentation().setEnabledAndVisible(played);
    }

}

package com.nazarov.radman.action.play;

import com.intellij.ide.util.RunOnceUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.psi.PsiFile;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.model.PlayingInfo;
import com.nazarov.radman.panel.PlayPanel;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.Metadata;
import com.nazarov.radman.util.UrlUtil;
import com.nazarov.radman.util.Util;
import com.nazarov.radman.util.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Action "Play"
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */
public class PlayAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //TODO: подумать периодичность LocalDate localDate = LocalDate.now();
        RunOnceUtil.runOnceForApp("id3", Util.runOnce());

        PlayingInfo playingInfo = PlayingInfo.getInstance();
        Caret primaryCaret = ActionUtil.getPrimaryCaret(e);
        PlayingInfo.setPrimaryCaret(primaryCaret);

        String allLineUnderCursor = ActionUtil.getStringUnderCursor(e);
        String[] allLine = allLineUnderCursor.split(" ", 2);

        if (allLine.length == 1) {
            ShowMsg.dialog(ShowMsg.NOTHING_TO_PLAY, ShowMsg.ERROR_TITLE);
        } else {
            playingInfo.setRadioStationInfo(allLine[1]);
            PlayPanel.setNowPlayingUrl(playingInfo.getRadioStationInfo());
            String urlAsString = allLine[0];
            playingInfo.setUrl(UrlUtil.makeUrl(urlAsString));

            PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
            if (psiFile != null) {
                PlayPanel.setPsiFile(psiFile);
                playingInfo.setPlayingFile(psiFile.getName());
                PlayPanel.setNowPlayingFile(playingInfo.getPlayingFile());
            }

            StationPlayer.stopPlay();
            Metadata.stopMetadata();
            StationPlayer.play();
            Metadata.startMetadata();
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad"));
    }

}

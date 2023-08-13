package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.psi.PsiFile;
import com.nazarov.radman.panel.PlayPanel;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.UrlUtil;
import com.nazarov.radman.util.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class PlayAction extends AnAction {

    private static final String EXTENSION = "rad";

    private static URL url;
    private static String playingFile;
    private static String nowPlayingUrl;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Caret primaryCaret = ActionUtil.getPrimaryCaret(e);
        VisualPosition visualPosition = primaryCaret.getVisualPosition();
        setLineAndColumn(visualPosition.getLine(), visualPosition.getColumn());

        String allLineUnderCursor = ActionUtil.getStringUnderCursor(e);

        nowPlayingUrl = allLineUnderCursor.split(" ", 2)[1];
        PlayPanel.setNowPlayingUrl(nowPlayingUrl);

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile != null) {
            PlayPanel.setPsiFile(psiFile);
            playingFile = psiFile.getName();
            PlayPanel.setNowPlayingFile(playingFile);
        }

        String selectedUrl = allLineUnderCursor.split(" ")[0];
        url = UrlUtil.makeUrl(selectedUrl);
        StationPlayer stationPlayer = StationPlayer.getInstance();
        stationPlayer.stopPlay();
        stationPlayer.play();
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals(EXTENSION)
        );
    }

    public static URL getUrl() {
        return url;
    }

    public static void setUrl(URL url) {
        PlayAction.url = url;
    }

    public static String getPlayingFile() {
        return playingFile;
    }

    public static String getNowPlayingUrl() {
        return nowPlayingUrl;
    }

    public static void setLineAndColumn(int line, int column) { // put cursor to the line of playing station
        PlayPanel.setLineAndColumn(line, column);
    }

}

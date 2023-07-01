package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.nazarov.radman.RadMan;
import com.nazarov.radman.util.UrlUtil;
import com.nazarov.radman.util.audio.StationPlayer;

import java.net.URL;

public class PlayAction extends AnAction {
    private static URL url;
    private static String playingFile;
    private static String nowPlayingUrl;

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();

        VisualPosition visualPosition = primaryCaret.getVisualPosition();
        setLineAndColumn(visualPosition.getLine(), visualPosition.getColumn());

        int start = primaryCaret.getVisualLineStart();
        int end = primaryCaret.getVisualLineEnd();
        TextRange textRange = new TextRange(start, end);

        Document document = editor.getDocument();

        String allLineUnderCursor = document.getText(textRange);
        nowPlayingUrl = allLineUnderCursor.split(" ", 2)[1];
        RadMan.setNowPlayingUrl(nowPlayingUrl);

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile != null) {
            RadMan.setPsiFile(psiFile);
            playingFile = psiFile.getName();
            RadMan.setNowPlayingFile(playingFile);
        }

        String selectedUrl = allLineUnderCursor.split(" ")[0];
        url = UrlUtil.makeUrl(selectedUrl);
        StationPlayer stationPlayer = StationPlayer.getInstance();
        stationPlayer.stopPlay();
        stationPlayer.play();
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
        RadMan.setLineAndColumn(line, column);
    }

}

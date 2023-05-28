package com.nazarov.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.nazarov.audio.StationPlayer;
import java.net.MalformedURLException;
import java.net.URL;

public class PlayAction extends AnAction {

    private static URL url;
    private static StationPlayer stationPlayer;

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getVisualLineStart();
        int end = primaryCaret.getVisualLineEnd();
        TextRange textRange = new TextRange(start, end);
        //TODO: сделать экшн НауПлейинг - брать стартовую позицию каретки и устанавливать туда курсор.

        String selectedUrl = document.getText(textRange).split(" ")[0];
        makeUrl(selectedUrl);
        stationPlayer = StationPlayer.getInstance();
        stationPlayer.stopPlay();
        stationPlayer.play();
    }

    public  URL makeUrl(String string) {
        try {
            url = new URL(string);
        } catch (
                MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    public static URL getUrl() {
        return url;
    }

}

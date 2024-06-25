package com.nazarov.radman.action.play;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.nazarov.radman.model.PlayingInfo;
import com.nazarov.radman.panel.PlayPanel;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.Metadata;
import com.nazarov.radman.util.UrlUtil;
import com.nazarov.radman.util.audio.StationPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Action PlayNextStation
 * Apparently will be used in next releases
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */

public class PlayNextAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        int check;
        PlayingInfo playingInfo = PlayingInfo.getInstance();
        Caret caret = PlayingInfo.getPrimaryCaret();
        VisualPosition visualPosition = getCursorPosition(caret);
        TextRange textRange = getTextRange(e, visualPosition);
        check = textRange.getEndOffset() - textRange.getStartOffset();
        //TODO: Check that string is not empty
        while (check < 11) {
            visualPosition = new VisualPosition(visualPosition.line + 1, 0);
            textRange = getTextRange(e, visualPosition);
            check = textRange.getEndOffset() - textRange.getStartOffset();
        }
        Document document = ActionUtil.getDocument(e);
        String allLineUnderCursor = document.getText(textRange);
        String[] allLine = allLineUnderCursor.split(" ", 2);
        playingInfo.setRadioStationInfo(allLine[1]);
        PlayPanel.setNowPlayingUrl(playingInfo.getRadioStationInfo());
        String urlAsString = allLine[0];

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile != null) {
            PlayPanel.setPsiFile(psiFile);
            playingInfo.setPlayingFile(psiFile.getName());
            PlayPanel.setNowPlayingFile(playingInfo.getPlayingFile());
        }

        playingInfo.setUrl(UrlUtil.makeUrl(urlAsString));
        StationPlayer stationPlayer = StationPlayer.getInstance();
        stationPlayer.stopPlay();
        stationPlayer.play();

        Metadata metadata = Metadata.getInstance();
        metadata.startMetadata();
    }

    private static VisualPosition getCursorPosition(Caret caret) {
        return caret.getVisualPosition();
    }

    private static TextRange getTextRange(@NotNull AnActionEvent e, VisualPosition position) {
        Editor editor = ActionUtil.getEditor(e);
        CaretModel caretModel = editor.getCaretModel();
        VisualPosition newVisualPosition = getNewVisualPosition(e, position);
        Caret newCaret = caretModel.addCaret(newVisualPosition);
        int start = 0;
        int end = 0;
        // Check that string is not last empty string
        try {
            newCaret.getVisualLineStart();
            start = newCaret.getVisualLineStart();
            end = newCaret.getVisualLineEnd();
        } catch (NullPointerException npe) {
            VisualPosition v = new VisualPosition(newVisualPosition.line + 1, 0);
            newVisualPosition = getNewVisualPosition(e, v);
            newCaret = caretModel.addCaret(newVisualPosition);
            start = newCaret.getVisualLineStart();
            end = newCaret.getVisualLineEnd();
        }
        PlayingInfo.setPrimaryCaret(newCaret);
        ActionUtil.removeAllCarets(caretModel);

        return new TextRange(start, end);
    }

    @NotNull
    private static VisualPosition getNewVisualPosition(@NotNull AnActionEvent e, VisualPosition position) {
        int totalStrings = ActionUtil.getLinesTotal(e);
        int lineNumber = position.getLine();
        int nextString = lineNumber + 1;
        if (nextString > totalStrings) {
            nextString = 0;
        }

        return new VisualPosition(nextString, 0);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad"));
    }

}

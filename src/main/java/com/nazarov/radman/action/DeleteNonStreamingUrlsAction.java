package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.CheckHeader;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteNonStreamingUrlsAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        progressIndicator(e.getProject(), e);
    }

    public static void progressIndicator(Project project, AnActionEvent e) {
        final int[] processedLines = {0};
        final int[] deletedLines = {0};
        List<VisualPosition> visualPositionList = new ArrayList<>();
        ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {
            public void run() {
                ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
                int totalLines = ActionUtil.getLinesTotal(e);


                for (int i = 0; i < totalLines; i++) {
                    VisualPosition v = ActionUtil.getVisualPosition(i);

                    final String[] line = new String[1];

                    WriteCommandAction.runWriteCommandAction(project, (Computable<String>) () ->
                            line[0] = ActionUtil.getStringFromEditor(e, v));

                    String url = line[0].split(" ")[0];

                    float h = (float) (i * 100 / totalLines) / 100;
                    int currentIndex = i + 1;
                    progressIndicator.setText("Processing " + currentIndex + " of " + totalLines);
                    progressIndicator.setText2("Url: " + url);
                    progressIndicator.setFraction(h); // indicators chunk
                    progressIndicator.checkCanceled();

                    if ((UrlUtil.urlValidator(url)) && (CheckHeader.isAudioStream(url))) {
                        processedLines[0]++;
                    } else {
                        if ((UrlUtil.urlValidator(url)) && (!CheckHeader.isAudioStream(url))) {
                            processedLines[0]++;
                            visualPositionList.add(v);
                        } else {
                            processedLines[0]++;

                        }
                    }
                }
            }
        }, "Processing the list", true, project);
        Collections.reverse(visualPositionList);
        for(VisualPosition v : visualPositionList) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                        Document document = ActionUtil.getDocument(e);
                        CaretModel caretModel = ActionUtil.getCaretModel(e);
                        Caret currentCaret = caretModel.addCaret(v);
                        if (currentCaret != null) {
                            int start = currentCaret.getVisualLineStart();
                            int end = currentCaret.getVisualLineEnd();
                            document.deleteString(start, end);
                            deletedLines[0]++;
                            caretModel.removeCaret(currentCaret);
                        }
                    }
            );
        }

        ShowMsg.Result("Lines processed: " + processedLines[0] + " " +
                "Deleted: " + deletedLines[0]);
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad")
        );
    }
}

package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.nazarov.radman.message.AskParam;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeleteByLanguage extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String askLanguage = AskParam.askLanguage();
        if (askLanguage != null) {
            progressIndicator(e.getProject(), e, askLanguage);
        }
    }

    private static void progressIndicator(Project project, AnActionEvent e, String askLanguage) {
        //AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
        String delimiter = " | ";

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

                    String lineUnderCaret = line[0];

                    float h = (float) (i * 100 / totalLines) / 100;
                    int currentIndex = i + 1;
                    progressIndicator.setText("Processing " + currentIndex + " of " + totalLines);
                    progressIndicator.setText2("Line: " + lineUnderCaret);
                    progressIndicator.setFraction(h); // indicators chunk
                    progressIndicator.checkCanceled();

                    if (lineUnderCaret.contains("Lang")) {
                        String[] details = lineUnderCaret.split(delimiter);
                        for (String s : details) {
                            if (s.contains("Lang")) {
                                String[] langArray = s.split(":");
                                if (langArray.length > 1) {
                                    processedLines[0]++;
                                    String lang = langArray[1];
                                    if (lang.equals(askLanguage)) {
                                        visualPositionList.add(v);
                                    }
                                } else {
                                    processedLines[0]++;
                                }
                            }
                        }
                    } else {
                        processedLines[0]++;
                    }
                }
            }

        }, "Processing the list", true, project);

        deletedLines[0] = ActionUtil.deleteLine(e, project, visualPositionList);
        ActionUtil.resultReport(processedLines[0], deletedLines[0]);
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad"));
    }

}

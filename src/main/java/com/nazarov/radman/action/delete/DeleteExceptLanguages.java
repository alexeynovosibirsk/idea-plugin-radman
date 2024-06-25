package com.nazarov.radman.action.delete;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action "Delete Except Languages"
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */

public class DeleteExceptLanguages extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String askExceptLanguage = AskParam.askExceptLanguage();
        if (askExceptLanguage != null) {
            List<String> languages = new ArrayList<>(Arrays.asList(askExceptLanguage.split(" ")));
            languages.add("-"); // in order to skip line if value of Lang absent
            progressIndicator(e.getProject(), e, languages);
        }
    }

    private static void progressIndicator(Project project, AnActionEvent e, List<String> languages) {
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

                    Map<String, String> map = new HashMap<>();

                    if (lineUnderCaret.contains("Lang")) {
                        processedLines[0]++;
                        String[] details = lineUnderCaret.split(delimiter);
                        for (String s : details) {
                            if (s.contains(":")) {
                                if (s.endsWith(":")) {
                                    s += "-";
                                }
                                String[] pair = s.split(":");
                                map.put(pair[0], pair[1]);
                            }
                        }
                        String valueLang = null;
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (key.equals("Lang")) {
                                valueLang = value;
                            }
                        }
                        if (!languages.contains(valueLang)) {
                            visualPositionList.add(v);
                        }
                    } else {
                        processedLines[0]++;
                    }
                }
            }
        }, "Processing the list", true, project);

        deletedLines[0] = ActionUtil.deleteLines(e, project, visualPositionList);
        ActionUtil.resultReport(processedLines[0], deletedLines[0]);
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

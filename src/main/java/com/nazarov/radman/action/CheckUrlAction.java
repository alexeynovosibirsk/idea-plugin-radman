package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.CheckHeader;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;

public class CheckUrlAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String allLineUnderCursor = ActionUtil.getStringUnderCursor(e);
        String selectedUrl = allLineUnderCursor.split(" ")[0];

        if ((UrlUtil.urlValidator(selectedUrl)) && (CheckHeader.isAudoStreamUrl(selectedUrl))) {
            ShowMsg.UrlIsOk();

        } else {

            if ((UrlUtil.urlValidator(selectedUrl)) && (!CheckHeader.isAudoStreamUrl(selectedUrl))) {
                ShowMsg.UrlIsNotValidAndWillBeDeleted();
                Project project = e.getProject();
                Editor editor = ActionUtil.getEditor(e);
                Document document = editor.getDocument();
                Caret primaryCaret = ActionUtil.getCaret(e);
                int start = primaryCaret.getVisualLineStart();
                int end = primaryCaret.getVisualLineEnd();
                WriteCommandAction.runWriteCommandAction(project, () ->
                                document.deleteString(start, end)
//                    document.replaceString(start, end, "editor_basics")
                );
                // De-select the text range that was just replaced
                primaryCaret.removeSelection();

            } else {
                ShowMsg.UrlIsNotValid(selectedUrl);
            }
        }

    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad")
        );
    }

}

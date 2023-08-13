package com.nazarov.radman.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;

public class ActionUtil {

    public static Editor getEditor(AnActionEvent e) {
        return e.getRequiredData(CommonDataKeys.EDITOR);
    }

    public static Document getDocument(AnActionEvent e) {
        Editor editor = getEditor(e);

        return editor.getDocument();
    }

    public static CaretModel getCaretModel(AnActionEvent e) {
        Editor editor = getEditor(e);

        return editor.getCaretModel();
    }

    public static Caret getPrimaryCaret(AnActionEvent e) {
        CaretModel caretModel = getCaretModel(e);

        return caretModel.getPrimaryCaret();
    }

    public static int getLinesTotal(AnActionEvent e) {
        Document document = getDocument(e);

        return document.getLineCount();
    }

    public static String getStringUnderCursor(AnActionEvent e) {
        Caret primaryCaret = getPrimaryCaret(e);
        int start = primaryCaret.getVisualLineStart();
        int end = primaryCaret.getVisualLineEnd();
        TextRange textRange = new TextRange(start, end);
        Editor editor = getEditor(e);
        Document document = editor.getDocument();

        return document.getText(textRange);
    }

    public static String getStringFromEditor(AnActionEvent e, VisualPosition v) {
        Editor editor = getEditor(e);
        CaretModel caretModel = editor.getCaretModel();

        caretModel.addCaret(v);

        Caret c = caretModel.getCurrentCaret();
        int start = c.getVisualLineStart();
        int end = c.getVisualLineEnd();

        caretModel.removeCaret(c);

        TextRange textRange = new TextRange(start, end);
        Document document = getDocument(e);

        return document.getText(textRange);
    }


    public static VisualPosition getVisualPosition(int i) {
        return new VisualPosition(i, 1);
    }

    public static String getDefaultExtension(AnActionEvent e) {
        Editor editor = getEditor(e);
        VirtualFile v = FileDocumentManager.getInstance().getFile(editor.getDocument());
        String defaultExtension = "null";
        if (v != null) {
            FileType fileType = v.getFileType();
            defaultExtension = fileType.getDefaultExtension();
        }

        return defaultExtension;
    }


    public static void deleteString(AnActionEvent e) {
        Project project = e.getProject();
        Document document = getDocument(e);
        Caret primaryCaret = getPrimaryCaret(e);
        int start = primaryCaret.getVisualLineStart();
        int end = primaryCaret.getVisualLineEnd();
        WriteCommandAction.runWriteCommandAction(project, () ->
                        document.deleteString(start, end)
        );
        // De-select the text range that was just replaced
        primaryCaret.removeSelection();
    }

}

package com.nazarov.radman.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;

public class ActionUtil {

    public static Editor getEditor(AnActionEvent e) {
        return e.getRequiredData(CommonDataKeys.EDITOR);
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

    public static Caret getCaret(AnActionEvent e) {
        Editor editor = getEditor(e);
        CaretModel caretModel = editor.getCaretModel();

        return caretModel.getPrimaryCaret();
    }



    public static String getStringUnderCursor(AnActionEvent e) {
        Caret primaryCaret = getCaret(e);
        int start = primaryCaret.getVisualLineStart();
        int end = primaryCaret.getVisualLineEnd();
        TextRange textRange = new TextRange(start, end);
        Editor editor = getEditor(e);
        Document document = editor.getDocument();

        return document.getText(textRange);
    }

}

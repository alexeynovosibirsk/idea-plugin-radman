package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;

public class ActionUtil {

    public static Editor getEditor(AnActionEvent e) {
        return e.getRequiredData(CommonDataKeys.EDITOR);
    }

    public static String getDefaultExtenstion(AnActionEvent e) {
        Editor editor = getEditor(e);
        VirtualFile v = FileDocumentManager.getInstance().getFile(editor.getDocument());
        String defaultExtension = "null";
        if (v != null) {
            FileType fileType = v.getFileType();
            defaultExtension = fileType.getDefaultExtension();
        }
        return defaultExtension;
    }

}

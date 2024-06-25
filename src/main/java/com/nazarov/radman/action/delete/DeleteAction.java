package com.nazarov.radman.action.delete;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Action "Delete"
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */
public class DeleteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ActionUtil.deleteString(e);
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

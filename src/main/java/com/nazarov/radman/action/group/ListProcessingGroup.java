package com.nazarov.radman.action.group;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Menu "RadMan List Deleting Ops"
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */
public class ListProcessingGroup extends DefaultActionGroup {

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

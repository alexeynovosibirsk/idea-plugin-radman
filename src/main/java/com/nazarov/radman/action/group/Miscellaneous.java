package com.nazarov.radman.action.group;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

public class Miscellaneous extends DefaultActionGroup {

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

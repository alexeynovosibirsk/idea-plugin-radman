package com.nazarov.radman.action.manipulation;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

public class MoveToFirst extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ActionUtil.moveStringToFirst(e);
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

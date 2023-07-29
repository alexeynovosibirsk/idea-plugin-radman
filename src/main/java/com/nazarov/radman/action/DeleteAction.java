package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.util.ActionUtil;
import org.jetbrains.annotations.NotNull;

public class DeleteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        ActionUtil.deleteString(e);

    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad")
        );
    }

}

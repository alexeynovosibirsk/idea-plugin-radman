package com.nazarov.radman.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class HelpAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

                URL url = UrlUtil.makeUrl("https://plugins.jetbrains.com/plugin/22185-radman/radman-help");
                if(url != null) {
                    BrowserUtil.browse(url);
                }
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

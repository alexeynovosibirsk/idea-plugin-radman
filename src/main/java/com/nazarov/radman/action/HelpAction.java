package com.nazarov.radman.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;
import java.net.URL;

/**
 * Action Help
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */
public class HelpAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
                URL url = UrlUtil.makeUrl("https://gitflic.ru/project/nixoved/radman/blob?file=README.md&branch=master&mode=markdown");
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

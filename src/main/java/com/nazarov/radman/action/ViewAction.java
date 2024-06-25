package com.nazarov.radman.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;
import java.net.URL;

/**
 * Action "View in browser"
 * CAUTION: AnAction classes do not have class fields of any kind. This restriction prevents memory leaks.
 */

public class ViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        String selected = primaryCaret.getSelectedText();

            if (selected == null) {
                ShowMsg.dialog(ShowMsg.HIGHLIGHT_THE_LINK, ShowMsg.HIGHLIGHT_THE_LINK_TITLE);
            } else {
                URL url = UrlUtil.makeUrl(selected);
                if(url != null) {
                    BrowserUtil.browse(url);
                }
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

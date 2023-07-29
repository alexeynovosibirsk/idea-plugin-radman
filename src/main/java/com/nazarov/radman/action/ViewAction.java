package com.nazarov.radman.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.UrlUtil;

import java.net.URL;

public class ViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        String selected = primaryCaret.getSelectedText();

            URL url = UrlUtil.makeUrl(selected);
            BrowserUtil.browse(url);

            if (selected == null) {
                ShowMsg.HighlightTheLink();
            } else {
                String msg = "[" + selected + "]";
                ShowMsg.UrlIsNotValid(msg);
            }

    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtension(e).equals("rad")
        );
    }

}

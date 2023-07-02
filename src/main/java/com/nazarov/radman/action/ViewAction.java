package com.nazarov.radman.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.nazarov.radman.util.UrlUtil;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.URL;

public class ViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Caret primaryCaret = editor.getCaretModel().getCurrentCaret();
        String selected = primaryCaret.getSelectedText();

        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        if (urlValidator.isValid(selected)) {
            URL url = UrlUtil.makeUrl(selected);
            BrowserUtil.browse(url);
        } else {

            if (selected == null) {
                message("In order to open an url link you have to highlight the link via cursor!");
            } else {
                message("[" + selected + "]");
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on opened filetype
        e.getPresentation().setEnabledAndVisible(
                ActionUtil.getDefaultExtenstion(e).equals("rad")
        );
    }

    private void message(String string) {
        Messages.showErrorDialog(string, "URL IS NOT VALID!");
    }

}

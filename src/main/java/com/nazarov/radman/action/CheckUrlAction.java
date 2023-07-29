package com.nazarov.radman.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.util.ActionUtil;
import com.nazarov.radman.util.CheckHeader;
import com.nazarov.radman.util.UrlUtil;
import org.jetbrains.annotations.NotNull;

public class CheckUrlAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String allLineUnderCursor = ActionUtil.getStringUnderCursor(e);
        String selectedUrl = allLineUnderCursor.split(" ")[0];

        if ((UrlUtil.urlValidator(selectedUrl)) && (CheckHeader.isAudioStream(selectedUrl))) {
            ShowMsg.UrlIsOk();

        } else {

            if ((UrlUtil.urlValidator(selectedUrl)) && (!CheckHeader.isAudioStream(selectedUrl))) {
                ShowMsg.UrlIsNotValidAndWillBeDeleted();

               ActionUtil.deleteString(e);

            } else {
                ShowMsg.UrlIsNotValid(selectedUrl);
            }
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

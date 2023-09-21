package com.nazarov.radman.panel;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.JBColor;
import com.nazarov.radman.model.PlayingInfo;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel "Playing info"
 */

public class PlayPanel {
    public static final String NO_METADATA = "The radio station is not provide any metadata";
    public static final String NOTHING_IS_PLAYED = "Nothing is played now...";
    public static final String FILENAME = "stub.rad";
    public static final String TXT = "stub";
    public static final String FIRST_PIPE = "|";
    public static final String DOUBLE_WHITESPACES = "  ";
    public static final String CLEAR = "";


    private static final JLabel metadata = new JLabel();
    private static final JLabel nowPlayingFile = new JLabel();
    private static final JLabel nowPlayingUrl = new JLabel();
    private static PsiFile psiFile;

    public static void setPsiFile(PsiFile pFile) {
        if (pFile != null) {
            psiFile = pFile;
        }
    }

    public static void setNowPlayingFile(String string) {
        if (string != null) {
            nowPlayingFile.setText(string);
        }
    }

    public static void setNowPlayingUrl(String string) {
        if (string != null) {
            String result = string.replace(FIRST_PIPE, DOUBLE_WHITESPACES).trim();
            nowPlayingUrl.setText(result);
        }
    }

    public static void setMetadata(String string) {
        if (string != null) {
            metadata.setText(string);
        } else {
            metadata.setText(NO_METADATA);
        }
    }

    @NotNull
    public static JPanel create(ToolWindow toolWindow) {
        Color myColor = new JBColor(Color.magenta, new Color(172, 62, 241));
        nowPlayingFile.setForeground(myColor);
        nowPlayingFile.setText(NOTHING_IS_PLAYED);

        nowPlayingFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final Project project = toolWindow.getProject();
                if (psiFile == null) { //this is only for automating test.
                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);
                    psiFile = factory.createFileFromText(FILENAME, PlainTextFileType.INSTANCE, TXT);
                }
                VirtualFile virtualFile = psiFile.getVirtualFile();
                Pair<Integer, Integer> pair = PlayingInfo.getCursorPositionLineAndColumn();
                new OpenFileDescriptor(project, virtualFile, pair.first, pair.second).navigate(true);
            }
        });


        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        Color myColor2 = new JBColor(Color.magenta, new Color(60, 141, 250));
        nowPlayingUrl.setForeground(myColor2);
        nowPlayingUrl.setText(CLEAR);

        jPanel.add(nowPlayingFile);
        jPanel.add(nowPlayingUrl);
        jPanel.add(metadata);

        return jPanel;
    }

}

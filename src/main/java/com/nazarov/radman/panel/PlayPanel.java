package com.nazarov.radman.panel;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.JBColor;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.model.PlayingInfo;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        metadata.setText(Objects.requireNonNullElse(string, NO_METADATA));
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
                LogicalPosition cursorPosition = PlayingInfo.getCursorPosition();
                new OpenFileDescriptor(project, virtualFile, cursorPosition.line, cursorPosition.column).navigate(true);
            }
        });

        nowPlayingUrl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayingInfo info = PlayingInfo.getInstance();
                List<String> list = new ArrayList<>(Arrays.asList(info.getRadioStationInfo().split(" ")));
                String link = null;
                for (String s : list) {
                    if (s.contains("http")) {
                        link = s;
                    }
                }
                if (link != null) {
                    BrowserUtil.browse(link);
                } else {
                    ShowMsg.dialog(ShowMsg.URL_IS_NOT_VALID + link, ShowMsg.ERROR_TITLE);
                }
            }
        });

        metadata.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String string = metadata.getText();
                String artist = string.split(" - ")[0];
                String clearArtist = artist.replace("'", "").trim();
                if (clearArtist != null) {
                    BrowserUtil.browse("https://www.google.com/search?q=" + clearArtist);
                } else {
                    ShowMsg.dialog(ShowMsg.URL_IS_NOT_VALID + clearArtist, ShowMsg.ERROR_TITLE);
                }
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

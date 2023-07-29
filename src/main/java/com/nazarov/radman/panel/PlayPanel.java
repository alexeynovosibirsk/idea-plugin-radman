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
import org.jetbrains.annotations.NotNull;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayPanel {

    public static JLabel nowPlayingFile = new JLabel();
    static JLabel nowPlayingUrl = new JLabel();

    private static Pair<Integer, Integer> lineAndColumn;

    public static void setLineAndColumn(int line, int column) {
        lineAndColumn = new Pair<>(line, column);
    }

    static PsiFile psiFile;

    public static void setPsiFile(PsiFile pFile) {
        psiFile = pFile;
    }

    public static void setNowPlayingFile(String string) {
        nowPlayingFile.setText(string);
    }

    public static void setNowPlayingUrl(String string) {
        // get all except station url:
        nowPlayingUrl.setText(string);
    }

    @NotNull
    public static JPanel create(ToolWindow toolWindow) {
        JLabel label = new JLabel();
        label.setText("Now playing: ");
        JPanel jPanel = new JPanel();

        Color myColor = new JBColor(Color.magenta, new Color(172, 62, 241));
        nowPlayingFile.setForeground(myColor);
        nowPlayingFile.setText("nothing...");

        nowPlayingFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final Project project = toolWindow.getProject();
                if (psiFile == null) { //this is only for automating test.
                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);
                    psiFile = factory.createFileFromText("stub.rad", PlainTextFileType.INSTANCE, "stub");
                }
                VirtualFile virtualFile = psiFile.getVirtualFile();
                new OpenFileDescriptor(project, virtualFile, lineAndColumn.first, lineAndColumn.second).navigate(true);
            }
        });

        Color myColor2 = new JBColor(Color.magenta, new Color(60, 141, 250));
        nowPlayingUrl.setForeground(myColor2);
        nowPlayingUrl.setText("");

        jPanel.add(label);
        jPanel.add(nowPlayingFile);
        jPanel.add(nowPlayingUrl);

        return jPanel;
    }
}

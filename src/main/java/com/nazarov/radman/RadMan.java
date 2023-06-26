package com.nazarov.radman;

import com.intellij.ide.HelpTooltip;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.*;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.nazarov.radman.model.CommunityRadioBrowser;
import com.nazarov.radman.util.Util;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class RadMan implements ToolWindowFactory, DumbAware {

    private final Icon logo = Icons.Headphones_icon;
    private final JTextField findParameter = new JTextField();
    private final JTextField limit = new JTextField();
    private final CommunityRadioBrowser crb = new CommunityRadioBrowser();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        final JPanel fPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel pPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fPanel.add(createFindPanel(toolWindow), BorderLayout.PAGE_START);
        pPanel.add(createProcessingPanel(toolWindow), BorderLayout.PAGE_START);

        Content procPanel = ContentFactory.getInstance().createContent(pPanel, "Playing Info", false);
        Content findPanel = ContentFactory.getInstance().createContent(fPanel, "Get Stations", false);

        toolWindow.getContentManager().addContent(procPanel);
        toolWindow.getContentManager().addContent(findPanel);
    }

    private JPanel createFindPanel(ToolWindow toolWindow) {

        JLabel label = new JLabel();
        label.setText("Find stations in https://www.radio-browser.info/: ");

        limit.setName("Limit");
        final String MESSAGE = "Limit of query must be integer";

        new ComponentValidator(toolWindow.getDisposable()).withValidator(v -> {
            String lim = limit.getText();
            if (StringUtil.isNotEmpty(lim)) {
                try {
                    int limitValue = Integer.parseInt(lim);
                    if (limitValue <= 1000) {
                        v.updateInfo(null);
                    } else {
                        v.updateInfo(new ValidationInfo(MESSAGE, limit));
                    }
                } catch (NumberFormatException nfe) {
                    v.updateInfo(new ValidationInfo(MESSAGE, limit));
                }
            } else {
                v.updateInfo(null);
            }
        }).installOn(limit);
        limit.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(limit).ifPresent(ComponentValidator::revalidate);
            }
        });

        final String REQUEST_COMPLETED = "Request Completed.";
        final String INIT_MESSAGE = "Type genre here";

        findParameter.setPreferredSize(new Dimension(150, 30));
        findParameter.setText(INIT_MESSAGE);
        findParameter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                findParameter.setText("");
            }
        });

        JButton goButton = new JButton("GO!");
        goButton.addActionListener(e -> {

            String genre = findParameter.getText();
            if ((!genre.isEmpty()) && (!genre.equalsIgnoreCase(INIT_MESSAGE))) {

                boolean isFileCreated = findInCommunityRadioBrowser(crb, toolWindow);
                if (isFileCreated) {
                    Messages.showMessageDialog("Stations found: " + Util.getStationsFound(), REQUEST_COMPLETED, logo);
                } else {
                    Messages.showMessageDialog("There is no results for " + genre, REQUEST_COMPLETED, logo);
                }

            } else {
                Messages.showMessageDialog("Please fill the genre", "Query Parameter Missed!", logo);
            }
        });

        JRadioButton url_resolvedButton = new JRadioButton("Url_resolved");
        url_resolvedButton.setSelected(true);
        url_resolvedButton.addActionListener(e -> {
            if (url_resolvedButton.isSelected()) {

                crb.setUrl_resolved(true);
                crb.setUrl(false);
            } else {
                crb.setUrl_resolved(false);
                crb.setUrl(true);
            }
        });

        JRadioButton nameButton = new JRadioButton("Name");
        nameButton.setSelected(true);
        nameButton.addActionListener(e -> {
            if (nameButton.isSelected()) {
                crb.setName(true);
            } else {
                crb.setName(false);
            }
        });

        JRadioButton homepageButton = new JRadioButton("Homepage");
        homepageButton.setSelected(true);
        homepageButton.addActionListener(e -> {
            if (homepageButton.isSelected()) {
                crb.setHomepage(true);
            } else {
                crb.setHomepage(false);
            }
        });

        JRadioButton countryButton = new JRadioButton("Country");
        countryButton.setSelected(true);
        countryButton.addActionListener(e -> {
            if (countryButton.isSelected()) {
                crb.setCountrycode(true);
            } else {
                crb.setCountrycode(false);
            }
        });

        JRadioButton langButton = new JRadioButton("Lang");
        langButton.setSelected(true);
        langButton.addActionListener(e -> {
            if (langButton.isSelected()) {
                crb.setLanguage(true);
            } else {
                crb.setLanguage(false);
            }
        });

        JRadioButton codecButton = new JRadioButton("Codec");
        codecButton.setSelected(true);
        codecButton.addActionListener(e -> {
            if (codecButton.isSelected()) {
                crb.setCodec(true);
            } else {
                crb.setCodec(false);
            }
        });

        JRadioButton bitrateButton = new JRadioButton("Bitrate");
        bitrateButton.setSelected(true);
        bitrateButton.addActionListener(e -> {
            if (bitrateButton.isSelected()) {
                crb.setBitrate(true);
            } else {
                crb.setBitrate(false);
            }
        });

        JRadioButton votesButton = new JRadioButton("Votes");
        votesButton.setSelected(true);
        votesButton.addActionListener(e -> {
            if (votesButton.isSelected()) {
                crb.setVotes(true);
            } else {
                crb.setVotes(false);
            }
        });

        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(findParameter);
        jPanel.add(limit);
        jPanel.add(url_resolvedButton);
        jPanel.add(nameButton);
        jPanel.add(homepageButton);
        jPanel.add(countryButton);
        jPanel.add(langButton);
        jPanel.add(codecButton);
        jPanel.add(bitrateButton);
        jPanel.add(votesButton);
        jPanel.add(goButton);

        String urlResolved = "An automatically \"resolved\" stream URL. This usefull if you don't want to decoding playlists yourself." +
                "If you switch it off, you will receive links provided by users";
        new HelpTooltip().setDescription(urlResolved).installOn(url_resolvedButton);
        String limitHelp = "Limit of query results. If you keep it empty you receive all found links.";
        new HelpTooltip().setDescription(limitHelp).installOn(limit);
        String nameButtonHelp = "Name of radio station.";
        new HelpTooltip().setDescription(nameButtonHelp).installOn(nameButton);
        String homepageButtonHelp = "URL of homepage radio station.";
        new HelpTooltip().setDescription(homepageButtonHelp).installOn(homepageButton);
        String countryButtonHelp = "Countrycode: 2 letters, uppercase. Official country codes as in ISO 3166-1 alpha-2";
        new HelpTooltip().setDescription(countryButtonHelp).installOn(countryButton);
        String langButtonHelp = "Languages that are spoken in this stream.";
        new HelpTooltip().setDescription(langButtonHelp).installOn(langButton);
        String codecButtonHelp = "The codec of this stream recorded at the last check.";
        new HelpTooltip().setDescription(codecButtonHelp).installOn(codecButton);
        String bitrateButtonHelp = "The bitrate of this stream recorded at the last check.";
        new HelpTooltip().setDescription(bitrateButtonHelp).installOn(bitrateButton);
        String votesButtonHelp = "Number of votes for this station. This number is by server and only ever increases. It will never be reset to 0.";
        new HelpTooltip().setDescription(votesButtonHelp).installOn(votesButton);

        return jPanel;
    }

    public boolean findInCommunityRadioBrowser(CommunityRadioBrowser crb, ToolWindow toolWindow) {
        boolean isFileCreated = false;
        try {
            String result = Util.getData(findParameter.getText(), limit.getText(), crb);

            Project project = toolWindow.getProject();
            VirtualFile virtualFile = project.getProjectFile();
            VirtualFile directory = virtualFile.getParent().getParent();

            PsiManager psiManager = PsiManager.getInstance(project);
            PsiDirectory psiDirectory = psiManager.findDirectory(directory);

            if (!result.equals("[]") && psiDirectory != null && Util.getStationsFound() != 0) {
                String fileName = Util.createFileName(findParameter.getText());
                final PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
                final PsiFile psiFile = psiFileFactory.createFileFromText(fileName, PlainTextFileType.INSTANCE, result);

                Runnable r = () -> psiDirectory.add(psiFile);
                WriteCommandAction.runWriteCommandAction(project, r);

                isFileCreated = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return isFileCreated;
    }

    public static JLabel nowPlayingFile = new JLabel();
    static JLabel nowPlayingUrl = new JLabel();

    private static Pair<Integer, Integer> lineAndColumn;

    public static void setLineAndColumn(int line, int column) {
        lineAndColumn = new Pair<>(line, column);
    }

    static PsiFile psiFile;

    @NotNull
    private static JPanel createProcessingPanel(ToolWindow toolWindow) {
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

    public static void setPsiFile(PsiFile psiFile) {
        RadMan.psiFile = psiFile;
    }

    public static void setNowPlayingFile(String string) {
        nowPlayingFile.setText(string);
    }

    public static void setNowPlayingUrl(String string) {
        // get all except station url:
        nowPlayingUrl.setText(string);
    }

//            String[] STRING_VALUES = {"Yes", "NO"};
//            ComboBox<String> eComboBox = new ComboBox<>(STRING_VALUES);
//            eComboBox.setEditable(true);
//            eComboBox.setEditor(new BasicComboBoxEditor(){
//                @Override
//                protected JTextField createEditorComponent() {
//                    ExtendableTextField ecbEditor = new ExtendableTextField();
//                    //       ecbEditor.addExtension(browseExtension);
//                    ecbEditor.setBorder(null);
//                    return ecbEditor;
//                }
//            });
//

}

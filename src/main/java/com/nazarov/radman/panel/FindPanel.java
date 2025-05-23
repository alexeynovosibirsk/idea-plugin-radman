package com.nazarov.radman.panel;

import com.intellij.ide.HelpTooltip;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.DocumentAdapter;
import com.nazarov.radman.message.ShowMsg;
import com.nazarov.radman.model.CommunityRadioBrowser;
import com.nazarov.radman.util.Util;
import com.nazarov.radman.validate.IntegerValidator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalTime;

public class FindPanel {

    public static JPanel create(ToolWindow toolWindow) {

        final String INIT_MESSAGE = "Type genre here";
        CommunityRadioBrowser crb = new CommunityRadioBrowser();
        JTextField findParameter = new JTextField();
        JTextField limit = new JTextField();

        limit.setName("Limit");
        final String MESSAGE = "Limit of query must be integer. This variable will be ignored!";
        new ComponentValidator(toolWindow.getDisposable()).withValidator(() -> {
            String lim = limit.getText();
            //TODO: инвертировать
            if (!IntegerValidator.isNumeric(lim)) {
                return new ValidationInfo(MESSAGE, limit);
            }
            return null;
        }).installOn(limit);

        limit.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(limit).ifPresent(ComponentValidator::revalidate);
            }
        });

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
                boolean isFileCreated = findInCommunityRadioBrowser(crb, toolWindow, genre, limit.getText());
                if (isFileCreated) {
                    ShowMsg.dialog("Stations found: " + Util.getStationsFound(), ShowMsg.REQUEST_COMPLETED);
                } else {
                    ShowMsg.dialog("There is no results for " + genre, ShowMsg.REQUEST_COMPLETED);
                }
            } else {
                ShowMsg.dialog(ShowMsg.QUERY_PARAMETER_IS_MISSED, ShowMsg.QUERY_PARAMETER_IS_MISSED_TITLE);
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

        JRadioButton bitrateButton = new JRadioButton("Bitrate");
        bitrateButton.setSelected(true);
        bitrateButton.addActionListener(e -> {
            if (bitrateButton.isSelected()) {
                crb.setBitrate(true);
            } else {
                crb.setBitrate(false);
            }
        });

        JLabel label = new JLabel();
        label.setText("Find stations in https://www.radio-browser.info/: ");

        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(findParameter);
        jPanel.add(limit);
        jPanel.add(url_resolvedButton);
        jPanel.add(nameButton);
        jPanel.add(homepageButton);
        jPanel.add(countryButton);
        jPanel.add(bitrateButton);
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
        String bitrateButtonHelp = "The bitrate of this stream recorded at the last check.";
        new HelpTooltip().setDescription(bitrateButtonHelp).installOn(bitrateButton);

        return jPanel;
    }

    public static boolean findInCommunityRadioBrowser(CommunityRadioBrowser crb, ToolWindow toolWindow, String findParameter,
                                                      String limit) {
        boolean isFileCreated = false;
        try {
            String result = Util.getDataFromRadioBrowser(findParameter, limit, crb);

            Project project = toolWindow.getProject();
            VirtualFile virtualFile = project.getProjectFile();
            if (virtualFile == null) {
                return false;
            }
            VirtualFile directory = virtualFile.getParent().getParent();

            PsiManager psiManager = PsiManager.getInstance(project);
            PsiDirectory psiDirectory = psiManager.findDirectory(directory);

            if (!result.equals("[]") && psiDirectory != null && Util.getStationsFound() != 0) {
                String fileName = findParameter + "-" + LocalTime.now().getNano() + ".rad";
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
}

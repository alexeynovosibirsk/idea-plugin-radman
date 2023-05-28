// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.nazarov;

import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.nazarov.util.Util;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class RadMan implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolWindowContent toolWindowContent = new ToolWindowContent(toolWindow);
        Content content = ContentFactory.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private static class ToolWindowContent {
        private final JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField findParameter = new JTextField();

        Boolean url_resolved = true;


        Boolean url = false;
        Boolean name = false;
        Boolean homepage = false;
        Boolean countrycode = false;
        Boolean language = false;
        Boolean codec = false;
        Boolean bitrate = false;
        Boolean votes = false;

        public ToolWindowContent(ToolWindow toolWindow) {
            contentPanel.add(createPanel(toolWindow), BorderLayout.PAGE_START);
        }

        @NotNull
        private JPanel createPanel(ToolWindow toolWindow) {
            JLabel label = new JLabel();
            label.setText("Find stations in Radio Browser: ");

            findParameter.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));

            if (findParameter.getText() != null) findParameter.setText("Type genre here");
            findParameter.setPreferredSize(new Dimension(150, 30));

            findParameter.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    findParameter.setText("");
                }
            });

            JButton jButton = new JButton("GO!");
            jButton.addActionListener(e -> {
                if (!findParameter.getText().isEmpty()) {
                    findInCommunityRadioBrowser(findParameter.getText(), url, url_resolved, name, homepage, countrycode, language, codec, bitrate, votes, toolWindow);
                } else {
                    Messages.showErrorDialog("Please fill the genre", "Query Parameter Missed!");
                }
            });


            JRadioButton url_resolvedButton = new JRadioButton("Url_resolved");
            url_resolvedButton.setSelected(true);
            url_resolvedButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (url_resolvedButton.isSelected()) {
                        url_resolved = true;
                        url = false;
                    } else {
                        url_resolved = false;
                        url = true;
                    }
                }
            });

            JRadioButton nameButton = new JRadioButton("Name");
            nameButton.addActionListener(e -> {
                if (nameButton.isSelected()) {
                    name = true;
                }
            });

            JRadioButton homepageButton = new JRadioButton("Homepage");
            homepageButton.addActionListener(e -> {
                if (homepageButton.isSelected()) {
                    homepage = true;
                }
            });

            JRadioButton countryButton = new JRadioButton("Country");
            countryButton.addActionListener(e -> {
                if (countryButton.isSelected()) {
                    countrycode = true;
                }
            });

            JRadioButton langButton = new JRadioButton("Lang");
            langButton.addActionListener(e -> {
                if (langButton.isSelected()) {
                    language = true;
                }
            });

            JRadioButton codecButton = new JRadioButton("Codec");
            codecButton.addActionListener(e -> {
                if (codecButton.isSelected()) {
                    codec = true;
                }
            });

            JRadioButton bitrateButton = new JRadioButton("Bitrate");
            bitrateButton.addActionListener(e -> {
                if (bitrateButton.isSelected()) {
                    bitrate = true;
                }
            });

            JRadioButton votesButton = new JRadioButton("Votes");
            votesButton.addActionListener(e -> {
                if (votesButton.isSelected()) {
                    votes = true;
                }
            });

            JPanel jPanel = new JPanel();
            jPanel.add(label);
            jPanel.add(findParameter);
            jPanel.add(url_resolvedButton);
            jPanel.add(nameButton);
            jPanel.add(homepageButton);
            jPanel.add(countryButton);
            jPanel.add(langButton);
            jPanel.add(codecButton);
            jPanel.add(bitrateButton);
            jPanel.add(votesButton);
            jPanel.add(jButton);

            return jPanel;
        }

        public void findInCommunityRadioBrowser(String findParameter, boolean url, boolean url_resolved, boolean name, boolean homepage,
                                                boolean countrycode, boolean language, boolean codec, boolean bitrate, boolean votes, ToolWindow toolWindow) {
            String result = null;
            try {
                result = Util.getData(findParameter, url, url_resolved, name, homepage, countrycode, language, codec, bitrate, votes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final Project project = toolWindow.getProject();
            PsiManager psiManager = PsiManager.getInstance(project);
            //TODO: getBaseDir deprecated - find change for example VirtualFile f = psiManager.getProject().getWorkspaceFile().;
            PsiDirectory psiDirectory = psiManager.findDirectory(project.getBaseDir());

            if (!result.equals("[]") && psiDirectory != null) {
                String fileName = Util.createFileName(findParameter);

                final PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
                final PsiFile psiFile = psiFileFactory.createFileFromText(fileName, PlainTextFileType.INSTANCE, result);
                psiDirectory.add(psiFile);
            } else {
                Messages.showErrorDialog("There is no result for " + findParameter, "Not found!");
            }
        }

        public JPanel getContentPanel() {
            return contentPanel;
        }
    }
}

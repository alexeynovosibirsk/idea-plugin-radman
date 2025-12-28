package com.nazarov.radman.panel;

import com.intellij.ide.HelpTooltip;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
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

        final String INIT_MESSAGE = "Например: rock";
        CommunityRadioBrowser crb = new CommunityRadioBrowser();
        JTextField findParameter = new JTextField();
        JTextField limit = new JTextField();

        limit.setName("Limit");
        final String MESSAGE = "Значение должно быть числом! Текущее значение будет проигнорировано.";
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

        JButton goButton = new JButton("Скачать!");
        goButton.addActionListener(e -> {

            ShowMsg.dialog("Запрос отправлен", "Выполнение запроса");

            String genre = findParameter.getText();
            boolean isFileCreated = false;
            if ((!genre.isEmpty()) && (!genre.equalsIgnoreCase(INIT_MESSAGE))) {
    //TODO            isFileCreated = runBackgroundTask(toolWindow.getProject(), crb, toolWindow, genre, limit.getText());
                isFileCreated = findInCommunityRadioBrowser(crb, toolWindow, genre, limit.getText());

                int counter = 0;
                while(!isFileCreated || counter > 5) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException ex) {

                    }
                    counter++;
                }

//                if(fileName != null) {
//                    File file = new File(fileName);
//                    VirtualFile virtualFileResult = LocalFileSystem.getInstance().findFileByIoFile(file);
//                    PsiFile checkPsiFile = PsiManager.getInstance(toolWindow.getProject()).findFile(virtualFileResult);
//                    if(checkPsiFile != null) {
//                        isFileCreated = true;
//                    }
//                }

                if (isFileCreated) {
                    ShowMsg.dialog("Станций найдено: " + Util.getStationsFound(), ShowMsg.REQUEST_COMPLETED);
                } else {
                    ShowMsg.dialog("Нет вхождений для " + genre, ShowMsg.REQUEST_COMPLETED);
                }
            } else {
                ShowMsg.dialog(ShowMsg.QUERY_PARAMETER_IS_MISSED, ShowMsg.QUERY_PARAMETER_IS_MISSED_TITLE);
            }
        });

        JRadioButton url_resolvedButton = new JRadioButton("URL");
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

        JRadioButton nameButton = new JRadioButton("Название");
        nameButton.setSelected(true);
        nameButton.addActionListener(e -> {
            if (nameButton.isSelected()) {
                crb.setName(true);
            } else {
                crb.setName(false);
            }
        });

        JRadioButton homepageButton = new JRadioButton("Сайт");
        homepageButton.setSelected(true);
        homepageButton.addActionListener(e -> {
            if (homepageButton.isSelected()) {
                crb.setHomepage(true);
            } else {
                crb.setHomepage(false);
            }
        });

        JRadioButton countryButton = new JRadioButton("Страна");
        countryButton.setSelected(true);
        countryButton.addActionListener(e -> {
            if (countryButton.isSelected()) {
                crb.setCountrycode(true);
            } else {
                crb.setCountrycode(false);
            }
        });

        JRadioButton langButton = new JRadioButton("Язык");
        langButton.setSelected(true);
        langButton.addActionListener(e -> {
            if (langButton.isSelected()) {
                crb.setLanguage(true);
            } else {
                crb.setLanguage(false);
            }
        });

        JRadioButton bitrateButton = new JRadioButton("Битрейт");
        bitrateButton.setSelected(true);
        bitrateButton.addActionListener(e -> {
            if (bitrateButton.isSelected()) {
                crb.setBitrate(true);
            } else {
                crb.setBitrate(false);
            }
        });

        JLabel label = new JLabel();
        label.setText("Жанр: ");
        JLabel labelLim = new JLabel();
        labelLim.setText("Лимит: ");

        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(findParameter);
        jPanel.add(labelLim);
        jPanel.add(limit);
     //   jPanel.add(url_resolvedButton);
        jPanel.add(nameButton);
        jPanel.add(homepageButton);
        jPanel.add(countryButton);
        jPanel.add(langButton);
        jPanel.add(bitrateButton);
        jPanel.add(goButton);

        String urlResolved = "Автоматически резолвящиеся УРЛы - удобно, если не хотите декодировать плейлисты. При отключении параметра будут выданы УРЛы предоставленные пользователями.";
        new HelpTooltip().setDescription(urlResolved).installOn(url_resolvedButton);
        String limitHelp = "Лимит вхождений. Если не заполнен будут выданы все найденные вхождения.";
        new HelpTooltip().setDescription(limitHelp).installOn(limit);
        String nameButtonHelp = "Название радиостанции.";
        new HelpTooltip().setDescription(nameButtonHelp).installOn(nameButton);
        String homepageButtonHelp = "УРЛ сайта радиостанции.";
        new HelpTooltip().setDescription(homepageButtonHelp).installOn(homepageButton);
        String countryButtonHelp = "Код страны: 2 буквы в верхнем регистре. Официальные коды стран согласно стандарта ISO 3166-1 alpha-2.";
        new HelpTooltip().setDescription(countryButtonHelp).installOn(countryButton);
        String langButtonHelp = "Язык вещания радиостанции.";
        new HelpTooltip().setDescription(langButtonHelp).installOn(langButton);
        String bitrateButtonHelp = "Битрейт потока, по данным последней проверки.";
        new HelpTooltip().setDescription(bitrateButtonHelp).installOn(bitrateButton);

        return jPanel;
    }

    public static boolean runBackgroundTask(Project project, CommunityRadioBrowser crb, ToolWindow toolWindow, String findParameter,
                                  String limit) {
        final boolean[] fileCreated = {false};
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "My Task Title", true) {
            @Override
            public void run(@NotNull com.intellij.openapi.progress.ProgressIndicator indicator) {
                // This runs on a background thread.
                indicator.setText("Working on something...");
                fileCreated[0] = findInCommunityRadioBrowser(crb, toolWindow, findParameter, limit);
                // ... perform long-running, non-UI task ...
                if (indicator.isCanceled()) {
                    return; // Check regularly for cancellation
                }
            }
        });

        return fileCreated[0];
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

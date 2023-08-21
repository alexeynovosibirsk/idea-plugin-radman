package com.nazarov.radman;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.nazarov.radman.panel.FindPanel;
import com.nazarov.radman.panel.PlayPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class RadMan implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        final JPanel findPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel playPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        findPanel.add(FindPanel.create(toolWindow), BorderLayout.PAGE_START);
        playPanel.add(PlayPanel.create(toolWindow), BorderLayout.PAGE_START);

        Content findPanelContent = ContentFactory.getInstance().createContent(findPanel, "Get Stations", false);
        Content playPanelContent = ContentFactory.getInstance().createContent(playPanel, "Playing Info", false);

        toolWindow.getContentManager().addContent(findPanelContent);
        toolWindow.getContentManager().addContent(playPanelContent);

    }

//TODO: will be used in the future
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

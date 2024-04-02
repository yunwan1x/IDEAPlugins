package com.vs2010wy.tool.ui;

import com.vs2010wy.tool.util.ProductUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class AboutDialog extends DialogWrapper {
    private AboutPanel aboutPanel;

    protected AboutDialog(@Nullable Project project) {
        super(project, false);

        aboutPanel = new AboutPanel();

        setTitle("About");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return aboutPanel;
    }
}

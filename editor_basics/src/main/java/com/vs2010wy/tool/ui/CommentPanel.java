package com.vs2010wy.tool.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class CommentPanel {
    private JPanel commitPanel;

    private JLabel categoriesLabel;
    private JTextField categoriesField;
    private JTextArea detailArea;

    private JLabel descriptionLabel;
    private JScrollPane descriptionScrollPanl;

    public JComponent getPanel() {
        return commitPanel;
    }

    public String getCategory() {
        return categoriesField.getText().trim();
    }

    public String getDetail() {
        return detailArea.getText().trim();
    }
}

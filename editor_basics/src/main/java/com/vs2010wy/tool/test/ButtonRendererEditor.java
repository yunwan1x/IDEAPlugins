package com.vs2010wy.tool.test;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JPanel panel;
    private JLabel viewButton;
    private JLabel deleteButton;
    private JLabel updateButton;

    public ButtonRendererEditor() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        viewButton = new JLabel("View");
        deleteButton = new JLabel("Delete");
        updateButton = new JLabel("Update");

        panel.add(viewButton);
        panel.add(deleteButton);
        panel.add(updateButton);

//        viewButton.addAncestorListener(this);
//        deleteButton.addActionListener(this);
//        updateButton.addActionListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println(btn.getText() + " button clicked");
        // 这里可以添加更多的逻辑来处理按钮点击事件
    }
}
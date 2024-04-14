package com.vs2010wy.tool.test;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCellRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JButton button;

    public ButtonCellRendererEditor() {
        button = new JButton();
        button.setOpaque(false);
        button.setBorderPainted(false); // 不显示边框
        button.setContentAreaFilled(false); // 不填充内容区域
        button.setFocusPainted(false); // 不显示焦点边框
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText((value == null) ? "" : value.toString());
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText((value == null) ? "" : value.toString());
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 执行点击按钮后的操作
        System.out.println("Button clicked: " + button.getText());
        fireEditingStopped(); // 结束编辑状态
    }
}
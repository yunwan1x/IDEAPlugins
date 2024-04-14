package com.vs2010wy.tool.test.test1;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class HyperlinkStyleButtonRendererEditor extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener {
    private JPanel panel;
    private JButton[] buttons = new JButton[3];

    public HyperlinkStyleButtonRendererEditor() {
        panel = new JPanel(new GridLayout(1, 3));
        panel.setOpaque(true);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setOpaque(true);
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            buttons[i].setContentAreaFilled(false);
            buttons[i].setMargin(new Insets(0, 0, 0, 0));
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].setForeground(Color.BLUE);
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }
        buttons[0].setText("view");
        buttons[1].setText("delete");
        buttons[2].setText("update");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Hyperlink clicked: " + e.getActionCommand());
        fireEditingStopped();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 1;
        }
        return true;
    }
}
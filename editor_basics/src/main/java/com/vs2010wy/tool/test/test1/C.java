package com.vs2010wy.tool.test.test1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class C {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Button in Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Action");

        model.addRow(new Object[]{"Item 1", "View"});
        model.addRow(new Object[]{"Item 2", "Delete"});

        JTable table = new JTable(model);
        HyperlinkStyleButtonRendererEditor buttonRendererEditor = new HyperlinkStyleButtonRendererEditor();
        table.getColumn("Action").setCellRenderer(buttonRendererEditor);
        table.getColumn("Action").setCellEditor(buttonRendererEditor);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//        table.setRowHeight(0);  // 调整行高

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
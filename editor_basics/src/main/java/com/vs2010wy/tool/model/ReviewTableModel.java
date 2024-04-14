package com.vs2010wy.tool.model;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewTableModel extends AbstractTableModel {
    public static final String[] TABLE_HEADER = {"Category", "Description","UpdateTime", "Actions"};

    public static final DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private List<Comment> comments = new ArrayList<>();

    @Override
    public int getRowCount() {
        return comments.size();
    }

    @Override
    public int getColumnCount() {
        return TABLE_HEADER.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // 第三列是按钮列
        if (columnIndex == 3) {
            return Object.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 只有按钮列可编辑
        return column == 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Comment comment = comments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return comment.getCategory();
            case 1:
                return comment.getDetail();
            case 2:
                return comment.getLocalDateTime().format(dateTimeFormatter);
            case 3:
                return "";
        }

        return "";
    }

    public String getColumnName(int col) {
        return TABLE_HEADER[col];
    }

    public void addRow(Comment comment) {
        comments.add(0, comment);
        fireTableRowsInserted(0, 0);
    }

    public void removeRow(int rowIndex) {
        comments.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void removeAll() {
        int totalRow = comments.size();
        comments.clear();
        if(totalRow>0){
            fireTableRowsDeleted(0, totalRow - 1);
        }
    }

    public Comment getComment(int index) {
        return comments.get(index);
    }
}

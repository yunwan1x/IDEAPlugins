package com.vs2010wy.tool.model;

import com.vs2010wy.tool.constant.TableHeader;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReviewTableModel extends AbstractTableModel {
    private List<Comment> comments = new ArrayList<>();

    @Override
    public int getRowCount() {
        return comments.size();
    }

    @Override
    public int getColumnCount() {
        return TableHeader.TABLE_HEADER.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Comment comment = comments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return comment.getFileName();
            case 1:
                return comment.getDetail();
            case 2:
                return comment.getFullPath();
        }

        return "";
    }

    public String getColumnName(int col) {
        return TableHeader.TABLE_HEADER[col];
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
        fireTableRowsDeleted(0, totalRow - 1);
    }

    public Comment getComment(int index) {
        return comments.get(index);
    }
}

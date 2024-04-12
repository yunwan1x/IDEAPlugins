package com.vs2010wy.tool.test;


import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SimplePaginatedTable {

    public JFrame frame;
    private JTable table;
    private DefaultTableModel model  = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            // 让除了"Age"列之外的所有列都不可编辑
            return column == 1; // 只有第二列（"Age"）是可编辑的
        }
    };
    private PlaceholderTextField searchField;
    private JButton addButton;

    private JButton deleteButton =  new JButton("delete");
    private JButton previousButton;
    private JButton nextButton;
    private JLabel pageLabel;
    private JComboBox<Integer> pageSizeComboBox;
    private int currentPage = 1;
    private int totalPage;

    private int totalCount;
    private int rowCountPerPage = 10; // Default rows per page
    private Vector<Vector<Object>> originalTableData = new Vector<>();

    private Vector<Vector<Object>> searchTableData = new Vector<>();// Used to store original data

    public void init(){
        for (int i = 0; i < 5; i++) { // Create 5 columns for example
            model.addColumn("列 " + (i + 1));
        }
        for (int i = 0; i < 100; i++) {
            Vector<Object> row = new Vector<>();
            for (int j = 0; j < 5; j++) {
                row.add("Data " + i + "," + j);
            }
            originalTableData.add(row);
        }
    }

    public void init0() {
        init();


        // Initialize the frame
        frame = new JFrame("Paginated JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and table

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));  // Age column
        sorter.setSortKeys(sortKeys);
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
                    // 获取当前的排序键
                    List<? extends RowSorter.SortKey> keys = sorter.getSortKeys();
                    if (!keys.isEmpty()) {
                        // 通常只有一个主键
                        RowSorter.SortKey key = keys.get(0);
                        int column = key.getColumn();
                        SortOrder order = key.getSortOrder();
                        System.out.println("Sorted column: " + column);
                        System.out.println("Sort order: " + order);
                    }
                }
            }
        });
        table.setRowSorter(sorter);

        // Initialize with 100 rows of sample data
        addButton =  new JButton("add");
        nextButton = new JButton("next");
        // Initialize search field
        searchField  = new PlaceholderTextField("");
        searchField.setPlaceholder("serch every column");
        searchField.addActionListener(this::searchAction);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Object> row = new Vector<>();
                for (int j = 0; j < 5; j++) {
                    row.add("Data"+j);
                }
                int selected = table.getSelectedRow();
                updateTableData(Math.max(currentPage,totalPage));
            }
        });
        addButton.addActionListener(new ActionListener() {
            //新增或者update
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Object> row = new Vector<>();
                for (int j = 0; j < 5; j++) {
                    row.add("Data"+j);
                }
                originalTableData.insertElementAt(row,0);
                updateTableData(currentPage);
            }
        });

        searchField.setPreferredSize(new Dimension(200 ,nextButton.getPreferredSize().height));
        // Initialize pagination buttons, label, and page size combo box
        pageSizeComboBox = new JComboBox<>(new Integer[]{ 10, 20, 50,100});
        pageSizeComboBox.setSelectedItem(rowCountPerPage);
        pageSizeComboBox.addActionListener(this::pageSizeChanged);

        totalPage = (int) Math.ceil(originalTableData.size() / (double) rowCountPerPage);
        pageLabel = new JLabel();
        updatePageLabel();
        previousButton = new JButton("pre");


        previousButton.addActionListener(this::previousAction);
        nextButton.addActionListener(this::nextAction);

        // Add components to the frame
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(pageSizeComboBox);
        bottomPanel.add(searchField);

        frame.add(bottomPanel, BorderLayout.NORTH);
        JLabel statusBar  =  new JLabel("xxxx");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.add(statusBar,BorderLayout.SOUTH);
        // Set the size of the frame and make it visible
        frame.setSize(800, 400);
        updateTableData(1);
    }



    private void pageSizeChanged(ActionEvent e) {
        updateTableData(1);
    }

    private void searchAction(ActionEvent e) {
        updateTableData(1);
    }



    private void updateTableData(int page) {
        rowCountPerPage = (int) pageSizeComboBox.getSelectedItem();
        int start = (page - 1) * rowCountPerPage;
        int end = Math.min(page * rowCountPerPage, originalTableData.size());

        Vector<Vector<Object>> pageData;
        String searchText = searchField.getText();

        if (!searchText.isEmpty()) {
            // Filter original data for search query
            Vector<Vector<Object>> filteredData = new Vector<>();
            for (Vector<Object> row : originalTableData) {
                for (Object cell : row) {
                    if (cell.toString().contains(searchText)) {
                        filteredData.add(row);
                        break;
                    }
                }
            }
            totalPage = (int) Math.ceil((double) filteredData.size() / rowCountPerPage);
            start = Math.max(0, (page - 1) * rowCountPerPage);
            end = Math.min(page * rowCountPerPage, filteredData.size());
            pageData = new Vector<>(filteredData.subList(start, end));
            totalCount= filteredData.size();
            searchTableData = filteredData;
        } else {
            // No search text, use original data
            pageData = new Vector<>(originalTableData.subList(start, end));
            totalPage = (int) Math.ceil((double) originalTableData.size() / rowCountPerPage);
            totalCount = originalTableData.size();
        }

        model.setRowCount(0); // Clear the table
        pageData.forEach(model::addRow); // Add rows to the table
        updatePageLabel();
    }

    private void updatePageLabel() {
        pageLabel.setText( String.format("共 %d 页,第 %d 页,共 %d 条",totalPage,currentPage,totalCount) );

    }

    private void previousAction(ActionEvent e) {
        if (currentPage > 1) {
            currentPage--;
            updateTableData(currentPage);
        }
    }

    private void nextAction(ActionEvent e) {
        if (currentPage < totalPage) {
            currentPage++;
            updateTableData(currentPage);
        }
    }



    public static void main(String[] args) {
        SimplePaginatedTable s =new SimplePaginatedTable();
        s.init0();
        s.frame.setVisible(true);
    }
}
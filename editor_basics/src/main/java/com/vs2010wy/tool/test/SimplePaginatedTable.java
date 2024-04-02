package com.vs2010wy.tool.test;
import com.example.demo.PlaceholderTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class SimplePaginatedTable {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private PlaceholderTextField searchField;
    private JButton previousButton;
    private JButton nextButton;
    private JLabel pageLabel;
    private JComboBox<Integer> pageSizeComboBox;
    private int currentPage = 1;
    private int totalPage;

    private int totalCount;
    private int rowCountPerPage = 10; // Default rows per page
    private Vector<Vector<Object>> originalTableData; // Used to store original data

    public SimplePaginatedTable() {
        originalTableData = new Vector<>();

        for (int i = 0; i < 100; i++) {
            Vector<Object> row = new Vector<>();
            for (int j = 0; j < 5; j++) {
                row.add("Data " + i + "," + j);
            }
            originalTableData.add(row);
        }
        // Initialize the frame
        frame = new JFrame("Paginated JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and table
        model = new DefaultTableModel();
        table = new JTable(model);
        for (int i = 0; i < 5; i++) { // Create 5 columns for example
            model.addColumn("Column " + (i + 1));
        }

        // Initialize with 100 rows of sample data

        nextButton = new JButton("next");
        // Initialize search field
        searchField  = new PlaceholderTextField("");
        searchField.setPlaceholder("press enter to search");
        searchField.addActionListener(this::searchAction);
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
        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(pageSizeComboBox);
        bottomPanel.add(searchField);

        frame.add(bottomPanel, BorderLayout.NORTH);

        // Set the size of the frame and make it visible
        frame.setSize(800, 400);
        frame.setVisible(true);

        updateTableData(1);
    }



    private void pageSizeChanged(ActionEvent e) {
        rowCountPerPage = (int) pageSizeComboBox.getSelectedItem();
        totalPage = (int) Math.ceil(originalTableData.size() / (double) rowCountPerPage);
        updateTableData(1); // Reset to the first page

        rowCountPerPage = (int) pageSizeComboBox.getSelectedItem();
        totalPage = (int) Math.ceil(originalTableData.size() / (double) rowCountPerPage);
        currentPage = 1; // Reset to the first page when page size is changed
        updateTableData(currentPage); // Update table to the first page
    }

    private void searchAction(ActionEvent e) {
        updateTableData(1);
    }

    private void updateTableWithFilteredData(Vector<Vector<Object>> filteredData) {

        model.setRowCount(0); // Clear the table
        filteredData.forEach(model::addRow); // Add search result rows to the table
        totalCount  = filteredData.size();
        // Update total pages and current page
        totalPage = (int) Math.ceil((double) filteredData.size() / rowCountPerPage);
        currentPage = 1; // Reset to the first page
        updatePageLabel();
    }

    private void updateTableData(int page) {
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
        } else {
            // No search text, use original data
            pageData = new Vector<>(originalTableData.subList(start, end));
        }

        model.setRowCount(0); // Clear the table
        pageData.forEach(model::addRow); // Add rows to the table
        totalCount = pageData.size();
        updatePageLabel();
    }

    private void updatePageLabel() {
        pageLabel.setText( currentPage + "/" + totalPage + "/" + totalCount);
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
        SwingUtilities.invokeLater(SimplePaginatedTable::new);
    }
}
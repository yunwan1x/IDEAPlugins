package com.vs2010wy.tool.test;


import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.vs2010wy.tool.model.Comment;
import com.vs2010wy.tool.model.Position;
import com.vs2010wy.tool.model.ReviewTableModel;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SimplePaginatedTable {

    public JFrame frame;
    private JTable table;
    private ReviewTableModel model  = new ReviewTableModel();
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
    private Vector<Comment> comments = new Vector<>();

    private Project project;
    private Vector<Comment> searchTableData = new Vector<>();// Used to store original data

    public void addComment(Comment comment){
        comments.insertElementAt(comment,0);
        updateTableData(currentPage);
    }


    public void init0() {

        // Initialize the frame
        frame = new JFrame("Paginated JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and table

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int rowIndex = table.rowAtPoint(e.getPoint());
                    if (rowIndex == -1) {
                        selectionModel.clearSelection();
                        return;
                    }

                    Comment comment = model.getComment(rowIndex);
                    VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(comment.getFullPath()));

                    Position position = comment.getLocation().getStart();
                    OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile,
                            position.getRow(), position.getColumn());
                    openFileDescriptor.navigate(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

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

        ButtonRendererEditor buttonRendererEditor = new ButtonRendererEditor();
        table.getColumn("Actions").setCellRenderer(buttonRendererEditor);
        table.getColumn("Actions").setCellEditor(buttonRendererEditor);
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
                updateTableData(Math.max(currentPage,totalPage));
            }
        });


        searchField.setPreferredSize(new Dimension(200 ,nextButton.getPreferredSize().height));
        // Initialize pagination buttons, label, and page size combo box
        pageSizeComboBox = new JComboBox<>(new Integer[]{ 10, 20, 50,100});
        pageSizeComboBox.setSelectedItem(rowCountPerPage);
        pageSizeComboBox.addActionListener(this::pageSizeChanged);

        totalPage = (int) Math.ceil(comments.size() / (double) rowCountPerPage);
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
//        JLabel statusBar  =  new JLabel("xxxx");
//        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
//        frame.add(statusBar,BorderLayout.SOUTH);
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
        int end = Math.min(page * rowCountPerPage, comments.size());

        Vector<Comment> pageData;
        String searchText = searchField.getText();

        if (!searchText.isEmpty()) {
            // Filter original data for search query
            Vector<Comment> filteredData = new Vector<>();
            for (Comment comment : comments) {
                if(comment.getCategory().contains(searchText)||comment.getDetail().contains(searchText)){
                    filteredData.add(comment);
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
            pageData = new Vector<>(comments.subList(start, end));
            totalPage = (int) Math.ceil((double) comments.size() / rowCountPerPage);
            totalCount = comments.size();
        }
        model.removeAll(); // Clear the table
        for(Comment comment: pageData){
            model.addRow(comment);
        }
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

    public void setProject(Project project) {
        this.project = project;
    }
}
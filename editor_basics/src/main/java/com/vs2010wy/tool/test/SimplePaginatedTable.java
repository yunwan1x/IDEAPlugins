package com.vs2010wy.tool.test;


import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.vs2010wy.tool.model.Comment;
import com.vs2010wy.tool.model.Location;
import com.vs2010wy.tool.model.Position;
import com.vs2010wy.tool.model.ReviewTableModel;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
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
        int index = comments.indexOf(comment);
        if(index>-1){
            Comment old = comments.get(index);
            old.setCategory(comment.getCategory());
            old.setDetail(comment.getDetail());
            old.setLocalDateTime(comment.getLocalDateTime());
        }else {
            comments.insertElementAt(comment,0);
        }
        updateTableData(currentPage);
    }


    public void init0() {

        for(int i=0;i<100;i++){
            Position start = new Position(i,i);
            Location location =  new Location(start,start);
            Comment comment =new Comment(i+1,"",i+"",location,i+"",i+"");
            comments.add(comment);
        }
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
                    try{
                        Comment comment = model.getComment(rowIndex);
                        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(comment.getFullPath()));
                        Position startPosition = comment.getLocation().getStart();
                        Position endPosition = comment.getLocation().getEnd();
                        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile,
                                startPosition.getRow(), startPosition.getColumn());
                        openFileDescriptor.navigate(true);
                        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                        if (editor == null) return;

                        // 获取选择模型
                        SelectionModel selectionModel = editor.getSelectionModel();
                        Document document = editor.getDocument();
                        int startOffset = document.getLineStartOffset(startPosition.getRow()) + startPosition.getColumn();
                        int endOffset = document.getLineStartOffset(endPosition.getRow()) + endPosition.getColumn();
                        // 设置选择区域，这里以选择文档的前10个字符为例
                        selectionModel.setSelection(startOffset, endOffset);
                    }catch (Exception v){

                    }

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
        TableColumn firstColumn = table.getColumn("UpdateTime");
        firstColumn.setPreferredWidth(200);
        firstColumn.setMaxWidth(200);
        firstColumn.setMinWidth(200);
        nextButton = new JButton("next");
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
        bottomPanel.add(deleteButton);
        bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(pageSizeComboBox);
        bottomPanel.add(searchField);
        deleteButton.addActionListener(this::deleteAction);
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

    private void deleteAction(ActionEvent e) {
        int rowIndex = table.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }
        try{
            rowIndex = table.getSelectedRow();
            ReviewTableModel reviewTableModel = (ReviewTableModel)table.getModel();
            Comment comment = reviewTableModel.getComment(rowIndex);
            comments.remove(comment);
        }catch (Exception v){

        }
        updateTableData(currentPage);
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
        pageLabel.setText( String.format("%d pages, page %d , %d items",totalPage,currentPage,totalCount) );

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
package com.vs2010wy.tool.ui;

import com.vs2010wy.tool.model.Comment;
import com.vs2010wy.tool.model.Position;
import com.vs2010wy.tool.model.ReviewTableModel;
import com.vs2010wy.tool.service.ReviewWindowService;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class WindowFactory implements ToolWindowFactory {

    private Project project;

    private JBTable reviewTable;
    private JScrollPane reviewScrollPanel;
    private ToolWindow myToolWindow;
    private JPanel myToolWindowContent;
    private JPanel functionPanel;
    private JButton exportBtn;
    private JButton removeBtn;
    private JButton removeAllBtn;
    private JButton aboutBtn;

    private ReviewTableModel reviewTableModel;

    public WindowFactory() {
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        myToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

        this.reviewTableModel = ServiceManager.getService(project, ReviewWindowService.class).getReviewTableModel();
        configureModelListener();
    }

    private void configureModelListener() {
        createReviewTable();
        createRemoveBtn();
        createRemoveAllBtn();
        createAboutBtn();
    }

    private void createReviewTable() {
        reviewTable.setModel(reviewTableModel);
        ListSelectionModel selectionModel = reviewTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reviewTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int rowIndex = reviewTable.rowAtPoint(e.getPoint());
                    if (rowIndex == -1) {
                        selectionModel.clearSelection();
                        return;
                    }

                    Comment comment = reviewTableModel.getComment(rowIndex);
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

    }

    private void createRemoveBtn() {
        removeBtn.addActionListener((actionEvent) ->
                reviewTableModel.removeRow(reviewTable.getSelectedRow()));
    }

    private void createRemoveAllBtn() {
        removeAllBtn.addActionListener((actionEvent) -> reviewTableModel.removeAll());
    }

    private void createAboutBtn() {
        aboutBtn.addActionListener((actionEvent) -> new AboutDialog(project).show());
    }
}

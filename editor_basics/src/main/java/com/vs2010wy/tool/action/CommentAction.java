package com.vs2010wy.tool.action;

import com.vs2010wy.tool.model.Comment;
import com.vs2010wy.tool.model.Location;
import com.vs2010wy.tool.model.Position;
import com.vs2010wy.tool.model.ReviewTableModel;
import com.vs2010wy.tool.service.ReviewWindowService;
import com.vs2010wy.tool.ui.CommentDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;

public class CommentAction extends AnAction {
    private static final Logger LOGGER = Logger.getInstance(CommentAction.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        CommentDialog commentDialog = new CommentDialog(event.getProject());
        commentDialog.show();
        if (commentDialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        Comment comment = commentDialog.getComment();
        fillCommentProjectInfo(event, comment);

        ReviewTableModel reviewModel = getReviewModel(event.getProject());
        if (reviewModel == null) {
            LOGGER.error("reviewModel is null");
            return;
        }

        reviewModel.addRow(comment);
    }

    private void fillCommentProjectInfo(AnActionEvent event, Comment comment) {
        VirtualFile commentFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        comment.setFullPath(commentFile.getCanonicalPath());
        comment.setFileName(commentFile.getName());

        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        LogicalPosition startPosition = editor.offsetToLogicalPosition(selectionModel.getSelectionStart());
        LogicalPosition endPosition = editor.offsetToLogicalPosition(selectionModel.getSelectionEnd());

        comment.setLocation(new Location(new Position(startPosition.line, startPosition.column),
                new Position(endPosition.line, endPosition.column)));
    }

    @Override
    public void update(AnActionEvent e) {
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor
        e.getPresentation().setVisible(project != null && editor != null);
    }

    private ReviewTableModel getReviewModel(Project project) {
        ReviewWindowService windowService = ServiceManager.getService(project, ReviewWindowService.class);
        return windowService.getReviewTableModel();
    }
}


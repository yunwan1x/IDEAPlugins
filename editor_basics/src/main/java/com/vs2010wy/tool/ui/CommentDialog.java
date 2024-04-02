package com.vs2010wy.tool.ui;

import com.vs2010wy.tool.model.Comment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class CommentDialog extends DialogWrapper {
    private static final String TITLE = "comment dialog";

    private CommentPanel commentPanel;

    private Comment comment;

    public CommentDialog(final Project project) {
        super(project, true);

        commentPanel = new CommentPanel();
        setTitle(TITLE);
        init();
    }

    @Override
    protected void doOKAction() {
        comment = new Comment();
        comment.setCategory(commentPanel.getCategory());
        comment.setDetail(commentPanel.getDetail());

        super.doOKAction();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return commentPanel.getPanel();
    }

    public Comment getComment() {
        return comment;
    }
}

package com.vs2010wy.tool.test;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class CodeReivewWindow  implements ToolWindowFactory {

    Project project;





    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        ContentFactory contentFactory = ContentFactory.getInstance();
        SimplePaginatedTable simplePaginatedTable =  new SimplePaginatedTable();
        simplePaginatedTable.init0();
        Content content = contentFactory.createContent(simplePaginatedTable.frame.getRootPane(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

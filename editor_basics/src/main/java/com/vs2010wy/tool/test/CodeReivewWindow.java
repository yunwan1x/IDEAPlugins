package com.vs2010wy.tool.test;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.vs2010wy.tool.service.ReviewWindowService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeReivewWindow  implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        SimplePaginatedTable simplePaginatedTable = ServiceManager.getService(project, ReviewWindowService.class).getSimplePaginatedTable();
        simplePaginatedTable.setProject(project);
        Content content = contentFactory.createContent(simplePaginatedTable.frame.getRootPane(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

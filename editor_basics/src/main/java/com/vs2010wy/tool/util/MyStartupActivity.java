package com.vs2010wy.tool.util;




import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class MyStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        // Your initialization code here
        MyProjectSettingsService settingsService = MyProjectSettingsService.getInstance(project);
    }
}


package com.vs2010wy.tool.util;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.PROJECT)
@State(
        name = "MyProjectSettings",
        storages = @Storage("myProjectSettings.xml")
)
public final class MyProjectSettingsService implements PersistentStateComponent<MyProjectSettingsState> {
    private MyProjectSettingsState myState = new MyProjectSettingsState();

    public static MyProjectSettingsService getInstance(Project project) {
        return project.getService(MyProjectSettingsService.class);
    }

    @Override
    public MyProjectSettingsState getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull MyProjectSettingsState state) {
        myState = state;
    }
}
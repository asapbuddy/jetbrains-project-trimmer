package dev.asapbuddy.prefixtrimmer;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.jetbrains.rider.projectView.ProjectModelViewUpdater;
import org.jetbrains.annotations.NotNull;

public final class ProjectViewRefresher {
    private ProjectViewRefresher() {
    }

    public static void refresh(@NotNull Project project) {
        if (project.isDisposed()) {
            return;
        }

        ApplicationManager.getApplication().invokeLater(() -> {
            if (!project.isDisposed()) {
                ProjectView.getInstance(project).refresh();
                for (ProjectModelViewUpdater updater : ProjectModelViewUpdater.Companion.getEP_NAME().getExtensionList(project)) {
                    updater.updateAllPresentations();
                }
            }
        });
    }
}

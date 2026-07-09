package dev.asapbuddy.prefixtrimmer;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.openapi.project.Project;
import com.jetbrains.rider.model.RdProjectDescriptor;
import com.jetbrains.rider.projectView.views.solutionExplorer.SolutionExplorerCustomization;
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity;
import org.jetbrains.annotations.NotNull;

public final class SolutionExplorerPrefixTrimmerCustomization extends SolutionExplorerCustomization {
    public SolutionExplorerPrefixTrimmerCustomization(@NotNull Project project) {
        super(project);
    }

    @Override
    public void updateNode(@NotNull PresentationData data, @NotNull ProjectModelEntity entity) {
        EffectivePrefixSettings settings = EffectivePrefixSettings.of(getProject());
        if (!settings.isActive()) {
            return;
        }

        if (entity.getDescriptor() instanceof RdProjectDescriptor) {
            PrefixPresentationTrimmer.trim(data, settings.prefixes());
        }
    }
}

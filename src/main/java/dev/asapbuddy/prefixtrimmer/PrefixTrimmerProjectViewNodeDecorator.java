package dev.asapbuddy.prefixtrimmer;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.openapi.project.Project;

public final class PrefixTrimmerProjectViewNodeDecorator implements ProjectViewNodeDecorator {
    @Override
    public void decorate(ProjectViewNode<?> node, PresentationData data) {
        Project project = node.getProject();
        if (project == null || project.isDisposed()) {
            return;
        }

        EffectivePrefixSettings settings = EffectivePrefixSettings.of(project);
        if (!settings.isActive()) {
            return;
        }

        PrefixPresentationTrimmer.trim(data, settings.prefixes());
    }
}

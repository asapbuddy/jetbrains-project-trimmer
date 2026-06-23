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

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        if (!settings.isEnabled() || settings.getPrefixes().isEmpty()) {
            return;
        }

        PrefixPresentationTrimmer.trim(data, settings);
    }
}

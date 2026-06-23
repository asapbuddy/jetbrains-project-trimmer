package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SetPrefixAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null || project.isDisposed()) {
            return;
        }

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        String currentPrefixes = PrefixTrimmer.prefixesToText(settings.getPrefixes());
        String input = Messages.showInputDialog(
                project,
                "Prefix to hide from project names. Use Settings | Tools | Solution Prefix Trimmer for multiple prefixes.",
                "Set Solution Prefix to Hide",
                null,
                currentPrefixes.lines().findFirst().orElse(""),
                null
        );

        if (input == null) {
            return;
        }

        List<String> prefixes = PrefixTrimmer.parsePrefixes(input);
        settings.setPrefixes(prefixes);
        settings.setEnabled(!prefixes.isEmpty());
        ProjectViewRefresher.refresh(project);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(event.getData(CommonDataKeys.PROJECT) != null);
    }
}

package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SetPrefixAction extends DumbAwareAction {
    private static final String GLOBAL_MESSAGE =
            "Prefix to hide from project names. This project follows the global prefixes, so the change applies to every project. "
                    + "Use Settings | Tools | Solution Prefix Trimmer for multiple prefixes.";
    private static final String PROJECT_MESSAGE =
            "Prefix to hide from project names. This project overrides the global prefixes. "
                    + "Use Settings | Tools | Solution Prefix Trimmer for multiple prefixes.";

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null || project.isDisposed()) {
            return;
        }

        boolean useGlobal = PrefixTrimmerSettings.getInstance(project).isUseGlobalPrefixes();
        String currentPrefixes = PrefixTrimmer.prefixesToText(EffectivePrefixSettings.of(project).prefixes());
        String input = Messages.showInputDialog(
                project,
                useGlobal ? GLOBAL_MESSAGE : PROJECT_MESSAGE,
                "Set Solution Prefix to Hide",
                null,
                currentPrefixes.lines().findFirst().orElse(""),
                null
        );

        if (input == null) {
            return;
        }

        List<String> prefixes = PrefixTrimmer.parsePrefixes(input);
        if (useGlobal) {
            PrefixTrimmerApplicationSettings settings = PrefixTrimmerApplicationSettings.getInstance();
            settings.setPrefixes(prefixes);
            settings.setEnabled(!prefixes.isEmpty());
            ProjectViewRefresher.refreshAll();
            return;
        }

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        settings.setPrefixes(prefixes);
        settings.setEnabled(!prefixes.isEmpty());
        ProjectViewRefresher.refresh(project);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(event.getData(CommonDataKeys.PROJECT) != null);
    }
}

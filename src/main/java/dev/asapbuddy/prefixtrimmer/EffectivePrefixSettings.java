package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record EffectivePrefixSettings(boolean enabled, @NotNull List<String> prefixes) {
    public static @NotNull EffectivePrefixSettings of(@NotNull Project project) {
        PrefixTrimmerSettings projectSettings = PrefixTrimmerSettings.getInstance(project);
        if (!projectSettings.isUseGlobalPrefixes()) {
            return new EffectivePrefixSettings(projectSettings.isEnabled(), projectSettings.getPrefixes());
        }

        PrefixTrimmerApplicationSettings applicationSettings = PrefixTrimmerApplicationSettings.getInstance();
        return new EffectivePrefixSettings(applicationSettings.isEnabled(), applicationSettings.getPrefixes());
    }

    public boolean isActive() {
        return enabled && !prefixes.isEmpty();
    }
}

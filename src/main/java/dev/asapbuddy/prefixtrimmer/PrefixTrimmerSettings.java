package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service(Service.Level.PROJECT)
@com.intellij.openapi.components.State(
        name = "SolutionPrefixTrimmerSettings",
        storages = @Storage(StoragePathMacros.WORKSPACE_FILE)
)
public final class PrefixTrimmerSettings implements PersistentStateComponent<PrefixTrimmerSettings.SettingsState> {
    private SettingsState state = new SettingsState();

    public static @NotNull PrefixTrimmerSettings getInstance(@NotNull Project project) {
        return project.getService(PrefixTrimmerSettings.class);
    }

    @Override
    public @Nullable SettingsState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        this.state = state;
        this.state.prefixes = new ArrayList<>(PrefixTrimmer.parsePrefixes(PrefixTrimmer.prefixesToText(safePrefixes(state.prefixes))));
    }

    public boolean isEnabled() {
        return state.enabled;
    }

    public void setEnabled(boolean enabled) {
        state.enabled = enabled;
    }

    public @NotNull List<String> getPrefixes() {
        if (state.prefixes == null) {
            state.prefixes = new ArrayList<>();
        }
        return state.prefixes;
    }

    public void setPrefixes(@NotNull List<String> prefixes) {
        state.prefixes = new ArrayList<>(PrefixTrimmer.parsePrefixes(PrefixTrimmer.prefixesToText(prefixes)));
    }

    private static @NotNull List<String> safePrefixes(@Nullable List<String> prefixes) {
        return prefixes == null ? Collections.emptyList() : prefixes;
    }

    public static final class SettingsState {
        public boolean enabled = true;
        public List<String> prefixes = new ArrayList<>();
    }
}

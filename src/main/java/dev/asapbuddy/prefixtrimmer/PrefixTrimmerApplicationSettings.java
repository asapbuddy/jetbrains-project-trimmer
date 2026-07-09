package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Service(Service.Level.APP)
@com.intellij.openapi.components.State(
        name = "SolutionPrefixTrimmerApplicationSettings",
        storages = @Storage("solution-prefix-trimmer.xml")
)
public final class PrefixTrimmerApplicationSettings implements PersistentStateComponent<PrefixTrimmerApplicationSettings.SettingsState> {
    private SettingsState state = new SettingsState();

    public static @NotNull PrefixTrimmerApplicationSettings getInstance() {
        return ApplicationManager.getApplication().getService(PrefixTrimmerApplicationSettings.class);
    }

    @Override
    public @Nullable SettingsState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        this.state = state;
        this.state.prefixes = PrefixTrimmer.normalizePrefixes(state.prefixes);
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
        state.prefixes = PrefixTrimmer.normalizePrefixes(prefixes);
    }

    public static final class SettingsState {
        public boolean enabled = true;
        public List<String> prefixes = new ArrayList<>();
    }
}

package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public final class PrefixTrimmerApplicationConfigurable implements SearchableConfigurable, Configurable.NoScroll {
    private JBCheckBox enabledCheckBox;
    private PrefixesEditor prefixesEditor;
    private JPanel panel;

    @Override
    public @NotNull String getId() {
        return "dev.asapbuddy.solution-prefix-trimmer";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Solution Prefix Trimmer";
    }

    @Override
    public @Nullable JComponent createComponent() {
        enabledCheckBox = new JBCheckBox("Hide configured prefixes in the Project/Solution tree");
        prefixesEditor = new PrefixesEditor("Global prefixes, one per line (used by every project):");

        panel = new JPanel(new BorderLayout(0, JBUI.scale(10)));
        panel.setBorder(JBUI.Borders.empty(8));
        panel.add(enabledCheckBox, BorderLayout.NORTH);
        panel.add(prefixesEditor.getPanel(), BorderLayout.CENTER);

        reset();
        return panel;
    }

    @Override
    public boolean isModified() {
        if (enabledCheckBox == null || prefixesEditor == null) {
            return false;
        }

        PrefixTrimmerApplicationSettings settings = PrefixTrimmerApplicationSettings.getInstance();
        return enabledCheckBox.isSelected() != settings.isEnabled()
                || !prefixesEditor.getPrefixes().equals(settings.getPrefixes());
    }

    @Override
    public void apply() {
        PrefixTrimmerApplicationSettings settings = PrefixTrimmerApplicationSettings.getInstance();
        settings.setEnabled(enabledCheckBox.isSelected());
        settings.setPrefixes(prefixesEditor.getPrefixes());
        ProjectViewRefresher.refreshAll();
    }

    @Override
    public void reset() {
        if (enabledCheckBox == null || prefixesEditor == null) {
            return;
        }

        PrefixTrimmerApplicationSettings settings = PrefixTrimmerApplicationSettings.getInstance();
        enabledCheckBox.setSelected(settings.isEnabled());
        prefixesEditor.setPrefixes(settings.getPrefixes());
    }

    @Override
    public void disposeUIResources() {
        enabledCheckBox = null;
        prefixesEditor = null;
        panel = null;
    }
}

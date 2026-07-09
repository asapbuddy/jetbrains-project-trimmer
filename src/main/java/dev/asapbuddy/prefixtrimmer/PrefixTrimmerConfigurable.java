package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public final class PrefixTrimmerConfigurable implements SearchableConfigurable, Configurable.NoScroll {
    private final Project project;
    private JBCheckBox useGlobalCheckBox;
    private JBCheckBox enabledCheckBox;
    private PrefixesEditor prefixesEditor;
    private JPanel panel;
    private boolean projectEnabled = true;
    private List<String> projectPrefixes = new ArrayList<>();

    public PrefixTrimmerConfigurable(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public @NotNull String getId() {
        return "dev.asapbuddy.solution-prefix-trimmer.project";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Project Overrides";
    }

    @Override
    public @Nullable JComponent createComponent() {
        useGlobalCheckBox = new JBCheckBox("Use global prefixes");
        enabledCheckBox = new JBCheckBox("Hide configured prefixes in the Project/Solution tree");
        prefixesEditor = new PrefixesEditor("Prefixes for this project only, one per line:");

        JPanel checkBoxes = new JPanel();
        checkBoxes.setLayout(new BoxLayout(checkBoxes, BoxLayout.Y_AXIS));
        checkBoxes.add(useGlobalCheckBox);
        checkBoxes.add(enabledCheckBox);

        panel = new JPanel(new BorderLayout(0, JBUI.scale(10)));
        panel.setBorder(JBUI.Borders.empty(8));
        panel.add(checkBoxes, BorderLayout.NORTH);
        panel.add(prefixesEditor.getPanel(), BorderLayout.CENTER);

        reset();
        useGlobalCheckBox.addActionListener(event -> {
            if (useGlobalCheckBox.isSelected()) {
                projectEnabled = enabledCheckBox.isSelected();
                projectPrefixes = prefixesEditor.getPrefixes();
            }
            showEffectiveSource();
        });
        return panel;
    }

    @Override
    public boolean isModified() {
        if (useGlobalCheckBox == null || enabledCheckBox == null || prefixesEditor == null) {
            return false;
        }

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        if (useGlobalCheckBox.isSelected() != settings.isUseGlobalPrefixes()) {
            return true;
        }
        if (useGlobalCheckBox.isSelected()) {
            return false;
        }

        return enabledCheckBox.isSelected() != settings.isEnabled()
                || !prefixesEditor.getPrefixes().equals(settings.getPrefixes());
    }

    @Override
    public void apply() {
        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        boolean useGlobal = useGlobalCheckBox.isSelected();
        settings.setUseGlobalPrefixes(useGlobal);
        if (!useGlobal) {
            projectEnabled = enabledCheckBox.isSelected();
            projectPrefixes = prefixesEditor.getPrefixes();
            settings.setEnabled(projectEnabled);
            settings.setPrefixes(projectPrefixes);
        }
        ProjectViewRefresher.refresh(project);
    }

    @Override
    public void reset() {
        if (useGlobalCheckBox == null || enabledCheckBox == null || prefixesEditor == null) {
            return;
        }

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        projectEnabled = settings.isEnabled();
        projectPrefixes = new ArrayList<>(settings.getPrefixes());
        useGlobalCheckBox.setSelected(settings.isUseGlobalPrefixes());
        showEffectiveSource();
    }

    @Override
    public void disposeUIResources() {
        useGlobalCheckBox = null;
        enabledCheckBox = null;
        prefixesEditor = null;
        panel = null;
    }

    private void showEffectiveSource() {
        boolean useGlobal = useGlobalCheckBox.isSelected();
        if (useGlobal) {
            PrefixTrimmerApplicationSettings globalSettings = PrefixTrimmerApplicationSettings.getInstance();
            enabledCheckBox.setSelected(globalSettings.isEnabled());
            prefixesEditor.setPrefixes(globalSettings.getPrefixes());
        } else {
            enabledCheckBox.setSelected(projectEnabled);
            prefixesEditor.setPrefixes(projectPrefixes);
        }

        enabledCheckBox.setEnabled(!useGlobal);
        prefixesEditor.setEditable(!useGlobal);
    }
}

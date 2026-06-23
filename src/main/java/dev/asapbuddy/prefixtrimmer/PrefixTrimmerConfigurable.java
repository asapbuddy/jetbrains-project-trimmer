package dev.asapbuddy.prefixtrimmer;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

public final class PrefixTrimmerConfigurable implements SearchableConfigurable, Configurable.NoScroll {
    private final Project project;
    private JBCheckBox enabledCheckBox;
    private JBTextArea prefixesTextArea;
    private JPanel panel;

    public PrefixTrimmerConfigurable(@NotNull Project project) {
        this.project = project;
    }

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
        prefixesTextArea = new JBTextArea();
        prefixesTextArea.setRows(6);
        prefixesTextArea.setLineWrap(false);

        JBLabel label = new JBLabel("Prefixes, one per line:");
        JBLabel example = new JBLabel("Example: Order.Kuper.Adapter");

        JPanel content = new JPanel(new BorderLayout(0, JBUI.scale(6)));
        content.add(label, BorderLayout.NORTH);
        JBScrollPane scrollPane = new JBScrollPane(prefixesTextArea);
        scrollPane.setPreferredSize(new Dimension(JBUI.scale(420), JBUI.scale(120)));
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(example, BorderLayout.SOUTH);

        panel = new JPanel(new BorderLayout(0, JBUI.scale(10)));
        panel.setBorder(JBUI.Borders.empty(8));
        panel.add(enabledCheckBox, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        reset();
        return panel;
    }

    @Override
    public boolean isModified() {
        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        return enabledCheckBox != null
                && prefixesTextArea != null
                && (enabledCheckBox.isSelected() != settings.isEnabled()
                || !PrefixTrimmer.parsePrefixes(prefixesTextArea.getText()).equals(settings.getPrefixes()));
    }

    @Override
    public void apply() throws ConfigurationException {
        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        List<String> prefixes = PrefixTrimmer.parsePrefixes(prefixesTextArea.getText());
        settings.setEnabled(enabledCheckBox.isSelected());
        settings.setPrefixes(prefixes);
        ProjectViewRefresher.refresh(project);
    }

    @Override
    public void reset() {
        if (enabledCheckBox == null || prefixesTextArea == null) {
            return;
        }

        PrefixTrimmerSettings settings = PrefixTrimmerSettings.getInstance(project);
        enabledCheckBox.setSelected(settings.isEnabled());
        prefixesTextArea.setText(PrefixTrimmer.prefixesToText(settings.getPrefixes()));
    }

    @Override
    public void disposeUIResources() {
        enabledCheckBox = null;
        prefixesTextArea = null;
        panel = null;
    }
}

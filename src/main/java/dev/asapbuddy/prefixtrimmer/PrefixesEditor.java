package dev.asapbuddy.prefixtrimmer;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

final class PrefixesEditor {
    private final JBTextArea textArea = new JBTextArea();
    private final JPanel panel;

    PrefixesEditor(@NotNull String labelText) {
        textArea.setRows(6);
        textArea.setLineWrap(false);

        JBScrollPane scrollPane = new JBScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(JBUI.scale(420), JBUI.scale(120)));

        panel = new JPanel(new BorderLayout(0, JBUI.scale(6)));
        panel.add(new JBLabel(labelText), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JBLabel("Example: Order.Kuper.Adapter"), BorderLayout.SOUTH);
    }

    @NotNull JPanel getPanel() {
        return panel;
    }

    @NotNull List<String> getPrefixes() {
        return PrefixTrimmer.parsePrefixes(textArea.getText());
    }

    void setPrefixes(@NotNull List<String> prefixes) {
        textArea.setText(PrefixTrimmer.prefixesToText(prefixes));
    }

    void setEditable(boolean editable) {
        textArea.setEnabled(editable);
    }
}

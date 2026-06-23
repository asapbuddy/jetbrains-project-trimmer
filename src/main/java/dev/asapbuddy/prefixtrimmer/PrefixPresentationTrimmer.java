package dev.asapbuddy.prefixtrimmer;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.PresentableNodeDescriptor;
import com.intellij.ui.SimpleTextAttributes;

import java.util.ArrayList;
import java.util.List;

final class PrefixPresentationTrimmer {
    private PrefixPresentationTrimmer() {
    }

    static void trim(PresentationData data, PrefixTrimmerSettings settings) {
        if (trimPresentableText(data, settings)) {
            return;
        }

        trimFirstColoredFragment(data, settings);
    }

    private static boolean trimPresentableText(PresentationData data, PrefixTrimmerSettings settings) {
        String originalText = data.getPresentableText();
        if (originalText == null || originalText.isBlank()) {
            return false;
        }

        String trimmedText = PrefixTrimmer.trim(originalText, settings.getPrefixes());
        if (trimmedText.equals(originalText) || trimmedText.isBlank()) {
            return false;
        }

        data.setPresentableText(trimmedText);
        data.clearText();
        data.addText(trimmedText, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        return true;
    }

    private static void trimFirstColoredFragment(PresentationData data, PrefixTrimmerSettings settings) {
        List<PresentableNodeDescriptor.ColoredFragment> fragments = new ArrayList<>(data.getColoredText());
        if (fragments.isEmpty()) {
            return;
        }

        PresentableNodeDescriptor.ColoredFragment firstFragment = fragments.get(0);
        String originalText = firstFragment.getText();
        if (originalText == null || originalText.isBlank()) {
            return;
        }

        String trimmedText = PrefixTrimmer.trim(originalText, settings.getPrefixes());
        if (trimmedText.equals(originalText) || trimmedText.isBlank()) {
            return;
        }

        data.setPresentableText(trimmedText);
        data.clearText();
        data.addText(trimmedText, firstFragment.getAttributes());
        for (int i = 1; i < fragments.size(); i++) {
            data.addText(fragments.get(i));
        }
    }
}

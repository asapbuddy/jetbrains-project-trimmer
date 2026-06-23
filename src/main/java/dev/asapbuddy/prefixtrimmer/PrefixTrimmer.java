package dev.asapbuddy.prefixtrimmer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class PrefixTrimmer {
    private PrefixTrimmer() {
    }

    public static @NotNull String trim(@NotNull String text, @NotNull Collection<String> rawPrefixes) {
        String current = text;
        for (String rawPrefix : rawPrefixes) {
            String prefix = normalizePrefix(rawPrefix);
            if (prefix.isEmpty()) {
                continue;
            }

            String dottedPrefix = prefix + ".";
            if (current.startsWith(dottedPrefix) && current.length() > dottedPrefix.length()) {
                return current.substring(dottedPrefix.length());
            }

        }

        return current;
    }

    public static @NotNull List<String> parsePrefixes(@NotNull String text) {
        List<String> prefixes = new ArrayList<>();
        String[] parts = text.split("[\\r\\n,;]+");
        for (String part : parts) {
            String prefix = normalizePrefix(part);
            if (!prefix.isEmpty() && !prefixes.contains(prefix)) {
                prefixes.add(prefix);
            }
        }
        return prefixes;
    }

    public static @NotNull String prefixesToText(@NotNull Collection<String> prefixes) {
        return String.join("\n", prefixes.stream()
                .filter(Objects::nonNull)
                .map(PrefixTrimmer::normalizePrefix)
                .filter(prefix -> !prefix.isEmpty())
                .distinct()
                .toList());
    }

    private static @NotNull String normalizePrefix(String rawPrefix) {
        if (rawPrefix == null) {
            return "";
        }

        String prefix = rawPrefix.trim();
        while (prefix.endsWith(".")) {
            prefix = prefix.substring(0, prefix.length() - 1).trim();
        }
        return prefix;
    }
}

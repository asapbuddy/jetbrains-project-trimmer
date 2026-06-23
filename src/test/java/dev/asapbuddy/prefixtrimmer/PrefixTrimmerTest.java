package dev.asapbuddy.prefixtrimmer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrefixTrimmerTest {
    @Test
    void trimsDottedProjectPrefix() {
        assertEquals(
                "Api",
                PrefixTrimmer.trim("Order.Kuper.Adapter.Api", List.of("Order.Kuper.Adapter"))
        );
    }

    @Test
    void trimsPrefixWithTrailingDot() {
        assertEquals(
                "Infrastructure.UnitTests",
                PrefixTrimmer.trim("Order.Kuper.Adapter.Infrastructure.UnitTests", List.of("Order.Kuper.Adapter."))
        );
    }

    @Test
    void leavesNonMatchingNamesAlone() {
        assertEquals(
                "Directory.Build.props",
                PrefixTrimmer.trim("Directory.Build.props", List.of("Order.Kuper.Adapter"))
        );
    }

    @Test
    void leavesSolutionRootSummaryAlone() {
        assertEquals(
                "Order.Kuper.Adapter · 9 projects",
                PrefixTrimmer.trim("Order.Kuper.Adapter · 9 projects", List.of("Order.Kuper.Adapter"))
        );
    }

    @Test
    void leavesExactSolutionNameAlone() {
        assertEquals(
                "Order.Kuper.Adapter",
                PrefixTrimmer.trim("Order.Kuper.Adapter", List.of("Order.Kuper.Adapter"))
        );
    }

    @Test
    void parsesMultiplePrefixes() {
        assertEquals(
                List.of("Order.Kuper.Adapter", "Billing.Core"),
                PrefixTrimmer.parsePrefixes("Order.Kuper.Adapter.\nBilling.Core; Order.Kuper.Adapter")
        );
    }
}

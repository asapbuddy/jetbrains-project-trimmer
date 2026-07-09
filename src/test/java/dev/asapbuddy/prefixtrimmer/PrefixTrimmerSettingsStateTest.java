package dev.asapbuddy.prefixtrimmer;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrefixTrimmerSettingsStateTest {
    @Test
    void legacyStateWithoutFlagKeepsProjectPrefixes() throws Exception {
        Element element = parse("""
                <State>
                  <option name="prefixes">
                    <list>
                      <option value="Order.Kuper.Adapter" />
                    </list>
                  </option>
                </State>
                """);

        PrefixTrimmerSettings.SettingsState state = XmlSerializer.deserialize(element, PrefixTrimmerSettings.SettingsState.class);
        assertNull(state.useGlobalPrefixes);

        PrefixTrimmerSettings settings = new PrefixTrimmerSettings();
        settings.loadState(state);
        assertFalse(settings.isUseGlobalPrefixes());
    }

    @Test
    void legacyStateWithoutPrefixesFallsBackToGlobal() throws Exception {
        Element element = parse("<State />");

        PrefixTrimmerSettings.SettingsState state = XmlSerializer.deserialize(element, PrefixTrimmerSettings.SettingsState.class);
        PrefixTrimmerSettings settings = new PrefixTrimmerSettings();
        settings.loadState(state);
        assertTrue(settings.isUseGlobalPrefixes());
    }

    @Test
    void explicitFlagSurvivesSerializationRoundTrip() {
        PrefixTrimmerSettings settings = new PrefixTrimmerSettings();
        settings.setPrefixes(java.util.List.of("Order.Kuper.Adapter"));
        settings.setUseGlobalPrefixes(true);

        Element serialized = XmlSerializer.serialize(settings.getState());
        assertNotNull(serialized);

        PrefixTrimmerSettings.SettingsState restored = XmlSerializer.deserialize(serialized, PrefixTrimmerSettings.SettingsState.class);
        PrefixTrimmerSettings reloaded = new PrefixTrimmerSettings();
        reloaded.loadState(restored);
        assertTrue(reloaded.isUseGlobalPrefixes());
    }

    private static Element parse(String xml) throws Exception {
        return new SAXBuilder().build(new StringReader(xml)).getRootElement();
    }
}

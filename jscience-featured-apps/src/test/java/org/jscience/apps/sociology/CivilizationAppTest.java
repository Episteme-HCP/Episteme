package org.jscience.apps.sociology;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;
import org.jscience.ui.i18n.I18nManager;

@ExtendWith(ApplicationExtension.class)
public class CivilizationAppTest {

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");

        // Enforce English locale for deterministic testing
        java.util.Locale.setDefault(java.util.Locale.ENGLISH);
        // Also reset the I18nManager singleton to use English
        I18nManager.getInstance().setLocale(java.util.Locale.ENGLISH);
    }

    @Start
    public void start(Stage stage) throws Exception {
        new CivilizationApp().start(stage);
    }

    @Test
    public void testLaunchAndI18n(FxRobot robot) {
        // Verify that the status label is correctly localized
        String expectedStatus = "STABLE";
        org.hamcrest.MatcherAssert.assertThat(robot.lookup(hasText(expectedStatus)).query(), isVisible());
    }

    @Test
    public void testMenuPresence(FxRobot robot) {
        // Verify "Run" button exists (standard from FeaturedAppBase)
        verifyThat(".toggle-button", hasText("Run"));
    }
}

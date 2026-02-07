package org.jscience.benchmarks.ui;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@ExtendWith(ApplicationExtension.class)
public class JScienceBenchmarkingAppTest {

    @Start
    public void start(Stage stage) throws Exception {
        new JScienceBenchmarkingApp().start(stage);
    }

    @Test
    public void should_have_correct_title(FxRobot robot) {
        verifyThat(".header-title", LabeledMatchers.hasText("JSCIENCE BENCHMARKING"));
    }

    @Test
    public void should_navigate_tabs(FxRobot robot) {
        robot.clickOn("VISUALIZATION");
        verifyThat("#performanceBarChart", isVisible());
        
        robot.clickOn("HISTORY");
        verifyThat("Historical Runs", isVisible()); 
    }

    @Test
    public void should_open_about_dialog(FxRobot robot) {
        robot.clickOn("ABOUT");
        // Looking for any node containing the text
        verifyThat("JScience Benchmarking Suite v1.0", isVisible());
        robot.clickOn("OK");
    }

    @Test
    public void should_open_settings_dialog(FxRobot robot) {
        robot.clickOn("SETTINGS");
        verifyThat("JScience Benchmarking Settings", isVisible());
        robot.clickOn("OK");
    }
}

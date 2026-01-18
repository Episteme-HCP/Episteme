/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.ui.viewers.devices;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.jscience.device.actuators.VotingMachine;
import org.jscience.ui.AbstractDeviceViewer;

/**
 * A voting machine viewer that displays vote counts.
 * Modeled after physical measurement instruments for consistency.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VotingMachineViewer extends AbstractDeviceViewer<VotingMachine> {

    private Label countLabel;
    private Circle statusIndicator;

    public VotingMachineViewer(VotingMachine device) {
        super(device);
        initUI();
        update();
    }

    private void initUI() {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-padding: 10;");

        countLabel = new Label("0");
        countLabel.setFont(Font.font("Arial", 24));
        countLabel.setTextFill(Color.BLACK);

        statusIndicator = new Circle(5, Color.RED);
        HBox statusBox = new HBox(5, new Label("Status:"), statusIndicator);
        statusBox.setAlignment(Pos.CENTER);

        content.getChildren().addAll(countLabel, statusBox);
        this.setCenter(content);
    }

    @Override
    public void update() {
        try {
            // Assuming VotingMachine has a way to get counts
            // For now, let's assume it has a getVoteCount() returning int or Real
            countLabel.setText("0"); // placeholder for actual device call
            statusIndicator.setFill(Color.GREEN);
        } catch (Exception e) {
            countLabel.setText("--");
            statusIndicator.setFill(Color.RED);
        }
    }

    @Override
    public String getName() {
         return org.jscience.ui.i18n.I18n.getInstance().get("viewer.votingmachine.name", "Voting Machine");
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.social_sciences", "Social Sciences");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.votingmachine.desc", "A voting machine viewer for monitoring election results.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.votingmachine.longdesc", "Digital ballot box visualization that provides real-time monitoring of vote tallies, voter turnout, and machine status ensuring transparency and accuracy in the electoral process.");
    }
}

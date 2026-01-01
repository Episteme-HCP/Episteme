/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import org.jscience.ui.i18n.SocialI18n;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
// import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.jscience.ui.AppProvider;

import java.util.*;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PoliticsVotingDemo implements AppProvider {

    private enum VotingSystem {
        FPTP("pol.voting.sys.fptp", "pol.voting.desc.fptp"),
        PR("pol.voting.sys.pr", "pol.voting.desc.pr"),
        APPROVAL("pol.voting.sys.approval", "pol.voting.desc.approval"),
        BORDA("pol.voting.sys.borda", "pol.voting.desc.borda");

        final String nameKey;
        final String descKey;

        VotingSystem(String nameKey, String descKey) {
            this.nameKey = nameKey;
            this.descKey = descKey;
        }

        @Override
        public String toString() {
            return SocialI18n.getInstance().get(nameKey);
        }
    }

    private Label descLabel;
    private BarChart<String, Number> chart;

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Politics";
    }

    @Override
    public String getName() {
        return SocialI18n.getInstance().get("PoliticsVoting.title");
    }

    @Override
    public String getDescription() {
        return SocialI18n.getInstance().get("PoliticsVoting.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(SocialI18n.getInstance().get("pol.voting.chart.title"));
        chart.setAnimated(false);
        root.setCenter(chart);

        // Sidebar / Controls
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(250);
        controls.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        Label sysLabel = new Label(SocialI18n.getInstance().get("pol.voting.label.system"));
        sysLabel.setStyle("-fx-font-weight: bold;");

        ComboBox<VotingSystem> sysCombo = new ComboBox<>(FXCollections.observableArrayList(VotingSystem.values()));
        sysCombo.setMaxWidth(Double.MAX_VALUE);
        sysCombo.setValue(VotingSystem.FPTP);

        descLabel = new Label(SocialI18n.getInstance().get(VotingSystem.FPTP.descKey));
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #555; -fx-padding: 5; -fx-background-color: white; -fx-border-color: #eee;");
        VBox.setVgrow(descLabel, Priority.ALWAYS);

        sysCombo.setOnAction(e -> {
            VotingSystem vs = sysCombo.getValue();
            if (vs != null) {
                descLabel.setText(SocialI18n.getInstance().get(vs.descKey));
            }
        });

        Button runBtn = new Button(SocialI18n.getInstance().get("toolbar.run"));
        runBtn.setMaxWidth(Double.MAX_VALUE);
        runBtn.setStyle("-fx-base: #2196F3; -fx-font-weight: bold; -fx-text-fill: white;");
        runBtn.setOnAction(e -> simulateElection(sysCombo.getValue()));

        controls.getChildren().addAll(sysLabel, sysCombo, new Separator(), new Label("Description:"), descLabel,
                new Separator(), runBtn);
        root.setRight(controls);

        Scene scene = new Scene(root, 900, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void simulateElection(VotingSystem system) {
        chart.getData().clear();
        String[] parties = { "Party A", "Party B", "Party C", "Party D", "Party E" };
        Map<String, Integer> results = new HashMap<>();
        for (String p : parties)
            results.put(p, 0);

        Random rand = new Random();
        int totalSeats = 100;

        switch (system) {
            case FPTP -> {
                // Simulate 100 districts
                for (int i = 0; i < totalSeats; i++) {
                    String winner = "";
                    int maxVotes = -1;
                    for (String p : parties) {
                        int votes = rand.nextInt(1000);
                        if (p.equals("Party A") || p.equals("Party B"))
                            votes += 200; // Bias
                        if (votes > maxVotes) {
                            maxVotes = votes;
                            winner = p;
                        }
                    }
                    results.put(winner, results.get(winner) + 1);
                }
            }
            case PR -> {
                // Total popular vote
                Map<String, Integer> votes = new HashMap<>();
                int totalVotes = 0;
                for (String p : parties) {
                    int v = rand.nextInt(50000);
                    if (p.equals("Party A") || p.equals("Party B"))
                        v += 10000;
                    votes.put(p, v);
                    totalVotes += v;
                }
                for (String p : parties) {
                    int s = (int) Math.round(((double) votes.get(p) / totalVotes) * totalSeats);
                    results.put(p, s);
                }
            }
            case APPROVAL -> {
                // Voters approve multiple candidates. Winner takes seat (simulating single
                // winner repeated or multi-seat approval?)
                // Let's simulate multi-seat approval: Top N candidates get seats strictly based
                // on approval count?
                // Or simplified: Just show Approval rating % as "seats" or score.
                // We'll map approval score scaled to 100 "score points".
                for (String p : parties) {
                    // Approval rate 0-100%
                    int approval = rand.nextInt(40) + (p.equals("Party C") ? 30 : 10); // Party C approved by many as
                                                                                       // 2nd choice?
                    if (p.equals("Party A"))
                        approval += 20;
                    results.put(p, approval); // Not seats, but score
                }
                chart.getYAxis().setLabel("Approval Rating");
            }
            case BORDA -> {
                // Weighted ranking.
                for (String p : parties) {
                    int score = rand.nextInt(5000);
                    if (p.equals("Party D"))
                        score += 2000; // Party D strong 2nd choice?
                    results.put(p, score / 100); // Scale down
                }
                chart.getYAxis().setLabel("Borda Score");
            }
        }

        if (system == VotingSystem.FPTP || system == VotingSystem.PR) {
            chart.getYAxis().setLabel("Seats");
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Results (" + system.toString() + ")");
        for (String p : parties) {
            series.getData().add(new XYChart.Data<>(p, results.get(p)));
        }
        chart.getData().add(series);
    }
}



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

package org.jscience.apps.sports;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.apps.framework.I18nManager;

/**
 * Sports Results Management System.
 * Tracks match results and calculates league standings.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SportsResultsViewer extends Application {

    private final I18nManager i18n = I18nManager.getInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();

    private final ObservableList<String> matchHistory = FXCollections.observableArrayList();

    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // --- Header ---
        Label headerVal = new Label(i18n.get("sports.header"));
        headerVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        HBox header = new HBox(headerVal);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle(
                "-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        root.setTop(header);

        // --- Center: League Table ---
        TableView<Team> table = new TableView<>();
        table.setItems(teams);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Team, Integer> rankCol = new TableColumn<>("#");
        rankCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.valueOf(getIndex() + 1));
            }
        });
        rankCol.setPrefWidth(40);

        TableColumn<Team, String> nameCol = new TableColumn<>(i18n.get("sports.col.team"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Team, Integer> playedCol = new TableColumn<>(i18n.get("sports.col.played"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));

        TableColumn<Team, Integer> wonCol = new TableColumn<>(i18n.get("sports.col.won"));
        wonCol.setCellValueFactory(new PropertyValueFactory<>("won"));

        TableColumn<Team, Integer> drawCol = new TableColumn<>(i18n.get("sports.col.drawn"));
        drawCol.setCellValueFactory(new PropertyValueFactory<>("drawn"));

        TableColumn<Team, Integer> lostCol = new TableColumn<>(i18n.get("sports.col.lost"));
        lostCol.setCellValueFactory(new PropertyValueFactory<>("lost"));

        TableColumn<Team, Integer> gdCol = new TableColumn<>(i18n.get("sports.col.gd"));
        gdCol.setCellValueFactory(new PropertyValueFactory<>("goalDifference"));

        TableColumn<Team, Integer> pointsCol = new TableColumn<>(i18n.get("sports.col.pts"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        pointsCol.setStyle("-fx-font-weight: bold;");

        table.getColumns().addAll(rankCol, nameCol, playedCol, wonCol, drawCol, lostCol, gdCol, pointsCol);

        VBox centerBox = new VBox(10, new Label(i18n.get("sports.label.standings")), table);
        centerBox.setPadding(new Insets(20));
        root.setCenter(centerBox);

        // --- Right: Controls & History ---
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(350);
        rightPanel.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 0 1;");

        // Add Result Form
        Label formTitle = new Label(i18n.get("sports.form.title"));
        formTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        ComboBox<Team> homeBox = new ComboBox<>(teams);
        homeBox.setPromptText(i18n.get("sports.form.home"));
        homeBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Team> awayBox = new ComboBox<>(teams);
        awayBox.setPromptText(i18n.get("sports.form.away"));
        awayBox.setMaxWidth(Double.MAX_VALUE);

        HBox scoresBox = new HBox(10);
        scoresBox.setAlignment(Pos.CENTER);
        TextField homeScoreField = new TextField();
        homeScoreField.setPromptText("0");
        homeScoreField.setPrefWidth(50);
        Label vsLabel = new Label("-");
        TextField awayScoreField = new TextField();
        awayScoreField.setPromptText("0");
        awayScoreField.setPrefWidth(50);
        scoresBox.getChildren().addAll(homeScoreField, vsLabel, awayScoreField);

        Button addBtn = new Button(i18n.get("sports.button.add"));
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.getStyleClass().add("accent-button");
        addBtn.setStyle("-fx-font-weight: bold;"); // Remove hardcoded green

        addBtn.setOnAction(e -> {
            try {
                Team home = homeBox.getValue();
                Team away = awayBox.getValue();
                int hScore = Integer.parseInt(homeScoreField.getText());
                int aScore = Integer.parseInt(awayScoreField.getText());

                if (home != null && away != null && home != away) {
                    processMatch(home, away, hScore, aScore);
                    homeBox.setValue(null);
                    awayBox.setValue(null);
                    homeScoreField.setText("");
                    awayScoreField.setText("");
                }
            } catch (NumberFormatException ex) {
                // Ignore invalid input
            }
        });

        // Match History
        Label historyTitle = new Label(i18n.get("sports.history.title"));
        historyTitle.setStyle("-fx-font-weight: bold;");
        ListView<String> historyList = new ListView<>(matchHistory);
        historyList.setPrefHeight(300);

        Button simBtn = new Button(i18n.get("sports.button.simulate"));
        simBtn.setMaxWidth(Double.MAX_VALUE);
        simBtn.setOnAction(e -> simulateSeason());

        rightPanel.getChildren().addAll(formTitle, homeBox, awayBox, scoresBox, addBtn, simBtn, new Separator(),
                historyTitle, historyList);
        root.setRight(rightPanel);

        // Initialize Data
        initTeams();

        Scene scene = new Scene(root, 1000, 700);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(i18n.get("sports.title"));
        stage.setScene(scene);
        stage.show();
    }

    private void initTeams() {
        teams.addAll(
                new Team("Manchester City"),
                new Team("Arsenal"),
                new Team("Liverpool"),
                new Team("Aston Villa"),
                new Team("Tottenham"),
                new Team("Newcastle"),
                new Team("Man United"),
                new Team("Chelsea"));
    }

    private void processMatch(Team home, Team away, int hScore, int aScore) {
        // Record match
        matchHistory.add(0, String.format("%s %d - %d %s", home.getName(), hScore, aScore, away.getName()));

        // Update stats
        home.played.set(home.getPlayed() + 1);
        away.played.set(away.getPlayed() + 1);

        home.gf += hScore;
        home.ga += aScore;
        away.gf += aScore;
        away.ga += hScore;

        if (hScore > aScore) {
            home.won.set(home.getWon() + 1);
            home.points.set(home.getPoints() + 3);
            away.lost.set(away.getLost() + 1);
        } else if (aScore > hScore) {
            away.won.set(away.getWon() + 1);
            away.points.set(away.getPoints() + 3);
            home.lost.set(home.getLost() + 1);
        } else {
            home.drawn.set(home.getDrawn() + 1);
            home.points.set(home.getPoints() + 1);
            away.drawn.set(away.getDrawn() + 1);
            away.points.set(away.getPoints() + 1);
        }

        home.updateGD();
        away.updateGD();

        // Sort Table
        FXCollections.sort(teams, (t1, t2) -> {
            int p = t2.getPoints() - t1.getPoints();
            if (p != 0)
                return p;
            int gd = t2.getGoalDifference() - t1.getGoalDifference();
            if (gd != 0)
                return gd;
            return t2.getGf() - t1.getGf();
        });
    }

    public static class Team {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty played = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty won = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty drawn = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty lost = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty goalDifference = new SimpleIntegerProperty(0);

        private int gf = 0;
        private int ga = 0;

        public Team(String name) {
            this.name = new SimpleStringProperty(name);
        }

        public void updateGD() {
            goalDifference.set(gf - ga);
        }

        public String getName() {
            return name.get();
        }

        public int getPlayed() {
            return played.get();
        }

        public int getWon() {
            return won.get();
        }

        public int getDrawn() {
            return drawn.get();
        }

        public int getLost() {
            return lost.get();
        }

        public int getPoints() {
            return points.get();
        }

        public int getGoalDifference() {
            return goalDifference.get();
        }

        public int getGf() {
            return gf;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public static void show(Stage stage) {
        new SportsResultsViewer().start(stage);
    }

    private void simulateSeason() {
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 5; i++) { // Simulate 5 matches
            Team h = teams.get(rand.nextInt(teams.size()));
            Team a = teams.get(rand.nextInt(teams.size()));
            if (h == a)
                continue;

            // Bias towards home team and stronger teams (simple logic)
            int hScore = rand.nextInt(4) + (rand.nextBoolean() ? 1 : 0);
            int aScore = rand.nextInt(3);
            processMatch(h, a, hScore, aScore);
        }
    }
}



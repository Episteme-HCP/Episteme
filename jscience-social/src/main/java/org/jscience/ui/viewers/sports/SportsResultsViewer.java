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

package org.jscience.ui.viewers.sports;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sports Results Management Viewer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SportsResultsViewer extends AbstractViewer {

    private final org.jscience.ui.i18n.I18n i18n = org.jscience.ui.i18n.I18n.getInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<String> matchHistory = FXCollections.observableArrayList();

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.sportsresults.name", "Sports Results"); }
    
    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.sports", "Sports"); }

    public SportsResultsViewer() {
        initUI();
    }
    
    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.sportsresults.desc", "Sports Results Management");
    }

    @Override
    public String getLongDescription() { 
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.sportsresults.longdesc", "Manage team standings, match results and simulate seasons.");
    }
    
    @Override
    public void show(javafx.stage.Stage stage) {
         javafx.scene.Scene scene = new javafx.scene.Scene(this);
         org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
         stage.setTitle(getName());
         stage.setScene(scene);
         stage.show();
    }

    @SuppressWarnings("unchecked")
    private void initUI() {
        Label headerVal = new Label(i18n.get("sports.header", "League Table"));
        headerVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox header = new HBox(headerVal);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        this.setTop(header);

        TableView<Team> table = new TableView<>();
        table.setItems(teams);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Team, Integer> rankCol = new TableColumn<>("#");
        rankCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        rankCol.setPrefWidth(40);

        TableColumn<Team, String> nameCol = new TableColumn<>(i18n.get("sports.col.team", "Team"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Team, Integer> playedCol = new TableColumn<>(i18n.get("sports.col.played", "Played"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));

        TableColumn<Team, Number> pointsCol = new TableColumn<>(i18n.get("sports.col.pts", "Points"));
        pointsCol.setCellValueFactory(cell -> cell.getValue().points);
        pointsCol.setStyle("-fx-font-weight: bold;");

        TableColumn<Team, String> avgCol = new TableColumn<>("Trend (SMA)");
        avgCol.setCellValueFactory(cell -> cell.getValue().trend);

        table.getColumns().addAll(rankCol, nameCol, playedCol, pointsCol, avgCol);

        VBox centerBox = new VBox(10, new Label(i18n.get("sports.label.standings", "Standings")), table);
        centerBox.setPadding(new Insets(20));
        this.setCenter(centerBox);

        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(350);

        ComboBox<Team> homeBox = new ComboBox<>(teams);
        homeBox.setPromptText(i18n.get("sports.form.home", "Home Team"));
        homeBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Team> awayBox = new ComboBox<>(teams);
        awayBox.setPromptText(i18n.get("sports.form.away", "Away Team"));
        awayBox.setMaxWidth(Double.MAX_VALUE);

        TextField hScore = new TextField();
        hScore.setPromptText("Home");
        hScore.setPrefWidth(50);
        TextField aScore = new TextField();
        aScore.setPromptText("Away");
        aScore.setPrefWidth(50);
        HBox scoreBox = new HBox(10, hScore, new Label("-"), aScore);
        scoreBox.setAlignment(Pos.CENTER);

        Button addBtn = new Button(i18n.get("sports.button.add", "Add Match"));
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> {
            try {
                processMatch(homeBox.getValue(), awayBox.getValue(),
                        Integer.parseInt(hScore.getText()), Integer.parseInt(aScore.getText()));
                hScore.clear();
                aScore.clear();
            } catch (Exception ex) { }
        });

        Button simBtn = new Button(i18n.get("sports.button.simulate", "Simulate"));
        simBtn.setMaxWidth(Double.MAX_VALUE);
        simBtn.setOnAction(e -> simulateSeason());

        ListView<String> historyView = new ListView<>(matchHistory);
        historyView.setPrefHeight(300);

        rightPanel.getChildren().addAll(new Label(i18n.get("sports.form.title", "Add Match")), homeBox, awayBox, scoreBox, addBtn, simBtn, new Separator(), historyView);
        this.setRight(rightPanel);

        initTeams();
    }

    @Override
    public List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        List<org.jscience.ui.Parameter<?>> parameters = new ArrayList<>();
        
        String defaultTeams = org.jscience.io.Configuration.get("viewer.sports.default.teams", "Man City,Arsenal,Liverpool");
        
        parameters.add(new org.jscience.ui.Parameter<String>(
            i18n.get("sports.param.teams", "Teams List"),
            i18n.get("sports.param.teams.desc", "Comma-separated list of team names"),
            defaultTeams,
            value -> reinitializeTeams(value)
        ));
        
        return parameters;
    }

    private void reinitializeTeams(String teamList) {
        teams.clear();
        matchHistory.clear();
        if (teamList != null && !teamList.isEmpty()) {
            for (String t : teamList.split(",")) {
                teams.add(new Team(t.trim()));
            }
        }
    }

    private void initTeams() {
        String defaultTeams = org.jscience.io.Configuration.get("viewer.sports.default.teams", "Man City,Arsenal,Liverpool");
        reinitializeTeams(defaultTeams);
    }

    private void processMatch(Team home, Team away, int homeGoals, int awayGoals) {
        if (home == null || away == null || home == away) return;
        
        home.played++;
        away.played++;
        
        int homePoints = 0, awayPoints = 0;
        if (homeGoals > awayGoals) {
            homePoints = 3;
        } else if (homeGoals < awayGoals) {
            awayPoints = 3;
        } else {
            homePoints = 1;
            awayPoints = 1;
        }
        
        home.addPoints(homePoints);
        away.addPoints(awayPoints);
        
        matchHistory.add(String.format("%s %d - %d %s", home.name, homeGoals, awayGoals, away.name));
        FXCollections.sort(teams, (t1, t2) -> Integer.compare(t2.points.get(), t1.points.get()));
    }

    private void simulateSeason() {
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                int homeGoals = rand.nextInt(4);
                int awayGoals = rand.nextInt(4);
                processMatch(teams.get(i), teams.get(j), homeGoals, awayGoals);
            }
        }
    }

    /**
     * Team data model for sports standings.
     */
    public static class Team {
        public final String name;
        public int played = 0;
        public final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        public final SimpleStringProperty trend = new SimpleStringProperty("--");
        private final java.util.List<Integer> pointHistory = new java.util.ArrayList<>();
        
        public Team(String name) {
            this.name = name;
        }
        
        public void addPoints(int pts) {
            points.set(points.get() + pts);
            pointHistory.add(pts);
            if (pointHistory.size() >= 3) {
                double avg = pointHistory.stream()
                    .mapToInt(Integer::intValue)
                    .average().orElse(0);
                trend.set(String.format("%.1f", avg));
            }
        }
        
        public String getName() { return name; }
        public int getPlayed() { return played; }
        
        @Override
        public String toString() { return name; }
    }
}

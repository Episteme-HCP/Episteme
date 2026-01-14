/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.ui.i18n.I18nManager;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.timeseries.TimeSeries;

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

    private final I18nManager i18n = I18nManager.getInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<String> matchHistory = FXCollections.observableArrayList();

    @Override
    public String getName() { return i18n.get("sports.title", "Sports Results"); }
    
    @Override
    public String getCategory() { return "Sports"; }

    public SportsResultsViewer() {
        initUI();
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

    private void initTeams() {
        teams.addAll(new Team("Man City"), new Team("Arsenal"), new Team("Liverpool"), new Team("Aston Villa"), new Team("Tottenham"));
    }

    private void processMatch(Team home, Team away, int hs, int as) {
        if (home == null || away == null || home == away) return;
        matchHistory.add(0, home.name.get() + " " + hs + " - " + as + " " + away.name.get());
        home.addResult(hs, as);
        away.addResult(as, hs);
        FXCollections.sort(teams, (t1, t2) -> t2.points.get() - t1.points.get());
    }

    private void simulateSeason() {
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < 5; i++) {
            processMatch(teams.get(r.nextInt(teams.size())), teams.get(r.nextInt(teams.size())), r.nextInt(4), r.nextInt(4));
        }
    }

    public static class Team {
        public final SimpleStringProperty name;
        public final SimpleIntegerProperty played = new SimpleIntegerProperty(0);
        public final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        public final SimpleStringProperty trend = new SimpleStringProperty("--");
        private final List<Real> pointsHistory = new ArrayList<>();

        public Team(String name) {
            this.name = new SimpleStringProperty(name);
            pointsHistory.add(Real.ZERO);
        }

        public void addResult(int gf, int ga) {
            played.set(played.get() + 1);
            int p = (gf > ga) ? 3 : (gf == ga ? 1 : 0);
            points.set(points.get() + p);
            pointsHistory.add(Real.of(points.get()));
            updateTrend();
        }

        private void updateTrend() {
            if (pointsHistory.size() < 3) return;
            Real[] data = pointsHistory.toArray(new Real[0]);
            Real[] sma = TimeSeries.movingAverage(data, Math.min(data.length, 3));
            if (sma.length > 0) {
                trend.set(String.format("%.1f", sma[sma.length - 1].doubleValue()));
            }
        }

        public String getName() { return name.get(); }
        public int getPlayed() { return played.get(); }
        public int getPoints() { return points.get(); }
        
        @Override
        public String toString() { return name.get(); }
    }
}

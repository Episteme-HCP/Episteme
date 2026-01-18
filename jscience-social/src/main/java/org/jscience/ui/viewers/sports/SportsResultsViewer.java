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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.ChoiceParameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.StringParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.io.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sports Results Management Viewer.
 * Refactored to be 100% parameter-based.
 */
public class SportsResultsViewer extends AbstractViewer {

    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<String> matchHistory = FXCollections.observableArrayList();
    private final List<Parameter<?>> parameters = new ArrayList<>();

    private String homeTeamName = "";
    private String awayTeamName = "";
    private int homeScore = 0;
    private int awayScore = 0;

    public SportsResultsViewer() {
        initTeams();
        setupParameters();
        initUI();
    }

    private void setupParameters() {
        parameters.add(new StringParameter("sports.teams", I18n.getInstance().get("sports.param.teams", "Teams List"), Configuration.get("viewer.sports.default.teams", "Man City,Arsenal,Liverpool"), v -> reinitializeTeams(v)));
        
        List<String> teamNames = teams.stream().map(Team::getName).collect(Collectors.toList());
        parameters.add(new ChoiceParameter("sports.match.home", I18n.getInstance().get("sports.form.home", "Home Team"), teamNames, "", v -> homeTeamName = v));
        parameters.add(new ChoiceParameter("sports.match.away", I18n.getInstance().get("sports.form.away", "Away Team"), teamNames, "", v -> awayTeamName = v));
        parameters.add(new NumericParameter("sports.match.hscore", "Home Score", 0, 20, 1, (double)homeScore, v -> homeScore = v.intValue()));
        parameters.add(new NumericParameter("sports.match.ascore", "Away Score", 0, 20, 1, (double)awayScore, v -> awayScore = v.intValue()));
        
        parameters.add(new BooleanParameter("sports.match.add", I18n.getInstance().get("sports.button.add", "Add Match"), false, v -> {
            if (v) {
                Team h = teams.stream().filter(t -> t.name.equals(homeTeamName)).findFirst().orElse(null);
                Team a = teams.stream().filter(t -> t.name.equals(awayTeamName)).findFirst().orElse(null);
                processMatch(h, a, homeScore, awayScore);
            }
        }));
        
        parameters.add(new BooleanParameter("sports.simulate", I18n.getInstance().get("sports.button.simulate", "Simulate Season"), false, v -> {
            if (v) simulateSeason();
        }));
    }

    private void initUI() {
        TableView<Team> table = new TableView<>(teams);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        
        TableColumn<Team, String> nameCol = new TableColumn<>(I18n.getInstance().get("sports.col.team", "Team"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Team, Integer> playedCol = new TableColumn<>(I18n.getInstance().get("sports.col.played", "Played"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));
        
        TableColumn<Team, Number> pointsCol = new TableColumn<>(I18n.getInstance().get("sports.col.pts", "Points"));
        pointsCol.setCellValueFactory(cell -> cell.getValue().points);
        
        table.getColumns().addAll(nameCol, playedCol, pointsCol);
        
        ListView<String> historyView = new ListView<>(matchHistory);
        SplitPane split = new SplitPane(table, historyView);
        split.setOrientation(javafx.geometry.Orientation.VERTICAL);
        
        setCenter(split);
    }

    private void reinitializeTeams(String teamList) {
        teams.clear(); matchHistory.clear();
        if (teamList != null && !teamList.isEmpty()) {
            for (String t : teamList.split(",")) teams.add(new Team(t.trim()));
        }
    }

    private void initTeams() {
        reinitializeTeams(Configuration.get("viewer.sports.default.teams", "Man City,Arsenal,Liverpool"));
    }

    private void processMatch(Team home, Team away, int homeGoals, int awayGoals) {
        if (home == null || away == null || home == away) return;
        home.played++; away.played++;
        if (homeGoals > awayGoals) home.addPoints(3);
        else if (homeGoals < awayGoals) away.addPoints(3);
        else { home.addPoints(1); away.addPoints(1); }
        matchHistory.add(0, String.format("%s %d - %d %s", home.name, homeGoals, awayGoals, away.name));
        FXCollections.sort(teams, (t1, t2) -> Integer.compare(t2.points.get(), t1.points.get()));
    }

    private void simulateSeason() {
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                processMatch(teams.get(i), teams.get(j), rand.nextInt(4), rand.nextInt(4));
            }
        }
    }

    public static class Team {
        public final String name;
        public int played = 0;
        public final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        public Team(String name) { this.name = name; }
        public void addPoints(int pts) { points.set(points.get() + pts); }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }

    @Override public String getName() { return I18n.getInstance().get("viewer.sportsresults.name", "Sports Results"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.sports", "Sports"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.sportsresults.desc", "Sports Results Management"); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.sportsresults.longdesc", "A comprehensive sports league management and visualization tool. Create lists of teams, record match results with home and away scores, and maintain a real-time league table. The viewer includes a season simulation feature to quickly populate results and analyze statistical trends in team performance."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}

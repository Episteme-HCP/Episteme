package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PoliticsVotingDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Politics";
    }

    @Override
    public String getName() {
        return "Politics: Voting Systems Simulation";
    }

    @Override
    public String getDescription() {
        return "Simulates First-Past-The-Post vs Proportional Representation with random voter preferences.";
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Election Results");

        Button runFPTP = new Button("Run First-Past-The-Post");
        Button runPR = new Button("Run Proportional Representation");

        Label status = new Label("Click a button to simulate an election.");

        runFPTP.setOnAction(e -> {
            simulateElection(chart, true);
            status.setText("FPTP: Winner takes all per district.");
        });

        runPR.setOnAction(e -> {
            simulateElection(chart, false);
            status.setText("PR: Seats allocated by total vote share.");
        });

        VBox controls = new VBox(10, runFPTP, runPR, status);
        controls.setStyle("-fx-padding: 10; -fx-background-color: #eee;");

        root.setCenter(chart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void simulateElection(BarChart<String, Number> chart, boolean fptp) {
        chart.getData().clear();
        String[] parties = { "Party A", "Party B", "Party C", "Party D" };
        Map<String, Integer> seats = new HashMap<>();
        for (String p : parties)
            seats.put(p, 0);

        Random rand = new Random();

        if (fptp) {
            // Simulate 100 districts
            for (int i = 0; i < 100; i++) {
                String winner = "";
                int maxVotes = -1;
                for (String p : parties) {
                    int votes = rand.nextInt(1000);
                    // Bias for larger parties
                    if (p.equals("Party A") || p.equals("Party B"))
                        votes += 200;

                    if (votes > maxVotes) {
                        maxVotes = votes;
                        winner = p;
                    }
                }
                seats.put(winner, seats.get(winner) + 1);
            }
        } else {
            // PR: Total popular vote
            Map<String, Integer> votes = new HashMap<>();
            int totalVotes = 0;
            for (String p : parties) {
                int v = rand.nextInt(100000);
                if (p.equals("Party A") || p.equals("Party B"))
                    v += 20000;
                votes.put(p, v);
                totalVotes += v;
            }

            for (String p : parties) {
                int s = (int) Math.round(((double) votes.get(p) / totalVotes) * 100);
                seats.put(p, s);
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Seats");
        for (String p : parties) {
            series.getData().add(new XYChart.Data<>(p, seats.get(p)));
        }
        chart.getData().add(series);
    }
}

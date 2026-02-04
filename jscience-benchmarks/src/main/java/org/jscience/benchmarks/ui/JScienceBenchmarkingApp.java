package org.jscience.benchmarks.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jscience.benchmarks.benchmark.*;
import org.jscience.core.ui.i18n.I18N;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;

/**
 * JScience Benchmarking App - Master Control for Benchmarking and Performance Analysis.
 */
public class JScienceBenchmarkingApp extends Application {

    private final List<RunnableBenchmark> benchmarks = new ArrayList<>();
    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private VBox logArea;
    private ProgressBar progressBar;
    private Label statusLabel;
    private ChartViewer chartViewer;

    @Override
    public void start(Stage primaryStage) {
        // Init I18N and Data
        I18N.getInstance().addBundle("org.jscience.benchmarks.i18n.messages_benchmarks");
        discoverBenchmarks();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #1e1e1e;");

        // --- TOP: Header ---
        Label headerLabel = new Label("JSCIENCE BENCHMARKING");
        headerLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.WHITE);
        HBox topBox = new HBox(headerLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);

        // --- LEFT: Sidebar (Benchmark List) ---
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #252526; -fx-border-color: #333333; -fx-border-width: 0 1 0 0;");
        
        Label sidebarTitle = new Label("BENCHMARKS");
        sidebarTitle.setTextFill(Color.GRAY);
        sidebarTitle.setFont(Font.font(14));
        sidebar.getChildren().add(sidebarTitle);

        for (RunnableBenchmark b : benchmarks) {
            Button btn = new Button(b.getName());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-alignment: center-left;");
            btn.setOnAction(e -> runBenchmark(b));
            sidebar.getChildren().add(btn);
        }

        Button runAllBtn = new Button("RUN ALL SUITE");
        runAllBtn.setMaxWidth(Double.MAX_VALUE);
        runAllBtn.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-weight: bold;");
        runAllBtn.setOnAction(e -> runAll());
        sidebar.getChildren().add(new Separator());
        sidebar.getChildren().add(runAllBtn);

        root.setLeft(sidebar);

        // --- CENTER: Visualization Area ---
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // Tab 1: Chart
        Tab chartTab = new Tab("Performance Chart");
        chartViewer = new ChartViewer(createInitialChart());
        chartTab.setContent(chartViewer);
        chartTab.setClosable(false);

        // Tab 2: Logs
        Tab logTab = new Tab("Execution Output");
        logArea = new VBox(5);
        logArea.setPadding(new Insets(10));
        logArea.setStyle("-fx-background-color: #1e1e1e;");
        ScrollPane logScroll = new ScrollPane(logArea);
        logScroll.setFitToWidth(true);
        logScroll.setStyle("-fx-background: #1e1e1e; -fx-border-color: #333333;");
        logTab.setContent(logScroll);
        logTab.setClosable(false);

        tabPane.getTabs().addAll(chartTab, logTab);
        root.setCenter(tabPane);

        // --- BOTTOM: Status Bar ---
        HBox statusBar = new HBox(15);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #007acc;");
        
        statusLabel = new Label("Ready");
        statusLabel.setTextFill(Color.WHITE);
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        
        statusBar.getChildren().addAll(statusLabel, progressBar);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("JScience Benchmarking - Master Performance Control");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void discoverBenchmarks() {
        benchmarks.clear();
        benchmarks.addAll(BenchmarkRegistry.discover());
    }

    private JFreeChart createInitialChart() {
        return ChartFactory.createBarChart("Performance Comparison", "Benchmark", "Ops/Sec", dataset);
    }

    private void runBenchmark(RunnableBenchmark b) {
        log("Running: " + b.getName());
        statusLabel.setText("Running " + b.getName() + "...");
        progressBar.setProgress(-1);

        new Thread(() -> {
            try {
                b.setup();
                long start = System.nanoTime();
                int iter = b.getSuggestedIterations();
                for (int i = 0; i < iter; i++) b.run();
                long end = System.nanoTime();
                
                double avgMs = (end - start) / 1_000_000.0 / iter;
                double opsSec = (iter * 1_000_000_000.0) / (end - start);
                
                Platform.runLater(() -> {
                    log("SUCCESS: " + b.getName() + " -> " + String.format("%.4f", avgMs) + " ms/op (" + String.format("%.0f", opsSec) + " ops/sec)");
                    dataset.addValue(opsSec, "Ops/Sec", b.getName());
                    chartViewer.setChart(createInitialChart()); // Refresh
                    statusLabel.setText("Finished " + b.getName());
                    progressBar.setProgress(1);
                });
            } catch (Exception e) {
                Platform.runLater(() -> log("FAILED: " + e.getMessage()));
            }
        }).start();
    }

    private void runAll() {
        new Thread(() -> {
            for (RunnableBenchmark b : benchmarks) {
                Platform.runLater(() -> runBenchmark(b));
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
            }
        }).start();
    }

    private void log(String msg) {
        Label l = new Label("> " + msg);
        l.setTextFill(Color.LIGHTGREEN);
        l.setFont(Font.font("Consolas", 12));
        logArea.getChildren().add(l);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package org.jscience.benchmarks.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.jscience.benchmarks.benchmark.*;
import java.util.*;

public class MainController {

    @FXML private TreeTableView<BenchmarkItem> benchmarkTreeTable;
    @FXML private TreeTableColumn<BenchmarkItem, String> nameColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> domainColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> statusColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> resultColumn;

    @FXML private BarChart<String, Number> performanceBarChart;
    @FXML private ProgressBar globalProgressBar;
    @FXML private Label statusBarLabel;
    @FXML private TabPane mainTabPane;

    private final XYChart.Series<String, Number> benchmarkSeries = new XYChart.Series<>();

    @FXML
    public void initialize() {
        setupTable();
        discoverBenchmarks();
        performanceBarChart.getData().add(benchmarkSeries);
        benchmarkSeries.setName("Throughput (Ops/Sec)");
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(p -> p.getValue().getValue().nameProperty());
        domainColumn.setCellValueFactory(p -> p.getValue().getValue().domainProperty());
        statusColumn.setCellValueFactory(p -> p.getValue().getValue().statusProperty());
        resultColumn.setCellValueFactory(p -> p.getValue().getValue().resultProperty());
        
        benchmarkTreeTable.setShowRoot(false);
    }

    private void discoverBenchmarks() {
        TreeItem<BenchmarkItem> root = new TreeItem<>(new BenchmarkItem("Root", "Root", null));
        Map<String, TreeItem<BenchmarkItem>> domainNodes = new HashMap<>();

        List<RunnableBenchmark> discovered = BenchmarkRegistry.discover();
        for (RunnableBenchmark b : discovered) {
            TreeItem<BenchmarkItem> domainNode = domainNodes.computeIfAbsent(b.getDomain(), d -> {
                TreeItem<BenchmarkItem> node = new TreeItem<>(new BenchmarkItem(d, d, null));
                root.getChildren().add(node);
                return node;
            });
            domainNode.getChildren().add(new TreeItem<>(new BenchmarkItem(b.getName(), b.getDomain(), b)));
        }
        benchmarkTreeTable.setRoot(root);
    }

    @FXML
    private void handleRunSelected() {
        TreeItem<BenchmarkItem> selected = benchmarkTreeTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue().getBenchmark() != null) {
            runBenchmark(selected.getValue());
        }
    }

    @FXML
    private void handleRunAll() {
        List<BenchmarkItem> toRun = new ArrayList<>();
        findLeafItems(benchmarkTreeTable.getRoot(), toRun);
        runBenchmarkSuite(toRun);
    }

    private void findLeafItems(TreeItem<BenchmarkItem> root, List<BenchmarkItem> resultList) {
        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            if (child.getChildren().isEmpty()) {
                if (child.getValue().getBenchmark() != null) {
                    resultList.add(child.getValue());
                }
            } else {
                findLeafItems(child, resultList);
            }
        }
    }

    private void runBenchmarkSuite(List<BenchmarkItem> benchmarks) {
        Task<Void> suiteTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int total = benchmarks.size();
                for (int i = 0; i < total; i++) {
                    BenchmarkItem item = benchmarks.get(i);
                    updateProgress(i, total);
                    updateMessage("Running " + item.getName() + "...");
                    
                    executeSingle(item);
                }
                updateProgress(total, total);
                updateMessage("Suite Complete");
                return null;
            }
        };

        globalProgressBar.progressProperty().bind(suiteTask.progressProperty());
        statusBarLabel.textProperty().bind(suiteTask.messageProperty());
        new Thread(suiteTask).start();
    }

    private void runBenchmark(BenchmarkItem item) {
        new Thread(() -> {
            Platform.runLater(() -> {
                statusBarLabel.setText("Running " + item.getName() + "...");
                item.statusProperty().set("Running...");
            });
            executeSingle(item);
            Platform.runLater(() -> statusBarLabel.setText("Finished " + item.getName()));
        }).start();
    }

    private void executeSingle(BenchmarkItem item) {
        RunnableBenchmark b = item.getBenchmark();
        try {
            b.setup();
            long start = System.nanoTime();
            int iter = b.getSuggestedIterations();
            for (int i = 0; i < iter; i++) b.run();
            long end = System.nanoTime();
            
            double opsSec = (iter * 1_000_000_000.0) / (end - start);
            String resultText = String.format("%.0f ops/s", opsSec);

            Platform.runLater(() -> {
                item.statusProperty().set("Success");
                item.resultProperty().set(resultText);
                updateChart(item.getName(), opsSec);
            });
            
            b.teardown();
        } catch (Exception e) {
            Platform.runLater(() -> {
                item.statusProperty().set("Error");
                item.resultProperty().set(e.getMessage());
            });
        }
    }

    private void updateChart(String name, double value) {
        for (XYChart.Data<String, Number> d : benchmarkSeries.getData()) {
            if (d.getXValue().equals(name)) {
                d.setYValue(value);
                return;
            }
        }
        benchmarkSeries.getData().add(new XYChart.Data<>(name, value));
    }
}

package org.jscience.benchmarks.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import org.jscience.benchmarks.benchmark.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.collections.FXCollections;

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
    @FXML private TableView<BenchmarkRunSummary> historyTable;
    @FXML private TableColumn<BenchmarkRunSummary, String> dateColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> suiteColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> resultsColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> descriptionColumn;

    @FXML private ComboBox<String> metricSelector;
    @FXML private Button exportChartBtn;

    private final XYChart.Series<String, Number> benchmarkSeries = new XYChart.Series<>();
    private final ObservableList<BenchmarkRunSummary> historyList = FXCollections.observableArrayList();
    private final org.jscience.benchmarks.persistence.BenchmarkResultService resultService = new org.jscience.benchmarks.persistence.BenchmarkResultService();

    // Helper method to load history
    private void loadHistory() {
        for (BenchmarkRunSummary summary : resultService.loadResults()) {
            historyList.add(summary);
            historyTable.getItems().add(summary);
        }
    }

    // Helper method to update category statuses
    private void updateCategoryStatuses(TreeItem<BenchmarkItem> root) {
        if (root == null || root.isLeaf()) return;

        boolean allSuccess = true;
        boolean anyRunning = false;
        boolean anyError = false;
        int childCount = 0;

        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            updateCategoryStatuses(child); // Recursive update
            childCount++;
            
            String status = child.getValue().statusProperty().get();
            if (status == null) status = "";
            
            if (status.contains("Running") || status.contains("Initializing")) anyRunning = true;
            if (status.contains("Error")) anyError = true;
            if (!"Success".equals(status)) allSuccess = false;
        }

        if (childCount == 0) return;

        String newStatus = "";
        if (anyRunning) newStatus = "Running...";
        else if (anyError) newStatus = "Error (Partial)";
        else if (allSuccess) newStatus = "Success";
        else newStatus = "Completed";

        final String s = newStatus;
        Platform.runLater(() -> root.getValue().statusProperty().set(s));
    }

    @FXML
    public void initialize() {
        setupTable();
        setupHistoryTable();
        loadHistory(); // Load persisted results
        startDiscovery(); // Background loading
        setupAnalytics();
        performanceBarChart.getData().add(benchmarkSeries);
        benchmarkSeries.setName("Throughput (Ops/Sec)");
    }

    private void setupAnalytics() {
        metricSelector.getItems().addAll("Throughput (Ops/Sec)", "Average Latency (ms)", "P99 Latency (ms)");
        metricSelector.getSelectionModel().select(0);
    }

    private void setupTable() {
        // Multi-selection enabled
        benchmarkTreeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        nameColumn.setCellValueFactory(p -> p.getValue().getValue().nameProperty());
        descriptionColumn.setCellValueFactory(p -> p.getValue().getValue().descriptionProperty());
        domainColumn.setCellValueFactory(p -> p.getValue().getValue().domainProperty());
        statusColumn.setCellValueFactory(p -> p.getValue().getValue().statusProperty());
        resultColumn.setCellValueFactory(p -> p.getValue().getValue().resultProperty());
        
        benchmarkTreeTable.setShowRoot(false);
        
        benchmarkTreeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                handleRunSelected();
            }
        });
    }

    private void setupHistoryTable() {
        dateColumn.setCellValueFactory(p -> p.getValue().dateProperty());
        suiteColumn.setCellValueFactory(p -> p.getValue().nameProperty());
        resultsColumn.setCellValueFactory(p -> p.getValue().resultProperty());
    }

    private void startDiscovery() {
         statusBarLabel.setText("Scanning for benchmarks... (This may take ~30s)");
         globalProgressBar.setProgress(-1); // Indeterminate
         
         Task<TreeItem<BenchmarkItem>> task = new Task<>() {
             @Override
             protected TreeItem<BenchmarkItem> call() {
                 return buildTree();
             }
         };
         
         task.setOnSucceeded(e -> {
             benchmarkTreeTable.setRoot(task.getValue());
             statusBarLabel.setText("Ready. Benchmarks loaded.");
             globalProgressBar.setProgress(0);
         });
         
         task.setOnFailed(e -> {
             statusBarLabel.setText("Discovery failed: " + task.getException().getMessage());
             globalProgressBar.setProgress(0);
             task.getException().printStackTrace();
         });
         
         new Thread(task).start();
    }

    private TreeItem<BenchmarkItem> buildTree() {
        TreeItem<BenchmarkItem> root = new TreeItem<>(new BenchmarkItem("Root", "Root", null));
        Map<String, TreeItem<BenchmarkItem>> domainNodes = new HashMap<>();

        // BenchmarkRegistry.discover() might be slow, so it runs in this background thread
        List<RunnableBenchmark> discovered = BenchmarkRegistry.discover();
        
        for (RunnableBenchmark b : discovered) {
            TreeItem<BenchmarkItem> domainNode = domainNodes.computeIfAbsent(b.getDomain(), d -> {
                TreeItem<BenchmarkItem> node = new TreeItem<>(new BenchmarkItem(d, d, null));
                node.setExpanded(true);
                Platform.runLater(() -> root.getChildren().add(node)); // UI updates on FX thread
                return node;
            });
            Platform.runLater(() -> domainNode.getChildren().add(new TreeItem<>(new BenchmarkItem(b.getName(), b.getDomain(), b))));
        }
        return root;
    }

    @FXML
    private void handleSettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText("JScience Benchmarking Settings");
        alert.setContentText("Configuration options will be available in the next update (v1.1).");
        alert.showAndWait();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About JScience Benchmarking");
        alert.setHeaderText("JScience Benchmarking Suite v1.0");
        alert.setContentText("A premium benchmarking suite for high-performance computing.\n\n" +
                "Built with JScience Core and JavaFX.\n" +
                "© 2026 JScience Project Contributors.");
        alert.show();
    }

    @FXML
    private void handleRunSelected() {
        ObservableList<TreeItem<BenchmarkItem>> selectedItems = benchmarkTreeTable.getSelectionModel().getSelectedItems();
        Set<BenchmarkItem> uniqueBenchmarks = new LinkedHashSet<>();
        
        for (TreeItem<BenchmarkItem> item : selectedItems) {
            if (item != null) {
                // Determine if it's a category and expand it so user sees children running
                if (!item.isLeaf()) {
                     item.setExpanded(true);
                }
                collectBenchmarks(item, uniqueBenchmarks);
            }
        }
        
        if (!uniqueBenchmarks.isEmpty()) {
            runBenchmarkSuite(new ArrayList<>(uniqueBenchmarks));
        } else {
            statusBarLabel.setText("No benchmarks selected.");
        }
    }

    private void collectBenchmarks(TreeItem<BenchmarkItem> node, Set<BenchmarkItem> result) {
        if (node.isLeaf() && node.getValue().getBenchmark() != null) {
            result.add(node.getValue());
        } else {
            for (TreeItem<BenchmarkItem> child : node.getChildren()) {
                collectBenchmarks(child, result);
            }
        }
    }

    @FXML
    private void handleRunAll() {
        List<BenchmarkItem> toRun = new ArrayList<>();
        findLeafItems(benchmarkTreeTable.getRoot(), toRun);
        runBenchmarkSuite(toRun);
    }

    private void findLeafItems(TreeItem<BenchmarkItem> root, List<BenchmarkItem> resultList) {
        if (root == null) return;
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
        // Unbind first to prevent "A bound value cannot be set" exception
        statusBarLabel.textProperty().unbind();
        globalProgressBar.progressProperty().unbind();

        // Reset progress status for new run
        statusBarLabel.setText("Starting suite...");
        globalProgressBar.setVisible(true);

        Task<Void> suiteTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int total = benchmarks.size();
                for (int i = 0; i < total; i++) {
                    if (isCancelled()) break;
                    
                    BenchmarkItem item = benchmarks.get(i);
                    updateProgress(i, total);
                    updateMessage("Running " + item.getName() + " (" + (i+1) + "/" + total + ")...");
                    
                    executeSingle(item);
                }
                updateProgress(total, total);
                updateMessage("Suite Complete");
                Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
                return null;
            }
        };

        globalProgressBar.progressProperty().bind(suiteTask.progressProperty());
        statusBarLabel.textProperty().bind(suiteTask.messageProperty());
        new Thread(suiteTask).start();
    }

    private void executeSingle(BenchmarkItem item) {
        RunnableBenchmark b = item.getBenchmark();
        try {
            Platform.runLater(() -> item.statusProperty().set("Initializing..."));
            b.setup();
            
            // 1. Warmup
            Platform.runLater(() -> item.statusProperty().set("Warming up..."));
            int warmupIter = Math.max(1, b.getSuggestedIterations() / 5);
            for (int i = 0; i < warmupIter; i++) {
                b.run();
            }

            // 2. Measure
            Platform.runLater(() -> item.statusProperty().set("Measuring..."));
            long start = System.nanoTime();
            int iter = b.getSuggestedIterations();
            for (int i = 0; i < iter; i++) {
                b.run();
            }
            long end = System.nanoTime();
            
            double durationSec = (end - start) / 1_000_000_000.0;
            double opsSec = iter / durationSec;
            String resultText = String.format("%.2f ops/s", opsSec);

            Platform.runLater(() -> {
                item.statusProperty().set("Success");
                item.resultProperty().set(resultText);
                updateChart(item.getName(), opsSec);
                addToHistory(item.getName(), resultText);
            });
            
            b.teardown();
        } catch (Throwable e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                item.statusProperty().set("Error");
                item.resultProperty().set(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
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

    @FXML
    private void handleExportChart() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setInitialFileName("benchmark_results.png");
        
        File file = fileChooser.showSaveDialog(exportChartBtn.getScene().getWindow());
        if (file != null) {
            WritableImage image = performanceBarChart.snapshot(new javafx.scene.SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                statusBarLabel.textProperty().unbind();
                statusBarLabel.setText("Chart exported to " + file.getName());
            } catch (IOException e) {
                statusBarLabel.textProperty().unbind();
                statusBarLabel.setText("Failed to export chart: " + e.getMessage());
            }
        }
    }

    private void addToHistory(String name, String result) {
        Platform.runLater(() -> {
            BenchmarkRunSummary run = new BenchmarkRunSummary(name, result);
            historyList.add(0, run);
            // Save to persistent storage
            resultService.saveResults(new ArrayList<>(historyList));
        });
    }
}

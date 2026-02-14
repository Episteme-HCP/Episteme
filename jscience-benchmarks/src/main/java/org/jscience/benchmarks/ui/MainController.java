package org.jscience.benchmarks.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.benchmarks.benchmark.BenchmarkRegistry;
import org.jscience.benchmarks.ui.BenchmarkRunSummary;
import org.jscience.benchmarks.persistence.BenchmarkResultService;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.collections.FXCollections;

public class MainController {

    @FXML private TreeTableView<BenchmarkItem> benchmarkTreeTable;
    @FXML private TreeTableColumn<BenchmarkItem, String> nameColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> backendColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> libraryColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> providerColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> descriptionColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> statusColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> resultColumn;

    @FXML private TabPane visualizationTabPane;
    @FXML private ProgressBar globalProgressBar;
    @FXML private Label statusBarLabel;
    @FXML private TabPane mainTabPane;
    @FXML private TableView<BenchmarkRunSummary> historyTable;
    @FXML private TableColumn<BenchmarkRunSummary, String> dateColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> suiteColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> resultsColumn;


    @FXML private ComboBox<String> metricSelector;
    @FXML private Button exportChartBtn;

    // Maps to manage dynamic charts per domain
    private final Map<String, XYChart.Series<String, Number>> domainSeriesMap = new HashMap<>();
    private final Map<String, BarChart<String, Number>> domainChartMap = new HashMap<>();

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

        boolean anyRunning = false;
        boolean anyError = false;
        boolean anySuccess = false;
        int totalChildren = 0;
        int readyCount = 0;

        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            updateCategoryStatuses(child); // Recursive update
            totalChildren++;
            
            String status = child.getValue().statusProperty().get();
            if (status == null) status = "";
            
            if (status.contains("Running") || status.contains("Initializing") || status.contains("Warming") || status.contains("Measuring")) anyRunning = true;
            else if (status.contains("Error") || status.contains("Skipped")) anyError = true; // Treat skipped as error-like for aggregation? Or maybe neutral?
            else if ("Success".equals(status)) anySuccess = true;
            else if ("Ready".equals(status) || status.isEmpty()) readyCount++;
        }

        if (totalChildren == 0) return;

        String newStatus = "";
        if (anyRunning) newStatus = "Running...";
        else if (anyError) newStatus = "Error (Partial)";
        else if (readyCount == totalChildren) newStatus = "Ready"; // Nothing ran
        else if (anySuccess && readyCount == 0 && !anyError) newStatus = "Success"; // All ran success
        else newStatus = "Completed"; // Mixed bag (some success, some ready?)

        final String s = newStatus;
        Platform.runLater(() -> root.getValue().statusProperty().set(s));
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        backendColumn.setCellValueFactory(param -> param.getValue().getValue().backendProperty());
        libraryColumn.setCellValueFactory(param -> param.getValue().getValue().libraryProperty());
        providerColumn.setCellValueFactory(param -> param.getValue().getValue().providerProperty());
        descriptionColumn.setCellValueFactory(param -> param.getValue().getValue().descriptionProperty());
        statusColumn.setCellValueFactory(param -> param.getValue().getValue().statusProperty());
        resultColumn.setCellValueFactory(param -> param.getValue().getValue().resultProperty());
        
        benchmarkTreeTable.setShowRoot(false);
        
        benchmarkTreeTable.setRowFactory(tv -> {
            TreeTableRow<BenchmarkItem> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    BenchmarkItem item = row.getItem();
                    if (item.isBenchmark()) {
                         new Thread(() -> executeSingle(item)).start();
                    }
                }
            });
            return row;
        });
    }

    private void setupHistoryTable() {
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        suiteColumn.setCellValueFactory(data -> data.getValue().suiteProperty());
        resultsColumn.setCellValueFactory(data -> data.getValue().resultProperty());
    }

    private void startDiscovery() {
        Task<TreeItem<BenchmarkItem>> task = new Task<>() {
            @Override
            protected TreeItem<BenchmarkItem> call() throws Exception {
                List<RunnableBenchmark> benchmarks = BenchmarkRegistry.discover();
                return buildTree(benchmarks);
            }
        };

        task.setOnSucceeded(e -> {
            benchmarkTreeTable.setRoot(task.getValue());
            initializeDomainTabs(task.getValue());
            updateCategoryStatuses(task.getValue());
        });

        task.setOnFailed(e -> {
            statusBarLabel.setText("Discovery Failed: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private TreeItem<BenchmarkItem> buildTree(List<RunnableBenchmark> benchmarks) {
        TreeItem<BenchmarkItem> root = new TreeItem<>(new BenchmarkItem("Root", "Root", "", "", "", false));
        root.setExpanded(true);

        Map<String, TreeItem<BenchmarkItem>> domainMap = new HashMap<>();

        for (RunnableBenchmark b : benchmarks) {
            String domain = b.getDomain(); 
            if (domain == null || domain.isEmpty()) domain = "Uncategorized";
            
            domain = domain.substring(0, 1).toUpperCase() + domain.substring(1);

            TreeItem<BenchmarkItem> domainItem = domainMap.computeIfAbsent(domain, d -> {
                TreeItem<BenchmarkItem> item = new TreeItem<>(new BenchmarkItem(d, d, "", "", "", false));
                item.setExpanded(true);
                root.getChildren().add(item);
                return item;
            });
            
            BenchmarkItem itemData = new BenchmarkItem(b);
            TreeItem<BenchmarkItem> item = new TreeItem<>(itemData);
            domainItem.getChildren().add(item);
        }
        
        root.getChildren().sort(Comparator.comparing(t -> t.getValue().getName()));
        return root;
    }
    
    private void initializeDomainTabs(TreeItem<BenchmarkItem> root) {
        for (TreeItem<BenchmarkItem> domain : root.getChildren()) {
            getOrCreateDomainChart(domain.getValue().getName());
        }
    }

    @FXML
    private void handleSettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText("Settings");
        alert.setContentText("Settings dialog not implemented yet.");
        alert.showAndWait();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("JScience Benchmarking Suite");
        alert.setContentText("Version 1.0\nCopyright 2026");
        alert.showAndWait();
    }

    @FXML
    private void handleRunSelected() {
        TreeItem<BenchmarkItem> selected = benchmarkTreeTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Set<BenchmarkItem> itemsToRun = new HashSet<>();
        collectBenchmarks(selected, itemsToRun);
        
        if (!itemsToRun.isEmpty()) {
            runBenchmarkSuite(new ArrayList<>(itemsToRun));
        }
    }
    
    private void collectBenchmarks(TreeItem<BenchmarkItem> root, Set<BenchmarkItem> items) {
        if (root == null) return;
        
        if (root.getValue().isBenchmark()) {
            items.add(root.getValue());
        } else {
            for (TreeItem<BenchmarkItem> child : root.getChildren()) {
                collectBenchmarks(child, items);
            }
        }
    }

    @FXML
    private void handleRunAll() {
        Set<BenchmarkItem> itemsToRun = new HashSet<>();
        collectBenchmarks(benchmarkTreeTable.getRoot(), itemsToRun);
        runBenchmarkSuite(new ArrayList<>(itemsToRun));
    }
    
    private void runBenchmarkSuite(List<BenchmarkItem> items) {
         Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (BenchmarkItem item : items) {
                     Platform.runLater(() -> {
                         item.statusProperty().set("Queued");
                         item.resultProperty().set("");
                         item.setScore(0);
                     });
                }
                
                int count = 0;
                for (BenchmarkItem item : items) {
                    if (isCancelled()) break;
                    count++;
                    updateProgress(count, items.size());
                    executeSingle(item);
                    Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
                }
                return null;
            }
        };
        
        globalProgressBar.progressProperty().bind(task.progressProperty());
        
        task.setOnSucceeded(e -> {
             globalProgressBar.progressProperty().unbind();
             globalProgressBar.setProgress(1.0);
             statusBarLabel.setText("Suite Completed.");
        });
        
        task.setOnFailed(e -> {
             globalProgressBar.progressProperty().unbind();
             globalProgressBar.setProgress(0);
             statusBarLabel.setText("Suite Failed: " + task.getException().getMessage());
             task.getException().printStackTrace();
        });
        
        new Thread(task).start();
    }

    @FXML
    public void initialize() {
        setupTable();
        setupHistoryTable();
        loadHistory(); // Load persisted results
        startDiscovery(); // Background loading
        setupAnalytics();
        // Charts will be added lazily as data comes in or domain nodes are discovered
    }

    private void setupAnalytics() {
        metricSelector.getItems().addAll("Throughput (Ops/Sec)", "Average Latency (ms)", "P99 Latency (N/A)");
        metricSelector.getSelectionModel().select(0);
        metricSelector.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            refreshAllCharts();
        });
    }

    private void refreshAllCharts() {
        // Clear all series data first? Or just update Y values.
        // Easiest is to iterate over all items and update chart if they have a score.
        List<BenchmarkItem> allItems = new ArrayList<>();
        Set<BenchmarkItem> collected = new HashSet<>();
        collectBenchmarks(benchmarkTreeTable.getRoot(), collected);
        allItems.addAll(collected);
        
        // Update Y-Axis label for all charts
        String label = "Ops/Sec";
        int index = metricSelector.getSelectionModel().getSelectedIndex();
        if (index == 1) label = "Latency (ms)";
        
        for (BarChart<String, Number> chart : domainChartMap.values()) {
            ((NumberAxis) chart.getYAxis()).setLabel(label);
        }

        for (BenchmarkItem item : allItems) {
            String status = item.statusProperty().get();
            if ("Success".equals(status) && item.getScore() > 0) {
                double val = calculateMetric(item.getScore());
                updateChart(item.getUniqueId(), val, item.getDomain());
            }
        }
    }
    
    private double calculateMetric(double opsSec) {
        int index = metricSelector.getSelectionModel().getSelectedIndex();
        if (index == 0) return opsSec; // Throughput
        if (index == 1) return (opsSec > 0) ? 1000.0 / opsSec : 0.0; // Latency ms
        return 0.0; // P99 not implemented
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
                item.setScore(opsSec);
                
                String uniqueId = item.getUniqueId();
                double val = calculateMetric(opsSec);
                
                updateChart(uniqueId, val, item.getDomain());
                addToHistory(uniqueId, resultText);
            });
            
            b.teardown();
        } catch (Throwable e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            
            // Handle expected "Missing Library" errors gracefully
            if (e instanceof UnsupportedOperationException || (errorMsg != null && errorMsg.contains("not found"))) {
                 System.out.println("[INFO] Benchmark skipped: " + item.getName() + " (" + errorMsg + ")");
                 Platform.runLater(() -> {
                    item.statusProperty().set("Skipped");
                    item.resultProperty().set("Skipped (Lib Missing)");
                 });
            } else {
                e.printStackTrace();
                Platform.runLater(() -> {
                    item.statusProperty().set("Error");
                    item.resultProperty().set(errorMsg);
                });
            }
        }
    }

    private void updateChart(String name, double value, String domain) {
        XYChart.Series<String, Number> series = getOrCreateDomainChart(domain);
        
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getXValue().equals(name)) {
                d.setYValue(value);
                return;
            }
        }
        
        XYChart.Data<String, Number> data = new XYChart.Data<>(name, value);
        series.getData().add(data);
        
        // Apply Orange Color Style when node is created
        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: #ff9900;"); // Orange
            }
        });
    }
    
    private XYChart.Series<String, Number> getOrCreateDomainChart(String domain) {
        if (domainSeriesMap.containsKey(domain)) {
            return domainSeriesMap.get(domain);
        }
        
        // Create new chart for this domain
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Benchmark");
        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE); // White labels
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ops/Sec");
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE); // White labels
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(domain + " Performance");
        chart.setAnimated(false); // Disable animation for smoother updates
        chart.setLegendVisible(false); // Just one series usually
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Performance");
        chart.getData().add(series);
        
        Tab tab = new Tab(domain);
        tab.setContent(chart);
        visualizationTabPane.getTabs().add(tab);
        
        domainSeriesMap.put(domain, series);
        domainChartMap.put(domain, chart);
        
        return series;
    }

    @FXML
    private void handleExportChart() {
        // Export currently selected tab's chart
        Tab selectedTab = visualizationTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null || selectedTab.getContent() == null) {
             statusBarLabel.setText("No chart selected to export.");
             return;
        }
        
        if (!(selectedTab.getContent() instanceof BarChart)) {
             statusBarLabel.setText("Selected tab does not contain a chart.");
             return;
        }
        
        BarChart<?,?> chart = (BarChart<?,?>) selectedTab.getContent();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setInitialFileName("benchmark_" + selectedTab.getText().toLowerCase().replaceAll("\\s+", "_") + ".png");
        
        File file = fileChooser.showSaveDialog(exportChartBtn.getScene().getWindow());
        if (file != null) {
            WritableImage image = chart.snapshot(new javafx.scene.SnapshotParameters(), null);
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
            historyTable.getItems().add(0, run); // Explicitly add to table items if binding missing
            // Save to persistent storage
            resultService.saveResults(new ArrayList<>(historyList));
        });
    }
}

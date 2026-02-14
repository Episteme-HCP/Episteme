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
    @FXML private TreeTableColumn<BenchmarkItem, String> domainColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> statusColumn;
    @FXML private TreeTableColumn<BenchmarkItem, String> resultColumn;

    @FXML private TabPane visualizationTabPane; // Changed from single BarChart
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
        // Charts will be added lazily as data comes in or domain nodes are discovered
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
             
             // Initialize tabs for discovered domains
             initializeDomainTabs(task.getValue());
         });
         
         task.setOnFailed(e -> {
             statusBarLabel.setText("Discovery failed: " + task.getException().getMessage());
             globalProgressBar.setProgress(0);
             task.getException().printStackTrace();
         });
         
         new Thread(task).start();
    }

    private void initializeDomainTabs(TreeItem<BenchmarkItem> root) {
        if (root == null) return;
        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            if (!child.isLeaf()) {
                 String domain = child.getValue().getName(); // Assuming category nodes use name as domain
                 getOrCreateDomainChart(domain);
            }
        }
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
        // Settings button removed
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
                updateChart(item.getName(), opsSec, item.getDomain());
                addToHistory(item.getName(), resultText);
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
        series.getData().add(new XYChart.Data<>(name, value));
    }
    
    private XYChart.Series<String, Number> getOrCreateDomainChart(String domain) {
        if (domainSeriesMap.containsKey(domain)) {
            return domainSeriesMap.get(domain);
        }
        
        // Create new chart for this domain
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Benchmark");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ops/Sec");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(domain + " Performance");
        chart.setAnimated(false); // Disable animation for smoother updates
        chart.setLegendVisible(false); // Just one series usually
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Throughput");
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

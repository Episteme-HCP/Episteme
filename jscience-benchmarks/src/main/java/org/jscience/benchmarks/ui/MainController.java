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

import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
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
    @FXML private TableColumn<BenchmarkRunSummary, String> histNameColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> histBackendColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> histLibraryColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> histProviderColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> histDomainColumn;
    @FXML private TableColumn<BenchmarkRunSummary, String> resultsColumn;
    @FXML private Button exportHistoryBtn;


    @FXML private ComboBox<String> metricSelector;
    @FXML private Button exportChartBtn;

    // Maps to manage dynamic charts per domain
    private final Map<String, XYChart.Series<String, Number>> domainSeriesMap = new HashMap<>();
    private final Map<String, BarChart<String, Number>> domainChartMap = new HashMap<>();

    private final ObservableList<BenchmarkRunSummary> historyList = FXCollections.observableArrayList();
    private final org.jscience.benchmarks.persistence.BenchmarkResultService resultService = new org.jscience.benchmarks.persistence.BenchmarkResultService();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

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
        int unavailableCount = 0;

        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            updateCategoryStatuses(child); // Recursive update
            totalChildren++;
            
            String status = child.getValue().statusProperty().get();
            if (status == null) status = "";
            
            if (status.contains("Running") || status.contains("Initializing") || status.contains("Warming") || status.contains("Measuring")) anyRunning = true;
            else if (status.contains("Error") || status.contains("Skipped")) anyError = true;
            else if ("Success".equals(status)) anySuccess = true;
            else if ("Ready".equals(status) || status.isEmpty() || "Queued".equals(status)) readyCount++;
            else if (status.contains("Unavailable")) unavailableCount++;
        }

        if (totalChildren == 0) return;

        String newStatus = "";
        if (anyRunning) newStatus = "Running...";
        else if (anyError) newStatus = "Error (Partial)";
        else if (readyCount + unavailableCount == totalChildren) {
             if (readyCount > 0) newStatus = "Ready";
             else newStatus = "Unavailable";
        }
        else if (anySuccess && readyCount == 0 && !anyError && unavailableCount == 0) newStatus = "Success"; 
        else if (anySuccess) newStatus = "Completed"; // Success + Unavailable/Skipped
        else newStatus = "Ready"; // Fallback

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
        benchmarkTreeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        
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
        histNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        histBackendColumn.setCellValueFactory(data -> data.getValue().backendProperty());
        histLibraryColumn.setCellValueFactory(data -> data.getValue().libraryProperty());
        histProviderColumn.setCellValueFactory(data -> data.getValue().providerProperty());
        histDomainColumn.setCellValueFactory(data -> data.getValue().domainProperty());
        resultsColumn.setCellValueFactory(data -> data.getValue().resultProperty());
    }

    private final java.util.concurrent.ExecutorService benchmarkExecutor = java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "BenchmarkInternalExecutor");
        t.setDaemon(true);
        return t;
    });

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
    
    // Shutdown executor on exit
    public void shutdown() {
        if (benchmarkExecutor != null) benchmarkExecutor.shutdownNow();
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
            
            // Mark unavailable benchmarks immediately
            if (!b.isAvailable()) {
                itemData.statusProperty().set("Unavailable");
            }
            
            domainItem.getChildren().add(item);
        }
        
        root.getChildren().sort(Comparator.comparing(t -> t.getValue().getName()));
        return root;
    }
    
    private void initializeDomainTabs(TreeItem<BenchmarkItem> root) {
        // Charts are now created lazily per operation when results arrive
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
                         // Only queue available benchmarks
                         if (item.getBenchmark() != null && item.getBenchmark().isAvailable()) {
                             item.statusProperty().set("Queued");
                             item.resultProperty().set("");
                             item.setScore(0);
                         } else {
                             item.statusProperty().set("Unavailable"); // Ensure status is clear
                         }
                     });
                }
                
                int count = 0;
                for (BenchmarkItem item : items) {
                    if (isCancelled()) break;
                    executeSingle(item);
                    count++;
                    updateProgress(count, items.size());
                    Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
                }
                // Ensure progress is 100% at the end
                updateProgress(items.size(), items.size());
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
                updateChart(item, val);
            }
        }
    }
    
    private double calculateMetric(double opsSec) {
        int index = metricSelector.getSelectionModel().getSelectedIndex();
        if (index == 0) return opsSec; // Throughput
        if (index == 1) return (opsSec > 0) ? 1000.0 / opsSec : 0.0; // Latency ms
        return 0.0; // P99 not implemented
    }

    private String determineSimpleProvider(String provider, String simpleName) {
        if (provider != null) {
            if (provider.contains("OpenCL")) return "OpenCL (GPU)";
            if (provider.contains("CUDA")) return "CUDA (GPU)";
            if (provider.contains("Unsafe")) return "Unsafe (CPU)";
            
            // Detection of specific algorithms for JScience
            if (provider.contains("Strassen")) return "Multicore JScience (Strassen)";
            if (provider.contains("CARMA")) return "Multicore JScience (CARMA)";
            if (provider.contains("Sparse")) return "Multicore JScience (Sparse)";
            if (provider.contains("Standard")) return "Multicore JScience (Standard)";

            if (provider.contains("Multicore")) return "Multicore " + simpleName + " (Standard)";
            if (provider.contains("Dense")) return "Monocore " + simpleName + " (Standard)";
        }
        return provider; // Default to original if no specific match
    }

    private void executeSingle(BenchmarkItem item) {
        if (!isRunning.compareAndSet(false, true)) {
            // Already running
             Platform.runLater(() -> {
                // Optional: show a small toast or just ignore
                System.out.println("[INFO] Benchmark run ignored: execution already in progress.");
            });
            return;
        }

        RunnableBenchmark b = item.getBenchmark();
        if (b == null) {
            isRunning.set(false);
            return;
        }
        
        // Check availability logic as requested
        if (!b.isAvailable()) {
            Platform.runLater(() -> {
                item.statusProperty().set("Skipped (Unavailable)");
                item.resultProperty().set("N/A");
                // No chart update for unavailable benchmarks — user wants no measurement shown
            });
            return;
        }

        try {
            Platform.runLater(() -> item.statusProperty().set("Running..."));
            b.setup();
            
            // 1. Warmup (Adaptive: ~500ms)
            Platform.runLater(() -> item.statusProperty().set("Warming up..."));
            long warmupStart = System.nanoTime();
            while (System.nanoTime() - warmupStart < 500_000_000L) { // 500ms
                b.run();
            }

            // 2. Measure (Adaptive: ~2000ms)
            Platform.runLater(() -> item.statusProperty().set("Measuring..."));
            
            // Garbage Collect before measurement to reduce GC noise during run
            System.gc();
            Thread.sleep(100); 

            long start = System.nanoTime();
            long iterations = 0;
            // Run for at least 2 seconds
            while (System.nanoTime() - start < 2_000_000_000L) {
                b.run();
                iterations++;
            }
            long end = System.nanoTime();
            
            double durationSec = (end - start) / 1_000_000_000.0;
            double opsSec = iterations / durationSec;
            String resultText = String.format("%.2f ops/s", opsSec);

            Platform.runLater(() -> {
                item.statusProperty().set("Success");
                item.resultProperty().set(resultText);
                item.setScore(opsSec);
                
                double val = calculateMetric(opsSec);
                
                updateChart(item, val);
                addToHistory(item, resultText);
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
        } finally {
            isRunning.set(false);
        }
    }

    // Library-specific colors for visual distinction
    private static final String[] LIBRARY_COLORS = {
        "#ff9900", "#3498db", "#2ecc71", "#e74c3c", "#9b59b6",
        "#1abc9c", "#f39c12", "#e67e22", "#34495e", "#16a085"
    };
    private final Map<String, Integer> libraryColorIndex = new HashMap<>();
    private int nextColorIndex = 0;

    private String getLibraryColor(String library) {
        return LIBRARY_COLORS[libraryColorIndex.computeIfAbsent(library, k -> nextColorIndex++ % LIBRARY_COLORS.length)];
    }

    private void updateChart(BenchmarkItem item, double value) {
        String operationName = item.getName(); // e.g. "Matrix Multiplication"
        String barLabel = item.getLibrary();   // e.g. "JScience", "EJML"
        if (!"Standard".equals(item.getProvider())) {
            barLabel += " (" + item.getProvider() + ")";
        }
        String color = getLibraryColor(item.getLibrary());

        BarChart<String, Number> chart = getOrCreateOperationChart(operationName);
        XYChart.Series<String, Number> series = domainSeriesMap.get(operationName);
        
        // Check if bar already exists for this label
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getXValue().equals(barLabel)) {
                d.setYValue(value);
                return;
            }
        }
        
        XYChart.Data<String, Number> data = new XYChart.Data<>(barLabel, value);
        series.getData().add(data);
        
        // Apply library-specific color
        final String barColor = color;
        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: " + barColor + ";");
            }
        });
    }
    
    private BarChart<String, Number> getOrCreateOperationChart(String operationName) {
        if (domainChartMap.containsKey(operationName)) {
            return domainChartMap.get(operationName);
        }
        
        // Create new chart for this operation
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Library / Provider");
        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE); 
        xAxis.setStyle("-fx-tick-label-fill: white; -fx-text-fill: white;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ops/Sec");
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        yAxis.setStyle("-fx-tick-label-fill: white; -fx-text-fill: white;");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(operationName);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(operationName);
        chart.getData().add(series);
        
        Tab tab = new Tab(operationName);
        tab.setContent(chart);
        visualizationTabPane.getTabs().add(tab);
        
        domainSeriesMap.put(operationName, series);
        domainChartMap.put(operationName, chart);
        
        return chart;
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

    private void addToHistory(BenchmarkItem item, String result) {
        Platform.runLater(() -> {
            String backend = item.backendProperty().get();
            String provider = item.providerProperty().get();
            String library = item.libraryProperty().get();
            String domain = item.getDomain();
            BenchmarkRunSummary run = new BenchmarkRunSummary(
                item.getName(), 
                backend != null ? backend : "", 
                provider != null ? provider : "",
                library != null ? library : "",
                domain != null ? domain : "",
                result);
            historyList.add(0, run);
            historyTable.getItems().add(0, run);
            resultService.saveResults(new ArrayList<>(historyList));
        });
    }

    @FXML
    private void handleExportHistoryJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export History as JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("benchmark_history.json");

        File file = fileChooser.showSaveDialog(exportHistoryBtn.getScene().getWindow());
        if (file != null) {
            try {
                StringBuilder sb = new StringBuilder("[\n");
                for (int i = 0; i < historyList.size(); i++) {
                    sb.append("  ").append(historyList.get(i).toJson());
                    if (i < historyList.size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append("]");
                java.nio.file.Files.writeString(file.toPath(), sb.toString());
                statusBarLabel.setText("History exported to " + file.getName());
            } catch (IOException e) {
                statusBarLabel.setText("Failed to export history: " + e.getMessage());
            }
        }
    }
}

package org.jscience.benchmarks.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.benchmarks.benchmark.BenchmarkRegistry;
import org.jscience.core.ui.i18n.I18N;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.collections.FXCollections;

public class MainController {

    @FXML private Label mainTitleLabel;
    @FXML private Label languageLabel;
    @FXML private Button aboutBtn;
    @FXML private Tab executionTab;
    @FXML private Tab visualizationTab;
    @FXML private Tab environmentTab;
    @FXML private Tab historyTab;
    @FXML private Label benchmarkSelectionLabel;
    @FXML private Button runSelectedBtn;
    @FXML private Button runAllBtn;
    @FXML private Button stopBtn;
    @FXML private Label analyticsLabel;
    @FXML private Label historyTitleLabel;

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
    @FXML private ComboBox<String> languageSelector;
    @FXML private Button exportChartBtn;

    // Maps to manage dynamic charts per domain
    private final Map<String, XYChart.Series<String, Number>> domainSeriesMap = new HashMap<>();
    private final Map<String, BarChart<String, Number>> domainChartMap = new HashMap<>();

    private final ObservableList<BenchmarkRunSummary> historyList = FXCollections.observableArrayList();
    private final org.jscience.benchmarks.persistence.BenchmarkResultService resultService = new org.jscience.benchmarks.persistence.BenchmarkResultService();
    private final java.util.concurrent.ConcurrentLinkedQueue<BenchmarkItem> benchmarkQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
    private int selectedCount = 0;
    private int completedCount = 0;
    private final Object executionLock = new Object();
    private ResourceBundle resources;
    private Task<Void> mainTask;
    private volatile org.jscience.core.ComputeContext activeComputeContext;
    private boolean isProcessingQueue = false;

    private javafx.stage.Stage primaryStage;
    @FXML private EnvironmentController environmentViewController;

    public void setPrimaryStage(javafx.stage.Stage stage) {
        this.primaryStage = stage;
        // Trigger UI update once stage is set (to update title)
        if (resources != null) updateUI(java.util.Locale.getDefault());
    }

    private void updateUI(Locale locale) {
        try {
            resources = ResourceBundle.getBundle("org.jscience.benchmarks.i18n.messages-benchmark", locale);
            
            // Header
            mainTitleLabel.setText(resources.getString("app.title"));
            if (primaryStage != null) primaryStage.setTitle(resources.getString("app.window.title"));
            
            languageLabel.setText(resources.getString("app.language"));
            aboutBtn.setText(resources.getString("app.about"));

            // Tabs
            executionTab.setText(resources.getString("tab.execution"));
            visualizationTab.setText(resources.getString("tab.visualization"));
            environmentTab.setText(resources.getString("tab.environment"));
            historyTab.setText(resources.getString("tab.history"));

            // Execution Tab
            benchmarkSelectionLabel.setText(resources.getString("lbl.benchmark_selection"));
            runSelectedBtn.setText(resources.getString("btn.run_selected"));
            runAllBtn.setText(resources.getString("btn.run_all"));
            if (stopBtn != null) stopBtn.setText(resources.getString("btn.stop"));
            
            // Columns
            nameColumn.setText(resources.getString("col.name"));
            backendColumn.setText(resources.getString("col.backend"));
            libraryColumn.setText(resources.getString("col.library"));
            providerColumn.setText(resources.getString("col.provider"));
            descriptionColumn.setText(resources.getString("col.description"));
            statusColumn.setText(resources.getString("col.status"));
            resultColumn.setText(resources.getString("col.result"));
            
            // Visualization Tab
            analyticsLabel.setText(resources.getString("lbl.analytics"));
            metricSelector.setPromptText(resources.getString("prompt.metric"));
            exportChartBtn.setText(resources.getString("btn.export_chart"));

            // History Tab
            historyTitleLabel.setText(resources.getString("lbl.historical_runs"));
            exportHistoryBtn.setText(resources.getString("btn.export_json"));
            historyTable.setPlaceholder(new Label(resources.getString("msg.no_history")));
            
            // History Columns
            dateColumn.setText(resources.getString("col.date"));
            histNameColumn.setText(resources.getString("col.name"));
            histBackendColumn.setText(resources.getString("col.backend"));
            histLibraryColumn.setText(resources.getString("col.library"));
            histProviderColumn.setText(resources.getString("col.provider"));
            histDomainColumn.setText(resources.getString("col.domain"));
            resultsColumn.setText(resources.getString("col.result"));

            // Update nested controller
            if (environmentViewController != null) {
                environmentViewController.updateUI(resources);
            }

        } catch (Exception e) {
            System.err.println("Failed to update UI language: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper method to load history
    private void loadHistory() {
        for (BenchmarkRunSummary summary : resultService.loadResults()) {
            historyList.add(summary);
            historyTable.getItems().add(summary);
        }
    }

    // Helper method to update category statuses
    private void updateCategoryStatuses(TreeItem<BenchmarkItem> root) {
        if (root == null) return;
        
        // If it's the root itself (hidden), update children first
        if (root.getValue() != null && "Root".equals(root.getValue().getName())) {
            for (TreeItem<BenchmarkItem> child : root.getChildren()) {
                updateCategoryStatuses(child);
            }
            return;
        }

        if (root.isLeaf()) return;

        int totalChildren = 0;
        int runningCount = 0;
        int errorCount = 0;
        int successCount = 0;
        int unavailableCount = 0;
        int queuedCount = 0;
        int skippedCount = 0;
        int canceledCount = 0;

        int readyCount = 0;
        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            if (!child.isLeaf()) {
                updateCategoryStatuses(child); // Recursive update
            }
            totalChildren++;
            
            String status = child.getValue().statusProperty().get();
            if (status == null) status = "";
            
            if (status.contains("Running") || status.contains("Warming") || status.contains("Measuring")) runningCount++;
            else if (status.contains("Error")) errorCount++;
            else if ("Success".equals(status)) successCount++;
            else if ("Queued".equals(status)) queuedCount++;
            else if (status.contains("Skipped")) skippedCount++;
            else if (status.contains("Canceled")) canceledCount++;
            else if ("Ready".equals(status) || status.isEmpty()) readyCount++;
            else if (status.contains("Unavailable")) unavailableCount++;
        }

        if (totalChildren == 0) return;

        int totalToRun = totalChildren - readyCount - unavailableCount;
        int finishedCount = successCount + errorCount + skippedCount + canceledCount;
        int currentProgress = finishedCount + runningCount;

        String newStatus;
        if (runningCount > 0) newStatus = "Running (" + currentProgress + "/" + totalToRun + ")";
        else if (queuedCount > 0) newStatus = "Queued (" + queuedCount + ")";
        else if (canceledCount > 0) newStatus = "Canceled (" + canceledCount + "/" + totalChildren + ")";
        else if (errorCount > 0) newStatus = "Error (Partial: " + errorCount + ")";
        else if (successCount == totalChildren - unavailableCount - skippedCount && successCount > 0) newStatus = "Success";
        else if (successCount > 0) newStatus = "Completed (" + successCount + "/" + totalChildren + ")";
        else if (unavailableCount == totalChildren) newStatus = "Unavailable";
        else newStatus = "Ready";

        final String s = newStatus;
        // Always update the status, even if it's the same, to ensure UI refresh if needed
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
        benchmarkTreeTable.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
        benchmarkTreeTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        
        // Set loading placeholder with spinning indicator
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize(40, 40);
        Label loadingLabel = new Label(resources != null ? resources.getString("msg.loading_providers") : "Loading providers...");
        loadingLabel.setStyle("-fx-text-fill: #aaaaaa;");
        VBox loadingBox = new VBox(10, spinner, loadingLabel);
        loadingBox.setAlignment(Pos.CENTER);
        benchmarkTreeTable.setPlaceholder(loadingBox);
        
        benchmarkTreeTable.setRowFactory(tv -> {
            TreeTableRow<BenchmarkItem> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    BenchmarkItem item = row.getItem();
                    if (item.isBenchmark()) {
                         executeSingle(item);
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
            if (task.getValue() == null || task.getValue().getChildren().isEmpty()) {
                String noProviders = resources != null ? resources.getString("msg.no_providers") : "No providers found.";
                benchmarkTreeTable.setPlaceholder(new Label(noProviders));
            }
            initializeDomainTabs(task.getValue());
            updateCategoryStatuses(task.getValue());
        });

        task.setOnFailed(e -> {
            String msg = resources != null ? resources.getString("msg.discoveryFailed") : "Discovery Failed: {0}";
            statusBarLabel.setText(msg.replace("{0}", String.valueOf(task.getException().getMessage())));
            benchmarkTreeTable.setPlaceholder(new Label(msg.replace("{0}", String.valueOf(task.getException().getMessage()))));
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
        ObservableList<TreeItem<BenchmarkItem>> selectedItems = benchmarkTreeTable.getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) return;

        Set<BenchmarkItem> itemsToRun = new HashSet<>();
        for (TreeItem<BenchmarkItem> selected : selectedItems) {
            collectBenchmarks(selected, itemsToRun);
        }
        
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
        // Clear previous results for newly selected items
        for (BenchmarkItem item : items) {
            Platform.runLater(() -> {
                if (item.getBenchmark() != null && item.getBenchmark().isAvailable()) {
                    item.statusProperty().set("Queued");
                    item.resultProperty().set("");
                    item.setScore(0);
                } else {
                    item.statusProperty().set("Unavailable");
                }
            });
            benchmarkQueue.add(item);
        }
        
        selectedCount += items.size();

        if (mainTask != null && mainTask.isRunning()) {
            // Already running, items added to thread-safe queue will be picked up
            return;
        }

        mainTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                activeComputeContext = org.jscience.core.ComputeContext.current();
                activeComputeContext.setCancelled(false);
                
                while (!benchmarkQueue.isEmpty()) {
                    if (isCancelled()) break;
                    
                    BenchmarkItem item = benchmarkQueue.poll();
                    if (item == null) continue;
                    if (item.getBenchmark() == null || !item.getBenchmark().isAvailable()) {
                        completedCount++;
                        updateProgress(completedCount, selectedCount);
                        continue;
                    }

                    final int currentProgressCount = completedCount + 1;
                    final String itemName = item.getName();
                    
                    Platform.runLater(() -> {
                        statusBarLabel.setText(String.format("Running [%d/%d]: %s", currentProgressCount, selectedCount, itemName));
                        updateCategoryStatuses(benchmarkTreeTable.getRoot());
                    });
                    
                    runBenchmarkItem(item);
                    
                    completedCount++;
                    updateProgress(completedCount, selectedCount);
                    Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
                }
                
                updateProgress(selectedCount, selectedCount);
                return null;
            }
        };
        
        globalProgressBar.progressProperty().bind(mainTask.progressProperty());
        
        mainTask.setOnSucceeded(e -> {
             globalProgressBar.progressProperty().unbind();
             globalProgressBar.setProgress(1.0);
             statusBarLabel.setText("Suite Completed (" + completedCount + " items).");
             Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
             if (benchmarkQueue.isEmpty()) {
                 selectedCount = 0;
                 completedCount = 0;
                 isProcessingQueue = false;
             }
        });
        
        mainTask.setOnFailed(e -> {
             globalProgressBar.progressProperty().unbind();
             globalProgressBar.setProgress(0);
             statusBarLabel.setText("Suite Failed: " + mainTask.getException().getMessage());
             mainTask.getException().printStackTrace();
             Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
             isProcessingQueue = false;
        });

        mainTask.setOnCancelled(e -> {
             globalProgressBar.progressProperty().unbind();
             statusBarLabel.setText("Operation stopped by user.");
             Platform.runLater(() -> updateCategoryStatuses(benchmarkTreeTable.getRoot()));
             isProcessingQueue = false;
        });
        
        new Thread(mainTask).start();
    }

    @FXML
    private void handleStop() {
        if (mainTask != null && mainTask.isRunning()) {
            mainTask.cancel();
            
            if (activeComputeContext != null) {
                activeComputeContext.setCancelled(true);
            }
            
            // Immediately update UI to show cancellation of queued items
            synchronized (benchmarkQueue) {
                benchmarkQueue.clear();
                isProcessingQueue = false;
            }
            
            statusBarLabel.setText("Operation stopped by user.");
            
            // Mark items as canceled in the UI
            Platform.runLater(() -> {
                globalProgressBar.progressProperty().unbind();
                markPendingAsCanceled(benchmarkTreeTable.getRoot());
                updateCategoryStatuses(benchmarkTreeTable.getRoot());
            });
        }
    }

    private void markPendingAsCanceled(TreeItem<BenchmarkItem> root) {
        if (root == null) return;
        BenchmarkItem item = root.getValue();
        if (item != null) {
            String status = item.statusProperty().get();
            if ("Queued".equals(status) || (status != null && status.startsWith("Running"))) {
                item.statusProperty().set("Canceled");
            }
        }
        for (TreeItem<BenchmarkItem> child : root.getChildren()) {
            markPendingAsCanceled(child);
        }
    }

    @FXML
    public void initialize() {
        setupTable();
        setupHistoryTable();
        loadHistory(); // Load persisted results
        startDiscovery(); // Background loading
        setupAnalytics();
        setupLanguageSelector();
        // Initialize UI with default locale (English)
        updateUI(Locale.ENGLISH);
        // Charts will be added lazily as data comes in or domain nodes are discovered
    }

    private void setupAnalytics() {
        metricSelector.getItems().addAll("Throughput (Ops/Sec)", "Average Latency (ms)", "P99 Latency (ms)");
        metricSelector.getSelectionModel().select(0);
        metricSelector.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            refreshAllCharts();
        });
    }
    
    private void setupLanguageSelector() {
        languageSelector.getItems().addAll("English", "Français", "Español", "Deutsch", "中文");
        languageSelector.getSelectionModel().select(0); // Default to English
        languageSelector.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            java.util.Locale newLocale = switch (index) {
                case 1 -> java.util.Locale.FRENCH;
                case 2 -> Locale.forLanguageTag("es");
                case 3 -> java.util.Locale.GERMAN;
                case 4 -> java.util.Locale.CHINESE;
                default -> java.util.Locale.ENGLISH;
            };
            org.jscience.core.ui.i18n.I18N.getInstance().setLocale(newLocale);
            updateUI(newLocale);
            System.out.println("Language changed to: " + newLocale.getDisplayName());
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
                double val = calculateMetric(item);
                updateChart(item, val);
            }
        }
    }
    
    private double calculateMetric(BenchmarkItem item) {
        int index = metricSelector.getSelectionModel().getSelectedIndex();
        double opsSec = item.getScore();
        if (index == 0) return opsSec; // Throughput
        if (index == 1) return (opsSec > 0) ? 1000.0 / opsSec : 0.0; // Average Latency ms
        if (index == 2) return item.getP99LatencyMs(); // P99 Latency ms
        return 0.0;
    }

    private void executeSingle(BenchmarkItem item) {
        synchronized (benchmarkQueue) {
            benchmarkQueue.add(item);
            item.statusProperty().set("Queued");
            if (!isProcessingQueue) {
                isProcessingQueue = true;
                new Thread(this::processQueue).start();
            }
        }
    }

    private void processQueue() {
        while (true) {
            BenchmarkItem item;
            synchronized (benchmarkQueue) {
                item = benchmarkQueue.poll();
                if (item == null) {
                    isProcessingQueue = false;
                    return;
                }
            }
            runBenchmarkItem(item);
        }
    }

    private void runBenchmarkItem(BenchmarkItem item) {
        synchronized (executionLock) {
            RunnableBenchmark b = item.getBenchmark();
            if (b == null) return;
            
            // Check availability logic as requested
            if (!b.isAvailable()) {
                Platform.runLater(() -> {
                    item.statusProperty().set("Skipped (Unavailable)");
                    item.resultProperty().set("N/A");
                    updateCategoryStatuses(benchmarkTreeTable.getRoot()); // UI update
                });
                return;
            }
    
            try {
                Platform.runLater(() -> item.statusProperty().set("Running..."));
                b.setup();
            
            // 1. Warmup (Adaptive: ~500ms, max 1M iterations)
            Platform.runLater(() -> {
                item.statusProperty().set("Warming up...");
                updateCategoryStatuses(benchmarkTreeTable.getRoot());
            });
            long warmupStart = System.nanoTime();
            long warmupIters = 0;
            final long MAX_WARMUP_TIME_NS = 500_000_000L; // 500ms
            final long MAX_WARMUP_ITERS = 1_000_000;
            while (System.nanoTime() - warmupStart < MAX_WARMUP_TIME_NS && warmupIters < MAX_WARMUP_ITERS) { 
                if (mainTask != null && mainTask.isCancelled()) throw new RuntimeException("Canceled");
                b.run();
                warmupIters++;
            }

            // 2. Measure (Adaptive: ~2000ms, max 5M iterations)
            Platform.runLater(() -> {
                item.statusProperty().set("Measuring...");
                updateCategoryStatuses(benchmarkTreeTable.getRoot());
            });
            
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            java.util.List<Long> iterNanos = new java.util.ArrayList<>();
            long totalStart = System.nanoTime();
            long measureIters = 0;
            final long MAX_MEASURE_TIME_NS = 2_000_000_000L; // 2 seconds
            final long MAX_MEASURE_ITERS = 5_000_000;
            final long MAX_SINGLE_ITER_TIME_NS = 60_000_000_000L; // 60 seconds
            while (System.nanoTime() - totalStart < MAX_MEASURE_TIME_NS && measureIters < MAX_MEASURE_ITERS) {
                if (mainTask != null && mainTask.isCancelled()) throw new RuntimeException("Canceled");
                long iterStart = System.nanoTime();
                b.run();
                iterNanos.add(System.nanoTime() - iterStart);
                measureIters++;
                
                // Safety: if a single iteration takes more than 60 seconds, abort to avoid stalling
                if (System.nanoTime() - iterStart > MAX_SINGLE_ITER_TIME_NS) {
                    System.err.println("Benchmark skipped/stalled: " + item.getName() + " (single iteration > 60s)");
                    break;
                }
            }
            long totalEnd = System.nanoTime();
            
            long iterations = iterNanos.size();
            double durationSec = (totalEnd - totalStart) / 1_000_000_000.0;
            double opsSec = iterations / durationSec;
            
            java.util.Collections.sort(iterNanos);
            int p99Index = Math.max(0, (int) Math.ceil(iterNanos.size() * 0.99) - 1);
            double p99Ms = iterNanos.get(p99Index) / 1_000_000.0;
            
            String resultText;
            if (opsSec < 1.0) {
                 resultText = String.format("%.5f ops/s", opsSec);
            } else if (opsSec < 100.0) {
                 resultText = String.format("%.3f ops/s", opsSec);
            } else {
                 resultText = String.format("%.2f ops/s", opsSec);
            }

            Platform.runLater(() -> {
                item.statusProperty().set("Success");
                item.resultProperty().set(resultText);
                item.setScore(opsSec);
                item.setP99LatencyMs(p99Ms);
                
                updateChart(item, calculateMetric(item));
                addToHistory(item, resultText);
                updateCategoryStatuses(benchmarkTreeTable.getRoot());
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
            } else if (e instanceof org.jscience.core.technical.algorithm.OperationCancelledException || "Canceled".equals(errorMsg)) {
                 Platform.runLater(() -> {
                    item.statusProperty().set("Canceled");
                    item.resultProperty().set("Stopped");
                 });
            } else {
                e.printStackTrace();
                Platform.runLater(() -> {
                    item.statusProperty().set("Error");
                    item.resultProperty().set(errorMsg);
                    updateCategoryStatuses(benchmarkTreeTable.getRoot());
                });
            }
        }
      } // End synchronized
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
        // Group by operation category (domain is set by SystematicBenchmark to specific op type)
        String category = item.getDomain();
        if (category == null || category.isBlank()) category = item.getName();
        if (category == null || category.isBlank()) category = "General";

        // X-axis bar label = benchmark name + library (+ provider if non-standard)
        String barLabel = item.getName();
        String lib = item.getLibrary();
        if (lib != null && !lib.isBlank()) barLabel += "\n" + lib;
        if (item.getProvider() != null && !"Standard".equals(item.getProvider()))
            barLabel += " (" + item.getProvider() + ")";
        String color = getLibraryColor(item.getLibrary() != null ? item.getLibrary() : "Default");

        getOrCreateOperationChart(category);
        XYChart.Series<String, Number> series = domainSeriesMap.get(category);

        // Update existing bar if already present for this benchmark
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getXValue().equals(barLabel)) {
                d.setYValue(value);
                return;
            }
        }

        XYChart.Data<String, Number> data = new XYChart.Data<>(barLabel, value);
        series.getData().add(data);

        final String barColor = color;
        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) newNode.setStyle("-fx-bar-fill: " + barColor + ";");
        });
    }
    
    private BarChart<String, Number> getOrCreateOperationChart(String category) {
        if (domainChartMap.containsKey(category)) {
            return domainChartMap.get(category);
        }

        // Create one chart per category
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(I18N.getInstance().get("benchmark.chart.xaxis", "Benchmark / Library"));
        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        xAxis.setStyle("-fx-tick-label-fill: white; -fx-text-fill: white;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(I18N.getInstance().get("benchmark.chart.yaxis"));
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        yAxis.setStyle("-fx-tick-label-fill: white; -fx-text-fill: white;");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(category);
        chart.setAnimated(false);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(category);
        chart.getData().add(series);

        Tab tab = new Tab(category);
        tab.setContent(chart);
        visualizationTabPane.getTabs().add(tab);

        domainSeriesMap.put(category, series);
        domainChartMap.put(category, chart);

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
        
        // Finalize labels before export
        int metricIndex = metricSelector.getSelectionModel().getSelectedIndex();
        String metricPostfix = switch (metricIndex) {
            case 0 -> "throughput";
            case 1 -> "latency";
            case 2 -> "p99_latency";
            default -> "results";
        };
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        
        // Use the requested naming convention: id_chart_de_benchmark_{type} or similar
        String opName = selectedTab.getText().toLowerCase().replaceAll("\\s+", "_");
        fileChooser.setInitialFileName("id_chart_de_benchmark_" + opName + "_" + metricPostfix + ".png");
        
        File file = fileChooser.showSaveDialog(exportChartBtn.getScene().getWindow());
        if (file != null) {
            javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.web("#2b2b2b")); // Dark background for contrast
            
            // Ensure labels are white for visibility on dark background
            chart.getXAxis().setTickLabelFill(javafx.scene.paint.Color.WHITE);
            chart.getYAxis().setTickLabelFill(javafx.scene.paint.Color.WHITE);
            chart.getXAxis().setStyle("-fx-text-fill: white;");
            chart.getYAxis().setStyle("-fx-text-fill: white;");

            WritableImage image = chart.snapshot(params, null);
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
                StringBuilder sb = new StringBuilder("{\n");
                // System Context
                sb.append("  \"context\": {\n");
                sb.append("    \"java_version\": \"").append(System.getProperty("java.version")).append("\",\n");
                sb.append("    \"os_name\": \"").append(System.getProperty("os.name")).append("\",\n");
                sb.append("    \"os_arch\": \"").append(System.getProperty("os.arch")).append("\",\n");
                sb.append("    \"processors\": ").append(Runtime.getRuntime().availableProcessors()).append(",\n");
                sb.append("    \"timestamp\": \"").append(java.time.Instant.now().toString()).append("\"\n");
                sb.append("  },\n");
                
                sb.append("  \"runs\": [\n");
                for (int i = 0; i < historyList.size(); i++) {
                    sb.append("    ").append(historyList.get(i).toJson());
                    if (i < historyList.size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append("  ]\n}");
                
                java.nio.file.Files.writeString(file.toPath(), sb.toString());
                statusBarLabel.setText("History exported to " + file.getName());
            } catch (IOException e) {
                statusBarLabel.setText("Failed to export history: " + e.getMessage());
            }
        }
    }
}

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Grid Monitor Dashboard - JavaFX application to visualize the JScience Grid.
 * Shows connected workers, job queue, and server health.
 */
public class GridMonitorApp extends Application {

    private ManagedChannel channel;
    private Label workerCountLabel;
    private Label jobCountLabel;
    private Label statusLabel;
    private ScheduledExecutorService scheduler;
    private final ObservableList<WorkerInfo> workers = FXCollections.observableArrayList();
    private final ObservableList<JobInfo> jobs = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Connect to server
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        HBox header = createHeader();
        root.setTop(header);

        // Main content - Split view
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.4);

        // Left: Workers Table
        VBox workersPane = createWorkersPane();

        // Right: Jobs Table
        VBox jobsPane = createJobsPane();

        splitPane.getItems().addAll(workersPane, jobsPane);
        root.setCenter(splitPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/monitor.css") != null
                ? getClass().getResource("/styles/monitor.css").toExternalForm()
                : "");

        primaryStage.setTitle("JScience Grid Monitor");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start polling
        startPolling();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("âš¡ JScience Grid Monitor");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Stats cards
        workerCountLabel = createStatCard("Workers", "0");
        jobCountLabel = createStatCard("Jobs", "0");

        header.getChildren().addAll(title, spacer, workerCountLabel, jobCountLabel);
        return header;
    }

    private Label createStatCard(String label, String value) {
        Label card = new Label(label + ": " + value);
        card.setStyle("-fx-font-size: 16px; -fx-text-fill: #0f3460; -fx-background-color: #e94560; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;");
        return card;
    }

    private VBox createWorkersPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #0f3460;");

        Label header = new Label("Connected Workers");
        header.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TableView<WorkerInfo> table = new TableView<>(workers);
        table.setStyle("-fx-background-color: #1a1a2e;");

        TableColumn<WorkerInfo, String> idCol = new TableColumn<>("Worker ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        idCol.setPrefWidth(150);

        TableColumn<WorkerInfo, String> hostCol = new TableColumn<>("Hostname");
        hostCol.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        hostCol.setPrefWidth(120);

        TableColumn<WorkerInfo, Integer> coresCol = new TableColumn<>("Cores");
        coresCol.setCellValueFactory(new PropertyValueFactory<>("cores"));
        coresCol.setPrefWidth(60);

        TableColumn<WorkerInfo, String> memoryCol = new TableColumn<>("Memory");
        memoryCol.setCellValueFactory(new PropertyValueFactory<>("memory"));
        memoryCol.setPrefWidth(80);

        TableColumn<WorkerInfo, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        table.getColumns().add(idCol);
        table.getColumns().add(hostCol);
        table.getColumns().add(coresCol);
        table.getColumns().add(memoryCol);
        table.getColumns().add(statusCol);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        pane.getChildren().addAll(header, table);
        return pane;
    }

    private VBox createJobsPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #0f3460;");

        Label header = new Label("Job Queue");
        header.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TableView<JobInfo> table = new TableView<>(jobs);
        table.setStyle("-fx-background-color: #1a1a2e;");

        TableColumn<JobInfo, String> idCol = new TableColumn<>("Job ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        idCol.setPrefWidth(200);

        TableColumn<JobInfo, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(80);

        TableColumn<JobInfo, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<JobInfo, String> submittedCol = new TableColumn<>("Submitted");
        submittedCol.setCellValueFactory(new PropertyValueFactory<>("submitted"));
        submittedCol.setPrefWidth(150);

        table.getColumns().add(idCol);
        table.getColumns().add(priorityCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(submittedCol);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        pane.getChildren().addAll(header, table);
        return pane;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statusLabel = new Label("â— Connected to localhost:50051");
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        refreshBtn.setOnAction(e -> refreshData());

        footer.getChildren().addAll(statusLabel, spacer, refreshBtn);
        return footer;
    }

    private void startPolling() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                refreshData();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("â— Disconnected: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: #e94560;");
                });
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void refreshData() {
        try {
            // Get server status (mock for now - would need ServerInfo RPC)
            // For demo, we'll show static data
            Platform.runLater(() -> {
                workerCountLabel.setText("Workers: " + workers.size());
                jobCountLabel.setText("Jobs: " + jobs.size());
                statusLabel.setText("â— Connected to server");
                statusLabel.setStyle("-fx-text-fill: #4ecca3;");
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                statusLabel.setText("â— Error: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: #e94560;");
            });
        }
    }

    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (channel != null) {
            channel.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Data classes for TableView
    public static class WorkerInfo {
        private String workerId;
        private String hostname;
        private int cores;
        private String memory;
        private String status;

        public WorkerInfo(String workerId, String hostname, int cores, String memory, String status) {
            this.workerId = workerId;
            this.hostname = hostname;
            this.cores = cores;
            this.memory = memory;
            this.status = status;
        }

        public String getWorkerId() {
            return workerId;
        }

        public String getHostname() {
            return hostname;
        }

        public int getCores() {
            return cores;
        }

        public String getMemory() {
            return memory;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class JobInfo {
        private String jobId;
        private String priority;
        private String status;
        private String submitted;

        public JobInfo(String jobId, String priority, String status, String submitted) {
            this.jobId = jobId;
            this.priority = priority;
            this.status = status;
            this.submitted = submitted;
        }

        public String getJobId() {
            return jobId;
        }

        public String getPriority() {
            return priority;
        }

        public String getStatus() {
            return status;
        }

        public String getSubmitted() {
            return submitted;
        }
    }
}


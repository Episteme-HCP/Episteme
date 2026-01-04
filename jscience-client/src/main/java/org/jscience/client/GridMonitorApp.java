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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.server.proto.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Real-time Grid Monitor Dashboard showing actual JScience Cluster status.
 */
public class GridMonitorApp extends Application {

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private Label workerCountLabel;
    private Label jobCountLabel;
    private Label completedCountLabel;
    private Label statusLabel;
    private ScheduledExecutorService scheduler;
    private final ObservableList<WorkerInfo> workers = FXCollections.observableArrayList();
    private final ObservableList<JobInfo> jobs = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        blockingStub = ComputeServiceGrpc.newBlockingStub(channel);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setTop(createHeader());

        SplitPane splitPane = new SplitPane(createWorkersPane(), createJobsPane());
        splitPane.setDividerPositions(0.4);
        root.setCenter(splitPane);
        root.setBottom(createFooter());

        primaryStage.setTitle("JScience Cluster Monitor");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();

        startPolling();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        Label title = new Label("⚡ JScience Grid Monitor");
        title.setStyle("-fx-font-size: 24; -fx-text-fill: #e94560;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS); // Stats cards
        workerCountLabel = createStatCard("Workers", "0");
        jobCountLabel = createStatCard("Jobs", "0");
        completedCountLabel = createStatCard("Completed", "0");

        header.getChildren().addAll(title, spacer, workerCountLabel, jobCountLabel, completedCountLabel);
        return header;
    }

    private Label createStatCard(String label, String value) {
        Label card = new Label(label + ": " + value);
        card.setStyle(
                "-fx-font-size: 16; -fx-text-fill: white; -fx-background-color: #0f3460; -fx-padding: 10 20; -fx-background-radius: 5;");
        return card;
    }

    private VBox createWorkersPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #0f3460;");
        TableView<WorkerInfo> table = new TableView<>(workers);
        table.getColumns().add(new TableColumn<WorkerInfo, String>("Worker ID"));
        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("workerId"));
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        pane.getChildren().addAll(new Label("Active Workers"), table);
        return pane;
    }

    private VBox createJobsPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #0f3460;");
        TableView<JobInfo> table = new TableView<>(jobs);
        table.getColumns().add(new TableColumn<JobInfo, String>("Job ID"));
        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("jobId"));
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        pane.getChildren().addAll(new Label("Task Queue"), table);
        return pane;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #16213e;");
        statusLabel = new Label("Connecting...");
        statusLabel.setTextFill(
                org.jscience.server.proto.Status.UNKNOWN.name().equals("UNKNOWN") ? Color.valueOf("#4ecca3")
                        : Color.valueOf("#e94560"));
        footer.getChildren().add(statusLabel);
        return footer;
    }

    private void startPolling() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::refreshData, 0, 2, TimeUnit.SECONDS);
    }

    private void refreshData() {
        try {
            ServerStatus status = blockingStub.getStatus(Empty.newBuilder().build());
            Platform.runLater(() -> {
                workerCountLabel.setText("Active Workers: " + status.getActiveWorkers());
                jobCountLabel.setText("Queued Tasks: " + status.getQueuedTasks());
                completedCountLabel.setText("Total Completed: " + status.getTotalTasksCompleted());
                statusLabel.setText("✅ Connected | Grid Load: " + (int) (status.getSystemLoad() * 100) + "%");
                statusLabel.setStyle("-fx-text-fill: #4ecca3;");
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                statusLabel.setText("❌ Server Disconnected");
                statusLabel.setStyle("-fx-text-fill: #e94560;");
            });
        }
    }

    @Override
    public void stop() {
        if (scheduler != null)
            scheduler.shutdown();
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class WorkerInfo {
        String workerId;

        public WorkerInfo(String id) {
            this.workerId = id;
        }

        public String getWorkerId() {
            return workerId;
        }
    }

    public static class JobInfo {
        String jobId;

        public JobInfo(String id) {
            this.jobId = id;
        }

        public String getJobId() {
            return jobId;
        }
    }
}

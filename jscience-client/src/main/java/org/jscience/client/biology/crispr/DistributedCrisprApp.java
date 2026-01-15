/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client.biology.crispr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.biology.genome.CrisprTask;
import org.jscience.biology.loaders.FASTAReader;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import org.jscience.ui.i18n.I18n;

/**
 * Distributed CRISPR Design Application.
 * Offloads heavy genomic scanning and off-target analysis to the JScience grid.
 */
public class DistributedCrisprApp extends Application implements org.jscience.ui.App {

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceStub asyncStub;
    private TextArea genomeArea;
    private TableView<CrisprTask.Target> resultsTable;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        asyncStub = ComputeServiceGrpc.newStub(channel);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1a1a2e;");

        root.setTop(createHeader());
        root.setCenter(createMainView());
        root.setBottom(createFooter());

        primaryStage.setTitle(I18n.getInstance().get("app.distributedcrisprapp.title", "JScience - Distributed CRISPR Designer"));
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        Label title = new Label(I18n.getInstance().get("app.distributedcrisprapp.header", "🧬 Distributed CRISPR Scanner"));
        title.setStyle("-fx-font-size: 24; -fx-text-fill: #4ecca3; -fx-font-weight: bold;");

        genomeArea = new TextArea(
                "ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");
        genomeArea.setPrefHeight(120);
        genomeArea.setStyle(
                "-fx-control-inner-background: #16213e; -fx-text-fill: #e94560; -fx-font-family: 'Consolas';");

        Button scanBtn = new Button(I18n.getInstance().get("app.distributedcrisprapp.btn.scan", "🚀 Start Distributed Scan"));
        scanBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");
        scanBtn.setOnAction(e -> startDistributedScan());

        Button exportBtn = new Button(I18n.getInstance().get("app.distributedcrisprapp.btn.export", "💾 Export to FASTA"));
        exportBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        exportBtn.setOnAction(e -> exportToFasta());

        Button loadBtn = new Button(I18n.getInstance().get("app.distributedcrisprapp.btn.load", "📂 Load FASTA"));
        loadBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        loadBtn.setOnAction(e -> loadFasta((Stage) genomeArea.getScene().getWindow()));

        HBox btns = new HBox(10, scanBtn, exportBtn, loadBtn);
        header.getChildren().addAll(title, genomeArea, btns);
        return header;
    }

    private void exportToFasta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.getInstance().get("app.distributedcrisprapp.file.save.title", "Save FASTA Export"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(I18n.getInstance().get("app.distributedcrisprapp.file.fasta", "FASTA Files"), "*.fasta", "*.fa"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                List<FASTAReader.Sequence> sequences = new ArrayList<>();
                // Create a FASTA sequence from the results
                int i = 1;
                for (CrisprTask.Target target : resultsTable.getItems()) {
                    sequences.add(new FASTAReader.Sequence("CrISPR_Target_" + i + "_Pos_" + target.position(),
                            target.spacer()));
                    i++;
                }
                new org.jscience.biology.loaders.FASTAWriter().save(sequences, file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, I18n.getInstance().get("app.distributedcrisprapp.alert.export.success", "Exported {0} targets to {1}", sequences.size(), file.getName()))
                        .show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, I18n.getInstance().get("app.distributedcrisprapp.alert.export.error", "Export failed: {0}", e.getMessage())).show();
            }
        }
    }

    private void loadFasta(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, I18n.getInstance().get("app.distributedcrisprapp.file.load.title", "Load FASTA"), I18n.getInstance().get("app.distributedcrisprapp.file.fasta", "FASTA Files"), "*.fasta",
                "*.fa");
        if (file != null) {
            try {
                List<FASTAReader.Sequence> seqs = new FASTAReader().load(file.getAbsolutePath());
                StringBuilder sb = new StringBuilder();
                for (FASTAReader.Sequence s : seqs) {
                    sb.append(s.data);
                }
                genomeArea.setText(sb.toString());
                statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.loaded", "Loaded {0} sequences from {1}", seqs.size(), file.getName()));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, I18n.getInstance().get("app.distributedcrisprapp.alert.load.error", "Load failed: {0}", e.getMessage())).show();
            }
        }
    }

    private VBox createMainView() {
        VBox main = new VBox(10);
        main.setPadding(new Insets(10, 0, 0, 0));

        resultsTable = new TableView<>();
        resultsTable.setStyle("-fx-background-color: #0f3460;");

        TableColumn<CrisprTask.Target, Integer> colPos = new TableColumn<>(I18n.getInstance().get("app.distributedcrisprapp.col.pos", "Position"));
        colPos.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<CrisprTask.Target, String> colSpacer = new TableColumn<>(I18n.getInstance().get("app.distributedcrisprapp.col.spacer", "Spacer (20bp)"));
        colSpacer.setCellValueFactory(new PropertyValueFactory<>("spacer"));
        colSpacer.setPrefWidth(250);

        TableColumn<CrisprTask.Target, String> colPam = new TableColumn<>(I18n.getInstance().get("app.distributedcrisprapp.col.pam", "PAM"));
        colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));

        TableColumn<CrisprTask.Target, Double> colScore = new TableColumn<>(I18n.getInstance().get("app.distributedcrisprapp.col.score", "Efficiency Score"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        @SuppressWarnings("unchecked")
        TableColumn<CrisprTask.Target, ?>[] columns = new TableColumn[] { colPos, colSpacer, colPam, colScore };
        resultsTable.getColumns().addAll(columns);
        VBox.setVgrow(resultsTable, javafx.scene.layout.Priority.ALWAYS);

        main.getChildren().add(resultsTable);
        return main;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        statusLabel = new Label(I18n.getInstance().get("app.distributedcrisprapp.status.ready", "Ready"));
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");
        footer.getChildren().add(statusLabel);
        return footer;
    }

    private void startDistributedScan() {
        String sequence = genomeArea.getText();
        statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.submitting", "🛰️ Submitting sequence to cluster..."));

        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("CRISPR-" + System.currentTimeMillis())
                    .setSerializedTask(com.google.protobuf.ByteString.copyFrom(serialize(new CrisprTask(sequence))))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .build();

            asyncStub.submitTask(request, new StreamObserver<TaskResponse>() {
                @Override
                public void onNext(TaskResponse response) {
                    Platform.runLater(() -> statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.processing", "⚙️ Cluster Processing: {0}", response.getStatus())));
                    trackResults(response.getTaskId());
                }

                @Override
                public void onError(Throwable t) {
                    Platform.runLater(() -> statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.grid_error", "❌ Grid Error: {0}", t.getMessage())));
                }

                @Override
                public void onCompleted() {
                }
            });
        } catch (IOException e) {
            statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.serialization_error", "❌ Local Serialization Error"));
        }
    }

    private void trackResults(String taskId) {
        TaskIdentifier id = TaskIdentifier.newBuilder().setTaskId(taskId).build();
        asyncStub.streamResults(id, new StreamObserver<TaskResult>() {
            @Override
            public void onNext(TaskResult result) {
                if (result.getStatus() == Status.COMPLETED) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<CrisprTask.Target> targets = (List<CrisprTask.Target>) deserialize(
                                result.getSerializedData().toByteArray());
                        Platform.runLater(() -> {
                            resultsTable.setItems(FXCollections.observableArrayList(targets));
                            statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.complete", "✅ Grid Scan Complete: Found {0} targets.", targets.size()));
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.deserialization_error", "❌ Deserialization failed")));
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> statusLabel.setText(I18n.getInstance().get("app.distributedcrisprapp.status.stream_error", "❌ Result stream error")));
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // App Interface Implementation
    @Override
    public boolean isDemo() {
        return false;
    }

    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedcrisprapp.name", "Distributed Crispr App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedcrisprapp.desc", "Distributed application for Distributed Crispr App."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedcrisprapp.longdesc", "Distributed application for Distributed Crispr App."); }

    @Override
    public void show(javafx.stage.Stage stage) {
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return new java.util.ArrayList<>();
    }
}

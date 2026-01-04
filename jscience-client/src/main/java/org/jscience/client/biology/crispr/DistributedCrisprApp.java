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
import org.jscience.io.scientific.FastaLoader;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

/**
 * Distributed CRISPR Design Application.
 * Offloads heavy genomic scanning and off-target analysis to the JScience grid.
 */
public class DistributedCrisprApp extends Application {

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

        primaryStage.setTitle("JScience - Distributed CRISPR Designer");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        Label title = new Label("🧬 Distributed CRISPR Scanner");
        title.setStyle("-fx-font-size: 24; -fx-text-fill: #4ecca3; -fx-font-weight: bold;");

        genomeArea = new TextArea(
                "ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");
        genomeArea.setPrefHeight(120);
        genomeArea.setStyle(
                "-fx-control-inner-background: #16213e; -fx-text-fill: #e94560; -fx-font-family: 'Consolas';");

        Button scanBtn = new Button("🚀 Start Distributed Scan");
        scanBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");
        scanBtn.setOnAction(e -> startDistributedScan());

        Button exportBtn = new Button("💾 Export to FASTA");
        exportBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        exportBtn.setOnAction(e -> exportToFasta());

        Button loadBtn = new Button("📂 Load FASTA");
        loadBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        loadBtn.setOnAction(e -> loadFasta((Stage) genomeArea.getScene().getWindow()));

        HBox btns = new HBox(10, scanBtn, exportBtn, loadBtn);
        header.getChildren().addAll(title, genomeArea, btns);
        return header;
    }

    private void exportToFasta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save FASTA Export");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FASTA Files", "*.fasta", "*.fa"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                List<FastaLoader.SequenceRecord> sequences = new ArrayList<>();
                // Create a FASTA sequence from the results
                int i = 1;
                for (CrisprTask.Target target : resultsTable.getItems()) {
                    sequences.add(new FastaLoader.SequenceRecord("CrISPR_Target_" + i + "_Pos_" + target.position(),
                            target.spacer()));
                    i++;
                }
                new FastaLoader().save(sequences, file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, "Exported " + sequences.size() + " targets to " + file.getName())
                        .show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();
            }
        }
    }

    private void loadFasta(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load FASTA", "FASTA Files", "*.fasta",
                "*.fa");
        if (file != null) {
            try {
                List<FastaLoader.SequenceRecord> seqs = new FastaLoader().load(file.getAbsolutePath());
                StringBuilder sb = new StringBuilder();
                for (FastaLoader.SequenceRecord s : seqs) {
                    sb.append(s.sequence());
                }
                genomeArea.setText(sb.toString());
                statusLabel.setText("Loaded " + seqs.size() + " sequences from " + file.getName());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Load failed: " + e.getMessage()).show();
            }
        }
    }

    private VBox createMainView() {
        VBox main = new VBox(10);
        main.setPadding(new Insets(10, 0, 0, 0));

        resultsTable = new TableView<>();
        resultsTable.setStyle("-fx-background-color: #0f3460;");

        TableColumn<CrisprTask.Target, Integer> colPos = new TableColumn<>("Position");
        colPos.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<CrisprTask.Target, String> colSpacer = new TableColumn<>("Spacer (20bp)");
        colSpacer.setCellValueFactory(new PropertyValueFactory<>("spacer"));
        colSpacer.setPrefWidth(250);

        TableColumn<CrisprTask.Target, String> colPam = new TableColumn<>("PAM");
        colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));

        TableColumn<CrisprTask.Target, Double> colScore = new TableColumn<>("Efficiency Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        resultsTable.getColumns().addAll(colPos, colSpacer, colPam, colScore);
        VBox.setVgrow(resultsTable, javafx.scene.layout.Priority.ALWAYS);

        main.getChildren().add(resultsTable);
        return main;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");
        footer.getChildren().add(statusLabel);
        return footer;
    }

    private void startDistributedScan() {
        String sequence = genomeArea.getText();
        statusLabel.setText("🛰️ Submitting sequence to cluster...");

        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("CRISPR-" + System.currentTimeMillis())
                    .setSerializedTask(com.google.protobuf.ByteString.copyFrom(serialize(new CrisprTask(sequence))))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .build();

            asyncStub.submitTask(request, new StreamObserver<TaskResponse>() {
                @Override
                public void onNext(TaskResponse response) {
                    Platform.runLater(() -> statusLabel.setText("⚙️ Cluster Processing: " + response.getStatus()));
                    trackResults(response.getTaskId());
                }

                @Override
                public void onError(Throwable t) {
                    Platform.runLater(() -> statusLabel.setText("❌ Grid Error: " + t.getMessage()));
                }

                @Override
                public void onCompleted() {
                }
            });
        } catch (IOException e) {
            statusLabel.setText("❌ Local Serialization Error");
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
                            statusLabel.setText("✅ Grid Scan Complete: Found " + targets.size() + " targets.");
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> statusLabel.setText("❌ Deserialization failed"));
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> statusLabel.setText("❌ Result stream error"));
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
}

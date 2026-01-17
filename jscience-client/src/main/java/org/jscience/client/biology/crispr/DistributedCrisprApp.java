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
import org.jscience.ui.ThemeManager;

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
        root.getStyleClass().add("viewer-root");

        root.setTop(createHeader());
        root.setCenter(createMainView());
        root.setBottom(createFooter());

        primaryStage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.title", "JScience - Distributed CRISPR Designer"));
        Scene scene = new Scene(root, 1000, 700);
        ThemeManager.getInstance().applyTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.header", "🧬 Distributed CRISPR Scanner"));
        title.getStyleClass().add("header-label-white");

        genomeArea = new TextArea(
                "ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");
        genomeArea.setPrefHeight(120);
        genomeArea.getStyleClass().add("text-area-monospaced"); // We'll assume this class exists or add it to theme.css if needed, or stick to simple style

        Button scanBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.btn.scan", "🚀 Start Distributed Scan"));
        scanBtn.getStyleClass().add("accent-button-red");
        scanBtn.setOnAction(e -> startDistributedScan());

        Button exportBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.btn.export", "💾 Export to FASTA"));
        exportBtn.getStyleClass().add("accent-button-green");
        exportBtn.setOnAction(e -> exportToFasta());

        Button loadBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.btn.load", "📂 Load FASTA"));
        loadBtn.getStyleClass().add("accent-button-green");
        loadBtn.setOnAction(e -> loadFasta((Stage) genomeArea.getScene().getWindow()));

        HBox btns = new HBox(10, scanBtn, exportBtn, loadBtn);
        header.getChildren().addAll(title, genomeArea, btns);
        return header;
    }

    private void exportToFasta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.file.save.title", "Save FASTA Export"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.file.fasta", "FASTA Files"), "*.fasta", "*.fa"));
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
                new Alert(Alert.AlertType.INFORMATION, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.alert.export.success", "Exported {0} targets to {1}", sequences.size(), file.getName()))
                        .show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.alert.export.error", "Export failed: {0}", e.getMessage())).show();
            }
        }
    }

    private void loadFasta(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.file.load.title", "Load FASTA"), org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.file.fasta", "FASTA Files"), "*.fasta",
                "*.fa");
        if (file != null) {
            try {
                List<FASTAReader.Sequence> seqs = new FASTAReader().load(file.getAbsolutePath());
                StringBuilder sb = new StringBuilder();
                for (FASTAReader.Sequence s : seqs) {
                    sb.append(s.data);
                }
                genomeArea.setText(sb.toString());
                statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.loaded", "Loaded {0} sequences from {1}", seqs.size(), file.getName()));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.alert.load.error", "Load failed: {0}", e.getMessage())).show();
            }
        }
    }

    private VBox createMainView() {
        VBox main = new VBox(10);
        main.setPadding(new Insets(10, 0, 0, 0));

        resultsTable = new TableView<>();
        resultsTable.setStyle("-fx-background-color: #0f3460;");

        TableColumn<CrisprTask.Target, Integer> colPos = new TableColumn<>(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.col.pos", "Position"));
        colPos.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<CrisprTask.Target, String> colSpacer = new TableColumn<>(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.col.spacer", "Spacer (20bp)"));
        colSpacer.setCellValueFactory(new PropertyValueFactory<>("spacer"));
        colSpacer.setPrefWidth(250);

        TableColumn<CrisprTask.Target, String> colPam = new TableColumn<>(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.col.pam", "PAM"));
        colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));

        TableColumn<CrisprTask.Target, Double> colScore = new TableColumn<>(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.col.score", "Efficiency Score"));
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
        statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.ready", "Ready"));
        statusLabel.getStyleClass().add("accent-button-green");
        footer.getChildren().add(statusLabel);
        return footer;
    }

    private void startDistributedScan() {
        String sequence = genomeArea.getText();
        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.submitting", "🛰️ Submitting sequence to cluster..."));

        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("CRISPR-" + System.currentTimeMillis())
                    .setSerializedTask(com.google.protobuf.ByteString.copyFrom(serialize(new CrisprTask(sequence))))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .build();

            asyncStub.submitTask(request, new StreamObserver<TaskResponse>() {
                @Override
                public void onNext(TaskResponse response) {
                    Platform.runLater(() -> statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.processing", "⚙️ Cluster Processing: {0}", response.getStatus())));
                    trackResults(response.getTaskId());
                }

                @Override
                public void onError(Throwable t) {
                    Platform.runLater(() -> statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.grid_error", "❌ Grid Error: {0}", t.getMessage())));
                }

                @Override
                public void onCompleted() {
                }
            });
        } catch (IOException e) {
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.serialization_error", "❌ Local Serialization Error"));
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
                            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.complete", "✅ Grid Scan Complete: Found {0} targets.", targets.size()));
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.deserialization_error", "❌ Deserialization failed")));
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.status.stream_error", "❌ Result stream error")));
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
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.name", "Distributed Crispr App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.desc", "Advanced CRISPR-Cas9 target scanner leveraging distributed grid computing."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedcrisprapp.longdesc", "Identify optimal CRISPR/Cas9 guide RNA target sites with high precision. This application offloads heavy genomic sequence scanning and off-target cross-referencing to the JScience grid for rapid analysis of large genomes."); }

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

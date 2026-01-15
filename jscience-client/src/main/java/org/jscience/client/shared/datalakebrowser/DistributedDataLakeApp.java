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

package org.jscience.client.shared.datalakebrowser;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jscience.server.proto.*;
import org.jscience.ui.i18n.I18n;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Data Lake Browser - Visualize streaming Genome and Star data from the server.
 * Demonstrates the efficiency of gRPC streaming for large scientific datasets.
 */
public class DistributedDataLakeApp extends Application implements org.jscience.ui.App {

    private ManagedChannel channel;
    private DataServiceGrpc.DataServiceStub asyncStub;
    private TextArea outputArea;
    private ProgressBar progressBar;
    private Label statsLabel;
    private Label throughputLabel;
    private Button streamGenomeBtn;
    private Button streamStarsBtn;
    private volatile boolean isStreaming = false;

    @Override
    public void start(Stage primaryStage) {
        // Connect to server
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = DataServiceGrpc.newStub(channel);

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        HBox header = createHeader();
        root.setTop(header);

        // Center - Output Area
        VBox centerPane = createCenterPane();
        root.setCenter(centerPane);

        // Footer - Stats
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle(I18n.getInstance().get("app.distributeddatalakeapp.title", "📊 Data Lake Browser"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(I18n.getInstance().get("app.distributeddatalakeapp.title", "📊 Data Lake Browser"));
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Stream controls
        streamGenomeBtn = new Button(I18n.getInstance().get("app.distributeddatalakeapp.btn.genome", "🧬 Stream Genome"));
        streamGenomeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px;");
        streamGenomeBtn.setOnAction(e -> streamGenomeData());

        streamStarsBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("generated.distributeddatalake.stream.stars", "Ã¢Â­Â Stream Stars"));
        streamStarsBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; -fx-font-size: 14px;");
        streamStarsBtn.setOnAction(e -> streamStarData());

        Button clearBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("collab.btn.clear", "Clear"));
        clearBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            outputArea.clear();
            progressBar.setProgress(0);
            statsLabel.setText(I18n.getInstance().get("app.distributeddatalakeapp.status.ready", "Ready"));
        });

        header.getChildren().addAll(title, spacer, streamGenomeBtn, streamStarsBtn, clearBtn);
        return header;
    }

    private VBox createCenterPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #0f3460;");

        Label label = new Label(I18n.getInstance().get("app.distributeddatalakeapp.lbl.output", "Streaming Data Output"));
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));
        outputArea.setStyle("-fx-control-inner-background: #1a1a2e; -fx-text-fill: #4ecca3;");
        VBox.setVgrow(outputArea, javafx.scene.layout.Priority.ALWAYS);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: #e94560;");

        pane.getChildren().addAll(label, outputArea, progressBar);
        return pane;
    }

    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statsLabel = new Label(I18n.getInstance().get("app.distributeddatalakeapp.status.ready", "Ready"));
        statsLabel.setStyle("-fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        throughputLabel = new Label(I18n.getInstance().get("app.distributeddatalakeapp.throughput.default", "Throughput: 0 items/s"));
        throughputLabel.setStyle("-fx-text-fill: #888;");

        footer.getChildren().addAll(statsLabel, spacer, throughputLabel);
        return footer;
    }

    private void streamGenomeData() {
        if (isStreaming)
            return;
        isStreaming = true;
        streamGenomeBtn.setDisable(true);
        streamStarsBtn.setDisable(true);
        outputArea.clear();

        GenomeRegionRequest request = GenomeRegionRequest.newBuilder()
                .setChromosome("chr1")
                .setStartBase(0)
                .setEndBase(100000)
                .build();

        AtomicInteger count = new AtomicInteger(0);
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        StringBuilder buffer = new StringBuilder();

        asyncStub.streamGenomeData(request, new StreamObserver<GenomeChunk>() {
            @Override
            public void onNext(GenomeChunk chunk) {
                int c = count.incrementAndGet();
                buffer.append(I18n.getInstance().get("app.distributeddatalakeapp.msg.chunk_format", "[{0}] Position: {1}, Sequence: {2}...",
                        c, chunk.getStartBase(),
                        chunk.getSequence().length() > 50 ? chunk.getSequence().substring(0, 50)
                                : chunk.getSequence())).append("\n");

                if (c % 10 == 0) {
                    Platform.runLater(() -> {
                        outputArea.appendText(buffer.toString());
                        buffer.setLength(0);
                        progressBar.setProgress(Math.min(c / 100.0, 1.0));
                        statsLabel.setText(I18n.getInstance().get("app.distributeddatalakeapp.status.received_chunks", "Received: {0} chunks", c));

                        long elapsed = System.currentTimeMillis() - startTime.get();
                        double throughput = c / (elapsed / 1000.0);
                        throughputLabel.setText(I18n.getInstance().get("app.distributeddatalakeapp.throughput.chunks", "Throughput: {0} chunks/s", String.format("%.1f", throughput)));
                    });
                }
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> {
                    outputArea.appendText("\nÃ°Å¸â€Â´ Error: " + t.getMessage() + "\n");
                    statsLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("generated.distributeddatalake.error", "Error"));
                    isStreaming = false;
                    streamGenomeBtn.setDisable(false);
                    streamStarsBtn.setDisable(false);
                });
            }

            @Override
            public void onCompleted() {
                Platform.runLater(() -> {
                    outputArea.appendText(buffer.toString());
                    outputArea.appendText("\nÃ¢Å“â€¦ Stream completed! Total: " + count.get() + " chunks\n");
                    progressBar.setProgress(1.0);
                    statsLabel.setText("Completed: " + count.get() + " chunks");
                    isStreaming = false;
                    streamGenomeBtn.setDisable(false);
                    streamStarsBtn.setDisable(false);
                });
            }
        });
    }

    private void streamStarData() {
        if (isStreaming)
            return;
        isStreaming = true;
        streamGenomeBtn.setDisable(true);
        streamStarsBtn.setDisable(true);
        outputArea.clear();

        SkyRegionRequest request = SkyRegionRequest.newBuilder()
                .setMinRa(0.0)
                .setMaxRa(360.0)
                .setMinDec(-90.0)
                .setMaxDec(90.0)
                .build();

        AtomicInteger count = new AtomicInteger(0);
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        StringBuilder buffer = new StringBuilder();

        asyncStub.streamStarCatalog(request, new StreamObserver<StarObject>() {
            @Override
            public void onNext(StarObject star) {
                int c = count.incrementAndGet();
                buffer.append(String.format("[%d] Ã¢Â­Â %s | RA: %.2fÃ‚Â° | Dec: %.2fÃ‚Â° | Mag: %.2f | Type: %s%n",
                        c, star.getStarId(), star.getRa(),
                        star.getDec(), star.getMagnitude(), star.getType()));

                if (c % 10 == 0) {
                    Platform.runLater(() -> {
                        outputArea.appendText(buffer.toString());
                        buffer.setLength(0);
                        progressBar.setProgress(Math.min(c / 100.0, 1.0));
                        statsLabel.setText(I18n.getInstance().get("app.distributeddatalakeapp.status.received_stars", "Received: {0} stars", c));

                        long elapsed = System.currentTimeMillis() - startTime.get();
                        double throughput = c / (elapsed / 1000.0);
                        throughputLabel.setText(I18n.getInstance().get("app.distributeddatalakeapp.throughput.stars", "Throughput: {0} stars/s", String.format("%.1f", throughput)));
                    });
                }
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> {
                    outputArea.appendText("\nÃ°Å¸â€Â´ Error: " + t.getMessage() + "\n");
                    statsLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("generated.distributeddatalake.error", "Error"));
                    isStreaming = false;
                    streamGenomeBtn.setDisable(false);
                    streamStarsBtn.setDisable(false);
                });
            }

            @Override
            public void onCompleted() {
                Platform.runLater(() -> {
                    outputArea.appendText(buffer.toString());
                    outputArea.appendText("\nÃ¢Å“â€¦ Stream completed! Total: " + count.get() + " stars\n");
                    progressBar.setProgress(1.0);
                    statsLabel.setText("Completed: " + count.get() + " stars");
                    isStreaming = false;
                    streamGenomeBtn.setDisable(false);
                    streamStarsBtn.setDisable(false);
                });
            }
        });
    }

    @Override
    public void stop() {
        if (channel != null) {
            channel.shutdown();
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
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.general", "General"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributeddatalakeapp.name", "Distributed Data Lake App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributeddatalakeapp.desc", "Distributed application for Distributed Data Lake App."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributeddatalakeapp.longdesc", "Distributed application for Distributed Data Lake App."); }

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

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

package org.jscience.client.politics.geopolitics;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.economics.DistributedEconomyTask;
import org.jscience.politics.loaders.WorldBankReader;
import org.jscience.politics.GeopoliticalEngineTask;
import org.jscience.politics.loaders.FactbookReader;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Distributed Social Simulation App (Economics & Politics).
 */
public class DistributedGeopoliticsApp extends Application {

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;

    private DistributedEconomyTask economyTask;
    private GeopoliticalEngineTask politicsTask;

    private ListView<String> console;
    private Label economyLabel;
    private boolean serverAvailable = false;
    private long step = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("📉 JScience Social Grid - Global Economics & Politics");

        // Fetch real data (blocking for simplicity in start)
        double gdp = 23000000000000.0; // Fallback
        double inflation = 0.03;
        try {
            Map<String, Double> gdpData = WorldBankReader.getInstance().fetchIndicatorData("USA", "NY.GDP.MKTP.CD")
                    .join();
            if (!gdpData.isEmpty())
                gdp = gdpData.values().iterator().next();

            Map<String, Double> inflData = WorldBankReader.getInstance().fetchIndicatorData("USA", "FP.CPI.TOTL.ZG")
                    .join();
            if (!inflData.isEmpty())
                inflation = inflData.values().iterator().next() / 100.0;
        } catch (Exception e) {
            System.err.println("Failed to fetch WB data: " + e.getMessage());
        }

        economyTask = new DistributedEconomyTask("Global Core",
                Real.of(gdp),
                Real.of(inflation));

        List<GeopoliticalEngineTask.NationState> nations = new ArrayList<>();
        // Load nations from FactbookReader
        FactbookReader loader = new FactbookReader();
        List<org.jscience.politics.Country> countries = loader.getMiniCatalog().getAll().get(0);

        for (org.jscience.politics.Country c : countries) {
            double stability = 0.5;
            if (c.getGovernmentType() != null && c.getGovernmentType().toLowerCase().contains("republic")) {
                stability = 0.8;
            }
            // Estimate military as fraction of population * GDP proxy
            double military = c.getPopulationLong() * 500.0;

            nations.add(
                    new GeopoliticalEngineTask.NationState(c.getName(), stability, military));
        }
        politicsTask = new GeopoliticalEngineTask(nations);

        console = new ListView<>();
        economyLabel = new Label("GDP: -- | Inflation: --");
        economyLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        Button exportBtn = new Button("📄 Export Report");
        exportBtn.setOnAction(e -> exportReport(stage));

        VBox root = new VBox(10, economyLabel, console, exportBtn);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 600, 450));
        stage.show();

        initGrpc();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (step++ % 60 == 0) { // Every ~1 second
                    runStep();
                }
            }
        }.start();
    }

    private void initGrpc() {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
            blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
            serverAvailable = true;
        } catch (Exception e) {
            serverAvailable = false;
        }
    }

    private void runStep() {
        if (!serverAvailable) {
            economyTask.run();
            politicsTask.run();
            updateUI();
            return;
        }

        new Thread(() -> {
            try {
                // Submit economy task
                TaskRequest reqE = TaskRequest.newBuilder()
                        .setTaskId("econ-" + step)
                        .setSerializedTask(ByteString.copyFrom(serialize(economyTask)))
                        .build();
                TaskResponse resE = blockingStub.submitTask(reqE);

                // Submit politics task
                TaskRequest reqP = TaskRequest.newBuilder()
                        .setTaskId("pol-" + step)
                        .setSerializedTask(ByteString.copyFrom(serialize(politicsTask)))
                        .build();
                TaskResponse resP = blockingStub.submitTask(reqP);

                // Stream results (simplified)
                TaskResult rE = blockingStub.withDeadlineAfter(3, TimeUnit.SECONDS)
                        .streamResults(TaskIdentifier.newBuilder().setTaskId(resE.getTaskId()).build()).next();
                TaskResult rP = blockingStub.withDeadlineAfter(3, TimeUnit.SECONDS)
                        .streamResults(TaskIdentifier.newBuilder().setTaskId(resP.getTaskId()).build()).next();

                if (rE.getStatus() == Status.COMPLETED)
                    economyTask = (DistributedEconomyTask) deserialize(rE.getSerializedData().toByteArray());
                if (rP.getStatus() == Status.COMPLETED)
                    politicsTask = (GeopoliticalEngineTask) deserialize(rP.getSerializedData().toByteArray());

                Platform.runLater(this::updateUI);
            } catch (Exception e) {
                Platform.runLater(() -> console.getItems().add(0, "Grid Error: " + e.getMessage()));
            }
        }).start();
    }

    private void updateUI() {
        economyLabel.setText(String.format("GDP: $%.2fT | Inflation: %.2f%%",
                economyTask.getGdp().doubleValue() / 1e12,
                economyTask.getInflation().doubleValue() * 100));

        for (GeopoliticalEngineTask.NationState n : politicsTask.getNations()) {
            console.getItems().add(0, String.format("[%d] %s: Stability=%.2f, Military=%.0f",
                    step, n.name, n.stability, n.militaryPower));
        }
        if (console.getItems().size() > 50)
            console.getItems().remove(50, console.getItems().size());
    }

    private void exportReport(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Export Report", "CSV Files", "*.csv");
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.println("Step,Metric,Value");
                pw.println(step + ",GDP," + economyTask.getGdp());
                pw.println(step + ",Inflation," + economyTask.getInflation());
                pw.println(step + ",ActiveNations," + politicsTask.getNations().size());
                new Alert(Alert.AlertType.INFORMATION, "Report saved").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Export failed").show();
            }
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    private Object deserialize(byte[] data) throws Exception {
        return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
    }

    @Override
    public void stop() {
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

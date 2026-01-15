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

package org.jscience.client.shared.whiteboard;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.server.proto.*;

/**
 * Collaborative Whiteboard - Real-time shared canvas using
 * CollaborationService.
 * Multiple users can draw simultaneously and see each other's drawings.
 */
public class DistributedWhiteboardApp extends Application implements org.jscience.ui.App {

    private ManagedChannel channel;
    private CollaborationServiceGrpc.CollaborationServiceStub asyncStub;
    private Canvas canvas;
    private GraphicsContext gc;
    private String sessionId;
    private String userId;
    private ColorPicker colorPicker;
    private Slider brushSizeSlider;
    private double lastX, lastY;
    private Label statusLabel;
    private boolean isConnected = false;

    @Override
    public void start(Stage primaryStage) {
        // Connect to server
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));
        userId = "user-" + System.currentTimeMillis() % 10000;

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = CollaborationServiceGrpc.newStub(channel);

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Toolbar
        HBox toolbar = createToolbar();
        root.setTop(toolbar);

        // Canvas
        StackPane canvasPane = new StackPane();
        canvas = new Canvas(1000, 600);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        canvasPane.getChildren().add(canvas);
        canvasPane.setStyle("-fx-background-color: white; -fx-padding: 10;");
        root.setCenter(canvasPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setTitle(java.text.MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("window.title.whiteboard", "JScience Collaborative Whiteboard - {0}"), userId));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(10, 20, 10, 20));
        toolbar.setStyle("-fx-background-color: #16213e;");

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("app.title.whiteboard", "Collaborative Whiteboard"));
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        // Session controls
        TextField sessionField = new TextField("default-session");
        sessionField.setPrefWidth(150);
        sessionField.setPromptText(org.jscience.ui.i18n.I18n.getInstance().get("collab.prompt.session", "Session ID"));

        Button joinBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("collab.btn.join", "Join Session"));
        joinBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white;");
        joinBtn.setOnAction(e -> joinSession(sessionField.getText()));

        Button createBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("collab.btn.create", "Create Session"));
        createBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        createBtn.setOnAction(e -> createSession(sessionField.getText()));

        // Drawing controls
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setStyle("-fx-background-color: #0f3460;");

        brushSizeSlider = new Slider(1, 20, 3);
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setPrefWidth(100);

        Label brushLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("collab.label.brush", "Brush:"));
        brushLabel.setStyle("-fx-text-fill: white;");

        Button clearBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("collab.btn.clear", "Clear"));
        clearBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> clearCanvas());

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        toolbar.getChildren().addAll(
                title, spacer,
                sessionField, createBtn, joinBtn,
                new Separator(), colorPicker, brushLabel, brushSizeSlider, clearBtn);
        return toolbar;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(8, 20, 8, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.distributedwhiteboard.not.connected.to.any", "Ã¢Å¡Âª Not connected to any session"));
        statusLabel.setStyle("-fx-text-fill: #888;");

        footer.getChildren().add(statusLabel);
        return footer;
    }

    private void createSession(String sessionName) {
        CreateSessionRequest request = CreateSessionRequest.newBuilder()
                .setOwnerId(userId)
                .build();

        CollaborationServiceGrpc.CollaborationServiceBlockingStub blockingStub = CollaborationServiceGrpc
                .newBlockingStub(channel);

        try {
            SessionResponse response = blockingStub.createSession(request);
            sessionId = response.getSessionId();
            Platform.runLater(() -> {
                statusLabel.setText(java.text.MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("status.session_created", "Created session: {0}"), sessionId));
                statusLabel.setStyle("-fx-text-fill: #4ecca3;");
            });
            joinSession(sessionId);
        } catch (Exception e) {
            Platform.runLater(() -> {
                statusLabel.setText(java.text.MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("status.session_create_error", "Failed to create session: {0}"), e.getMessage()));
                statusLabel.setStyle("-fx-text-fill: #e94560;");
            });
        }
    }

    private void joinSession(String sessionName) {
        sessionId = sessionName;
        SessionRequest request = SessionRequest.newBuilder()
                .setSessionId(sessionId)
                .setUserId(userId)
                .build();

        // Subscribe to session events
        asyncStub.joinSession(request, new StreamObserver<SessionEvent>() {
            @Override
            public void onNext(SessionEvent event) {
                handleRemoteEvent(event);
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() -> {
                    statusLabel.setText(java.text.MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("status.connection_error", "Connection error: {0}"), t.getMessage()));
                    statusLabel.setStyle("-fx-text-fill: #e94560;");
                    isConnected = false;
                });
            }

            @Override
            public void onCompleted() {
                Platform.runLater(() -> {
                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("generated.distributedwhiteboard.session.ended", "Ã¢Å¡Âª Session ended"));
                    statusLabel.setStyle("-fx-text-fill: #888;");
                    isConnected = false;
                });
            }
        });

        isConnected = true;
        Platform.runLater(() -> {
            statusLabel.setText(java.text.MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("status.session_connected_user", "Connected to session: {0} as {1}"), sessionId, userId));
            statusLabel.setStyle("-fx-text-fill: #4ecca3;");
        });
    }

    private void handleRemoteEvent(SessionEvent event) {
        if (event.getUserId().equals(userId)) {
            return; // Ignore own events
        }

        Platform.runLater(() -> {
            String data = event.getPayload();
            if (event.getEventType().equals("DRAW") && data.contains(",")) {
                String[] parts = data.split(",");
                if (parts.length >= 6) {
                    double x1 = Double.parseDouble(parts[0]);
                    double y1 = Double.parseDouble(parts[1]);
                    double x2 = Double.parseDouble(parts[2]);
                    double y2 = Double.parseDouble(parts[3]);
                    String color = parts[4];
                    double size = Double.parseDouble(parts[5]);

                    gc.setStroke(Color.web(color));
                    gc.setLineWidth(size);
                    gc.strokeLine(x1, y1, x2, y2);
                }
            }
        });
    }

    private void onMousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    private void onMouseDragged(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Color color = colorPicker.getValue();
        double size = brushSizeSlider.getValue();

        // Draw locally
        gc.setStroke(color);
        gc.setLineWidth(size);
        gc.strokeLine(lastX, lastY, x, y);

        // Broadcast to session
        if (isConnected) {
            String colorHex = String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));

            String data = String.format("%.0f,%.0f,%.0f,%.0f,%s,%.1f",
                    lastX, lastY, x, y, colorHex, size);

            SessionEvent event = SessionEvent.newBuilder()
                    .setSessionId(sessionId)
                    .setUserId(userId)
                    .setEventType("DRAW")
                    .setPayload(data)
                    .build();

            asyncStub.publishEvent(event, new StreamObserver<org.jscience.server.proto.PublishAck>() {
                @Override
                public void onNext(org.jscience.server.proto.PublishAck value) {
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onCompleted() {
                }
            });
        }

        lastX = x;
        lastY = y;
    }

    private void onMouseReleased(MouseEvent e) {
        // Nothing special on release
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedwhiteboardapp.name", "Distributed Whiteboard App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedwhiteboardapp.desc", "Distributed application for Distributed Whiteboard App."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedwhiteboardapp.longdesc", "Distributed application for Distributed Whiteboard App."); }

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

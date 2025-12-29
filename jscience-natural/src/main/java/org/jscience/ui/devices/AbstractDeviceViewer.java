package org.jscience.ui.devices;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jscience.device.Device;

/**
 * Base class for device viewers.
 *
 * @param <T> the type of device
 */
public abstract class AbstractDeviceViewer<T extends Device> extends VBox {

    protected final T device;
    protected final Label nameLabel;
    protected final Label statusLabel;

    public AbstractDeviceViewer(T device) {
        this.device = device;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        this.nameLabel = new Label(device.getName());
        this.nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        this.statusLabel = new Label("Status: Connected");

        this.getChildren().addAll(nameLabel, statusLabel);

        // Initial style
        this.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #fafafa;");
    }

    public T getDevice() {
        return device;
    }

    public abstract void update();
}

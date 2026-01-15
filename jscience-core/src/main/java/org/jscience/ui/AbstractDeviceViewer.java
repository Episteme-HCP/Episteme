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

package org.jscience.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jscience.device.Device;
import java.util.List;
import java.util.ArrayList;

/**
 * Base class for device viewers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractDeviceViewer<T extends Device> extends VBox implements Viewer {

    protected final T device;
    protected final Label nameLabel;
    protected final Label statusLabel;

    public AbstractDeviceViewer(T device) {
        this.device = device;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        this.nameLabel = new Label(device.getName());
        this.nameLabel.getStyleClass().add("header-label");

        this.statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("device.status.connected", "Status: Connected"));
        this.statusLabel.getStyleClass().add("status-label");

        this.getChildren().addAll(nameLabel, statusLabel);

        // Initial style
        this.getStyleClass().add("device-viewer");
    }

    public T getDevice() {
        return device;
    }

    public abstract void update();

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return java.text.MessageFormat.format(
            org.jscience.ui.i18n.I18n.getInstance().get("device.viewer.title.fmt", "{0} Viewer"), 
            device.getName());
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.devices", "Devices");
    }

    @Override
    public String getDescription() {
        return java.text.MessageFormat.format(
            org.jscience.ui.i18n.I18n.getInstance().get("device.control.desc.fmt", "Control interface for {0}"), 
            device.getName());
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    @Override
    public void show(javafx.stage.Stage stage) {
        javafx.scene.Scene scene = new javafx.scene.Scene(this, 400, 300);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}

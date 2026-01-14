/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.mathematics.linearalgebra.MatrixViewer;
import org.jscience.ui.i18n.I18n;

public class MatrixDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("matrix.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("matrix.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        return new MatrixViewer<org.jscience.mathematics.numbers.real.Real>();
    }

    @Override
    protected String getLongDescription() {
        return I18n.getInstance().get("matrix.desc");
    }
}

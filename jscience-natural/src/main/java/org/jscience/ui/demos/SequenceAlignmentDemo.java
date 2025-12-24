/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.genetics.SequenceAlignmentViewer;

public class SequenceAlignmentDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "DNA Sequence Alignment";
    }

    @Override
    public String getDescription() {
        return "Visualization of the Needleman-Wunsch algorithm for global sequence alignment of DNA or RNA strings.";
    }

    @Override
    public void show(Stage stage) {
        SequenceAlignmentViewer.show(stage);
    }
}

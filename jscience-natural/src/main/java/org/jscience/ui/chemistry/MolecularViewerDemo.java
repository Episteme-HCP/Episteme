package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.Element;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

public class MolecularViewerDemo extends Application {

    @Override
    public void start(Stage stage) {
        // Create a simple water molecule for demo
        Molecule water = new Molecule("Water");
        // H-O-H
        // Use PeriodicTable to get elements
        Element O = PeriodicTable.OXYGEN; // Assuming these are populated
        Element H = PeriodicTable.HYDROGEN;

        // Approximate coordinates (Angstroms -> convert to Meters for standard storage)
        double scale = 1e-10;

        // O at origin
        Atom o = new Atom(O, VectorFactory.<Real>create(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Real.ZERO));

        // H1
        Atom h1 = new Atom(H, VectorFactory.<Real>create(List.of(
                Real.of(0.757 * scale),
                Real.of(0.586 * scale),
                Real.ZERO), Real.ZERO));

        // H2
        Atom h2 = new Atom(H, VectorFactory.<Real>create(List.of(
                Real.of(-0.757 * scale),
                Real.of(0.586 * scale),
                Real.ZERO), Real.ZERO));

        water.addAtom(o);
        water.addAtom(h1);
        water.addAtom(h2);

        // Bonds
        water.addBond(new Bond(o, h1));
        water.addBond(new Bond(o, h2));

        MolecularViewer viewer = new MolecularViewer(water);

        StackPane root = new StackPane();
        root.getChildren().add(viewer.getView3D(800, 600));

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Molecular Viewer Demo (Water H2O)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

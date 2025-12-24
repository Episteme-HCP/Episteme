/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Visualizer for Scientific Formulas and Mathematical Notation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class FormulaNotationViewer extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Mathematical Formula Renderer");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        VBox formulaList = new VBox(40);
        formulaList.getChildren().addAll(
                createFormulaBox("Euler's Identity", createEulerIdentity()),
                createFormulaBox("Quadratic Formula", createQuadraticFormula()),
                createFormulaBox("Schrödinger Equation", createSchrodingerEquation()),
                createFormulaBox("Newton's Law of Gravitation", createGravitationLaw()));

        root.getChildren().addAll(title, new Separator(), new ScrollPane(formulaList) {
            {
                setFitToWidth(true);
            }
        });

        Scene scene = new Scene(root, 800, 700);
        stage.setTitle("JScience - Formula Display");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFormulaBox(String name, Region formula) {
        VBox box = new VBox(10);
        Label lbl = new Label(name);
        lbl.setStyle("-fx-font-style: italic; -fx-text-fill: #666;");

        StackPane frame = new StackPane(formula);
        frame.setPadding(new Insets(20));
        frame.setStyle("-fx-border-color: #eee; -fx-border-radius: 5; -fx-background-color: #fafafa;");

        box.getChildren().addAll(lbl, frame);
        return box;
    }

    // --- Helper components ---

    private HBox createEulerIdentity() {
        HBox hbox = new HBox(0);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        Text base = new Text("e");
        base.setFont(Font.font("Serif", 36));
        Text expo = new Text("iπ");
        expo.setFont(Font.font("Serif", 20));
        VBox vExponent = new VBox(expo);
        vExponent.setPadding(new Insets(-20, 0, 0, 0));
        Text rest = new Text(" + 1 = 0");
        rest.setFont(Font.font("Serif", 36));
        hbox.getChildren().addAll(base, vExponent, rest);
        return hbox;
    }

    private HBox createQuadraticFormula() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        Text x = new Text("x = ");
        x.setFont(Font.font("Serif", 36));

        VBox fraction = new VBox(2);
        fraction.setAlignment(javafx.geometry.Pos.CENTER);

        HBox numerator = new HBox(2);
        numerator.setAlignment(javafx.geometry.Pos.CENTER);
        Text b = new Text("-b ± ");
        b.setFont(Font.font("Serif", 24));

        HBox sqrt = new HBox(0);
        sqrt.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
        Text rootSym = new Text("√");
        rootSym.setFont(Font.font("Serif", 30));
        VBox underRoot = new VBox(2);
        underRoot.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 0;");
        HBox content = new HBox(0);
        Text b2 = new Text("b");
        b2.setFont(Font.font("Serif", 24));
        Text p2 = new Text("2");
        p2.setFont(Font.font("Serif", 14));
        VBox vp = new VBox(p2);
        vp.setPadding(new Insets(-10, 0, 0, 0));
        Text ac = new Text(" - 4ac");
        ac.setFont(Font.font("Serif", 24));
        content.getChildren().addAll(b2, vp, ac);
        underRoot.getChildren().add(content);
        sqrt.getChildren().addAll(rootSym, underRoot);

        numerator.getChildren().addAll(b, sqrt);

        Region line = new Region();
        line.setPrefHeight(2);
        line.setStyle("-fx-background-color: black;");
        line.setMinWidth(150);

        Text den = new Text("2a");
        den.setFont(Font.font("Serif", 24));

        fraction.getChildren().addAll(numerator, line, den);
        hbox.getChildren().addAll(x, fraction);
        return hbox;
    }

    private HBox createSchrodingerEquation() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        Text prefix = new Text("iħ");
        prefix.setFont(Font.font("Serif", 36));

        VBox partial = new VBox(0);
        partial.setAlignment(javafx.geometry.Pos.CENTER);
        Text pTop = new Text("∂ψ");
        pTop.setFont(Font.font("Serif", 18));
        Region l = new Region();
        l.setPrefHeight(1);
        l.setStyle("-fx-background-color: black;");
        Text pBot = new Text("∂t");
        pBot.setFont(Font.font("Serif", 18));
        partial.getChildren().addAll(pTop, l, pBot);

        Text eq = new Text(" = Ĥψ");
        eq.setFont(Font.font("Serif", 36));
        hbox.getChildren().addAll(prefix, partial, eq);
        return hbox;
    }

    private HBox createGravitationLaw() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        Text f = new Text("F = G");
        f.setFont(Font.font("Serif", 36));

        VBox frac = new VBox(2);
        frac.setAlignment(javafx.geometry.Pos.CENTER);
        HBox num = new HBox(0);
        Text m1 = new Text("m");
        m1.setFont(Font.font("Serif", 24));
        Text s1 = new Text("1");
        s1.setFont(Font.font("Serif", 12));
        Text m2 = new Text("m");
        m2.setFont(Font.font("Serif", 24));
        Text s2 = new Text("2");
        s2.setFont(Font.font("Serif", 12));
        num.getChildren().addAll(m1, new VBox(s1) {
            {
                setPadding(new Insets(10, 0, 0, 0));
            }
        }, m2, new VBox(s2) {
            {
                setPadding(new Insets(10, 0, 0, 0));
            }
        });

        Region line = new Region();
        line.setPrefHeight(2);
        line.setStyle("-fx-background-color: black;");
        line.setMinWidth(60);

        HBox den = new HBox(0);
        Text r = new Text("r");
        r.setFont(Font.font("Serif", 24));
        Text p2 = new Text("2");
        p2.setFont(Font.font("Serif", 14));
        den.getChildren().addAll(r, new VBox(p2) {
            {
                setPadding(new Insets(-10, 0, 0, 0));
            }
        });

        frac.getChildren().addAll(num, line, den);
        hbox.getChildren().addAll(f, frac);
        return hbox;
    }

    public static void show(Stage stage) {
        new FormulaNotationViewer().start(stage);
    }
}

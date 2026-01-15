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

package org.jscience.ui.viewers.mathematics.symbolic;

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.util.List;
import java.util.ArrayList;

public class FormulaNotationViewer extends AbstractViewer {

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    public FormulaNotationViewer() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        Label title = new Label(
                org.jscience.ui.i18n.I18n.getInstance().get("formula.title", "Mathematical Formula Renderer"));
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        VBox formulaList = new VBox(40);
        org.jscience.ui.i18n.I18n i18n = org.jscience.ui.i18n.I18n.getInstance();
        formulaList.getChildren().addAll(
                createFormulaBox(i18n.get("formula.euler", "Euler's Identity"), createEulerIdentity()),
                createFormulaBox(i18n.get("formula.quadratic", "Quadratic Formula"), createQuadraticFormula()),
                createFormulaBox(i18n.get("formula.schrodinger", "Schr\u00F6dinger Equation"),
                        createSchrodingerEquation()),
                createFormulaBox(i18n.get("formula.gravitation", "Newton's Law of Gravitation"),
                        createGravitationLaw()));

        content.getChildren().addAll(title, new Separator(), new ScrollPane(formulaList) {
            {
                setFitToWidth(true);
            }
        });

        getChildren().add(content);
    }

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.formula");
    }

    private VBox createFormulaBox(String name, Region formula) {
        VBox box = new VBox(10);
        Label lbl = new Label(name);
        lbl.setStyle("-fx-font-style: italic; -fx-text-fill: black;");

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
        Text expo = new Text("i\u03C0");
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
        Text b = new Text("-b \u00B1 ");
        b.setFont(Font.font("Serif", 24));

        HBox sqrt = new HBox(0);
        sqrt.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
        Text rootSym = new Text("\u221A");
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
        Text prefix = new Text("i\u210F");
        prefix.setFont(Font.font("Serif", 36));

        VBox partial = new VBox(0);
        partial.setAlignment(javafx.geometry.Pos.CENTER);
        Text pTop = new Text("\u2202\u03A8");
        pTop.setFont(Font.font("Serif", 18));
        Region l = new Region();
        l.setPrefHeight(1);
        l.setStyle("-fx-background-color: black;");
        Text pBot = new Text("\u2202t");
        pBot.setFont(Font.font("Serif", 18));
        partial.getChildren().addAll(pTop, l, pBot);

        Text eq = new Text(" = \u0124\u03A8");
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
                setPadding(new Insets(-10, 0, 0, 0)); // Superscript (negative top padding)
            }
        });

        frac.getChildren().addAll(num, line, den);
        hbox.getChildren().addAll(f, frac);
        return hbox;
    }


    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("FormulaNotationViewer.desc", "FormulaNotationViewer description");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}

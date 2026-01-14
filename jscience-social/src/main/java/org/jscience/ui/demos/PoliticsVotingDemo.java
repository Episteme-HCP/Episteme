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

package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import org.jscience.ui.i18n.SocialI18n;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
// import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.jscience.ui.AbstractDemo;

public class PoliticsVotingDemo extends AbstractDemo {

    private enum VotingSystem {
        FPTP("pol.voting.sys.fptp", "pol.voting.desc.fptp"),
        PR("pol.voting.sys.pr", "pol.voting.desc.pr"),
        APPROVAL("pol.voting.sys.approval", "pol.voting.desc.approval"),
        BORDA("pol.voting.sys.borda", "pol.voting.desc.borda");

        final String nameKey;
        final String descKey;

        VotingSystem(String nameKey, String descKey) {
            this.nameKey = nameKey;
            this.descKey = descKey;
        }

        @Override
        public String toString() {
            return SocialI18n.getInstance().get(nameKey);
        }
    }

    private Label descLabel;
    private BarChart<String, Number> chart;

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.politics", "Politics");
    }

    @Override
    public String getName() {
        return SocialI18n.getInstance().get("PoliticsVoting.title");
    }

    @Override
    public String getDescription() {
        return SocialI18n.getInstance().get("PoliticsVoting.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
         // User requested FunctionExplorer2DViewer for PoliticsVotingDemo.
         org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer v = new org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer();
         this.viewer = v;
         return v;
    }
    
    @Override
    protected String getLongDescription() {
        return getDescription();
    }
}

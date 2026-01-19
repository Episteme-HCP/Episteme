package org.jscience.ui.viewers.linguistics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.chart.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.linguistics.LinguisticData;

/**
 * Visualizes linguistic structures: syntax trees, Zipf distributions, and sentiment trends.
 */
public final class LinguisticMetricViewer extends AbstractViewer {

    private final TabPane tabs = new TabPane();
    private final Canvas treeCanvas = new Canvas(800, 400);
    private final LineChart<Number, Number> sentimentChart;
    private final BarChart<String, Number> zipfChart;

    public LinguisticMetricViewer() {
        // Tab 1: Syntax Tree
        Tab treeTab = new Tab(I18n.getInstance().get("viewer.linguistic.syntax", "Syntax Tree"), new Pane(treeCanvas));
        treeTab.setClosable(false);
        treeCanvas.widthProperty().bind(((Pane)treeTab.getContent()).widthProperty());
        treeCanvas.heightProperty().bind(((Pane)treeTab.getContent()).heightProperty());

        // Tab 2: Sentiment Timeline
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis(-1.0, 1.0, 0.1);
        sentimentChart = new LineChart<>(xAxis, yAxis);
        sentimentChart.setTitle(I18n.getInstance().get("viewer.linguistic.sentiment", "Sentiment Intensity"));
        Tab sentimentTab = new Tab("Sentiment", sentimentChart);
        sentimentTab.setClosable(false);

        // Tab 3: Zipf Distribution
        CategoryAxis zipfX = new CategoryAxis();
        NumberAxis zipfY = new NumberAxis();
        zipfChart = new BarChart<>(zipfX, zipfY);
        zipfChart.setTitle(I18n.getInstance().get("viewer.linguistic.zipf", "Word Frequency (Zipf)"));
        Tab zipfTab = new Tab("Zipf Law", zipfChart);
        zipfTab.setClosable(false);

        tabs.getTabs().addAll(treeTab, sentimentTab, zipfTab);
        setCenter(tabs);
        getStyleClass().add("linguistic-metric-viewer");
    }

    public void setData(LinguisticData data) {
        // Update Sentiment
        sentimentChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Tone");
        for (var p : data.getSentimentTimeline()) {
            series.getData().add(new XYChart.Data<>(p.time(), p.score().doubleValue()));
        }
        sentimentChart.getData().add(series);

        // Update Zipf
        zipfChart.getData().clear();
        XYChart.Series<String, Number> zipfSeries = new XYChart.Series<>();
        data.getWordFrequencies().forEach((word, count) -> {
            zipfSeries.getData().add(new XYChart.Data<>(word, count));
        });
        zipfChart.getData().add(zipfSeries);

        // Draw Tree (Mock representation in Canvas)
        drawTree(data.getRootNode());
    }

    private void drawTree(LinguisticData.SyntaxNode root) {
        if (root == null) return;
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());
        gc.setStroke(Color.DODGERBLUE);
        gc.setFill(Color.WHITE);
        drawNode(gc, root, treeCanvas.getWidth() / 2, 50, treeCanvas.getWidth() / 3);
    }

    private void drawNode(GraphicsContext gc, LinguisticData.SyntaxNode node, double x, double y, double spread) {
        gc.strokeOval(x - 20, y - 15, 40, 30);
        gc.fillText(node.label(), x - 10, y + 5);
        if (node.word() != null) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillText(node.word(), x - 10, y + 25);
            gc.setFill(Color.WHITE);
        }
        
        int childCount = node.children().size();
        for (int i = 0; i < childCount; i++) {
            double cx = x - spread / 2 + (spread / (childCount + 1)) * (i + 1);
            double cy = y + 80;
            gc.strokeLine(x, y + 15, cx, cy - 15);
            drawNode(gc, node.children().get(i), cx, cy, spread / 2);
        }
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.social", "Social Sciences"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.linguistic.name", "Linguistic Metric Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.linguistic.desc", "Visualizes syntax, sentiment and language statistics."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("viewer.linguistic.longdesc", "Deep language analysis tool. Combines syntax tree visualization with quantitative metrics like sentiment polarity and word distribution Zipf analysis."); 
    }
}

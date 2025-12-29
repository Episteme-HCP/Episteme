package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LinguisticsWordFreqDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.linguistics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("LinguisticsWordFreq.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("LinguisticsWordFreq.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        TextArea input = new TextArea(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.input.placeholder"));
        input.setWrapText(true);
        input.setPrefHeight(200);
        input.setText(
                "The study of linguistics explores the structure and meaning of language. " +
                        "Language is a complex system of communication that involves phonetics, syntax, semantics, and pragmatics. "
                        +
                        "Phonetics studies the sounds of speech, while syntax governs the structure of sentences. " +
                        "Semantics concerns the meaning of words and sentences, and pragmatics looks at how context influences interpretation. "
                        +
                        "Computational linguistics applies computer science to the analysis of language, enabling technologies like machine translation, "
                        +
                        "speech recognition, and natural language processing. " +
                        "The frequency of words follows Zipf's law, which states that the frequency of any word is inversely proportional to its rank in the frequency table. "
                        +
                        "For example, the most common word will occur approximately twice as often as the second most common word, three times as often as the third, and so on. "
                        +
                        "This distribution is found in many natural languages and even in constructed languages. " +
                        "Understanding word frequency is crucial for cryptography, search engines, and data compression. "
                        +
                        "In this demo, we analyze the frequency of words in this text to see if common words like 'the', 'of', and 'and' dominate the distribution.");

        Button analyzeBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.btn.analyze"));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.axis.word"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.axis.freq"));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.chart.title"));
        barChart.setAnimated(false); // Fix rendering glitch at startup

        Runnable analyze = () -> {
            String text = input.getText().toLowerCase().replaceAll("[^a-z ]", "");
            Map<String, Long> counts = Arrays.stream(text.split("\\s+"))
                    .filter(w -> !w.isEmpty())
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(org.jscience.ui.i18n.SocialI18n.getInstance().get("ling.freq.series.counts"));

            counts.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                    .limit(10) // Top 10
                    .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

            barChart.getData().clear();
            barChart.getData().add(series);
        };

        analyzeBtn.setOnAction(e -> analyze.run());

        VBox top = new VBox(10, input, analyzeBtn);
        top.setStyle("-fx-padding: 10;");

        root.setTop(top);
        root.setCenter(barChart);

        Scene scene = new Scene(root, 800, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();

        // Run initial analysis
        analyze.run();
    }
}

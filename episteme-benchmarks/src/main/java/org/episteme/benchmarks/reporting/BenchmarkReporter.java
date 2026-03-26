package org.episteme.benchmarks.reporting;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for generating performance and correctness reports.
 */
public class BenchmarkReporter {

    private final String title;
    private final java.util.List<BenchmarkResult> results = new ArrayList<>();
    private final Map<String, String> sections = new LinkedHashMap<>();

    public BenchmarkReporter(String title) {
        this.title = title;
    }

    public void addSection(String name, String content) {
        sections.put(name, content);
    }

    public void addResult(BenchmarkResult result) {
        results.add(result);
    }

    public void exportMarkdown(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("# ").append(title).append("\n\n");
            
            sections.forEach((name, content) -> {
                sb.append("## ").append(name).append("\n");
                sb.append(content).append("\n\n");
            });

            if (!results.isEmpty()) {
                sb.append("## Performance Benchmarking Summary\n\n");
                java.util.List<String> keys = new ArrayList<>();
                results.forEach(r -> r.extraMetrics().keySet().forEach(k -> {
                    if (!keys.contains(k)) keys.add(k);
                }));

                sb.append("| Provider | Domain | Status |");
                for (String k : keys) sb.append(" ").append(k).append(" |");
                sb.append("\n|");
                for (int i = 0; i < keys.size() + 3; i++) sb.append(" --- |");
                sb.append("\n");

                for (BenchmarkResult r : results) {
                    sb.append("| ").append(r.provider()).append(" | ")
                      .append(r.domain()).append(" | ")
                      .append(r.status()).append(" |");
                    for (String k : keys) {
                        Object val = r.extraMetrics().get(k);
                        sb.append(" ").append(val != null ? val : "-").append(" |");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }

            Files.writeString(Paths.get(path), sb.toString());
            System.out.println("[INFO] Report exported to: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export report: " + e.getMessage());
        }
    }

    public void generateReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseDir = "docs/benchmark-results";
        new java.io.File(baseDir).mkdirs();
        
        String cleanTitle = title.replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
        String baseName = baseDir + "/" + cleanTitle + "_" + timestamp;
        
        System.out.println("[INFO] Generating standardized reports to: " + baseName);
        exportJson(baseName + ".json");
        generateReport(baseName + ".pdf");
    }

    public void exportJson(String path) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"context\": {\n");
            json.append(String.format("    \"title\": \"%s\",\n", escape(title)));
            json.append(String.format("    \"timestamp\": \"%s\"\n", java.time.Instant.now().toString()));
            json.append("  },\n");
            json.append("  \"runs\": [\n");
            
            for (int i = 0; i < results.size(); i++) {
                BenchmarkResult r = results.get(i);
                json.append("    {");
                json.append(String.format("\"name\":\"%s\",", escape(r.benchmarkName())));
                json.append(String.format("\"provider\":\"%s\",", escape(r.provider())));
                json.append(String.format("\"domain\":\"%s\",", escape(r.domain())));
                json.append(String.format("\"status\":\"%s\",", escape(r.status())));
                
                json.append("\"metrics\":{");
                if (r.extraMetrics() != null) {
                    java.util.List<String> keys = new ArrayList<>(r.extraMetrics().keySet());
                    for (int j = 0; j < keys.size(); j++) {
                        String k = keys.get(j);
                        Object v = r.extraMetrics().get(k);
                        json.append(String.format("\"%s\":%s", escape(k), v instanceof Number ? v : "\"" + escape(String.valueOf(v)) + "\""));
                        if (j < keys.size() - 1) json.append(",");
                    }
                }
                json.append("}");
                json.append("}");
                if (i < results.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ]\n");
            json.append("}");

            try (FileWriter fw = new FileWriter(path)) {
                fw.write(json.toString());
            }
            System.out.println("[INFO] JSON exported to: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export JSON: " + e.getMessage());
        }
    }

    private String escape(String s) {
        if (s == null) return "unknown";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public void generateReport(String pdfPath) {
        System.out.println("[INFO] Generating PDF from " + results.size() + " results to " + pdfPath);
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(0, 51, 102));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(0, 102, 204));

            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingBefore(10);
            document.add(titlePara);

            Paragraph subtitle = new Paragraph("Precision Performance Analytics | Generated: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            Map<String, java.util.List<BenchmarkResult>> grouped = results.stream()
                .collect(Collectors.groupingBy(BenchmarkResult::domain));

            java.util.List<String> sortedDomains = grouped.keySet().stream().sorted().collect(Collectors.toList());

            for (String domain : sortedDomains) {
                java.util.List<BenchmarkResult> domainResults = grouped.get(domain);
                Set<String> categories = new LinkedHashSet<>();
                for (BenchmarkResult r : domainResults) {
                    for (String key : r.extraMetrics().keySet()) {
                        if (key.contains(":")) categories.add(key.split(":")[0]);
                    }
                }

                if (categories.isEmpty()) {
                    addDomainSection(document, writer, sectionFont, domain, domainResults, null);
                } else {
                    Paragraph domainHeader = new Paragraph(domain, sectionFont);
                    domainHeader.setAlignment(Element.ALIGN_CENTER);
                    domainHeader.setSpacingBefore(30);
                    document.add(domainHeader);

                    for (String category : categories) {
                        addDomainSection(document, writer, sectionFont, category, domainResults, category);
                    }
                }
            }
            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (document.isOpen()) document.close();
        }
    }

    private static void addDomainSection(Document document, PdfWriter writer, Font sectionFont, String title, java.util.List<BenchmarkResult> results, String categoryFilter) throws DocumentException {
        Paragraph header = new Paragraph(title, sectionFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingBefore(20);
        document.add(header);
        document.add(new Paragraph(" "));

        JFreeChart chart = createChart(title, results, categoryFilter);
        java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(750, 400);
        try {
            Image pdfImage = Image.getInstance(writer, bufferedImage, 1.0f);
            pdfImage.setAlignment(Element.ALIGN_CENTER);
            pdfImage.scaleToFit(700, 400);
            document.add(pdfImage);
        } catch (IOException e) {
            throw new DocumentException(e);
        }
        document.add(new Paragraph(" "));
    }

    private static JFreeChart createChart(String title, java.util.List<BenchmarkResult> results, String categoryFilter) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (categoryFilter == null) {
            results.sort(Comparator.comparingDouble(BenchmarkResult::operationsPerSecond).reversed());
            for (BenchmarkResult r : results) {
                double val = r.operationsPerSecond();
                if (!"SUCCESS".equals(r.status())) val = 0.01;
                dataset.addValue(val, "Throughput", r.provider());
            }
        } else {
            for (BenchmarkResult r : results) {
                for (Map.Entry<String, Object> entry : r.extraMetrics().entrySet()) {
                    if (entry.getKey().startsWith(categoryFilter + ":")) {
                        String op = entry.getKey().substring(categoryFilter.length() + 1);
                        double latency = (entry.getValue() instanceof Number) ? ((Number)entry.getValue()).doubleValue() : -1.0;
                        if (latency > 0) dataset.addValue(latency, r.provider(), op);
                    }
                }
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title + (categoryFilter == null ? " | Scaling Performance" : " | Performance Latency (ms)"),
                null,
                categoryFilter == null ? "Throughput (Ops/sec)" : "Latency (ms) - Lower is Better",
                dataset,
                PlotOrientation.HORIZONTAL,
                categoryFilter != null,
                true, false);

        chart.setBackgroundPaint(java.awt.Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 86, 179)); 
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.10);

        return chart;
    }
}

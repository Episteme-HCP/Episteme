package org.episteme.benchmarks.reporting;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility for generating performance and compliance reports.
 * Supports multiple report types: ALL, FAST, NORMAL, EXACT.
 */
public class BenchmarkReporter {

    public enum ReportType { ALL, FAST, NORMAL, EXACT }

    private final java.util.List<BenchmarkResult> results = new java.util.ArrayList<>();
    private final Map<String, String> metadata = new LinkedHashMap<>();

    public BenchmarkReporter(String title) {
        this.reportTitle = title;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public void addResult(BenchmarkResult result) {
        results.add(result);
    }

    public java.util.List<BenchmarkResult> getResults() {
        return results;
    }

    public void generateReport(String customName, ReportType type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path rootPath = getProjectRoot();
        String baseDir = rootPath.resolve("docs/benchmark-results").toString();
        new java.io.File(baseDir).mkdirs();
        
        String reportName = (customName != null) ? customName + "_" + type.name() + "_" + timestamp : "benchmark_result_" + type.name() + "_" + timestamp;
        String baseName = baseDir + "/" + reportName;
        
        System.out.println("[INFO] Generating " + type.name() + " audit reports to: " + baseName);
        
        generateJsonReport(baseName + ".json");
        generatePdfReport(baseName + ".pdf", type);
    }

    public void generateReport(String customName) {
        generateReport(customName, ReportType.ALL);
    }

    private void generateJsonReport(String path) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"context\": {\n");
            json.append(String.format("    \"title\": \"%s\",\n", escape(reportTitle)));
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
                    java.util.List<String> keys = new java.util.ArrayList<>(r.extraMetrics().keySet());
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

            Files.writeString(Paths.get(path), json.toString());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export JSON: " + e.getMessage());
        }
    }

    private void generatePdfReport(String filePath, ReportType type) {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC);

            // --- TITLE PAGE ---
            Paragraph titlePara = new Paragraph(reportTitle + " (" + type.name() + ")", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(50);
            document.add(titlePara);

            document.add(new Paragraph("Audit Context:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            metadata.forEach((k, v) -> {
                try {
                    document.add(new Paragraph(k + ": " + v));
                } catch (DocumentException e) {}
            });

            document.add(new Paragraph("\n"));

            // --- SUMMARY TABLE ---
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Provider");
            table.addCell("Operation");
            table.addCell("Domain");
            table.addCell("Result Summary");

            for (BenchmarkResult r : results) {
                table.addCell(r.provider());
                table.addCell(r.benchmarkName());
                table.addCell(r.domain());
                
                if (type == ReportType.ALL) {
                    table.addCell("FAST/NORMAL/EXACT results collected");
                } else {
                    Object val = r.extraMetrics().get(type.name() + ":latency");
                    table.addCell(val != null ? String.format("%.3f ms", ((Number)val).doubleValue()) : "N/A");
                }
            }
            document.add(table);

            // --- OPERATION COMPARISON PAGES ---
            java.util.Set<String> allOps = new java.util.LinkedHashSet<>();
            for (BenchmarkResult r : results) allOps.add(r.benchmarkName());

            for (String op : allOps) {
                document.newPage();
                Paragraph opHeader = new Paragraph("Operation Comparison: " + op + " (" + type.name() + ")", sectionFont);
                opHeader.setAlignment(Element.ALIGN_CENTER);
                opHeader.setSpacingAfter(30);
                document.add(opHeader);

                JFreeChart chart = createComparisonChart(op, type);
                if (chart != null) {
                    addChartToPdf(document, writer, chart);
                }

                Paragraph opFooter = new Paragraph("Episteme Multimodal Audit | " + op, footerFont);
                opFooter.setAlignment(Element.ALIGN_RIGHT);
                document.add(opFooter);
            }

            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (document.isOpen()) document.close();
        }
    }

    private JFreeChart createComparisonChart(String operation, ReportType type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        java.util.List<BenchmarkResult> opResults = results.stream()
                .filter(r -> r.benchmarkName().equals(operation))
                .toList();

        boolean hasData = false;
        String[] modes = (type == ReportType.ALL) ? new String[]{"FAST", "NORMAL", "EXACT"} : new String[]{type.name()};

        for (BenchmarkResult r : opResults) {
            for (String mode : modes) {
                Object valObj = r.extraMetrics().get(mode + ":throughput");
                if (valObj instanceof Number n) {
                    double val = n.doubleValue();
                    if (val > 0) {
                        dataset.addValue(val, mode, r.provider());
                        hasData = true;
                    }
                }
            }
        }

        if (!hasData) return null;

        JFreeChart chart = ChartFactory.createBarChart(
                operation + " - " + type.name() + " Performance Comparison",
                "Provider",
                "Throughput (Ops/sec)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        chart.setBackgroundPaint(java.awt.Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        
        if (type == ReportType.ALL) {
            renderer.setSeriesPaint(0, new Color(40, 167, 69));  // FAST: Green
            renderer.setSeriesPaint(1, new Color(0, 123, 255));   // NORMAL: Blue
            renderer.setSeriesPaint(2, new Color(220, 53, 69));   // EXACT: Red
        } else {
            Color c = switch(type) {
                case FAST -> new Color(40, 167, 69);
                case NORMAL -> new Color(0, 123, 255);
                case EXACT -> new Color(220, 53, 69);
                default -> Color.GRAY;
            };
            renderer.setSeriesPaint(0, c);
        }

        return chart;
    }

    private void addChartToPdf(Document document, PdfWriter writer, JFreeChart chart) throws DocumentException {
        java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(750, 240);
        try {
            Image pdfImage = Image.getInstance(writer, bufferedImage, 1.0f);
            pdfImage.setAlignment(Element.ALIGN_CENTER);
            pdfImage.scaleToFit(700, 240);
            document.add(pdfImage);
        } catch (IOException e) {
            throw new DocumentException(e);
        }
    }

    public void exportToRoot(String relativePath) {
        Path rootPath = getProjectRoot();
        String absolutePath = rootPath.resolve(relativePath).toString();
        exportMarkdown(absolutePath);
    }

    private void exportMarkdown(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("# ").append(reportTitle).append("\n\n");
            
            if (!results.isEmpty()) {
                sb.append("## Performance Benchmarking Summary\n\n");
                sb.append("| Provider | Operation | Domain | FAST (ms) | NORMAL (ms) | EXACT (ms) |\n");
                sb.append("| --- | --- | --- | --- | --- | --- |\n");

                for (BenchmarkResult r : results) {
                    sb.append("| ").append(r.provider()).append(" | ")
                      .append(r.benchmarkName()).append(" | ")
                      .append(r.domain()).append(" | ")
                      .append(formatMetric(r.extraMetrics().get("FAST:latency"))).append(" | ")
                      .append(formatMetric(r.extraMetrics().get("NORMAL:latency"))).append(" | ")
                      .append(formatMetric(r.extraMetrics().get("EXACT:latency"))).append(" |\n");
                }
            }

            Files.writeString(Paths.get(path), sb.toString());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export Markdown: " + e.getMessage());
        }
    }

    private String formatMetric(Object val) {
        if (val instanceof Number n) return String.format("%.3f", n.doubleValue());
        return "-";
    }

    private Path getProjectRoot() {
        String userDir = System.getProperty("user.dir");
        Path path = Paths.get(userDir);
        if (path.endsWith("episteme-benchmarks")) {
            return path.getParent();
        }
        return path;
    }

    private String escape(String s) {
        if (s == null) return "unknown";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

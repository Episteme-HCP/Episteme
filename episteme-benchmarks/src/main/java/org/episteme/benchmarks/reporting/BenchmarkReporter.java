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
    private final Map<String, String> metadata = new LinkedHashMap<>();
    private String comments = "";
    private String footer = "";

    public BenchmarkReporter(String title) {
        this.title = title;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void addSection(String name, String content) {
        sections.put(name, content);
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

    public void exportMarkdown(String path) {
        try {
            java.nio.file.Path p = Paths.get(path);
            if (p.getParent() != null) {
                java.nio.file.Files.createDirectories(p.getParent());
            }
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
                
                // Sort keys: RB: matrix, RB: transcendental, C: matrix, C: transcendental
                keys.sort((a, b) -> {
                    String[] ptsA = a.split(":");
                    String[] ptsB = b.split(":");
                    String prefA = ptsA[0]; String opA = ptsA.length > 1 ? ptsA[1] : "";
                    String prefB = ptsB[0]; String opB = ptsB.length > 1 ? ptsB[1] : "";

                    int domainA = prefA.equals("R:") ? 0 : prefA.equals("RB:") ? 1 : prefA.equals("C:") ? 2 : 3;
                    int domainB = prefB.equals("R:") ? 0 : prefB.equals("RB:") ? 1 : prefB.equals("C:") ? 2 : 3;
                    if (domainA != domainB) return Integer.compare(domainA, domainB);

                    java.util.Set<String> matrixOps = java.util.Set.of(
                        "Add", "Sub", "Scale", "Mul", "MatVec", "Trans", "Inv", "Det", "Solve",
                        "Dot", "Norm", "LU", "QR", "SVD", "Chol", "Eigen", "BiCGSTAB", "ConjGrad", "GMRES"
                    );

                    boolean isMatA = matrixOps.contains(opA);
                    boolean isMatB = matrixOps.contains(opB);
                    if (isMatA != isMatB) return isMatA ? -1 : 1;

                    return opA.compareTo(opB);
                });

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

            if (footer != null && !footer.isEmpty()) {
                sb.append("\n").append(footer).append("\n");
            }

            java.nio.file.Files.createDirectories(p.getParent());
            Files.writeString(p, sb.toString());
            System.out.println("[INFO] Report exported to: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export report: " + e.getMessage());
        }
    }

    public void generateReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        
        java.nio.file.Path rootPath = getProjectRoot();
        String baseDir = rootPath.resolve("docs/benchmark-results").toString();
        new java.io.File(baseDir).mkdirs();
        
        String baseName = baseDir + "/benchmark_result_high_precision_" + timestamp;
        
        System.out.println("[INFO] Generating standardized high-precision audit reports to: " + baseName);
        exportJson(baseName + ".json");
        generateReport(baseName + ".pdf");
    }

    public void exportToRoot(String relativePath) {
        java.nio.file.Path rootPath = getProjectRoot();
        String absolutePath = rootPath.resolve(relativePath).toString();
        exportMarkdown(absolutePath);
    }

    private java.nio.file.Path getProjectRoot() {
        String userDir = System.getProperty("user.dir");
        java.nio.file.Path path = Paths.get(userDir);
        if (path.endsWith("episteme-benchmarks")) {
            return path.getParent();
        }
        return path;
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
        System.out.println("[INFO] Generating High-Precision Audit PDF (" + results.size() + " providers) to " + pdfPath);
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, new Color(0, 51, 102));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(0, 102, 204));
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.LIGHT_GRAY);

            // --- SUMMARY PAGE ---
            Paragraph titlePara = new Paragraph(title + " High-Precision Audit", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingBefore(100);
            document.add(titlePara);

            Paragraph subtitle = new Paragraph("Comprehensive Mathematical Compliance & Performance Report\nGenerated: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(50);
            document.add(subtitle);

            if (comments != null && !comments.isEmpty()) {
                Font commentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
                Paragraph commentPara = new Paragraph("Executive Summary:\n" + comments, commentFont);
                commentPara.setSpacingBefore(30);
                document.add(commentPara);
            }

            // Environment metadata table
            if (!metadata.isEmpty()) {
                document.add(new Paragraph(" "));
                Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(80, 80, 80));
                Paragraph metaHeader = new Paragraph("Test Environment", sectionFont);
                metaHeader.setSpacingBefore(10);
                document.add(metaHeader);
                com.lowagie.text.pdf.PdfPTable metaTable = new com.lowagie.text.pdf.PdfPTable(2);
                metaTable.setWidthPercentage(60);
                metaTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                for (Map.Entry<String, String> e : metadata.entrySet()) {
                    metaTable.addCell(new Paragraph(e.getKey(), metaFont));
                    metaTable.addCell(new Paragraph(e.getValue(), metaFont));
                }
                document.add(metaTable);
            }

            // Summary Table
            document.add(new Paragraph(" "));
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(3);
            table.setWidthPercentage(80);
            table.addCell("Provider");
            table.addCell("Domain");
            table.addCell("Status");
            for (BenchmarkResult r : results) {
                table.addCell(r.provider());
                table.addCell(r.domain());
                table.addCell(r.status());
            }
            document.add(table);

            // --- INDIVIDUAL OPERATION PAGES ---
            java.util.Set<String> allOps = new java.util.LinkedHashSet<>();
            for (BenchmarkResult r : results) {
                for (String key : r.extraMetrics().keySet()) {
                    if (key.endsWith(":latency")) {
                        allOps.add(key.replace(":latency", ""));
                    } else if (!key.contains(":")) {
                        allOps.add(key);
                    }
                }
            }

            for (String op : allOps) {
                document.newPage();
                Paragraph opHeader = new Paragraph("Operation Audit: " + op, sectionFont);
                opHeader.setAlignment(Element.ALIGN_LEFT);
                opHeader.setSpacingAfter(20);
                document.add(opHeader);

                // Throughput Chart
                JFreeChart throughputChart = createOperationChart(op, "Throughput", "Ops/sec (Higher is Better)", results, false);
                addChartToPdf(document, writer, throughputChart);

                document.add(new Paragraph(" "));

                // Latency Chart
                JFreeChart latencyChart = createOperationChart(op, "Latency", "Latency (ms) (Lower is Better)", results, true);
                addChartToPdf(document, writer, latencyChart);
                
                Paragraph opFooter = new Paragraph("Episteme High-Precision Audit Service | " + op, footerFont);
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

    private JFreeChart createOperationChart(String op, String metricType, String yLabel, java.util.List<BenchmarkResult> results, boolean isLatency) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (BenchmarkResult r : results) {
            String key = isLatency ? op + ":latency" : op + ":throughput";
            Object valObj = r.extraMetrics().get(key);
            if (valObj == null && !isLatency) {
                 // Fallback to legacy key or manual calc if throughput not present
                 valObj = r.extraMetrics().get(op);
                 if (valObj instanceof Number n && n.doubleValue() > 0 && !isLatency) {
                     // If it's legacy duration, convert to ops/sec
                     // but wait, duration is what we have. Let's stick to the new format.
                 }
            }
            
            double val = (valObj instanceof Number n) ? n.doubleValue() : 0.0;
            if (val > 0) {
                dataset.addValue(val, metricType, r.provider());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                op + " | " + metricType,
                null,
                yLabel,
                dataset,
                PlotOrientation.HORIZONTAL,
                false, true, false);

        chart.setBackgroundPaint(java.awt.Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, isLatency ? new Color(220, 53, 69) : new Color(40, 167, 69)); 
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.15);

        return chart;
    }

}

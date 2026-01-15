# Audit of Hardcoded Strings

Generated from scan of *Viewer.java, *App.java, *Loader.java, *Reader.java, *Writer.java.
Excluded: SuppressWarnings, Logger calls, Comments.

## DistributedCrisprApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\biology\crispr\DistributedCrisprApp.java`

| Line | Content |
| --- | --- |
| 61 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 66 | `root.setStyle("-fx-background-color: #1a1a2e;");` |
| 72 | `primaryStage.setTitle("JScience - Distributed CRISPR Designer");` |
| 79 | `Label title = new Label("🧬 Distributed CRISPR Scanner");` |
| 80 | `title.setStyle("-fx-font-size: 24; -fx-text-fill: #4ecca3; -fx-font-weight: bold;");` |
| 83 | `"ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");` |
| 86 | `"-fx-control-inner-background: #16213e; -fx-text-fill: #e94560; -fx-font-family: 'Consolas';");` |
| 88 | `Button scanBtn = new Button("🚀 Start Distributed Scan");` |
| 89 | `scanBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");` |
| 92 | `Button exportBtn = new Button("💾 Export to FASTA");` |
| 93 | `exportBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");` |
| 96 | `Button loadBtn = new Button("📂 Load FASTA");` |
| 97 | `loadBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");` |
| 107 | `fileChooser.setTitle("Save FASTA Export");` |
| 108 | `fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FASTA Files", "*.fasta", "*.fa"));` |
| 116 | `sequences.add(new FASTAReader.Sequence("CrISPR_Target_" + i + "_Pos_" + target.position(),` |
| 121 | `new Alert(Alert.AlertType.INFORMATION, "Exported " + sequences.size() + " targets to " + file.getName())` |
| 124 | `new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();` |
| 130 | `File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load FASTA", "FASTA Files", "*.fasta",` |
| 131 | `"*.fa");` |
| 140 | `statusLabel.setText("Loaded " + seqs.size() + " sequences from " + file.getName());` |
| 142 | `new Alert(Alert.AlertType.ERROR, "Load failed: " + e.getMessage()).show();` |
| 152 | `resultsTable.setStyle("-fx-background-color: #0f3460;");` |
| 154 | `TableColumn<CrisprTask.Target, Integer> colPos = new TableColumn<>("Position");` |
| 155 | `colPos.setCellValueFactory(new PropertyValueFactory<>("position"));` |
| 157 | `TableColumn<CrisprTask.Target, String> colSpacer = new TableColumn<>("Spacer (20bp)");` |
| 158 | `colSpacer.setCellValueFactory(new PropertyValueFactory<>("spacer"));` |
| 161 | `TableColumn<CrisprTask.Target, String> colPam = new TableColumn<>("PAM");` |
| 162 | `colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));` |
| 164 | `TableColumn<CrisprTask.Target, Double> colScore = new TableColumn<>("Efficiency Score");` |
| 165 | `colScore.setCellValueFactory(new PropertyValueFactory<>("score"));` |
| 178 | `statusLabel = new Label("Ready");` |
| 179 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 186 | `statusLabel.setText("🛰️ Submitting sequence to cluster...");` |
| 190 | `.setTaskId("CRISPR-" + System.currentTimeMillis())` |
| 198 | `Platform.runLater(() -> statusLabel.setText("⚙️ Cluster Processing: " + response.getStatus()));` |
| 204 | `Platform.runLater(() -> statusLabel.setText("❌ Grid Error: " + t.getMessage()));` |
| 212 | `statusLabel.setText("❌ Local Serialization Error");` |
| 228 | `statusLabel.setText("✅ Grid Scan Complete: Found " + targets.size() + " targets.");` |
| 231 | `Platform.runLater(() -> statusLabel.setText("❌ Deserialization failed"));` |
| 238 | `Platform.runLater(() -> statusLabel.setText("❌ Result stream error"));` |

## DistributedDnaFoldingApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\biology\dnafolding\DistributedDnaFoldingApp.java`

| Line | Content |
| --- | --- |
| 79 | `stage.setTitle("🧬 DNA Folding - Distributed JScience");` |
| 105 | `task = new DnaFoldingTask("ATGCATGCATGCATGC", 200, 300.0);` |
| 122 | `statusLabel.setText("Status: Local Performance");` |
| 130 | `fileChooser.setTitle("Save PDB Export");` |
| 131 | `fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDB Files", "*.pdb"));` |
| 135 | `Protein p = new Protein("DNA-FOLD");` |
| 136 | `Protein.Chain chain = new Protein.Chain("A");` |
| 139 | `Protein.Residue res = new Protein.Residue("NUC", resIdx++);` |
| 143 | `Atom atom = new Atom(PeriodicTable.getElement("P"), pos);` |
| 149 | `new Alert(Alert.AlertType.INFORMATION, "PDB Export successful").show();` |
| 151 | `new Alert(Alert.AlertType.ERROR, "Export failed: " + ex.getMessage()).show();` |
| 158 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 170 | `.setTaskId("dna-fold-" + stepCount)` |
| 185 | `statusLabel.setText("Status: Grid Computed ✅");` |
| 193 | `statusLabel.setText("Status: Grid Pending ⏳");` |
| 197 | `statusLabel.setText("Status: Grid Error ❌");` |
| 204 | `dos.writeUTF("DNA_FOLDING");` |
| 244 | `energyLabel.setText(String.format("Energy: %.2f kcal/mol", task.getFinalEnergy()));` |
| 260 | `CheckBox checkbox = new CheckBox("Distributed Mode");` |
| 261 | `Button exportBtn = new Button("💾 Export PDB");` |
| 264 | `pane.setStyle("-fx-background-color: rgba(30,30,50,0.8); -fx-padding: 20;");` |
| 265 | `label.setStyle("-fx-text-fill: #4ecca3; -fx-font-size: 16;");` |
| 266 | `status.setStyle("-fx-text-fill: white; -fx-font-size: 12;");` |
| 267 | `checkbox.setStyle("-fx-text-fill: white;");` |

## DistributedProteinFoldingApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\biology\proteinfolding\DistributedProteinFoldingApp.java`

| Line | Content |
| --- | --- |
| 74 | `primaryStage.setTitle("🧬 JScience - Distributed Protein Folding (HP Model)");` |
| 104 | `panel.setStyle("-fx-background-color: rgba(30, 30, 60, 0.9); -fx-background-radius: 10;");` |
| 107 | `Label titleLabel = new Label("Protein Folding Simulation");` |
| 108 | `titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #4ecca3;");` |
| 110 | `Label seqLabel = new Label("HP Sequence (H=Hydrophobic, P=Polar):");` |
| 111 | `seqLabel.setStyle("-fx-text-fill: white;");` |
| 113 | `sequenceInput = new TextArea("HPPHHHPHHPPHHHPPHPHPHHPH");` |
| 116 | `sequenceInput.setStyle("-fx-font-family: monospace;");` |
| 118 | `Button startBtn = new Button("▶ Start Folding");` |
| 119 | `startBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white;");` |
| 122 | `Button resetBtn = new Button("↻ Reset");` |
| 123 | `resetBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");` |
| 128 | `energyLabel = new Label("Energy: --");` |
| 129 | `energyLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 14;");` |
| 131 | `statusLabel = new Label("Ready");` |
| 132 | `statusLabel.setStyle("-fx-text-fill: #888;");` |
| 140 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051)` |
| 145 | `statusLabel.setText("✅ Grid Connected");` |
| 148 | `statusLabel.setText("⚠️ Local Mode");` |
| 153 | `String sequence = sequenceInput.getText().trim().toUpperCase().replaceAll("[^HP]", "");` |
| 155 | `statusLabel.setText("Invalid sequence");` |
| 160 | `statusLabel.setText("Folding...");` |
| 179 | `.setTaskId("protein-" + System.currentTimeMillis())` |
| 195 | `Platform.runLater(() -> statusLabel.setText("Task failed"));` |
| 199 | `statusLabel.setText("Grid error: " + e.getMessage());` |
| 210 | `statusLabel.setText("No result");` |
| 245 | `energyLabel.setText(String.format("Energy: %.1f \| Monomers: %d",` |
| 247 | `statusLabel.setText("✅ Folding Complete");` |
| 289 | `energyLabel.setText("Energy: --");` |
| 290 | `statusLabel.setText("Ready");` |

## DistributedMolecularDynamicsApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\chemistry\moleculardynamics\DistributedMolecularDynamicsApp.java`

| Line | Content |
| --- | --- |
| 91 | `stage.setTitle("🧪 Molecular Dynamics - Distributed JScience");` |
| 95 | `box.setMaterial(new PhongMaterial(Color.web("#ffffff", 0.1)));` |
| 138 | `stats = new Label("Initializing Grid...");` |
| 140 | `stats.setStyle("-fx-font-size: 16;");` |
| 142 | `localSimCheckBox = new CheckBox("Local Simulation");` |
| 146 | `Button exportBtn = new Button("💾 Export PDB");` |
| 149 | `loadPdbBtn = new Button("📂 Load PDB");` |
| 153 | `overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 20;");` |
| 176 | `fileChooser.setTitle("Save PDB Export");` |
| 177 | `fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDB Files", "*.pdb"));` |
| 181 | `Protein p = new Protein("MD-SIM");` |
| 182 | `Protein.Chain chain = new Protein.Chain("A");` |
| 183 | `Protein.Residue res = new Protein.Residue("UNK", 1);` |
| 189 | `Atom atom = new Atom(PeriodicTable.getElement("Ar"), pos); // Assume Argon for MD gas` |
| 196 | `new Alert(Alert.AlertType.INFORMATION, "Exported " + task.getNumAtoms() + " atoms to " + file.getName())` |
| 199 | `new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();` |
| 206 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 314 | `updateUI("Local Provider");` |
| 328 | `.setTaskId("MD-" + stepCount)` |
| 345 | `Platform.runLater(() -> stats.setText("Grid Error: " + e.getMessage()));` |
| 353 | `dos.writeUTF("MOLECULAR_DYNAMICS");` |
| 398 | `stats.setText(String.format("Mode: %s \| Energy: %.2f", mode, task.getTotalEnergy()));` |
| 412 | `File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load Protein PDB", "PDB Files", "*.pdb",` |
| 413 | `"*.ent");` |
| 423 | `String outputPath = "output.pdb"; // Placeholder` |
| 452 | `stats.setText("Loaded: " + protein.getName() + " (" + particles.size() + " atoms)");` |
| 455 | `new Alert(Alert.AlertType.ERROR, "Failed to load PDB: " + e.getMessage()).show();` |
| 462 | `case "H":` |
| 464 | `case "C":` |
| 466 | `case "N":` |
| 468 | `case "O":` |
| 470 | `case "S":` |
| 472 | `case "P":` |

## DistributedClimateSimApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\earth\climatesim\DistributedClimateSimApp.java`

| Line | Content |
| --- | --- |
| 72 | `stage.setTitle("🌍 General Circulation Model (GCM) - JScience Grid");` |
| 82 | `overlay.setStyle("-fx-background-color: rgba(30,30,30,0.85); -fx-background-radius: 10;");` |
| 84 | `Label title = new Label("Advanced GCM Dynamics");` |
| 85 | `title.setStyle("-fx-text-fill: #00e5ff; -fx-font-size: 20; -fx-font-weight: bold;");` |
| 86 | `avgTempLabel = new Label("Avg Surface Temp: -- K");` |
| 87 | `avgTempLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");` |
| 88 | `statusLabel = new Label("Status: Initializing...");` |
| 89 | `statusLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12;");` |
| 91 | `CheckBox distCheck = new CheckBox("Grid computing enabled");` |
| 93 | `distCheck.setStyle("-fx-text-fill: white;");` |
| 96 | `Button exportBtn = new Button("Export VTK");` |
| 99 | `Button exportJsonBtn = new Button("Export JSON");` |
| 102 | `Button loadJsonBtn = new Button("Load JSON");` |
| 122 | `statusLabel.setText("Mode: Local Core (Heavy Compute)");` |
| 131 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 134 | `statusLabel.setText("Status: Connected to Grid ✅");` |
| 137 | `statusLabel.setText("Status: Grid Offline (Local Fallback)");` |
| 144 | `.setTaskId("gcm-step-" + stepCount)` |
| 166 | `statusLabel.setText("Mode: Distributed GCM Grid");` |
| 175 | `statusLabel.setText("Mode: Local Fallback (Grid Delay)");` |
| 180 | `File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save VTK Export", "VTK Files", "*.vtk");` |
| 184 | `new Alert(Alert.AlertType.INFORMATION, "Export successful: " + file.getName()).show();` |
| 186 | `new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();` |
| 192 | `File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save JSON State", "JSON Files",` |
| 193 | `"*.json");` |
| 204 | `new Alert(Alert.AlertType.INFORMATION, "Export successful: " + file.getName()).show();` |
| 206 | `new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();` |
| 212 | `File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load JSON State", "JSON Files",` |
| 213 | `"*.json");` |
| 220 | `new Alert(Alert.AlertType.INFORMATION, "Loaded state from: " + file.getName()).show();` |
| 222 | `new Alert(Alert.AlertType.ERROR, "Load failed: " + e.getMessage()).show();` |
| 253 | `avgTempLabel.setText(String.format("Global Avg Surface Temp: %.2f K", totalTemp / (rows * cols)));` |

## DistributedMandelbrotApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\mathematics\mandelbrot\DistributedMandelbrotApp.java`

| Line | Content |
| --- | --- |
| 97 | `String host = getParameters().getNamed().getOrDefault("host", "localhost");` |
| 98 | `int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));` |
| 109 | `root.setStyle("-fx-background-color: #1a1a2e;");` |
| 120 | `primaryStage.setTitle("JScience Distributed Mandelbrot");` |
| 130 | `System.out.println("Server status: " + status.getActiveWorkers() + " workers available");` |
| 133 | `System.out.println("Server not available, will use local computation");` |
| 142 | `statusLabel.setText("Ready");` |
| 143 | `timeLabel.setText("Time: --");` |
| 155 | `statusLabel.setText("💻 Computing locally...");` |
| 158 | `statusLabel.setText("🌐 Submitting slices to grid...");` |
| 176 | `finish(startTime, "local");` |
| 192 | `String taskId = "mandel-" + slice + "-" + System.currentTimeMillis();` |
| 195 | `String type = (highPrecisionToggle != null && highPrecisionToggle.isSelected()) ? "MANDELBROT_REAL"` |
| 196 | `: "MANDELBROT";` |
| 258 | `finish(startTime, serverAvailable ? "distributed" : "local");` |
| 358 | `String modeLabel = mode.equals("distributed") ? "🌐" : "💻";` |
| 359 | `statusLabel.setText(String.format("%s Computation complete (%s)", modeLabel, mode));` |
| 360 | `timeLabel.setText(String.format("Time: %.2fs", elapsed / 1000.0));` |
| 367 | `header.setStyle("-fx-background-color: #16213e;");` |
| 370 | `Label title = new Label("🔬 Distributed Mandelbrot Set");` |
| 371 | `title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");` |
| 376 | `localModeToggle = new ToggleButton("💻 Local Only");` |
| 377 | `localModeToggle.setStyle("-fx-background-color: #666; -fx-text-fill: white;");` |
| 380 | `statusLabel.setText("💻 Local mode - will not use grid");` |
| 383 | `statusLabel.setText(serverAvailable ? "🌐 Grid available" : "⚠️ Grid unavailable");` |
| 387 | `highPrecisionToggle = new CheckBox("High Precision");` |
| 388 | `highPrecisionToggle.setStyle("-fx-text-fill: white;");` |
| 390 | `computeBtn = new Button("⚡ Compute on Grid");` |
| 391 | `computeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");` |
| 394 | `Button saveImgBtn = new Button("💾 Save Image");` |
| 395 | `saveImgBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e;");` |
| 398 | `Button saveCfgBtn = new Button("⚙ Save Config");` |
| 401 | `Button loadCfgBtn = new Button("📂 Load Config");` |
| 412 | `footer.setStyle("-fx-background-color: #16213e;");` |
| 414 | `statusLabel = new Label(serverAvailable ? "🌐 Grid available" : "💻 Local mode");` |
| 415 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 420 | `timeLabel = new Label("Time: --");` |
| 421 | `timeLabel.setStyle("-fx-text-fill: #888;");` |
| 437 | `File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save Image", "PNG Images", "*.png");` |
| 440 | `ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);` |
| 442 | `new Alert(Alert.AlertType.ERROR, "Failed to save image: " + e.getMessage()).show();` |
| 448 | `File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save Config", "JSON Config", "*.json");` |
| 451 | `pw.printf("{\"minRe\": %f, \"maxRe\": %f, \"minIm\": %f, \"maxIm\": %f}", minRe, maxRe, minIm, maxIm);` |
| 453 | `new Alert(Alert.AlertType.ERROR, "Failed to save config").show();` |
| 459 | `File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load Config", "JSON Config", "*.json");` |
| 461 | `try (java.util.Scanner s = new java.util.Scanner(file).useDelimiter("\\A")) {` |
| 462 | `String content = s.hasNext() ? s.next() : "";` |
| 463 | `content = content.replace("{", "").replace("}", "").replace("\"", "");` |
| 464 | `String[] parts = content.split(",");` |
| 466 | `String[] kv = p.split(":");` |
| 471 | `if (key.equals("minRe"))` |
| 473 | `if (key.equals("maxRe"))` |
| 475 | `if (key.equals("minIm"))` |
| 477 | `if (key.equals("maxIm"))` |
| 481 | `new Alert(Alert.AlertType.ERROR, "Failed to load config").show();` |

## DistributedMonteCarloPiApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\mathematics\montecarlopi\DistributedMonteCarloPiApp.java`

| Line | Content |
| --- | --- |
| 79 | `String host = getParameters().getNamed().getOrDefault("host", "localhost");` |
| 80 | `int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));` |
| 89 | `stage.setTitle("🎯 Monte Carlo π Estimation - JScience");` |
| 97 | `controls.setStyle("-fx-background-color: #1a1a2e;");` |
| 100 | `Label title = new Label("Monte Carlo π");` |
| 101 | `title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #e94560;");` |
| 103 | `piLabel = new Label("π ≈ ?");` |
| 104 | `piLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #eee;");` |
| 106 | `samplesLabel = new Label("Samples: 0");` |
| 107 | `samplesLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");` |
| 109 | `modeLabel = new Label(serverAvailable ? "🌐 Distributed Mode" : "💻 Local Mode");` |
| 110 | `modeLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #4ecca3;");` |
| 115 | `Label accuracyLabel = new Label("Accuracy: ±?");` |
| 116 | `accuracyLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");` |
| 119 | `sep.setStyle("-fx-background-color: #333;");` |
| 121 | `ToggleButton distributedBtn = new ToggleButton("🌐 Distributed");` |
| 123 | `distributedBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; " +` |
| 124 | `"-fx-font-size: 12; -fx-padding: 8 15;");` |
| 130 | `modeLabel.setText(useDistributed && serverAvailable ? "🌐 Distributed Mode" : "💻 Local Mode");` |
| 133 | `Button startBtn = new Button("▶ Start Sampling");` |
| 134 | `startBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +` |
| 135 | `"-fx-font-size: 14; -fx-padding: 10 20;");` |
| 138 | `startBtn.setText(running ? "⏸ Pause" : "▶ Resume");` |
| 144 | `Button resetBtn = new Button("↺ Reset");` |
| 145 | `resetBtn.setStyle("-fx-background-color: #333; -fx-text-fill: white; " +` |
| 146 | `"-fx-font-size: 14; -fx-padding: 10 20;");` |
| 150 | `TextArea infoArea = new TextArea("""` |
| 160 | `""");` |
| 164 | `infoArea.setStyle("-fx-control-inner-background: #16213e; -fx-text-fill: #aaa;");` |
| 171 | `root.setStyle("-fx-background-color: #0f0f1a;");` |
| 204 | `String taskId = "montecarlo-" + workerId + "-" + System.currentTimeMillis();` |
| 264 | `throw new IOException("Failed to deserialize result", e);` |
| 279 | `gc.setFill(Color.web("#16213e"));` |
| 283 | `gc.setStroke(Color.web("#e94560"));` |
| 288 | `gc.setStroke(Color.web("#333"));` |
| 299 | `piLabel.setText("π ≈ ?");` |
| 300 | `samplesLabel.setText("Samples: 0");` |
| 326 | `gc.setFill(inside ? Color.web("#4ecca3", 0.7) : Color.web("#e94560", 0.5));` |
| 338 | `piLabel.setText(String.format("π ≈ %.8f", pi));` |
| 339 | `samplesLabel.setText(String.format("Samples: %,d", total));` |
| 341 | `accuracyLabel.setText(String.format("Error: %.6f", error));` |

## DistributedFluidSimApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\physics\fluidsim\DistributedFluidSimApp.java`

| Line | Content |
| --- | --- |
| 71 | `stage.setTitle("🌊 Fluid Dynamics - Distributed LBM");` |
| 79 | `controls.setStyle("-fx-background-color: #1a1a2e;");` |
| 80 | `statusLabel = new Label("Grid Status: Checking...");` |
| 82 | `localSimCheckBox = new CheckBox("Local LBM Simulation"); // Renamed and initialized` |
| 85 | `Button loadObstacleBtn = new Button("Load Obstacle Map");` |
| 89 | `controls.getChildren().addAll(new Label("Fluid Control"), localSimCheckBox, loadObstacleBtn, statusLabel);` |
| 121 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 131 | `.setTaskId("lbm-" + stepCount)` |
| 146 | `statusLabel.setText("Mode: Distributed ✅");` |
| 154 | `statusLabel.setText("Mode: Local (Grid Sluggish)");` |
| 158 | `statusLabel.setText("Mode: Grid Offline");` |
| 165 | `dos.writeUTF("FLUID_LBM");` |
| 207 | `statusLabel.setText("Mode: Local LBM");` |
| 248 | `File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load Obstacle Map", "Images", "*.png",` |
| 249 | `"*.jpg", "*.bmp");` |
| 278 | `statusLabel.setText("Map Loaded: " + file.getName());` |
| 280 | `statusLabel.setText("Load Failed");` |

## DistributedNBodyApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\physics\nbody\DistributedNBodyApp.java`

| Line | Content |
| --- | --- |
| 91 | `String host = getParameters().getNamed().getOrDefault("host", "localhost");` |
| 92 | `int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));` |
| 106 | `root.setStyle("-fx-background-color: #0a0a1a;");` |
| 125 | `Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));` |
| 143 | `primaryStage.setTitle("JScience Distributed N-Body Simulation");` |
| 154 | `System.out.println("Server available: " + status.getActiveWorkers() + " workers, "` |
| 155 | `+ status.getQueuedTasks() + " queued tasks");` |
| 158 | `System.out.println("Server not available, distributed mode will use local fallback");` |
| 169 | `.setTaskId("nbody-step-" + stepCount)` |
| 205 | `Platform.runLater(() -> statusLabel.setText("⚠️ Server unavailable, using local mode"));` |
| 257 | `String mode = distributed ? (serverAvailable ? "🌐 Distributed" : "⚠️ Fallback Local") : "💻 Local";` |
| 260 | `() -> statusLabel.setText(String.format("%s \| Step: %d \| E: %.2e J", mode, stepCount, energy)));` |
| 335 | `header.setStyle("-fx-background-color: #16213e;");` |
| 338 | `Label title = new Label("🌌 Distributed N-Body Simulation");` |
| 339 | `title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");` |
| 344 | `startBtn = new Button("▶ Start");` |
| 345 | `startBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white;");` |
| 348 | `ToggleButton distributedToggle = new ToggleButton("🌐 Distributed");` |
| 349 | `distributedToggle.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");` |
| 355 | `statusLabel.setText(distributed ? (serverAvailable ? "🌐 Distributed Mode" : "⚠️ Server unavailable")` |
| 356 | `: "💻 Local Mode");` |
| 359 | `Button resetBtn = new Button("🔄 Reset");` |
| 360 | `resetBtn.setStyle("-fx-background-color: #888; -fx-text-fill: white;");` |
| 374 | `startBtn.setText("⏸ Pause");` |
| 377 | `startBtn.setText("▶ Start");` |
| 384 | `footer.setStyle("-fx-background-color: #16213e;");` |
| 386 | `statusLabel = new Label("💻 Local Mode - Ready");` |
| 387 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 392 | `fpsLabel = new Label("FPS: --");` |
| 393 | `fpsLabel.setStyle("-fx-text-fill: #888;");` |

## DistributedWaveSimApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\physics\wavesim\DistributedWaveSimApp.java`

| Line | Content |
| --- | --- |
| 72 | `stage.setTitle("🌊 Wave Equation - Distributed JScience");` |
| 80 | `controls.setStyle("-fx-background-color: #1a1a2e;");` |
| 83 | `Label title = new Label("Wave Equation");` |
| 84 | `title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #7c3aed;");` |
| 92 | `statusLabel = new Label("Status: Connected to Grid");` |
| 93 | `statusLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11;");` |
| 95 | `CheckBox distCheck = new CheckBox("Distributed Mode");` |
| 97 | `distCheck.setStyle("-fx-text-fill: white;");` |
| 100 | `Button startBtn = new Button("▶ Start");` |
| 101 | `startBtn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-pref-width: 200;");` |
| 104 | `startBtn.setText(running ? "⏸ Pause" : "▶ Resume");` |
| 107 | `controls.getChildren().addAll(title, new Label("Speed:"), speedSlider, new Label("Damping:"), dampSlider,` |
| 112 | `rootPanel.setStyle("-fx-background-color: #0f0f1a;");` |
| 127 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 146 | `statusLabel.setText("Status: Local Performance");` |
| 157 | `.setTaskId("wave-" + stepCount)` |
| 170 | `statusLabel.setText("Status: Grid Computed ✅");` |
| 179 | `statusLabel.setText("Status: Grid Queued ⏳");` |
| 183 | `statusLabel.setText("Status: Grid Offline ❌");` |
| 190 | `dos.writeUTF("WAVE_2D");` |

## DistributedGeopoliticsApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\politics\geopolitics\DistributedGeopoliticsApp.java`

| Line | Content |
| --- | --- |
| 69 | `stage.setTitle("📉 JScience Social Grid - Global Economics & Politics");` |
| 75 | `Map<String, Double> gdpData = WorldBankReader.getInstance().fetchIndicatorData("USA", "NY.GDP.MKTP.CD")` |
| 80 | `Map<String, Double> inflData = WorldBankReader.getInstance().fetchIndicatorData("USA", "FP.CPI.TOTL.ZG")` |
| 85 | `System.err.println("Failed to fetch WB data: " + e.getMessage());` |
| 88 | `economyTask = new DistributedEconomyTask("Global Core",` |
| 99 | `if (c.getGovernmentType() != null && c.getGovernmentType().toLowerCase().contains("republic")) {` |
| 111 | `economyLabel = new Label("GDP: -- \| Inflation: --");` |
| 112 | `economyLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");` |
| 114 | `Button exportBtn = new Button("📄 Export Report");` |
| 137 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 157 | `.setTaskId("econ-" + step)` |
| 164 | `.setTaskId("pol-" + step)` |
| 182 | `Platform.runLater(() -> console.getItems().add(0, "Grid Error: " + e.getMessage()));` |
| 188 | `economyLabel.setText(String.format("GDP: $%.2fT \| Inflation: %.2f%%",` |
| 193 | `console.getItems().add(0, String.format("[%d] %s: Stability=%.2f, Military=%.0f",` |
| 201 | `File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Export Report", "CSV Files", "*.csv");` |
| 204 | `pw.println("Step,Metric,Value");` |
| 205 | `pw.println(step + ",GDP," + economyTask.getGdp());` |
| 206 | `pw.println(step + ",Inflation," + economyTask.getInflation());` |
| 207 | `pw.println(step + ",ActiveNations," + politicsTask.getNations().size());` |
| 208 | `new Alert(Alert.AlertType.INFORMATION, "Report saved").show();` |
| 210 | `new Alert(Alert.AlertType.ERROR, "Export failed").show();` |

## DistributedDataLakeApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\shared\datalakebrowser\DistributedDataLakeApp.java`

| Line | Content |
| --- | --- |
| 64 | `String host = getParameters().getNamed().getOrDefault("host", "localhost");` |
| 65 | `int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));` |
| 74 | `root.setStyle("-fx-background-color: #1a1a2e;");` |
| 89 | `primaryStage.setTitle("JScience Data Lake Browser");` |
| 97 | `header.setStyle("-fx-background-color: #16213e;");` |
| 100 | `Label title = new Label("Ã°Å¸â€œÅ  Data Lake Browser");` |
| 101 | `title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #4ecca3;");` |
| 107 | `streamGenomeBtn = new Button("Ã°Å¸Â§Â¬ Stream Genome");` |
| 108 | `streamGenomeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px;");` |
| 111 | `streamStarsBtn = new Button("Ã¢Â­Â Stream Stars");` |
| 112 | `streamStarsBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; -fx-font-size: 14px;");` |
| 115 | `Button clearBtn = new Button("Clear");` |
| 116 | `clearBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");` |
| 120 | `statsLabel.setText("Ready");` |
| 130 | `pane.setStyle("-fx-background-color: #0f3460;");` |
| 132 | `Label label = new Label("Streaming Data Output");` |
| 133 | `label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");` |
| 138 | `outputArea.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));` |
| 139 | `outputArea.setStyle("-fx-control-inner-background: #1a1a2e; -fx-text-fill: #4ecca3;");` |
| 144 | `progressBar.setStyle("-fx-accent: #e94560;");` |
| 153 | `footer.setStyle("-fx-background-color: #16213e;");` |
| 155 | `statsLabel = new Label("Ready");` |
| 156 | `statsLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 161 | `throughputLabel = new Label("Throughput: 0 items/s");` |
| 162 | `throughputLabel.setStyle("-fx-text-fill: #888;");` |
| 177 | `.setChromosome("chr1")` |
| 190 | `buffer.append(String.format("[%d] Position: %d, Sequence: %s...%n",` |
| 200 | `statsLabel.setText("Received: " + c + " chunks");` |
| 204 | `throughputLabel.setText(String.format("Throughput: %.1f chunks/s", throughput));` |
| 212 | `outputArea.appendText("\nÃ°Å¸â€Â´ Error: " + t.getMessage() + "\n");` |
| 213 | `statsLabel.setText("Error");` |
| 224 | `outputArea.appendText("\nÃ¢Å“â€¦ Stream completed! Total: " + count.get() + " chunks\n");` |
| 226 | `statsLabel.setText("Completed: " + count.get() + " chunks");` |
| 258 | `buffer.append(String.format("[%d] Ã¢Â­Â %s \| RA: %.2fÃ‚Â° \| Dec: %.2fÃ‚Â° \| Mag: %.2f \| Type: %s%n",` |
| 267 | `statsLabel.setText("Received: " + c + " stars");` |
| 271 | `throughputLabel.setText(String.format("Throughput: %.1f stars/s", throughput));` |
| 279 | `outputArea.appendText("\nÃ°Å¸â€Â´ Error: " + t.getMessage() + "\n");` |
| 280 | `statsLabel.setText("Error");` |
| 291 | `outputArea.appendText("\nÃ¢Å“â€¦ Stream completed! Total: " + count.get() + " stars\n");` |
| 293 | `statsLabel.setText("Completed: " + count.get() + " stars");` |

## DistributedGridMonitorApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\shared\gridmonitor\DistributedGridMonitorApp.java`

| Line | Content |
| --- | --- |
| 62 | `channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();` |
| 66 | `root.setStyle("-fx-background-color: #1a1a2e;");` |
| 74 | `primaryStage.setTitle("JScience Cluster Monitor");` |
| 84 | `header.setStyle("-fx-background-color: #16213e;");` |
| 85 | `Label title = new Label("⚡ JScience Grid Monitor");` |
| 86 | `title.setStyle("-fx-font-size: 24; -fx-text-fill: #e94560;");` |
| 89 | `workerCountLabel = createStatCard("Workers", "0");` |
| 90 | `jobCountLabel = createStatCard("Jobs", "0");` |
| 91 | `completedCountLabel = createStatCard("Completed", "0");` |
| 98 | `Label card = new Label(label + ": " + value);` |
| 100 | `"-fx-font-size: 16; -fx-text-fill: white; -fx-background-color: #0f3460; -fx-padding: 10 20; -fx-background-radius: 5;");` |
| 107 | `pane.setStyle("-fx-background-color: #0f3460;");` |
| 109 | `table.getColumns().add(new TableColumn<WorkerInfo, String>("Worker ID"));` |
| 110 | `table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("workerId"));` |
| 112 | `pane.getChildren().addAll(new Label("Active Workers"), table);` |
| 119 | `pane.setStyle("-fx-background-color: #0f3460;");` |
| 121 | `table.getColumns().add(new TableColumn<JobInfo, String>("Job ID"));` |
| 122 | `table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("jobId"));` |
| 124 | `pane.getChildren().addAll(new Label("Task Queue"), table);` |
| 131 | `footer.setStyle("-fx-background-color: #16213e;");` |
| 132 | `statusLabel = new Label("Connecting...");` |
| 134 | `org.jscience.server.proto.Status.UNKNOWN.name().equals("UNKNOWN") ? Color.valueOf("#4ecca3")` |
| 135 | `: Color.valueOf("#e94560"));` |
| 149 | `workerCountLabel.setText("Active Workers: " + status.getActiveWorkers());` |
| 150 | `jobCountLabel.setText("Queued Tasks: " + status.getQueuedTasks());` |
| 151 | `completedCountLabel.setText("Total Completed: " + status.getTotalTasksCompleted());` |
| 152 | `statusLabel.setText("✅ Connected \| Grid Load: " + (int) (status.getSystemLoad() * 100) + "%");` |
| 153 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 157 | `statusLabel.setText("❌ Server Disconnected");` |
| 158 | `statusLabel.setStyle("-fx-text-fill: #e94560;");` |

## DistributedWhiteboardApp.java (jscience-client)
`C:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\java\org\jscience\client\shared\whiteboard\DistributedWhiteboardApp.java`

| Line | Content |
| --- | --- |
| 64 | `String host = getParameters().getNamed().getOrDefault("host", "localhost");` |
| 65 | `int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));` |
| 66 | `userId = "user-" + System.currentTimeMillis() % 10000;` |
| 75 | `root.setStyle("-fx-background-color: #1a1a2e;");` |
| 92 | `canvasPane.setStyle("-fx-background-color: white; -fx-padding: 10;");` |
| 100 | `primaryStage.setTitle("JScience Collaborative Whiteboard - " + userId);` |
| 108 | `toolbar.setStyle("-fx-background-color: #16213e;");` |
| 110 | `Label title = new Label("Ã°Å¸Å½Â¨ Collaborative Whiteboard");` |
| 111 | `title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e94560;");` |
| 114 | `TextField sessionField = new TextField("default-session");` |
| 116 | `sessionField.setPromptText("Session ID");` |
| 118 | `Button joinBtn = new Button("Join Session");` |
| 119 | `joinBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white;");` |
| 122 | `Button createBtn = new Button("Create Session");` |
| 123 | `createBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");` |
| 128 | `colorPicker.setStyle("-fx-background-color: #0f3460;");` |
| 134 | `Label brushLabel = new Label("Brush:");` |
| 135 | `brushLabel.setStyle("-fx-text-fill: white;");` |
| 137 | `Button clearBtn = new Button("Clear");` |
| 138 | `clearBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");` |
| 154 | `footer.setStyle("-fx-background-color: #16213e;");` |
| 156 | `statusLabel = new Label("Ã¢Å¡Âª Not connected to any session");` |
| 157 | `statusLabel.setStyle("-fx-text-fill: #888;");` |
| 175 | `statusLabel.setText("Ã°Å¸Å¸Â¢ Created session: " + sessionId);` |
| 176 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 181 | `statusLabel.setText("Ã°Å¸â€Â´ Failed to create session: " + e.getMessage());` |
| 182 | `statusLabel.setStyle("-fx-text-fill: #e94560;");` |
| 204 | `statusLabel.setText("Ã°Å¸â€Â´ Connection error: " + t.getMessage());` |
| 205 | `statusLabel.setStyle("-fx-text-fill: #e94560;");` |
| 213 | `statusLabel.setText("Ã¢Å¡Âª Session ended");` |
| 214 | `statusLabel.setStyle("-fx-text-fill: #888;");` |
| 222 | `statusLabel.setText("Ã°Å¸Å¸Â¢ Connected to session: " + sessionId + " as " + userId);` |
| 223 | `statusLabel.setStyle("-fx-text-fill: #4ecca3;");` |
| 234 | `if (event.getEventType().equals("DRAW") && data.contains(",")) {` |
| 235 | `String[] parts = data.split(",");` |
| 270 | `String colorHex = String.format("#%02X%02X%02X",` |
| 275 | `String data = String.format("%.0f,%.0f,%.0f,%.0f,%s,%.1f",` |
| 281 | `.setEventType("DRAW")` |

## AbstractResourceReader.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\io\AbstractResourceReader.java`

| Line | Content |
| --- | --- |
| 88 | `throw new Exception("Could not load '" + id + "' from any source");` |

## PropertiesReader.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\io\properties\PropertiesReader.java`

| Line | Content |
| --- | --- |
| 56 | `String cacheKey = resourcePath + ":" + itemName;` |
| 63 | `if (isTemp == null && resourcePath.startsWith("/")) {` |
| 69 | `throw new IllegalArgumentException("Resource not found: " + resourcePath);` |
| 77 | `throw new IllegalArgumentException("Item '" + itemName + "' not found in " + resourcePath);` |
| 88 | `throw new RuntimeException("Failed to load properties from " + resourcePath, e);` |

## VTKWriter.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\mathematics\loaders\VTKWriter.java`

| Line | Content |
| --- | --- |
| 43 | `return "Graphics";` |
| 48 | `return "VTK Writer";` |
| 53 | `return "Exports 2D data to VTK Structured Points format.";` |
| 62 | `writer.write("# vtk DataFile Version 3.0\n");` |
| 63 | `writer.write("JScience GCM Data\n");` |
| 64 | `writer.write("ASCII\n");` |
| 65 | `writer.write("DATASET STRUCTURED_POINTS\n");` |
| 68 | `writer.write("DIMENSIONS " + cols + " " + rows + " 1\n");` |
| 69 | `writer.write("ORIGIN 0 0 0\n");` |
| 70 | `writer.write("SPACING 1 1 1\n");` |
| 71 | `writer.write("POINT_DATA " + (rows * cols) + "\n");` |
| 72 | `writer.write("SCALARS temperature double 1\n");` |
| 73 | `writer.write("LOOKUP_TABLE default\n");` |
| 77 | `writer.write(data[i][j] + " ");` |
| 79 | `writer.write("\n");` |

## AbstractDeviceViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\AbstractDeviceViewer.java`

| Line | Content |
| --- | --- |
| 54 | `this.nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");` |
| 56 | `this.statusLabel = new Label("Status: Connected");` |
| 61 | `this.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #fafafa;");` |
| 77 | `return device.getName() + " Viewer";` |
| 82 | `return "Devices";` |
| 87 | `return "Control interface for " + device.getName();` |

## AbstractViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\AbstractViewer.java`

| Line | Content |
| --- | --- |
| 44 | `return "General";` |

## IconLoader.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\IconLoader.java`

| Line | Content |
| --- | --- |
| 53 | `String path = "/org/jscience/ui/icons/" + name + ".svg";` |
| 56 | `System.err.println("Icon not found: " + path);` |
| 97 | `} else if (tag.getTagName().equals("g")) {` |
| 108 | `case "path":` |
| 110 | `path.setContent(tag.getAttribute("d"));` |
| 112 | `case "circle":` |
| 114 | `parse(tag.getAttribute("cx")),` |
| 115 | `parse(tag.getAttribute("cy")),` |
| 116 | `parse(tag.getAttribute("r")));` |
| 117 | `case "rect":` |
| 119 | `parse(tag.getAttribute("x")),` |
| 120 | `parse(tag.getAttribute("y")),` |
| 121 | `parse(tag.getAttribute("width")),` |
| 122 | `parse(tag.getAttribute("height")));` |
| 123 | `String rx = tag.getAttribute("rx");` |
| 124 | `String ry = tag.getAttribute("ry");` |
| 130 | `case "line":` |
| 132 | `parse(tag.getAttribute("x1")),` |
| 133 | `parse(tag.getAttribute("y1")),` |
| 134 | `parse(tag.getAttribute("x2")),` |
| 135 | `parse(tag.getAttribute("y2")));` |
| 136 | `case "polyline":` |
| 138 | `parsePoints(tag.getAttribute("points"), polyline.getPoints());` |
| 140 | `case "polygon":` |
| 142 | `parsePoints(tag.getAttribute("points"), polygon.getPoints());` |
| 152 | `String[] pairs = pointsObj.trim().split("\\s+");` |
| 155 | `String[] coords = pair.split("[,\\s]+");` |
| 175 | `s.getStyleClass().add("icon-shape");` |
| 178 | `if (tag.hasAttribute("stroke-width")) {` |
| 179 | `s.setStrokeWidth(parse(tag.getAttribute("stroke-width")));` |

## JScienceDemosApp.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\JScienceDemosApp.java`

| Line | Content |
| --- | --- |
| 72 | `cssResource = getClass().getResource("/org/jscience/ui/theme.css");` |
| 74 | `cssResource = getClass().getResource("/org/jscience/ui/style.css");` |
| 97 | `allContent.getStyleClass().add("content-box");` |
| 101 | `allContent.getChildren().add(new Label(I18n.getInstance().get("app.nodemos")));` |
| 108 | `if ("Social Sciences".equalsIgnoreCase(entry.getKey())` |
| 109 | `\|\| "Sciences Sociales".equalsIgnoreCase(entry.getKey())) {` |
| 128 | `primaryStage.setTitle(I18n.getInstance().get("app.header.title", "JScience Demos"));` |
| 145 | `Menu languageMenu = new Menu(I18n.getInstance().get("menu.preferences.language", "Language"));` |
| 165 | `Menu themeMenu = new Menu(I18n.getInstance().get("app.menu.theme", "Theme"));` |
| 169 | `RadioMenuItem modenaItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.modena", "Modena (Light)"));` |
| 171 | `modenaItem.setSelected("Modena".equalsIgnoreCase(currentTheme));` |
| 173 | `ThemeManager.getInstance().setTheme("Modena");` |
| 177 | `RadioMenuItem caspianItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.caspian", "Caspian"));` |
| 179 | `caspianItem.setSelected("Caspian".equalsIgnoreCase(currentTheme));` |
| 181 | `ThemeManager.getInstance().setTheme("Caspian");` |
| 186 | `I18n.getInstance().get("app.menu.theme.highcontrast", "High Contrast"));` |
| 188 | `highContrastItem.setSelected("HighContrast".equalsIgnoreCase(currentTheme));` |
| 190 | `ThemeManager.getInstance().setTheme("HighContrast");` |
| 194 | `RadioMenuItem darkItem = new RadioMenuItem(I18n.getInstance().get("menu.view.theme.dark", "Dark"));` |
| 196 | `darkItem.setSelected("Dark".equalsIgnoreCase(currentTheme));` |
| 198 | `ThemeManager.getInstance().setTheme("Dark");` |
| 212 | `header.getStyleClass().add("header-box");` |
| 215 | `Label title = new Label(I18n.getInstance().get("app.header.title", "JScience Demos"));` |
| 216 | `title.getStyleClass().add("header-label");` |
| 219 | `Label subtitle = new Label(I18n.getInstance().get("app.header.subtitle", "Scientific Applications & Tools"));` |
| 220 | `subtitle.getStyleClass().add("header-subtitle");` |
| 229 | `box.getStyleClass().add("section-box"); // Styled in CSS` |
| 245 | `row.getStyleClass().add("demo-card");` |
| 247 | `Button btn = new Button(I18n.getInstance().get("app.button.launch", "Launch"));` |
| 248 | `btn.getStyleClass().add("launch-button");` |
| 249 | `btn.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80;");` |
| 255 | `showError("Failed to launch: " + demo.getName(), ex.getMessage(), ex);` |
| 261 | `String prefix = demo.getCategory() + " : ";` |
| 267 | `name.getStyleClass().add("card-title");` |
| 270 | `Label description = new Label(desc != null ? desc : "");` |
| 272 | `description.getStyleClass().add("description-label");` |
| 292 | `showError("Launch Error", "Could not start " + demo.getName(), e);` |
| 298 | `alert.setTitle("Error");` |
| 300 | `alert.setContentText(message + "\n" + ex.getMessage());` |
| 335 | `String catName = I18n.getInstance().get("category." + key.toLowerCase().replace(" ", "_"), key);` |

## Viewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\Viewer.java`

| Line | Content |
| --- | --- |
| 51 | `return "";` |

## LorenzViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\analysis\chaos\LorenzViewer.java`

| Line | Content |
| --- | --- |
| 54 | `return "Mathematics";` |
| 59 | `return I18n.getInstance().get("lorenz.title");` |
| 101 | `setStyle("-fx-background-color: #f8f8f8;");` |
| 108 | `sidebar.setStyle("-fx-background-color: #eee;");` |
| 110 | `Label title = new Label(I18n.getInstance().get("lorenz.title"));` |
| 111 | `title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");` |
| 114 | `createSlider(I18n.getInstance().get("lorenz.param.sigma"), 0, 50, sigma, v -> sigma = v),` |
| 115 | `createSlider(I18n.getInstance().get("lorenz.param.rho"), 0, 100, rho, v -> rho = v),` |
| 116 | `createSlider(I18n.getInstance().get("lorenz.param.beta"), 0, 10, beta, v -> beta = v));` |

## MandelbrotViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\analysis\chaos\fractals\MandelbrotViewer.java`

| Line | Content |
| --- | --- |
| 49 | `return "Mathematics";` |
| 54 | `return I18n.getInstance().get("mandelbrot.title", "Mandelbrot Set");` |
| 86 | `I18n.getInstance().get("mandelbrot.mode.julia"));` |

## FunctionExplorer2DViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\analysis\real\FunctionExplorer2DViewer.java`

| Line | Content |
| --- | --- |
| 42 | `private static final String DEFAULT_FX = "sin(x)";` |
| 43 | `private static final String DEFAULT_GX = "x^2/10";` |
| 63 | `sidebar.setStyle("-fx-background-color: #fafafa;");` |
| 66 | `Label fLabel = new Label("f(x) =");` |
| 68 | `Label gLabel = new Label("g(x) =");` |
| 71 | `Label opLabel = new Label(i18n.get("funcexplorer.plotop", "Plot Operation:"));` |
| 73 | `"f(x)", "g(x)", "f(x) + g(x)", "f(x) * g(x)"));` |
| 74 | `opCombo.setValue("f(x)");` |
| 76 | `Label rangeLabel = new Label(i18n.get("funcexplorer.range", "Range [Min, Max]"));` |
| 78 | `TextField xMinField = new TextField("-10");` |
| 79 | `TextField xMaxField = new TextField("10");` |
| 84 | `Button plotBtn = new Button(i18n.get("funcexplorer.btn.plot", "Plot Function"));` |
| 86 | `plotBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");` |
| 90 | `Label analysisLabel = new Label(i18n.get("funcexplorer.analysis", "Analysis (Click Chart)"));` |
| 91 | `analysisLabel.setStyle("-fx-font-weight: bold;");` |
| 93 | `Label cursorVal = new Label(i18n.get("funcexplorer.cursor", "Cursor:") + " -");` |
| 95 | `Label symTitle = new Label(i18n.get("funcexplorer.symderiv", "Symbolic Derivative:"));` |
| 101 | `Button symbBtn = new Button(i18n.get("funcexplorer.btn.deriv", "Calculate Derivative (Symbolic)"));` |
| 115 | `chartContainer.setStyle("-fx-background-color: white;");` |
| 116 | `Label placeholder = new Label(i18n.get("funcexplorer.placeholder", "Enter function and click Plot"));` |
| 128 | `Plot2D plot = new JavaFXPlot2D("Function Plot");` |
| 141 | `if (op.equals("f(x)"))` |
| 143 | `if (op.equals("g(x)"))` |
| 145 | `if (op.equals("f(x) + g(x)"))` |
| 147 | `if (op.equals("f(x) * g(x)"))` |
| 157 | `cursorVal.setText(String.format(i18n.get("funcexplorer.cursor", "Cursor:") + " (%.1f, %.1f)",` |
| 165 | `Alert alert = new Alert(Alert.AlertType.ERROR, "Plot Error: " + ex.getMessage());` |
| 177 | `String res = "";` |
| 178 | `if (op.equals("f(x)"))` |
| 180 | `else if (op.equals("g(x)"))` |
| 182 | `else if (op.equals("f(x) + g(x)")) {` |
| 183 | `res = SymbolicUtil.differentiate(fStr) + " + " + SymbolicUtil.differentiate(gStr);` |
| 185 | `res = i18n.get("funcexplorer.error.complex", "Complex operations not supported in demo.");` |
| 198 | `vars.put("x", x);` |

## FunctionExplorer3DViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\analysis\real\FunctionExplorer3DViewer.java`

| Line | Content |
| --- | --- |
| 38 | `private static final String DEFAULT_3D_FUNC = "sin(sqrt(x^2+y^2))";` |
| 54 | `sidebar.setStyle("-fx-background-color: #fafafa;");` |
| 56 | `Label funcLabel = new Label("z = f(x, y) =");` |
| 59 | `Label rangeLabel = new Label(i18n.get("funcexplorer.range3d", "Range [\u00B1X, \u00B1Y]"));` |
| 60 | `TextField rangeField = new TextField("10");` |
| 62 | `CheckBox gridChk = new CheckBox(i18n.get("funcexplorer.grid", "Show Grid/Axes"));` |
| 65 | `Button plotBtn = new Button(i18n.get("funcexplorer.btn.plot3d", "Plot 3D Surface"));` |
| 67 | `plotBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");` |
| 71 | `Label analysisLabel = new Label(i18n.get("funcexplorer.analysis3d", "Analysis"));` |
| 72 | `TextField xEval = new TextField("0");` |
| 73 | `xEval.setPromptText("x");` |
| 74 | `TextField yEval = new TextField("0");` |
| 75 | `yEval.setPromptText("y");` |
| 76 | `Label zRes = new Label("z = -");` |
| 77 | `Button calcBtn = new Button(i18n.get("funcexplorer.btn.calcpt", "Calculate Point"));` |
| 83 | `zRes.setText(String.format("z = %.4f", val));` |
| 85 | `zRes.setText("Error");` |
| 93 | `chartContainer.setStyle("-fx-background-color: #222;");` |
| 94 | `Label placeholder = new Label(i18n.get("funcexplorer.placeholder", "Enter function and click Plot"));` |
| 104 | `JavaFXPlot3D plot = new JavaFXPlot3D("3D Surface");` |
| 113 | `}, Real.of(-range), Real.of(range), Real.of(-range), Real.of(range), "Surface");` |
| 118 | `Alert alert = new Alert(Alert.AlertType.ERROR, "Error plotting: " + ex.getMessage());` |
| 130 | `vars.put("x", x);` |
| 131 | `vars.put("y", y);` |

## NetworkViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\discrete\NetworkViewer.java`

| Line | Content |
| --- | --- |
| 61 | `new Label("Active Network Backend: " + provider.get().getName()),` |
| 68 | `container.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");` |
| 69 | `container.getChildren().add(new Label("No Network Backend Available (Install GraphStream, JUNG, etc.)"));` |
| 76 | `return "Mathematics";` |
| 81 | `return "Network Viewer";` |
| 86 | `return "Visualizes networks and graphs.";` |

## GeometryBoardViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\geometry\GeometryBoardViewer.java`

| Line | Content |
| --- | --- |
| 50 | `return "Mathematics";` |
| 55 | `return I18n.getInstance().get("GeometryBoard.title", "Geometry Board");` |
| 90 | `setStyle("-fx-background-color: white;");` |
| 95 | `toolbar.setStyle("-fx-background-color: #f0f0f0;");` |
| 99 | `createToolBtn(I18n.getInstance().get("geometry.tool.point"), Tool.POINT, group),` |
| 100 | `createToolBtn(I18n.getInstance().get("geometry.tool.line"), Tool.LINE, group),` |
| 101 | `createToolBtn(I18n.getInstance().get("geometry.tool.circle"), Tool.CIRCLE, group),` |
| 102 | `createToolBtn("Triangle", Tool.TRIANGLE, group),` |
| 103 | `createToolBtn(I18n.getInstance().get("geometry.tool.select"), Tool.SELECT, group),` |
| 105 | `new Button(I18n.getInstance().get("geometry.button.clear")) {` |
| 155 | `sidebar.setStyle("-fx-background-color: #fafafa;");` |
| 157 | `.add(new Label(I18n.getInstance().get("geometry.help")));` |

## CSGViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\geometry\csg\CSGViewer.java`

| Line | Content |
| --- | --- |
| 52 | `return "Mathematics";` |
| 57 | `return I18n.getInstance().get("csg.title", "CSG Viewer");` |
| 72 | `subScene.setFill(Color.web("#1e1e1e"));` |
| 85 | `sidebar.setStyle("-fx-background-color: #333; -fx-text-fill: white;");` |
| 87 | `Label title = new Label(I18n.getInstance().get("csg.title"));` |
| 88 | `title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");` |
| 91 | `opBox.getItems().addAll("Show Both", "A Union B", "A Subtract B", "A Intersect B");` |
| 92 | `opBox.setValue("Show Both");` |
| 96 | `Label offsetLbl = new Label(I18n.getInstance().get("csg.offset") + " 5.0");` |
| 99 | `offsetLbl.setText(I18n.getInstance().get("csg.offset") + " " + String.format("%.2f", nv.doubleValue()));` |
| 102 | `sidebar.getChildren().addAll(title, new Separator(), new Label(I18n.getInstance().get("csg.operation")), opBox,` |
| 148 | `case "Show Both" -> {` |
| 154 | `case "A Union B" -> {` |
| 158 | `case "A Subtract B" -> {` |
| 163 | `case "A Intersect B" -> {` |

## MatrixViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\linearalgebra\MatrixViewer.java`

| Line | Content |
| --- | --- |
| 62 | `return "Mathematics";` |
| 67 | `return I18n.getInstance().get("matrix.title", "Matrix Explorer");` |
| 72 | `layout.setStyle("-fx-background-color: white;");` |
| 75 | `Label header = new Label(I18n.getInstance().get("matrix.label.header"));` |
| 76 | `header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");` |
| 80 | `headerBox.setStyle("-fx-background-color: #16213e;");` |
| 87 | `controls.setStyle("-fx-background-color: #f4f4f4;");` |
| 89 | `Label sizeLabel = new Label(I18n.getInstance().get("matrix.label.size"));` |
| 90 | `sizeLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");` |
| 94 | `Label rowLabel = new Label(I18n.getInstance().get("matrix.label.rows") + ":");` |
| 101 | `Label colLabel = new Label(I18n.getInstance().get("matrix.label.cols") + ":");` |
| 106 | `Button generateBtn = new Button(I18n.getInstance().get("matrix.button.random"));` |
| 108 | `generateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");` |
| 111 | `Button identityBtn = new Button(I18n.getInstance().get("matrix.button.identity"));` |
| 117 | `Label viewLabel = new Label(I18n.getInstance().get("matrix.view.label"));` |
| 118 | `viewLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");` |
| 121 | `RadioButton tableRadio = new RadioButton(I18n.getInstance().get("matrix.view.table"));` |
| 122 | `tableRadio.setStyle("-fx-text-fill: #888;");` |
| 127 | `RadioButton heatmapRadio = new RadioButton(I18n.getInstance().get("matrix.view.heatmap"));` |
| 128 | `heatmapRadio.setStyle("-fx-text-fill: #888;");` |
| 134 | `Label opsLabel = new Label(I18n.getInstance().get("matrix.ops.label"));` |
| 135 | `opsLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");` |
| 137 | `Button transposeBtn = new Button(I18n.getInstance().get("matrix.ops.transpose"));` |
| 141 | `Button scaleBtn = new Button(I18n.getInstance().get("matrix.ops.scale"));` |
| 145 | `infoLabel = new Label("8x8 " + I18n.getInstance().get("matrix.info.matrix"));` |
| 146 | `infoLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");` |
| 157 | `viewContainer.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");` |
| 178 | `infoLabel.setText(rows + "\u00D7" + cols + " " + I18n.getInstance().get("matrix.info.matrix"));` |
| 193 | `infoLabel.setText(size + "\u00D7" + size + " " + I18n.getInstance().get("matrix.info.identity"));` |
| 205 | `infoLabel.setText(matrix.rows() + "\u00D7" + matrix.cols() + " ("` |
| 206 | `+ I18n.getInstance().get("matrix.info.transposed") + ")");` |
| 222 | `infoLabel.setText(matrix.rows() + "\u00D7" + matrix.cols() + " ("` |
| 223 | `+ I18n.getInstance().get("matrix.info.scaled") + " \u00D7" + factor + ")");` |
| 249 | `Label placeholder = new Label("Table View Not Implemented in this Refactor (Use Heatmap)");` |
| 302 | `gc.fillText(String.format("%.1f", val),` |
| 309 | `gc.setStroke(Color.web("#333"));` |
| 334 | `gc.fillText(String.format("%.1f", max), legendX + 20, 10);` |
| 335 | `gc.fillText(String.format("%.1f", min), legendX + 20, legendH);` |

## MetamathViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\logic\MetamathViewer.java`

| Line | Content |
| --- | --- |
| 77 | `Label header = new Label(I18n.getInstance().get("metamath.title"));` |
| 78 | `header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");` |
| 100 | `selectorBox.getChildren().addAll(new Label(I18n.getInstance().get("metamath.select")), theoremSelector);` |
| 104 | `"-fx-font-size: 16px; -fx-background-color: #e3f2fd; -fx-padding: 10; -fx-background-radius: 5;");` |
| 112 | `center.getChildren().addAll(selectorBox, theoremLabel, new Label(I18n.getInstance().get("metamath.proof")),` |
| 120 | `sidebar.setStyle("-fx-background-color: #f5f5f5;");` |
| 122 | `Button nextStepBtn = new Button(I18n.getInstance().get("metamath.btn.next"));` |
| 126 | `Button resetBtn = new Button(I18n.getInstance().get("metamath.btn.reset"));` |
| 130 | `sidebar.getChildren().addAll(new Label(I18n.getInstance().get("metamath.tactics")), nextStepBtn, resetBtn);` |
| 143 | `"Disjunctive Syllogism",` |
| 144 | `"(p \u2228 q) \u2227 \u00ACp \u22A2 q",` |
| 146 | `"1. (p \u2228 q) \u2227 \u00ACp [Hypothesis]",` |
| 147 | `"2. (p \u2228 q) [Simplification (1)]",` |
| 148 | `"3. \u00ACp [Simplification (1)]",` |
| 149 | `"4. p \u2192 q [Material Implication (2)]",` |
| 150 | `"5. q [Modus Ponens (3, 4)]",` |
| 151 | `"Q.E.D."` |
| 154 | `"Modus Tollens",` |
| 155 | `"(p \u2192 q) \u2227 \u00ACq \u22A2 \u00ACp",` |
| 157 | `"1. (p \u2192 q) \u2227 \u00ACq [Hypothesis]",` |
| 158 | `"2. p \u2192 q [Simplification (1)]",` |
| 159 | `"3. \u00ACq [Simplification (1)]",` |
| 160 | `"4. \u00ACq \u2192 \u00ACp [Transposition (2)]",` |
| 161 | `"5. \u00ACp [Modus Ponens (3, 4)]",` |
| 162 | `"Q.E.D."` |
| 165 | `"Double Negation",` |
| 166 | `"p \u22A2 \u00AC\u00ACp",` |
| 168 | `"1. p [Hypothesis]",` |
| 169 | `"2. p \u2228 p [Addition]",` |
| 170 | `"3. \u00ACp \u2192 p [Material Implication (2)]",` |
| 171 | `"4. \u00AC\u00ACp \u2228 p [Material Implication]",` |
| 172 | `"5. \u00AC\u00ACp [Derived Step]",` |
| 173 | `"Q.E.D."` |
| 181 | `theoremLabel.setText("Theorem: " + t.formula + " (" + t.name + ")");` |
| 193 | `l.setStyle("-fx-font-family: monospace; -fx-font-size: 14px; -fx-background-color: " +` |
| 194 | `(step.contains("Q.E.D") ? "#c8e6c9" : "white")` |
| 195 | `+ "; -fx-padding: 5; -fx-border-color: #eee; -fx-border-width: 0 0 1 0;");` |

## DistributionsViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\statistics\DistributionsViewer.java`

| Line | Content |
| --- | --- |
| 53 | `return "Mathematics";` |
| 58 | `return I18n.getInstance().get("distributions.title", "Statistical Distributions");` |
| 70 | `layout.getStyleClass().add("dark-viewer-root");` |
| 73 | `Label header = new Label(I18n.getInstance().get("distributions.title"));` |
| 74 | `header.setStyle("-fx-font-size: 24px; -fx-padding: 15; -fx-font-weight: bold;");` |
| 96 | `controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");` |
| 99 | `Label meanLabel = new Label(I18n.getInstance().get("distributions.mean") + ": 0.0");` |
| 102 | `String.format(I18n.getInstance().get("distributions.mean") + ": %.1f", nv.doubleValue())));` |
| 105 | `Label stdLabel = new Label(I18n.getInstance().get("distributions.std") + ": 1.0");` |
| 108 | `String.format(I18n.getInstance().get("distributions.std") + ": %.1f", nv.doubleValue())));` |
| 110 | `controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),` |
| 116 | `xAxis.setLabel(I18n.getInstance().get("distributions.axis.value"));` |
| 118 | `yAxis.setLabel(I18n.getInstance().get("distributions.axis.pdf"));` |
| 121 | `chart.setTitle(I18n.getInstance().get("distributions.chart.normal"));` |
| 125 | `series.setName("N(\u03BC, \u03C3\u00B2)");` |
| 152 | `return new Tab(I18n.getInstance().get("distributions.tab.normal"), pane);` |
| 161 | `controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");` |
| 164 | `Label lambdaLabel = new Label(I18n.getInstance().get("distributions.lambda") + ": 5.0");` |
| 167 | `String.format(I18n.getInstance().get("distributions.lambda") + ": %.1f", nv.doubleValue())));` |
| 169 | `controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),` |
| 173 | `xAxis.setLabel(I18n.getInstance().get("distributions.axis.k_occ"));` |
| 175 | `yAxis.setLabel(I18n.getInstance().get("distributions.axis.pmf"));` |
| 178 | `chart.setTitle(I18n.getInstance().get("distributions.chart.poisson"));` |
| 182 | `series.setName("P(k; \u03BB)");` |
| 202 | `return new Tab(I18n.getInstance().get("distributions.tab.poisson"), pane);` |
| 211 | `controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");` |
| 214 | `Label nLabel = new Label(I18n.getInstance().get("distributions.trials") + ": 10");` |
| 217 | `String.format(I18n.getInstance().get("distributions.trials") + ": %d", nv.intValue())));` |
| 220 | `Label pLabel = new Label(I18n.getInstance().get("distributions.prob") + ": 0.5");` |
| 223 | `String.format(I18n.getInstance().get("distributions.prob") + ": %.2f", nv.doubleValue())));` |
| 225 | `controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),` |
| 229 | `xAxis.setLabel(I18n.getInstance().get("distributions.axis.k_suc"));` |
| 231 | `yAxis.setLabel(I18n.getInstance().get("distributions.axis.pmf"));` |
| 234 | `chart.setTitle(I18n.getInstance().get("distributions.chart.binomial"));` |
| 238 | `series.setName("B(n, p)");` |
| 258 | `return new Tab(I18n.getInstance().get("distributions.tab.binomial"), pane);` |

## FormulaNotationViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\mathematics\symbolic\FormulaNotationViewer.java`

| Line | Content |
| --- | --- |
| 51 | `content.setStyle("-fx-background-color: white;");` |
| 54 | `org.jscience.ui.i18n.I18n.getInstance().get("formula.title", "Mathematical Formula Renderer"));` |
| 55 | `title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");` |
| 60 | `createFormulaBox(i18n.get("formula.euler", "Euler's Identity"), createEulerIdentity()),` |
| 61 | `createFormulaBox(i18n.get("formula.quadratic", "Quadratic Formula"), createQuadraticFormula()),` |
| 62 | `createFormulaBox(i18n.get("formula.schrodinger", "Schr\u00F6dinger Equation"),` |
| 64 | `createFormulaBox(i18n.get("formula.gravitation", "Newton's Law of Gravitation"),` |
| 78 | `return "Mathematics";` |
| 83 | `return org.jscience.ui.i18n.I18n.getInstance().get("viewer.formula");` |
| 89 | `lbl.setStyle("-fx-font-style: italic; -fx-text-fill: black;");` |
| 93 | `frame.setStyle("-fx-border-color: #eee; -fx-border-radius: 5; -fx-background-color: #fafafa;");` |
| 104 | `Text base = new Text("e");` |
| 105 | `base.setFont(Font.font("Serif", 36));` |
| 106 | `Text expo = new Text("i\u03C0");` |
| 107 | `expo.setFont(Font.font("Serif", 20));` |
| 110 | `Text rest = new Text(" + 1 = 0");` |
| 111 | `rest.setFont(Font.font("Serif", 36));` |
| 119 | `Text x = new Text("x = ");` |
| 120 | `x.setFont(Font.font("Serif", 36));` |
| 127 | `Text b = new Text("-b \u00B1 ");` |
| 128 | `b.setFont(Font.font("Serif", 24));` |
| 132 | `Text rootSym = new Text("\u221A");` |
| 133 | `rootSym.setFont(Font.font("Serif", 30));` |
| 135 | `underRoot.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 0;");` |
| 137 | `Text b2 = new Text("b");` |
| 138 | `b2.setFont(Font.font("Serif", 24));` |
| 139 | `Text p2 = new Text("2");` |
| 140 | `p2.setFont(Font.font("Serif", 14));` |
| 143 | `Text ac = new Text(" - 4ac");` |
| 144 | `ac.setFont(Font.font("Serif", 24));` |
| 153 | `line.setStyle("-fx-background-color: black;");` |
| 156 | `Text den = new Text("2a");` |
| 157 | `den.setFont(Font.font("Serif", 24));` |
| 167 | `Text prefix = new Text("i\u210F");` |
| 168 | `prefix.setFont(Font.font("Serif", 36));` |
| 172 | `Text pTop = new Text("\u2202\u03A8");` |
| 173 | `pTop.setFont(Font.font("Serif", 18));` |
| 176 | `l.setStyle("-fx-background-color: black;");` |
| 177 | `Text pBot = new Text("\u2202t");` |
| 178 | `pBot.setFont(Font.font("Serif", 18));` |
| 181 | `Text eq = new Text(" = \u0124\u03A8");` |
| 182 | `eq.setFont(Font.font("Serif", 36));` |
| 190 | `Text f = new Text("F = G");` |
| 191 | `f.setFont(Font.font("Serif", 36));` |
| 196 | `Text m1 = new Text("m");` |
| 197 | `m1.setFont(Font.font("Serif", 24));` |
| 198 | `Text s1 = new Text("1");` |
| 199 | `s1.setFont(Font.font("Serif", 12));` |
| 200 | `Text m2 = new Text("m");` |
| 201 | `m2.setFont(Font.font("Serif", 24));` |
| 202 | `Text s2 = new Text("2");` |
| 203 | `s2.setFont(Font.font("Serif", 12));` |
| 216 | `line.setStyle("-fx-background-color: black;");` |
| 220 | `Text r = new Text("r");` |
| 221 | `r.setFont(Font.font("Serif", 24));` |
| 222 | `Text p2 = new Text("2");` |
| 223 | `p2.setFont(Font.font("Serif", 14));` |

## UnitConverterViewer.java (jscience-core)
`C:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\jscience\ui\viewers\measure\units\UnitConverterViewer.java`

| Line | Content |
| --- | --- |
| 61 | `return "Measurement";` |
| 66 | `return I18n.getInstance().get("converter.title", "Unit Converter");` |
| 87 | `I18n.getInstance().get("converter.title", "Universal Measure Converter"));` |
| 88 | `title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");` |
| 94 | `categoryBox.setValue("Length");` |
| 98 | `return I18n.getInstance().get("converter.cat." + object, object);` |
| 108 | `new Label(I18n.getInstance().get("converter.category", "Category:")),` |
| 116 | `inputField = new TextField("1.0");` |
| 119 | `outputField.setStyle("-fx-font-weight: bold;");` |
| 124 | `grid.add(new Label(I18n.getInstance().get("converter.from", "From:")), 0, 0);` |
| 128 | `grid.add(new Label(I18n.getInstance().get("converter.to", "To:")), 0, 1);` |
| 134 | `Button convertBtn = new Button(I18n.getInstance().get("converter.convert", "Convert"));` |
| 145 | `.get("converter.error.launch", "Error launching Unit Converter:\n%s"), e.getMessage())));` |
| 151 | `unitsByCategory.put("Length",` |
| 153 | `unitsByCategory.put("Mass", Arrays.asList(KILOGRAM, GRAM, MILLIGRAM, TONNE, POUND, OUNCE));` |
| 154 | `unitsByCategory.put("Time", Arrays.asList(SECOND, MINUTE, HOUR, DAY, WEEK, YEAR));` |
| 155 | `unitsByCategory.put("Temperature", Arrays.asList(KELVIN, CELSIUS, FAHRENHEIT));` |
| 156 | `unitsByCategory.put("Velocity", Arrays.asList(METER_PER_SECOND, KILOMETER_PER_HOUR, MILE_PER_HOUR, KNOT, MACH));` |
| 157 | `unitsByCategory.put("Energy", Arrays.asList(JOULE, KILOJOULE, CALORIE, KILOCALORIE, WATT_HOUR, KILOWATT_HOUR));` |
| 158 | `unitsByCategory.put("Pressure", Arrays.asList(PASCAL, BAR, ATMOSPHERE, MILLIMETRE_OF_MERCURY));` |
| 180 | `outputField.setText(String.format("%.6g", result.getValue().doubleValue()));` |
| 182 | `outputField.setText(org.jscience.ui.i18n.I18n.getInstance().get("converter.error", "Error"));` |

## PandemicForecasterApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\biology\PandemicForecasterApp.java`

| Line | Content |
| --- | --- |
| 89 | `System.err.println("CRITICAL: Failed to initialize PandemicForecasterApp: " + t.getMessage());` |
| 99 | `return i18n.get("pandemic.title");` |
| 104 | `return i18n.get("pandemic.desc");` |
| 149 | `log("Loaded " + countries.size() + " countries from WorldBankReader");` |
| 157 | `chartAreaTitleLabel = new Label(i18n.get("pandemic.panel.chart"));` |
| 158 | `chartAreaTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");` |
| 161 | `seirChart = ChartFactory.createLineChart(i18n.get("pandemic.panel.chart"), "Day", "Population");` |
| 165 | `susSeriesS = ChartFactory.createSeries(i18n.get("pandemic.label.susceptible"));` |
| 166 | `expSeriesE = ChartFactory.createSeries(i18n.get("pandemic.label.exposed"));` |
| 167 | `infSeriesI = ChartFactory.createSeries(i18n.get("pandemic.label.infectious"));` |
| 168 | `recSeriesR = ChartFactory.createSeries(i18n.get("pandemic.label.recovered"));` |
| 169 | `deaSeriesD = ChartFactory.createSeries(i18n.get("pandemic.label.deceased"));` |
| 170 | `deaSeriesD.setName("Deceased"); // Fallback if i18n missing` |
| 197 | `stats.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");` |
| 199 | `populationLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.population"), "--"));` |
| 200 | `peakLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));` |
| 201 | `totalLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));` |
| 202 | `deadLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), "--"));` |
| 204 | `populationLabel.setStyle("-fx-font-size: 14px;");` |
| 205 | `peakLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");` |
| 206 | `totalLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #388e3c;");` |
| 207 | `deadLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");` |
| 216 | `panel.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");` |
| 219 | `countrySelectLabel = new Label(i18n.get("pandemic.label.select"));` |
| 220 | `countrySelectLabel.setStyle("-fx-font-weight: bold;");` |
| 227 | `setText(empty \|\| item == null ? ""` |
| 228 | `: item.getName() + " (Pop: " + formatNumber(item.getPopulation()) + ")");` |
| 241 | `parametersTitleLabel = new Label(i18n.get("pandemic.panel.parameters"));` |
| 242 | `parametersTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");` |
| 245 | `VBox betaBox = createSliderWithLabel(i18n.get("pandemic.param.beta"), 0.05, 1.0, 0.3, "beta");` |
| 249 | `VBox sigmaBox = createSliderWithLabel(i18n.get("pandemic.param.sigma"), 0.05, 1.0, 0.2, "sigma");` |
| 253 | `VBox gammaBox = createSliderWithLabel(i18n.get("pandemic.param.gamma"), 0.05, 0.5, 0.1, "gamma");` |
| 257 | `VBox muBox = createSliderWithLabel(i18n.get("pandemic.param.mu"), 0.00, 0.1, 0.005, "mu");` |
| 261 | `VBox initialBox = createSliderWithLabel(i18n.get("pandemic.param.initial"), 1, 10000, 100, "initial");` |
| 266 | `VBox daysBox = createSliderWithLabel(i18n.get("pandemic.param.days"), 30, 365, 180, "days");` |
| 271 | `Label r0Label = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.r0"), "--"));` |
| 272 | `r0Label.setStyle("-fx-font-style: italic;");` |
| 277 | `logHeaderLabel = new Label(i18n.get("pandemic.log.header"));` |
| 278 | `logHeaderLabel.setStyle("-fx-font-weight: bold;");` |
| 299 | `Label nameLabel = new Label(name + ": " + String.format("%.3f", initial));` |
| 305 | `nameLabel.setText(i18n.get("pandemic.param." + paramId) + ": " + String.format("%.3f", nv.doubleValue()));` |
| 315 | `label.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.r0"), String.format("%.2f", r0)));` |
| 327 | `java.text.MessageFormat.format(i18n.get("pandemic.label.population"),` |
| 329 | `log(java.text.MessageFormat.format(i18n.get("pandemic.log.selected"), selected.getName()));` |
| 342 | `showError(i18n.get("dialog.error.title"), i18n.get("pandemic.error.select"));` |
| 368 | `log(java.text.MessageFormat.format(i18n.get("pandemic.log.start"), country.getName(), formatNumber(N),` |
| 375 | `setStatus(i18n.get("status.running"));` |
| 377 | `showError("Simulation Error", e.getMessage());` |
| 415 | `java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), formatNumber((long) maxI)));` |
| 417 | `.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), formatNumber((long) r)));` |
| 419 | `java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), formatNumber((long) dVal)));` |
| 428 | `setStatus(i18n.get("status.complete"));` |
| 430 | `log(java.text.MessageFormat.format(i18n.get("pandemic.log.complete"), currentDay));` |
| 437 | `setStatus(i18n.get("status.paused"));` |
| 452 | `setStatus(i18n.get("status.ready"));` |
| 453 | `log(i18n.get("pandemic.log.reset"));` |
| 459 | `chartAreaTitleLabel.setText(i18n.get("pandemic.panel.chart"));` |
| 461 | `countrySelectLabel.setText(i18n.get("pandemic.label.select"));` |
| 463 | `parametersTitleLabel.setText(i18n.get("pandemic.panel.parameters"));` |
| 465 | `logHeaderLabel.setText(i18n.get("pandemic.log.header"));` |
| 468 | `if (sliderLabels.get("beta") != null)` |
| 469 | `sliderLabels.get("beta").setText(i18n.get("pandemic.param.beta"));` |
| 470 | `if (sliderLabels.get("sigma") != null)` |
| 471 | `sliderLabels.get("sigma").setText(i18n.get("pandemic.param.sigma"));` |
| 472 | `if (sliderLabels.get("gamma") != null)` |
| 473 | `sliderLabels.get("gamma").setText(i18n.get("pandemic.param.gamma"));` |
| 474 | `if (sliderLabels.get("mu") != null)` |
| 475 | `sliderLabels.get("mu").setText(i18n.get("pandemic.param.mu"));` |
| 476 | `if (sliderLabels.get("initial") != null)` |
| 477 | `sliderLabels.get("initial").setText(i18n.get("pandemic.param.initial"));` |
| 478 | `if (sliderLabels.get("days") != null)` |
| 479 | `sliderLabels.get("days").setText(i18n.get("pandemic.param.days"));` |
| 482 | `seirChart.setTitle(i18n.get("pandemic.panel.chart"));` |
| 484 | `susSeriesS.setName(i18n.get("pandemic.label.susceptible"));` |
| 486 | `expSeriesE.setName(i18n.get("pandemic.label.exposed"));` |
| 488 | `infSeriesI.setName(i18n.get("pandemic.label.infectious"));` |
| 490 | `recSeriesR.setName(i18n.get("pandemic.label.recovered"));` |
| 492 | `deaSeriesD.setName(i18n.get("pandemic.label.deceased"));` |
| 511 | `peakLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));` |
| 512 | `totalLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));` |
| 513 | `deadLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), "--"));` |
| 520 | `chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));` |
| 526 | `showInfo(i18n.get("menu.file.export"), i18n.get("pandemic.info.no_data"));` |
| 530 | `if ("csv".equals(format)) {` |
| 532 | `pw.println("Day,Susceptible,Exposed,Infectious,Recovered");` |
| 534 | `pw.printf("%d,%.0f,%.0f,%.0f,%.0f%n",` |
| 541 | `log(java.text.MessageFormat.format(i18n.get("pandemic.log.export"), file.getName()));` |
| 542 | `showInfo(i18n.get("menu.file.export"),` |
| 543 | `java.text.MessageFormat.format(i18n.get("pandemic.info.export"), file.getName()));` |
| 545 | `showError("Export Error", e.getMessage());` |
| 554 | `eventLog.getItems().add(0, "[" + timestamp + "] " + message);` |
| 562 | `return String.format("%.1fB", num / 1e9);` |
| 564 | `return String.format("%.1fM", num / 1e6);` |
| 566 | `return String.format("%.1fK", num / 1e3);` |
| 577 | `dialog.addTopic("Scientific Model", "SEIR Model",` |
| 578 | `"This application uses the **SEIR** compartmental model:\n\n" +` |
| 579 | `"- **S**usceptible: Individuals who can catch the disease.\n" +` |
| 580 | `"- **E**xposed: Infected but not yet infectious (incubation).\n" +` |
| 581 | `"- **I**nfectious: Can spread the disease to Susceptibles.\n" +` |
| 582 | `"- **R**ecovered: Immune or recovered.\n" +` |
| 583 | `"- **D**eceased: Died from the disease.\n\n" +` |
| 584 | `"Parameters:\n" +` |
| 585 | `"- **Beta**: Transmission rate.\n" +` |
| 586 | `"- **Sigma**: Incubation rate (1/incubation period).\n" +` |
| 587 | `"- **Gamma**: Recovery rate (1/infectious period).\n" +` |
| 588 | `"- **Mu**: Mortality rate.",` |
| 594 | `dialog.addTopic("Tutorial", "Running a Simulation",` |
| 595 | `"1. Select a **Country** from the dropdown.\n" +` |
| 596 | `"2. Adjust the parameters (Transmission, Incubation, etc.) to match the disease characteristics.\n"` |
| 598 | `"3. Click **Run** in the toolbar to start.\n" +` |
| 599 | `"4. Use **Pause** to freeze the simulation.\n" +` |
| 600 | `"5. **Export** the data to CSV via the File menu for further analysis.",` |
| 608 | `props.setProperty("country", countrySelector.getValue().getName()); // Use name as ID for simplicity` |
| 610 | `props.setProperty("beta", String.valueOf(betaSlider.getValue()));` |
| 611 | `props.setProperty("sigma", String.valueOf(sigmaSlider.getValue()));` |
| 612 | `props.setProperty("gamma", String.valueOf(gammaSlider.getValue()));` |
| 613 | `props.setProperty("mu", String.valueOf(muSlider.getValue()));` |
| 614 | `props.setProperty("initial", String.valueOf(initialSlider.getValue()));` |
| 615 | `props.setProperty("days", String.valueOf(daysSlider.getValue()));` |
| 619 | `props.store(baos, "Pandemic State");` |
| 632 | `String countryName = props.getProperty("country");` |
| 642 | `betaSlider.setValue(Double.parseDouble(props.getProperty("beta", "0.5")));` |
| 643 | `sigmaSlider.setValue(Double.parseDouble(props.getProperty("sigma", "0.2")));` |
| 644 | `gammaSlider.setValue(Double.parseDouble(props.getProperty("gamma", "0.1")));` |
| 645 | `muSlider.setValue(Double.parseDouble(props.getProperty("mu", "0.01")));` |
| 646 | `initialSlider.setValue(Double.parseDouble(props.getProperty("initial", "1")));` |
| 647 | `daysSlider.setValue(Double.parseDouble(props.getProperty("days", "180")));` |
| 651 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |

## CrystalStructureApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\chemistry\CrystalStructureApp.java`

| Line | Content |
| --- | --- |
| 74 | `String type; // "Na", "Cl", "C", "Si"` |
| 89 | `System.err.println("CRITICAL: Failed to initialize CrystalStructureApp: " + t.getMessage());` |
| 95 | `SC("crystal.info.sc"),` |
| 96 | `BCC("crystal.info.bcc"),` |
| 97 | `FCC("crystal.info.fcc"),` |
| 98 | `HCP("crystal.info.hcp"),` |
| 99 | `DIAMOND("crystal.info.diamond"),` |
| 100 | `NACL("crystal.info.nacl"),` |
| 101 | `CSCL("crystal.info.cscl"),` |
| 102 | `CIF("crystal.info.cif");` |
| 119 | `return i18n.get("crystal.title");` |
| 124 | `return i18n.get("crystal.desc");` |
| 170 | `ss.setFill(Color.web("#222"));` |
| 195 | `box.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");` |
| 197 | `viewTitleLabel = new Label("💎 " + i18n.get("crystal.title"));` |
| 198 | `viewTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");` |
| 213 | `loadCifBtn = new Button(i18n.get("crystal.button.loadcif"));` |
| 218 | `MessageFormat.format(i18n.get("crystal.button.loadsample"), LatticeType.DIAMOND.toString()));` |
| 227 | `showAtoms = new CheckBox(i18n.get("crystal.check.atoms"));` |
| 231 | `showBonds = new CheckBox(i18n.get("crystal.check.bonds"));` |
| 235 | `showUnitCell = new CheckBox(i18n.get("crystal.check.unitcell"));` |
| 250 | `controlsTitleLabel = new Label(i18n.get("crystal.panel.controls"));` |
| 251 | `rotationLabel = new Label(i18n.get("crystal.label.rotation"));` |
| 252 | `axisXLabel = new Label(i18n.get("crystal.axis.x"));` |
| 253 | `axisYLabel = new Label(i18n.get("crystal.axis.y"));` |
| 275 | `viewTitleLabel.setText("💎 " + i18n.get("crystal.title"));` |
| 277 | `controlsTitleLabel.setText(i18n.get("crystal.panel.controls"));` |
| 279 | `loadCifBtn.setText(i18n.get("crystal.button.loadcif"));` |
| 282 | `MessageFormat.format(i18n.get("crystal.button.loadsample"), LatticeType.DIAMOND.toString()));` |
| 284 | `showAtoms.setText(i18n.get("crystal.check.atoms"));` |
| 286 | `showBonds.setText(i18n.get("crystal.check.bonds"));` |
| 288 | `showUnitCell.setText(i18n.get("crystal.check.unitcell"));` |
| 290 | `rotationLabel.setText(i18n.get("crystal.label.rotation"));` |
| 292 | `axisXLabel.setText(i18n.get("crystal.axis.x"));` |
| 294 | `axisYLabel.setText(i18n.get("crystal.axis.y"));` |
| 314 | `infoLabel.setText(MessageFormat.format(i18n.get("crystal.details.formula"), cif.chemicalFormula) + "\n" +` |
| 315 | `"a=" + cif.a + ", b=" + cif.b + ", c=" + cif.c + "\n" +` |
| 316 | `"alpha=" + cif.alpha + ", beta=" + cif.beta + ", gamma=" + cif.gamma);` |
| 369 | `if (cif.chemicalFormula != null && cif.chemicalFormula.contains("Na"))` |
| 401 | `infoLabel.setText(i18n.get("crystal.error.load") + ": " + e.getMessage());` |
| 408 | `fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("CIF Files", "*.cif"));` |
| 420 | `try (java.io.InputStream is = getClass().getResourceAsStream("data/diamond.cif")) {` |
| 424 | `infoLabel.setText(i18n.get("crystal.error.sample"));` |
| 441 | `infoLabel.setText("");` |
| 450 | `infoLabel.setText(i18n.get("crystal.details.sc"));` |
| 454 | `atoms.add(new AtomRecord(i, j, k, "Po"));` |
| 457 | `infoLabel.setText(i18n.get("crystal.details.bcc"));` |
| 462 | `atoms.add(new AtomRecord(i, j, k, "Fe"));` |
| 465 | `atoms.add(new AtomRecord(i + 0.5, j + 0.5, k + 0.5, "Fe"));` |
| 469 | `infoLabel.setText(i18n.get("crystal.details.fcc"));` |
| 473 | `atoms.add(new AtomRecord(i, j, k, "Cu"));` |
| 475 | `atoms.add(new AtomRecord(i + 0.5, j + 0.5, k, "Cu"));` |
| 476 | `atoms.add(new AtomRecord(i + 0.5, j, k + 0.5, "Cu"));` |
| 477 | `atoms.add(new AtomRecord(i, j + 0.5, k + 0.5, "Cu"));` |
| 481 | `infoLabel.setText(i18n.get("crystal.details.nacl"));` |
| 487 | `infoLabel.setText(i18n.get("crystal.details.diamond"));` |
| 491 | `addDiamondUnit(atoms, i, j, k, "C");` |
| 493 | `infoLabel.setText(i18n.get("crystal.details.cscl"));` |
| 494 | `atoms.add(new AtomRecord(0, 0, 0, "Cl"));` |
| 495 | `atoms.add(new AtomRecord(0.5, 0.5, 0.5, "Cs"));` |
| 540 | `atoms.add(new AtomRecord(dx, dy, dz, "Na"));` |
| 541 | `atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz, "Na"));` |
| 542 | `atoms.add(new AtomRecord(dx + 0.5, dy, dz + 0.5, "Na"));` |
| 543 | `atoms.add(new AtomRecord(dx, dy + 0.5, dz + 0.5, "Na"));` |
| 545 | `atoms.add(new AtomRecord(dx + 0.5, dy, dz, "Cl"));` |
| 546 | `atoms.add(new AtomRecord(dx, dy + 0.5, dz, "Cl"));` |
| 547 | `atoms.add(new AtomRecord(dx, dy, dz + 0.5, "Cl"));` |
| 548 | `atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz + 0.5, "Cl"));` |
| 565 | `case "C":` |
| 566 | `m.setDiffuseColor(Color.web("#333333")); // Dark Grey for Carbon` |
| 568 | `case "Si":` |
| 569 | `m.setDiffuseColor(Color.web("#666666"));` |
| 571 | `case "Na":` |
| 574 | `case "Cl":` |
| 577 | `case "Cs":` |
| 617 | `dialog.addTopic("Structures", "Lattice Types",` |
| 618 | `"Explore various crystal lattice structures:\n\n" +` |
| 619 | `"Ã¢â‚¬Â¢ **Simple Cubic (SC)**: Simplest repeating unit (e.g. Polonium).\n" +` |
| 620 | `"Ã¢â‚¬Â¢ **Body-Centered Cubic (BCC)**: Atoms at corners and center (e.g. Iron).\n" +` |
| 621 | `"Ã¢â‚¬Â¢ **Face-Centered Cubic (FCC)**: Atoms at corners and faces (e.g. Copper).\n" +` |
| 622 | `"Ã¢â‚¬Â¢ **Diamond**: Tetrahedral coordination (e.g. Carbon).\n" +` |
| 623 | `"Ã¢â‚¬Â¢ **NaCl**: Rock salt structure.\n" +` |
| 624 | `"Ã¢â‚¬Â¢ **CsCl**: Cesium Chloride structure.",` |
| 630 | `dialog.addTopic("Tutorial", "Navigating 3D Space",` |
| 631 | `"1. **Rotate**: Drag with the mouse to rotate the crystal structure.\n" +` |
| 632 | `"2. **Zoom**: Use the scroll wheel to zoom in and out.\n" +` |
| 633 | `"3. **Select Structure**: Use the dropdown menu to switch between lattice types.\n" +` |
| 634 | `"4. **Toggles**: Show/Hide Atoms, Bonds, or Unit Cell outlines.\n" +` |
| 635 | `"5. **Load CIF**: Import custom .cif files for advanced visualization.",` |
| 643 | `props.setProperty("lattice", latticeCombo.getValue().name());` |
| 645 | `props.setProperty("showAtoms", String.valueOf(showAtoms.isSelected()));` |
| 646 | `props.setProperty("showBonds", String.valueOf(showBonds.isSelected()));` |
| 647 | `props.setProperty("showUnitCell", String.valueOf(showUnitCell.isSelected()));` |
| 648 | `props.setProperty("rotateX", String.valueOf(rotateXSlider.getValue()));` |
| 649 | `props.setProperty("rotateY", String.valueOf(rotateYSlider.getValue()));` |
| 653 | `props.store(baos, "Crystal State");` |
| 666 | `String latticeName = props.getProperty("lattice");` |
| 671 | `showAtoms.setSelected(Boolean.parseBoolean(props.getProperty("showAtoms", "true")));` |
| 672 | `showBonds.setSelected(Boolean.parseBoolean(props.getProperty("showBonds", "true")));` |
| 673 | `showUnitCell.setSelected(Boolean.parseBoolean(props.getProperty("showUnitCell", "true")));` |
| 674 | `rotateXSlider.setValue(Double.parseDouble(props.getProperty("rotateX", "0")));` |
| 675 | `rotateYSlider.setValue(Double.parseDouble(props.getProperty("rotateY", "0")));` |
| 679 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |

## TitrationApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\chemistry\TitrationApp.java`

| Line | Content |
| --- | --- |
| 61 | `System.err.println("CRITICAL: Failed to initialize TitrationApp: " + t.getMessage());` |
| 68 | `HCL("titration.acid.hcl", new double[] { -7.0 }), // Strong acid, effectively complete dissociation` |
| 69 | `H2SO4("titration.acid.h2so4", new double[] { -3.0, 1.99 }), // Diprotic` |
| 70 | `H3PO4("titration.acid.h3po4", new double[] { 2.15, 7.20, 12.35 }), // Triprotic` |
| 71 | `H2CO3("titration.acid.h2co3", new double[] { 6.35, 10.33 }), // Diprotic` |
| 72 | `ACETIC("titration.acid.acetic", new double[] { 4.76 }); // Weak monoprotic` |
| 111 | `PHENOLPHTHALEIN("titration.ind.phen", 8.2, 10.0, Color.TRANSPARENT, Color.DEEPPINK),` |
| 112 | `METHYL_ORANGE("titration.ind.methyl", 3.1, 4.4, Color.RED, Color.YELLOW),` |
| 113 | `BROMOTHYMOL_BLUE("titration.ind.brom", 6.0, 7.6, Color.YELLOW, Color.BLUE),` |
| 114 | `LITMUS("titration.ind.litmus", 5.0, 8.0, Color.RED, Color.BLUE),` |
| 115 | `UNIVERSAL("titration.ind.univ", 1.0, 14.0, Color.RED, Color.VIOLET);` |
| 141 | `return i18n.get("titration.title");` |
| 146 | `return i18n.get("titration.desc");` |
| 176 | `box.setStyle("-fx-background-color: #ddd;");` |
| 191 | `phChart = ChartFactory.createLineChart(i18n.get("titration.panel.chart"),` |
| 192 | `i18n.get("titration.label.volume"), i18n.get("titration.label.ph_axis"));` |
| 194 | `phSeries.setName(i18n.get("titration.series.ph"));` |
| 204 | `phLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");` |
| 233 | `setupTitleLabel = new Label(i18n.get("titration.panel.setup"));` |
| 234 | `setupTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");` |
| 235 | `acidTypeLabel = new Label(i18n.get("titration.label.acidtype"));` |
| 236 | `indicatorLabel = new Label(i18n.get("titration.label.indicator"));` |
| 237 | `titrantLabel = new Label(i18n.get("titration.label.titrant"));` |
| 371 | `phLabel.setText(MessageFormat.format(i18n.get("titration.label.ph"), String.format("%.2f", ph)));` |
| 373 | `MessageFormat.format(i18n.get("titration.label.voladded"), String.format("%.2f", volumeBaseAdded)));` |
| 386 | `setupTitleLabel.setText(i18n.get("titration.panel.setup"));` |
| 388 | `acidTypeLabel.setText(i18n.get("titration.label.acidtype"));` |
| 390 | `indicatorLabel.setText(i18n.get("titration.label.indicator"));` |
| 392 | `titrantLabel.setText(i18n.get("titration.label.titrant"));` |
| 395 | `phChart.setTitle(i18n.get("titration.panel.chart"));` |
| 397 | `((NumberAxis) phChart.getXAxis()).setLabel(i18n.get("titration.label.volume"));` |
| 400 | `((NumberAxis) phChart.getYAxis()).setLabel(i18n.get("titration.label.ph_axis"));` |
| 403 | `phChart.getData().get(0).setName(i18n.get("titration.series.ph"));` |
| 436 | `gc.setFill(Color.web("#ccf", 0.5));` |
| 484 | `dialog.addTopic("Chemistry", "Acid-Base Titration",` |
| 485 | `"Simulate titration experiments:\n\n" +` |
| 486 | `"\u2022 **Titration**: Determining concentration of an acid by neutralizing it with a base of known concentration.\n"` |
| 488 | `"\u2022 **Equivalence Point**: Where moles of acid equals moles of base (adjusted for stoichiometry).\n"` |
| 490 | `"\u2022 **Indicators**: Change color at specific pH ranges to visually signal the end point.\n"` |
| 492 | `"\u2022 **pH Curve**: Shows the change in pH as titrant is added.",` |
| 498 | `dialog.addTopic("Tutorial", "Performing a Titration",` |
| 499 | `"1. **Select Acid**: Choose the acid type (e.g., HCl, H2SO4).\n" +` |
| 500 | `"2. **Select Indicator**: Choose an appropriate indicator for the expected pH change.\n" +` |
| 501 | `"3. **Open Valve**: Move the slider to start adding the base titrant.\n" +` |
| 502 | `"4. **Observe**: Watch the beaker color and the pH curve.\n" +` |
| 503 | `"5. **Stop**: Close the valve when the color changes permanently.",` |
| 510 | `props.setProperty("volumeBaseAdded", String.valueOf(volumeBaseAdded));` |
| 512 | `props.setProperty("acid", selectedAcid.name());` |
| 515 | `props.setProperty("indicator", indicatorSelector.getValue().name());` |
| 517 | `props.setProperty("valve", String.valueOf(valveSlider.getValue()));` |
| 521 | `props.store(baos, "Titration State");` |
| 534 | `volumeBaseAdded = Double.parseDouble(props.getProperty("volumeBaseAdded", "0"));` |
| 536 | `String acidName = props.getProperty("acid");` |
| 542 | `String indName = props.getProperty("indicator");` |
| 547 | `valveSlider.setValue(Double.parseDouble(props.getProperty("valve", "0")));` |
| 553 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |

## MarketCrashApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\economics\MarketCrashApp.java`

| Line | Content |
| --- | --- |
| 78 | `System.err.println("CRITICAL: Failed to initialize MarketCrashApp: " + t.getMessage());` |
| 85 | `return i18n.get("market.title");` |
| 90 | `return i18n.get("market.desc");` |
| 126 | `chartAreaTitleLabel = new Label(i18n.get("market.panel.chart"));` |
| 127 | `chartAreaTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");` |
| 129 | `priceChart = ChartFactory.createLineChart(i18n.get("market.panel.chart"), i18n.get("market.label.day"),` |
| 130 | `i18n.get("market.label.price_axis"));` |
| 135 | `priceSeries = ChartFactory.createSeries(i18n.get("market.series.price"));` |
| 136 | `smaSeries = ChartFactory.createSeries(i18n.get("market.series.sma"));` |
| 137 | `upperBB = ChartFactory.createSeries(i18n.get("market.series.upperbb"));` |
| 138 | `lowerBB = ChartFactory.createSeries(i18n.get("market.series.lowerbb"));` |
| 144 | `indicatorsTitleLabel = new Label(i18n.get("market.indicator.rsi"));` |
| 145 | `indicatorsTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");` |
| 147 | `rsiChart = ChartFactory.createLineChart("", i18n.get("market.label.day"), i18n.get("market.indicator.rsi"));` |
| 154 | `rsiSeries = ChartFactory.createSeries(i18n.get("market.indicator.rsi"));` |
| 164 | `panel.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");` |
| 167 | `riskTitleLabel = new Label(i18n.get("market.risk.title"));` |
| 168 | `riskTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");` |
| 170 | `riskLabel = new Label(i18n.get("market.risk.none"));` |
| 171 | `riskLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");` |
| 174 | `riskBox.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 8; -fx-padding: 15;");` |
| 177 | `statsTitleLabel = new Label(i18n.get("market.stats.title"));` |
| 178 | `statsTitleLabel.setStyle("-fx-font-weight: bold;");` |
| 180 | `currentPriceLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.price"), "--"));` |
| 181 | `smaLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.sma"), 20, "--"));` |
| 182 | `rsiLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.rsi"), 14, "--"));` |
| 185 | `statsBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-background-radius: 5;");` |
| 188 | `settingsTitleLabel = new Label(i18n.get("market.settings.title"));` |
| 189 | `settingsTitleLabel.setStyle("-fx-font-weight: bold;");` |
| 191 | `VBox smaBox = createSliderBox(i18n.get("market.slider.sma"), 5, 50, 20);` |
| 194 | `VBox rsiBox = createSliderBox(i18n.get("market.slider.rsi"), 7, 21, 14);` |
| 197 | `showSMA = new CheckBox(i18n.get("market.check.sma"));` |
| 201 | `showBollinger = new CheckBox(i18n.get("market.check.bb"));` |
| 209 | `logTitleLabel = new Label(i18n.get("market.log.title"));` |
| 210 | `logTitleLabel.setStyle("-fx-font-weight: bold;");` |
| 223 | `Label label = new Label(name + " " + (int) initial);` |
| 227 | `slider.valueProperty().addListener((o, ov, nv) -> label.setText(name + " " + nv.intValue()));` |
| 234 | `try (var is = getClass().getResourceAsStream("data/SP500_Sample.csv")) {` |
| 236 | `marketData = FinancialMarketReader.loadCSV(is, "USD");` |
| 237 | `log(java.text.MessageFormat.format(i18n.get("market.log.loaded"), marketData.size()));` |
| 242 | `log(i18n.get("market.error.load", e.getMessage()));` |
| 248 | `log("Sample data not found. Generating synthetic market data...");` |
| 283 | `log(java.text.MessageFormat.format(i18n.get("market.log.loaded"), marketData.size()));` |
| 303 | `setStatus(i18n.get("status.running"));` |
| 344 | `.setText(java.text.MessageFormat.format(i18n.get("market.label.price"), String.format("$%.2f", close)));` |
| 350 | `smaLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.sma"),` |
| 351 | `(int) smaPeriodSlider.getValue(), String.format("$%.2f", sma.doubleValue())));` |
| 357 | `rsiLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.rsi"),` |
| 358 | `(int) rsiPeriodSlider.getValue(), String.format("%.1f", rsiVal)));` |
| 361 | `log(java.text.MessageFormat.format(i18n.get("market.log.oversold"), currentIndex,` |
| 362 | `String.format("%.1f", rsiVal)));` |
| 364 | `log(java.text.MessageFormat.format(i18n.get("market.log.overbought"), currentIndex,` |
| 365 | `String.format("%.1f", rsiVal)));` |
| 405 | `riskText = i18n.get("market.risk.extreme");` |
| 407 | `log(java.text.MessageFormat.format(i18n.get("market.log.extreme"), currentIndex));` |
| 409 | `riskText = i18n.get("market.risk.high");` |
| 412 | `riskText = i18n.get("market.risk.moderate");` |
| 415 | `riskText = i18n.get("market.risk.low");` |
| 426 | `setStatus(i18n.get("status.complete"));` |
| 428 | `log(java.text.MessageFormat.format(i18n.get("market.log.complete"), currentIndex));` |
| 435 | `setStatus(i18n.get("status.paused"));` |
| 446 | `chartAreaTitleLabel.setText(i18n.get("market.panel.chart"));` |
| 448 | `indicatorsTitleLabel.setText(i18n.get("market.indicator.rsi"));` |
| 450 | `riskTitleLabel.setText(i18n.get("market.risk.title"));` |
| 452 | `statsTitleLabel.setText(i18n.get("market.stats.title"));` |
| 454 | `settingsTitleLabel.setText(i18n.get("market.settings.title"));` |
| 456 | `logTitleLabel.setText(i18n.get("market.log.title"));` |
| 459 | `priceChart.setTitle(i18n.get("market.panel.chart"));` |
| 461 | `priceSeries.setName(i18n.get("market.series.price"));` |
| 463 | `smaSeries.setName(i18n.get("market.series.sma"));` |
| 465 | `upperBB.setName(i18n.get("market.series.upperbb"));` |
| 467 | `lowerBB.setName(i18n.get("market.series.lowerbb"));` |
| 470 | `rsiChart.setTitle(i18n.get("market.indicator.rsi"));` |
| 474 | `showSMA.setText(i18n.get("market.check.sma"));` |
| 476 | `showBollinger.setText(i18n.get("market.check.bb"));` |
| 482 | `riskLabel.setText(i18n.get("market.risk.none"));` |
| 483 | `currentPriceLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.price"), "--"));` |
| 484 | `smaLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.sma"),` |
| 485 | `(int) smaPeriodSlider.getValue(), "--"));` |
| 486 | `rsiLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.rsi"),` |
| 487 | `(int) rsiPeriodSlider.getValue(), "--"));` |
| 496 | `riskLabel.setText("--");` |
| 497 | `setStatus(i18n.get("status.ready"));` |
| 510 | `alertLog.getItems().add(0, "[" + ts + "] " + msg);` |
| 518 | `chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));` |
| 528 | `dialog.addTopic("Economics", "Market Analysis",` |
| 529 | `"Technical analysis tools included:\n\n" +` |
| 530 | `"- **SMA (Simple Moving Average)**: Average price over a specific period (trend).\n" +` |
| 531 | `"- **RSI (Relative Strength Index)**: Momentum oscillator measuring speed and change of price movements.\n"` |
| 533 | `"- **Bollinger Bands**: Volatility bands placed above and below a moving average.\n" +` |
| 534 | `"- **Volume**: Total number of shares traded (if available).",` |
| 540 | `dialog.addTopic("Tutorial", "Predicting Crashes",` |
| 541 | `"1. **Load Data**: The app loads S&P 500 data automatically.\n" +` |
| 542 | `"2. **Run Simulation**: Click **Run** to replay historical market data.\n" +` |
| 543 | `"3. **Monitor Risk**: Watch the 'Risk Assessment' panel. High risk indicates potential crash conditions.\n"` |
| 545 | `"4. **Analyze Indicators**: Look for divergence between Price and RSI, or price breaking Bollinger Bands.\n"` |
| 547 | `"5. **Adjust Parameters**: Use sliders to tune SMA and RSI periods for different sensitivities.",` |
| 554 | `props.setProperty("currentIndex", String.valueOf(currentIndex));` |
| 555 | `props.setProperty("smaPeriod", String.valueOf(smaPeriodSlider.getValue()));` |
| 556 | `props.setProperty("rsiPeriod", String.valueOf(rsiPeriodSlider.getValue()));` |
| 557 | `props.setProperty("showSMA", String.valueOf(showSMA.isSelected()));` |
| 558 | `props.setProperty("showBollinger", String.valueOf(showBollinger.isSelected()));` |
| 562 | `props.store(baos, "Market State");` |
| 575 | `currentIndex = Integer.parseInt(props.getProperty("currentIndex", "0"));` |
| 576 | `smaPeriodSlider.setValue(Double.parseDouble(props.getProperty("smaPeriod", "20")));` |
| 577 | `rsiPeriodSlider.setValue(Double.parseDouble(props.getProperty("rsiPeriod", "14")));` |
| 578 | `showSMA.setSelected(Boolean.parseBoolean(props.getProperty("showSMA", "true")));` |
| 579 | `showBollinger.setSelected(Boolean.parseBoolean(props.getProperty("showBollinger", "true")));` |
| 590 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |

## SmartGridApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\engineering\SmartGridApp.java`

| Line | Content |
| --- | --- |
| 93 | `System.err.println("CRITICAL: Failed to initialize SmartGridApp: " + t.getMessage());` |
| 100 | `return i18n.get("grid.title");` |
| 105 | `return i18n.get("grid.desc");` |
| 137 | `vizTitleLabel = new Label(i18n.get("grid.viz.title"));` |
| 138 | `vizTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");` |
| 152 | `loadChart = ChartFactory.createAreaChart(i18n.get("grid.chart.title"), i18n.get("grid.label.time"),` |
| 153 | `i18n.get("grid.label.power"));` |
| 158 | `supplySeries.setName(i18n.get("grid.series.supply"));` |
| 161 | `demandSeries.setName(i18n.get("grid.series.demand"));` |
| 171 | `frequencyLabel = new Label(java.text.MessageFormat.format(i18n.get("grid.label.freq"), 50.00));` |
| 172 | `frequencyLabel.setStyle("-fx-font-size: 24px; -fx-font-family: monospace; -fx-font-weight: bold;");` |
| 174 | `statusLabel = new Label(i18n.get("grid.status.stable"));` |
| 176 | `"-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");` |
| 181 | `statusBox.getChildren().addAll(new Label(i18n.get("grid.label.freq_short")), frequencyLabel, loadBalanceBar,` |
| 192 | `box.setStyle("-fx-background-color: #f5f5f5;"); // Light control panel theme` |
| 194 | `controlRoomTitleLabel = new Label(i18n.get("grid.panel.control"));` |
| 195 | `controlRoomTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");` |
| 198 | `coalLabel = new Label(i18n.get("grid.control.coal"));` |
| 203 | `windLabel = new Label(i18n.get("grid.control.wind"));` |
| 208 | `solarLabel = new Label(i18n.get("grid.control.solar"));` |
| 213 | `demandLabel = new Label(i18n.get("grid.control.demand"));` |
| 219 | `batteryTitleLabel = new Label(i18n.get("grid.label.battery_title"));` |
| 221 | `batteryTitleLabel.setStyle("-fx-font-weight: bold;");` |
| 225 | `batteryBar.setStyle("-fx-accent: #27ae60;");` |
| 228 | `MessageFormat.format(i18n.get("grid.label.battery_charge"), batteryCharge, batteryCapacity));` |
| 241 | `lbl.setStyle("-fx-font-weight: bold;");` |
| 247 | `Label valLbl = new Label(MessageFormat.format(i18n.get("grid.label.mw_value"), val));` |
| 250 | `(o, ov, nv) -> valLbl.setText(MessageFormat.format(i18n.get("grid.label.mw_value"), nv.doubleValue())));` |
| 265 | `setStatus(i18n.get("status.running"));` |
| 318 | `.setText(MessageFormat.format(i18n.get("grid.label.battery_charge"), batteryCharge, batteryCapacity));` |
| 336 | `MessageFormat.format(i18n.get("grid.log.blackout_reason"), String.format("%.2f", gridFrequency)));` |
| 352 | `frequencyLabel.setText(java.text.MessageFormat.format(i18n.get("grid.label.freq"), gridFrequency));` |
| 356 | `statusLabel.setText(i18n.get("grid.status.stable"));` |
| 358 | `"-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");` |
| 360 | `statusLabel.setText(i18n.get("grid.status.unstable"));` |
| 362 | `"-fx-font-size: 18px; -fx-background-color: #FF5722; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");` |
| 379 | `gc.setFill(Color.web("#eee"));` |
| 396 | `gc.fillText(i18n.get("grid.viz.city") + "\n" + (int) currentDemand + "MW", cx - 15, cy);` |
| 402 | `gc.fillText(i18n.get("grid.viz.wind") + "\n" + (int) (windF * 100) + "%", cx - 95, cy - 100);` |
| 408 | `gc.fillText(i18n.get("grid.viz.sun") + "\n" + (int) (solarF * 100) + "%", cx + 65, cy - 100);` |
| 414 | `gc.fillText(i18n.get("grid.viz.plant"), cx - 15, cy + 125);` |
| 419 | `statusLabel.setText(i18n.get("grid.status.blackout"));` |
| 421 | `"-fx-font-size: 18px; -fx-background-color: #000000; -fx-text-fill: red; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");` |
| 422 | `log(MessageFormat.format(i18n.get("grid.log.blackout"), reason));` |
| 444 | `setStatus(i18n.get("status.paused"));` |
| 451 | `setStatus(i18n.get("status.complete"));` |
| 466 | `dialog.addTopic("Engineering", "Smart Grid Concepts",` |
| 467 | `"Understand the power grid:\n\n" +` |
| 468 | `"- **Supply vs Demand**: Grid frequency deviates when supply != demand.\n" +` |
| 469 | `"- **Frequency**: Must stay near 50Hz. <48Hz risks blackout.\n" +` |
| 470 | `"- **Renewables**: Intermittent power sources (Wind/Solar) require backup or storage.\n" +` |
| 471 | `"- **Battery Storage**: Absorbs excess power and releases it during deficits.",` |
| 477 | `dialog.addTopic("Tutorial", "Balancing the Grid",` |
| 478 | `"1. **Monitor Frequency**: Keep the frequency gauge near 50Hz (Green Zone).\n" +` |
| 479 | `"2. **Adjust Supply**: Use sliders to increase Coal, Wind, or Solar output to match Demand.\n" +` |
| 480 | `"3. **Watch Batteries**: Batteries buffer small fluctuations but have limited capacity.\n" +` |
| 481 | `"4. **Avoid Blackouts**: If frequency drops below 48Hz or spikes above 52Hz, the grid trips.\n"` |
| 483 | `"5. **Reset**: Use the Reset button to restart after a blackout.",` |
| 490 | `props.setProperty("time", String.valueOf(time));` |
| 491 | `props.setProperty("coal", String.valueOf(coalOutputSlider.getValue()));` |
| 492 | `props.setProperty("wind", String.valueOf(windCapacitySlider.getValue()));` |
| 493 | `props.setProperty("solar", String.valueOf(solarCapacitySlider.getValue()));` |
| 494 | `props.setProperty("demand", String.valueOf(demandScaleSlider.getValue()));` |
| 495 | `props.setProperty("batteryCharge", String.valueOf(batteryCharge));` |
| 496 | `props.setProperty("frequency", String.valueOf(gridFrequency));` |
| 497 | `props.setProperty("blackout", String.valueOf(blackout));` |
| 501 | `props.store(baos, "Smart Grid State");` |
| 514 | `time = Double.parseDouble(props.getProperty("time", "0"));` |
| 515 | `coalOutputSlider.setValue(Double.parseDouble(props.getProperty("coal", "500")));` |
| 516 | `windCapacitySlider.setValue(Double.parseDouble(props.getProperty("wind", "100")));` |
| 517 | `solarCapacitySlider.setValue(Double.parseDouble(props.getProperty("solar", "100")));` |
| 518 | `demandScaleSlider.setValue(Double.parseDouble(props.getProperty("demand", "100")));` |
| 519 | `batteryCharge = Double.parseDouble(props.getProperty("batteryCharge", "100"));` |
| 520 | `gridFrequency = Double.parseDouble(props.getProperty("frequency", "50"));` |
| 521 | `blackout = Boolean.parseBoolean(props.getProperty("blackout", "false"));` |
| 529 | `triggerBlackout("Restored from file");` |
| 532 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |
| 539 | `vizTitleLabel.setText(i18n.get("grid.viz.title"));` |
| 541 | `controlRoomTitleLabel.setText(i18n.get("grid.panel.control"));` |
| 543 | `coalLabel.setText(i18n.get("grid.control.coal"));` |
| 545 | `windLabel.setText(i18n.get("grid.control.wind"));` |
| 547 | `solarLabel.setText(i18n.get("grid.control.solar"));` |
| 549 | `demandLabel.setText(i18n.get("grid.control.demand"));` |
| 551 | `batteryTitleLabel.setText(i18n.get("grid.label.battery_title"));` |
| 554 | `loadChart.setTitle(i18n.get("grid.chart.title"));` |
| 555 | `loadChart.getXAxis().setLabel(i18n.get("grid.label.time"));` |
| 556 | `loadChart.getYAxis().setLabel(i18n.get("grid.label.power"));` |
| 558 | `supplySeries.setName(i18n.get("grid.series.supply"));` |
| 560 | `demandSeries.setName(i18n.get("grid.series.demand"));` |

## QuantumCircuitApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\QuantumCircuitApp.java`

| Line | Content |
| --- | --- |
| 55 | `System.err.println("CRITICAL: Failed to initialize QuantumCircuitApp: " + t.getMessage());` |
| 71 | `return i18n.get("quantum.title");` |
| 76 | `return i18n.get("quantum.desc");` |
| 95 | `mainTitleLabel = new Label(i18n.get("quantum.title"));` |
| 105 | `gateToolbarLabel = new Label(i18n.get("quantum.toolbar.gates"));` |
| 108 | `String[] gates = { "H", "X", "Y", "Z", "CNOT", "M" };` |
| 111 | `b.setTooltip(new Tooltip(i18n.get("quantum.gate.tooltip." + g.toLowerCase())));` |
| 117 | `runBtn = new Button(i18n.get("quantum.button.run"));` |
| 118 | `runBtn.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");` |
| 122 | `clearBtn = new Button(i18n.get("quantum.button.clear"));` |
| 129 | `circuitPane.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");` |
| 136 | `resultsTitleLabel = new Label(i18n.get("quantum.label.results"));` |
| 140 | `stateVectorLabel.setFont(Font.font("Monospaced", 12));` |
| 145 | `probChart.setTitle(i18n.get("quantum.chart.probabilities"));` |
| 164 | `line.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #eee;");` |
| 167 | `ToggleButton qBtn = new ToggleButton(i18n.get("quantum.label.qubit_title", i));` |
| 201 | `gateBtn.setStyle("-fx-base: #e0f0ff;");` |
| 210 | `stateVectorLabel.setText(i18n.get("quantum.label.statevector") + ": \|0...0>");` |
| 220 | `series.setName(i18n.get("quantum.series.probabilities"));` |
| 231 | `String state = String.format("\|%3s>", Integer.toBinaryString(i)).replace(' ', '0');` |
| 236 | `.setText(i18n.get("quantum.label.statevector") + ": " + i18n.get("quantum.status.superposition"));` |
| 243 | `mainTitleLabel.setText(i18n.get("quantum.title"));` |
| 245 | `gateToolbarLabel.setText(i18n.get("quantum.toolbar.gates"));` |
| 247 | `runBtn.setText(i18n.get("quantum.button.run"));` |
| 249 | `clearBtn.setText(i18n.get("quantum.button.clear"));` |
| 251 | `resultsTitleLabel.setText(i18n.get("quantum.label.results"));` |
| 254 | `.setText(i18n.get("quantum.label.statevector") + ": " + i18n.get("quantum.status.superposition"));` |
| 256 | `stateVectorLabel.setText(i18n.get("quantum.label.statevector") + ": \|0...0>");` |
| 260 | `probChart.setTitle(i18n.get("quantum.chart.probabilities"));` |
| 262 | `probChart.getData().get(0).setName(i18n.get("quantum.series.probabilities"));` |
| 270 | `((ToggleButton) line.getChildren().get(0)).setText(i18n.get("quantum.label.qubit_title", i));` |
| 282 | `dialog.addTopic("Physics", "Quantum Computing",` |
| 283 | `"Learn about quantum circuits:\n\n" +` |
| 284 | `"- **Qubit**: Quantum bit, exists in superposition of \|0> and \|1>.\n" +` |
| 285 | `"- **H Gate (Hadamard)**: Puts a qubit into superposition (50/50 probability).\n" +` |
| 286 | `"- **X Gate**: Quantum NOT gate, flips \|0> to \|1> and vice versa.\n" +` |
| 287 | `"- **CNOT**: Conditional NOT, entangles two qubits.\n" +` |
| 288 | `"- **Measurement**: Collapses the quantum state to a classical bit.",` |
| 294 | `dialog.addTopic("Tutorial", "Building a Circuit",` |
| 295 | `"1. **Select Qubit**: Click a qubit line (e.g., q0) to select it.\n" +` |
| 296 | `"2. **Add Gate**: Click a gate button (H, X, etc.) to add it to the selected line.\n" +` |
| 297 | `"3. **Run**: Click the Run button to simulate measurement outcome probabilities.\n" +` |
| 298 | `"4. **Clear**: Use Clear to reset the circuit.\n" +` |
| 299 | `"5. **Bell State**: Try H on q0, then CNOT on q0-q1 (not fully supported in visual editor yet) to create entanglement.",` |
| 313 | `sb.append(((Button) node).getText()).append(",");` |
| 316 | `props.setProperty("line." + i, sb.toString());` |
| 321 | `props.store(baos, "Quantum State");` |
| 335 | `String lineData = props.getProperty("line." + i);` |
| 337 | `String[] gates = lineData.split(",");` |
| 349 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |

## SpinValveApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\SpinValveApp.java`

| Line | Content |
| --- | --- |
| 92 | `System.err.println("CRITICAL: Failed to initialize SpinValveApp: " + t.getMessage());` |
| 99 | `return i18n.get("spintronics.title");` |
| 117 | `Tab vizTab = new Tab(i18n.get("spintronics.tab.visualization"));` |
| 124 | `Tab chartTab = new Tab(i18n.get("spintronics.tab.chart"));` |
| 131 | `Tab spectrumTab = new Tab("RF Spectrum");` |
| 137 | `freqAxis.setLabel("Frequency (GHz)");` |
| 139 | `powerAxis.setLabel("Power (a.u.)");` |
| 141 | `spectrumChart.setTitle("Magnetization RF Spectrum");` |
| 145 | `spectrumSeries.setName("PSD");` |
| 148 | `peakFreqLabel = new Label("Peak: -- GHz");` |
| 149 | `peakFreqLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");` |
| 156 | `Tab magTab = new Tab(i18n.get("spintronics.tab.visualization") + " (Field)");` |
| 218 | `pmaCheckBox = new CheckBox("Enable PMA (Perpendicular)");` |
| 240 | `safCheckBox = new CheckBox("Enable SAF (Co/Ru/Co)");` |
| 246 | `Button hysteresisBtn = new Button("Run Hysteresis Loop");` |
| 250 | `Button exportBtn = new Button("Export Trace (CSV)");` |
| 251 | `exportBtn.getStyleClass().add("button-secondary");` |
| 256 | `new Label(i18n.get("spintronics.label.pinned_layer")), pinnedMaterialCombo, pinnedThicknessSlider,` |
| 259 | `new Label(i18n.get("spintronics.label.spacer_layer")), spacerMaterialCombo, spacerThicknessSlider,` |
| 261 | `new Label(i18n.get("spintronics.label.free_layer")), freeMaterialCombo, freeThicknessSlider,` |
| 262 | `new Label(i18n.get("spintronics.label.angle")), freeAngleSlider,` |
| 263 | `new HBox(10, new Label("Damping (\u03B1)"), dampingSlider),` |
| 266 | `new Label("SOT Physics (SHE)"),` |
| 267 | `new HBox(10, new Label("J_SOT"), sotCurrentSlider),` |
| 268 | `new HBox(10, new Label("\u03B8_SH"), sotHallSlider),` |
| 270 | `new Label("Geometry"),` |
| 271 | `new HBox(10, new Label("Area (nm)"), areaSlider),` |
| 273 | `new Label("Temperature (K)"), temperatureSlider,` |
| 302 | `sb.append("* Spin Valve App Circuit\n");` |
| 303 | `sb.append("V1 1 0 0.5 DC\n"); // 500mV bias` |
| 304 | `sb.append("R1 1 2 50\n");     // 50 Ohm source impedance` |
| 305 | `sb.append("MTJ1 2 0 0\n");    // MTJ from node 2 to ground` |
| 306 | `sb.append(".TRAN 0 10n 1p\n");` |
| 307 | `sb.append(".END\n");` |
| 311 | `simulator.registerPhysicsModel("MTJ1", spinValve);` |
| 320 | `resistanceValueLabel = new Label("0.0");` |
| 321 | `gmrRatioLabel = new Label("0.0 %");` |
| 322 | `sttLabel = new Label("0.0");` |
| 323 | `statusLabelInfo = new Label("Ready");` |
| 326 | `new VBox(5, new Label(i18n.get("spintronics.label.resistance")), resistanceValueLabel),` |
| 327 | `new VBox(5, new Label(i18n.get("spintronics.label.gmr_ratio")), gmrRatioLabel),` |
| 328 | `new VBox(5, new Label("STT Vector (N\u00B7m)"), sttLabel));` |
| 384 | `resistanceValueLabel.setText(String.format("%.2f \u03A9\u00B7nm\u00B2", r.doubleValue() * 1e18)); // AR product` |
| 385 | `gmrRatioLabel.setText(String.format("%.1f %%", gmr.doubleValue() * 100));` |
| 390 | `sttLabel.setText(String.format("[%.1e, %.1e]", stt[0].doubleValue(), stt[1].doubleValue()));` |
| 426 | `java.io.File file = new java.io.File("spintronics_trace.csv");` |
| 428 | `pw.println("Sample,mx,my,mz");` |
| 431 | `pw.println(String.format("%d,%.6f,%.6f,%.6f", i, m[0], m[1], m[2]));` |
| 434 | `statusLabelInfo.setText("Exported " + historyM.size() + " samples to " + file.getName());` |
| 436 | `statusLabelInfo.setText("Export failed: " + e.getMessage());` |
| 480 | `resistanceValueLabel.setText(String.format("%.2f Ohms", r.doubleValue() * 1e18));` |
| 521 | `peakFreqLabel.setText(String.format("Peak: %.2f GHz", peakGHz));` |
| 529 | `spectrumSeries.getNode().setStyle("-fx-stroke: hsb(" + hue + ", 80%, 90%); -fx-stroke-width: 3px;");` |

## JavaFXSpintronic3DViewer.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\viewer\JavaFXSpintronic3DViewer.java`

| Line | Content |
| --- | --- |
| 49 | `subScene.setFill(Color.web("#1a1a2e"));` |

## Jzy3dSpintronic3DViewer.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\viewer\Jzy3dSpintronic3DViewer.java`

| Line | Content |
| --- | --- |
| 49 | `chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");` |

## SpinField2DViewer.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\viewer\SpinField2DViewer.java`

| Line | Content |
| --- | --- |
| 69 | `this.chart = AWTChartComponentFactory.chart(Quality.Advanced, "awt");` |

## VRSpinViewer.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\physics\spintronics\viewer\VRSpinViewer.java`

| Line | Content |
| --- | --- |
| 62 | `System.out.println("VRSpinViewer: OpenXR Runtime initialized (SIMULATED)");` |
| 66 | `System.err.println("VRSpinViewer: Failed to init OpenXR: " + e.getMessage());` |

## CivilizationApp.java (jscience-featured-apps)
`C:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java\org\jscience\apps\sociology\CivilizationApp.java`

| Line | Content |
| --- | --- |
| 78 | `System.err.println("CRITICAL: Failed to initialize CivilizationApp: " + t.getMessage());` |
| 103 | `return i18n.get("civilization.title");` |
| 108 | `return i18n.get("civilization.desc");` |
| 130 | `dialog.addTopic("Dynamics", "Population Growth",` |
| 131 | `"Population grows based on birth rate minus death rate.\n\nDeath rate is influenced by pollution and scarcity.",` |
| 133 | `dialog.addTopic("Dynamics", "Resources",` |
| 134 | `"Resources are consumed by the population.\n\nResources regenerate slowly over time.", null);` |
| 135 | `dialog.addTopic("Dynamics", "Pollution", "Pollution is generated by consumption and causes death.", null);` |
| 140 | `dialog.addTopic("Tutorials", "Avoiding Collapse",` |
| 141 | `"To avoid collapse:\n1. Keep consumption moderate.\n2. Invest in innovation.\n3. Control birth rate if resources dwindle.",` |
| 157 | `statusLabel = new Label(i18n.get("civilization.status.stable"));` |
| 158 | `statusLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: green;");` |
| 174 | `xAxis.setLabel(i18n.get("civilization.label.years"));` |
| 176 | `yAxis.setLabel(i18n.get("civilization.label.value"));` |
| 179 | `lc.setTitle(i18n.get("civilization.chart.title"));` |
| 184 | `popSeries.setName(i18n.get("civilization.label.population"));` |
| 186 | `resSeries.setName(i18n.get("civilization.label.resources"));` |
| 188 | `polSeries.setName(i18n.get("civilization.series.pollution"));` |
| 198 | `box.setStyle("-fx-background-color: #eee;");` |
| 203 | `createSlider(i18n.get("civilization.label.consumption"), 0.5, 5.0, 1.0, v -> consumptionPerCapita = v,` |
| 204 | `"Consumption"));` |
| 206 | `.add(createSlider(i18n.get("civilization.label.birth"), 0.0, 0.2, 0.05, v -> birthRateBase = v,` |
| 207 | `"Birth Rate"));` |
| 209 | `.add(createSlider(i18n.get("civilization.label.innovation"), 0.0, 0.05, 0.0, v -> innovationRate = v,` |
| 210 | `"Innovation"));` |
| 212 | `.add(createSlider(i18n.get("civilization.label.regen"), 0.0, 1.0, 0.0, v -> regenerationRate = v,` |
| 213 | `"Regeneration"));` |
| 215 | `.add(createSlider(i18n.get("civilization.label.aggression"), 0.0, 1.0, 0.0, v -> aggression = v,` |
| 216 | `"Aggression"));` |
| 218 | `paramsTitleLabel = new Label(i18n.get("civilization.label.params"));` |
| 219 | `paramsTitleLabel.setStyle("-fx-font-weight: bold;");` |
| 232 | `s.setTooltip(new Tooltip("Adjust the " + name` |
| 233 | `+ " parameter.\nScientific impact: Changes the system dynamics equations directly."));` |
| 258 | `return "Change " + paramName;` |
| 273 | `statusLabel.setText(i18n.get("civilization.status.extinct"));` |
| 276 | `statusLabel.setText(i18n.get("civilization.status.collapse"));` |
| 279 | `statusLabel.setText(i18n.get("civilization.status.declining"));` |
| 282 | `statusLabel.setText(i18n.get("civilization.status.thriving"));` |
| 290 | `props.setProperty("time", String.valueOf(time));` |
| 291 | `props.setProperty("pop", String.valueOf(population.getValue().doubleValue()));` |
| 292 | `props.setProperty("res", String.valueOf(resources.getValue().doubleValue()));` |
| 293 | `props.setProperty("pol", String.valueOf(pollution.getValue().doubleValue()));` |
| 295 | `props.setProperty("param.consumption", String.valueOf(consumptionPerCapita));` |
| 296 | `props.setProperty("param.birth", String.valueOf(birthRateBase));` |
| 297 | `props.setProperty("param.innovation", String.valueOf(innovationRate));` |
| 298 | `props.setProperty("param.regen", String.valueOf(regenerationRate));` |
| 299 | `props.setProperty("param.aggression", String.valueOf(aggression));` |
| 303 | `props.store(baos, "Civilization State");` |
| 315 | `time = Double.parseDouble(props.getProperty("time", "0"));` |
| 316 | `population = Quantities.create(Double.parseDouble(props.getProperty("pop", "1000")), Units.ONE);` |
| 317 | `resources = Quantities.create(Double.parseDouble(props.getProperty("res", "100000")), Units.KILOGRAM);` |
| 318 | `pollution = Quantities.create(Double.parseDouble(props.getProperty("pol", "0")), Units.ONE);` |
| 320 | `updateParam("Consumption", props.getProperty("param.consumption"));` |
| 321 | `updateParam("Birth Rate", props.getProperty("param.birth"));` |
| 322 | `updateParam("Innovation", props.getProperty("param.innovation"));` |
| 323 | `updateParam("Regeneration", props.getProperty("param.regen"));` |
| 324 | `updateParam("Aggression", props.getProperty("param.aggression"));` |
| 333 | `showError("Load Error", "Failed to restore state: " + e.getMessage());` |
| 440 | `statusLabel.setText(i18n.get("civilization.status.extinct"));` |
| 443 | `statusLabel.setText(i18n.get("civilization.status.collapse"));` |
| 446 | `statusLabel.setText(i18n.get("civilization.status.declining"));` |
| 449 | `statusLabel.setText(i18n.get("civilization.status.thriving"));` |
| 483 | `statusLabel.setText(i18n.get("civilization.status.stable"));` |
| 489 | `paramsTitleLabel.setText(i18n.get("civilization.label.params"));` |
| 495 | `mainChart.setTitle(i18n.get("civilization.chart.title"));` |
| 497 | `((NumberAxis) mainChart.getXAxis()).setLabel(i18n.get("civilization.label.years"));` |
| 500 | `((NumberAxis) mainChart.getYAxis()).setLabel(i18n.get("civilization.label.value"));` |
| 503 | `popSeries.setName(i18n.get("civilization.label.population"));` |
| 504 | `resSeries.setName(i18n.get("civilization.label.resources"));` |
| 505 | `polSeries.setName(i18n.get("civilization.series.pollution"));` |
| 510 | `if (sliderLabels.get("Consumption") != null)` |
| 511 | `sliderLabels.get("Consumption").setText(i18n.get("civilization.label.consumption"));` |
| 512 | `if (sliderLabels.get("Birth Rate") != null)` |
| 513 | `sliderLabels.get("Birth Rate").setText(i18n.get("civilization.label.birth"));` |
| 514 | `if (sliderLabels.get("Innovation") != null)` |
| 515 | `sliderLabels.get("Innovation").setText(i18n.get("civilization.label.innovation"));` |
| 516 | `if (sliderLabels.get("Regeneration") != null)` |
| 517 | `sliderLabels.get("Regeneration").setText(i18n.get("civilization.label.regen"));` |
| 518 | `if (sliderLabels.get("Aggression") != null)` |
| 519 | `sliderLabels.get("Aggression").setText(i18n.get("civilization.label.aggression"));` |

## App.java (jscience-jni)
`C:\Silvere\Encours\Developpement\JScience\jscience-jni\src\main\java\org\App.java`

| Line | Content |
| --- | --- |
| 37 | `System.out.println( "Hello World!" );` |

## FASTAReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\FASTAReader.java`

| Line | Content |
| --- | --- |
| 44 | `if (resourceId.startsWith("http")) {` |
| 51 | `throw new java.io.IOException("FASTA resource not found: " + resourceId);` |
| 58 | `return "Biology";` |
| 63 | `return "FASTA Sequence Reader.";` |
| 68 | `return "/";` |
| 102 | `if (line.startsWith(">")) {` |

## FASTAWriter.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\FASTAWriter.java`

| Line | Content |
| --- | --- |
| 39 | `return "/";` |
| 52 | `pw.println(">" + seq.header);` |

## FbxMeshReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\FbxMeshReader.java`

| Line | Content |
| --- | --- |
| 64 | `return "/";` |
| 74 | `if (id.startsWith("http") \|\| id.startsWith("file:")) {` |
| 114 | `File tempFile = File.createTempFile("jscience_fbx_", ".fbx");` |
| 135 | `throw new IOException("FBX resource not found: " + resourcePath);` |
| 148 | `FBXNode objectsNode = root.getChildByName("Objects");` |
| 151 | `System.out.println("DEBUG: Starting parseJfbx (Hierarchy + Raw Transforms)");` |
| 183 | `List<FBXNode> modelNodes = objectsNode.getChildrenByName("Model");` |
| 194 | `FBXNode props = m.getChildByName("Properties70");` |
| 197 | `FBXNode p = props.getChild(i); // "P"` |
| 202 | `if ("Lcl Translation".equals(pName)) {` |
| 206 | `} else if ("Lcl Rotation".equals(pName)) {` |
| 210 | `} else if ("Lcl Scaling".equals(pName)) {` |
| 214 | `} else if ("PreRotation".equals(pName)) {` |
| 218 | `} else if ("RotationPivot".equals(pName)) {` |
| 222 | `} else if ("ScalingPivot".equals(pName)) {` |
| 229 | `else if ("GeometricTranslation".equals(pName)) {` |
| 233 | `} else if ("GeometricRotation".equals(pName)) {` |
| 237 | `} else if ("GeometricScaling".equals(pName)) {` |
| 292 | `System.out.println("DEBUG: Parsed " + models.size() + " Models");` |
| 305 | `FBXNode connections = root.getChildByName("Connections");` |
| 311 | `if ("OO".equals(c.getProperty(0).getData())) {` |
| 327 | `System.out.println("DEBUG: Found " + childToParent.size() + " Model hierarchy links");` |
| 328 | `System.out.println("DEBUG: Found " + geomModelLinks.size() + " Geometry-Model links");` |
| 343 | `List<FBXNode> geometryNodes = objectsNode.getChildrenByName("Geometry");` |
| 349 | `if (!"Mesh".equals(type)) {` |
| 375 | `if (modelName.startsWith("Model::")) modelName = modelName.substring(7);` |
| 387 | `meshWrapper.setId(modelName + "_Wrapper"); // Optional` |
| 411 | `System.out.println("DEBUG: Attached " + attachedCount + " meshes to models");` |
| 412 | `System.out.println("DEBUG: fbxRoot has " + fbxRoot.getChildren().size() + " root children");` |
| 440 | `System.out.println("DEBUG: Applied global scale: " + globalScale + " with Y-Flip. Auto-Center DISABLED for alignment.");` |
| 450 | `FBXNode verticesNode = geomNode.getChildByName("Vertices");` |
| 458 | `FBXNode indicesNode = geomNode.getChildByName("PolygonVertexIndex");` |
| 509 | `if (raw == null) return "Unknown";` |
| 512 | `if (raw.startsWith("Model::")) {` |
| 518 | `String[] parts = raw.split("[^\\x20-\\x7E]+");` |

## GBIFSpeciesReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\GBIFSpeciesReader.java`

| Line | Content |
| --- | --- |
| 45 | `return "Biology";` |
| 49 | `return "GBIF Species API Reader.";` |
| 56 | `try (InputStream is = GBIFSpeciesReader.class.getResourceAsStream("/jscience.properties")) {` |
| 61 | `BASE_URL = props.getProperty("api.gbif.taxonomy.base", "https://api.gbif.org/v1/species");` |
| 101 | `return String.format("%s [%s] - %s > %s > %s",` |
| 115 | `String url = BASE_URL + "/" + key;` |
| 118 | `.header("Accept", "application/json")` |
| 127 | `System.err.println("GBIF fetch failed: " + e.getMessage());` |
| 142 | `String url = BASE_URL + "/search?q=" +` |
| 143 | `java.net.URLEncoder.encode(name, "UTF-8") + "&limit=" + limit;` |
| 146 | `.header("Accept", "application/json")` |
| 153 | `JsonNode resultList = root.path("results");` |
| 161 | `System.err.println("GBIF search failed: " + e.getMessage());` |
| 172 | `String url = BASE_URL + "/" + parentKey + "/children?limit=" + limit;` |
| 175 | `.header("Accept", "application/json")` |
| 182 | `JsonNode results = root.path("results");` |
| 190 | `System.err.println("GBIF children fetch failed: " + e.getMessage());` |
| 196 | `if (!node.has("key"))` |
| 200 | `node.path("key").asLong(),` |
| 201 | `node.path("scientificName").asText(null),` |
| 202 | `node.path("canonicalName").asText(null),` |
| 203 | `node.path("rank").asText(null),` |
| 204 | `node.path("kingdom").asText(null),` |
| 205 | `node.path("phylum").asText(null),` |
| 206 | `node.path("class").asText(null),` |
| 207 | `node.path("order").asText(null),` |
| 208 | `node.path("family").asText(null),` |
| 209 | `node.path("genus").asText(null),` |
| 210 | `node.path("species").asText(null)));` |
| 221 | `String url = String.format("%s/%d/media", BASE_URL, speciesKey);` |
| 231 | `JsonNode results = root.get("results");` |
| 234 | `if ("StillImage".equals(media.path("type").asText())) {` |
| 235 | `return Optional.ofNullable(media.path("identifier").asText(null));` |
| 241 | `System.err.println("GBIF media fetch failed: " + e.getMessage());` |

## NCBITaxonomyReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\NCBITaxonomyReader.java`

| Line | Content |
| --- | --- |
| 53 | `return "Biology";` |
| 58 | `return "NCBI Taxonomy Reader.";` |
| 78 | `throw new IllegalArgumentException("Expected numeric TaxID, got: " + resourceId);` |
| 87 | `try (InputStream is = NCBITaxonomyReader.class.getResourceAsStream("/jscience.properties")) {` |
| 92 | `BASE_URL = props.getProperty("api.ncbi.taxonomy.base",` |
| 93 | `"https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");` |
| 94 | `SEARCH_URL = props.getProperty("api.ncbi.taxonomy.search",` |
| 95 | `"https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi");` |
| 109 | `String url = BASE_URL + "?db=taxonomy&id=" + taxId + "&retmode=xml";` |
| 120 | `System.err.println("NCBI fetch failed: " + e.getMessage());` |
| 134 | `String url = SEARCH_URL + "?db=taxonomy&term=" +` |
| 135 | `java.net.URLEncoder.encode(name, "UTF-8") + "&retmode=json";` |
| 144 | `JsonNode idList = root.path("esearchresult").path("idlist");` |
| 152 | `System.err.println("NCBI search failed: " + e.getMessage());` |
| 159 | `String scientificName = extractXmlValue(xml, "ScientificName");` |
| 160 | `String rank = extractXmlValue(xml, "Rank");` |
| 161 | `String lineage = extractXmlValue(xml, "Lineage");` |
| 162 | `String division = extractXmlValue(xml, "Division");` |
| 168 | `s.addAttribute("ncbi_taxid", String.valueOf(taxId));` |
| 170 | `s.addAttribute("rank", rank); // Species doesn't have generic rank field setter exposed as string` |
| 174 | `s.addAttribute("lineage", lineage);` |
| 176 | `s.addAttribute("division", division);` |
| 184 | `String open = "<" + tag + ">";` |
| 185 | `String close = "</" + tag + ">";` |
| 207 | `if (is == null && !resourcePath.startsWith("/")) {` |
| 208 | `is = NCBITaxonomyReader.class.getResourceAsStream("/" + resourcePath);` |
| 212 | `System.err.println("NCBITaxonomyReader: Resource not found: " + resourcePath);` |
| 222 | `} else if (root.has("species")) {` |
| 223 | `for (JsonNode node : root.get("species")) {` |
| 236 | `String commonName = node.has("commonName") ? node.get("commonName").asText()` |
| 237 | `: (node.has("common_name") ? node.get("common_name").asText() : "");` |
| 239 | `String scientificName = node.has("scientificName") ? node.get("scientificName").asText()` |
| 240 | `: (node.has("scientific_name") ? node.get("scientific_name").asText() : "");` |
| 244 | `if (node.has("kingdom"))` |
| 245 | `s.setKingdom(node.get("kingdom").asText());` |
| 246 | `if (node.has("phylum"))` |
| 247 | `s.setPhylum(node.get("phylum").asText());` |
| 248 | `if (node.has("class"))` |
| 249 | `s.setTaxonomicClass(node.get("class").asText());` |
| 250 | `if (node.has("order"))` |
| 251 | `s.setOrder(node.get("order").asText());` |
| 252 | `if (node.has("family"))` |
| 253 | `s.setFamily(node.get("family").asText());` |
| 254 | `if (node.has("genus"))` |
| 255 | `s.setGenus(node.get("genus").asText());` |
| 257 | `if (node.has("attributes")) {` |
| 258 | `Iterator<String> fieldNames = node.get("attributes").fieldNames();` |
| 261 | `s.addAttribute(key, node.get("attributes").get(key).asText());` |

## ObjMeshReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\ObjMeshReader.java`

| Line | Content |
| --- | --- |
| 58 | `return "/";` |
| 68 | `if (id.startsWith("http") \|\| id.startsWith("file:")) {` |
| 118 | `throw new IOException("OBJ resource not found: " + resourcePath);` |
| 150 | `if (line.isEmpty() \|\| line.startsWith("#")) {` |
| 154 | `String[] parts = line.split("\\s+");` |
| 156 | `case "v":` |
| 163 | `case "vt":` |
| 169 | `case "vn":` |
| 176 | `case "f":` |
| 232 | `String[] parts = vertex.split("/");` |

## PDBReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\PDBReader.java`

| Line | Content |
| --- | --- |
| 43 | `return "Biology";` |
| 48 | `return "Protein Data Bank (PDB) Reader.";` |
| 53 | `return "https://files.rcsb.org/download/";` |
| 70 | `String urlString = getResourcePath() + path.toUpperCase() + ".pdb";` |
| 88 | `if (line.startsWith("ATOM  ") \|\| line.startsWith("HETATM")) {` |
| 97 | `chainId = "A";` |
| 110 | `String elementSymbol = "";` |
| 126 | `element = org.jscience.chemistry.PeriodicTable.getElement("C"); // Fallback` |
| 167 | `} else if (line.startsWith("HEADER")) {` |

## PDBWriter.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\PDBWriter.java`

| Line | Content |
| --- | --- |
| 42 | `return "https://files.rcsb.org/download/";` |
| 53 | `writer.println("REMARK   GENERATED BY JSCIENCE");` |
| 54 | `writer.printf(Locale.US, "HEADER    %-40s%11s   %4s\n", protein.getName(), "", protein.getPdbId());` |
| 71 | `"ATOM  %5d %-4s %3s %1s%4d    %8.3f%8.3f%8.3f  1.00  0.00          %2s\n",` |
| 85 | `writer.println("END");` |

## StlMeshReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\StlMeshReader.java`

| Line | Content |
| --- | --- |
| 53 | `return "Biology";` |
| 58 | `return "STL 3D Mesh Reader.";` |
| 63 | `return "/";` |
| 73 | `if (id.startsWith("http")) {` |

## UniProtReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\UniProtReader.java`

| Line | Content |
| --- | --- |
| 43 | `private static final String API_URL = "https://rest.uniprot.org/uniprotkb/";` |
| 44 | `private static final String SEARCH_URL = "https://rest.uniprot.org/uniprotkb/search?query=";` |
| 58 | `return "Biology";` |
| 63 | `return "UniProt Protein Database Reader.";` |
| 85 | `URL url = java.net.URI.create(API_URL + accession + ".json").toURL();` |
| 87 | `conn.setRequestMethod("GET");` |
| 88 | `conn.setRequestProperty("Accept", "application/json");` |
| 105 | `result.put("accession", accession);` |
| 106 | `result.put("raw_json", response.toString());` |
| 110 | `extractField(json, "fullName", result, "protein_name");` |
| 111 | `extractField(json, "scientificName", result, "organism");` |
| 124 | `String urlStr = SEARCH_URL + java.net.URLEncoder.encode(query, "UTF-8") + "&size=5";` |
| 127 | `conn.setRequestMethod("GET");` |
| 128 | `conn.setRequestProperty("Accept", "application/json");` |
| 145 | `result.put("query", query);` |
| 146 | `result.put("raw_json", response.toString());` |
| 155 | `String pattern = "\"" + field + "\":\"";` |
| 159 | `int end = json.indexOf("\"", start);` |
| 170 | `return "https://www.uniprot.org/uniprotkb/" + accession;` |

## VirusReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\loaders\VirusReader.java`

| Line | Content |
| --- | --- |
| 48 | `private static final String RESOURCE_PATH = "/org/jscience/biology/viruses.json";` |
| 66 | `return "Biology";` |
| 71 | `return "Virus Species Reader (JSON).";` |
| 107 | `throw new IOException("Resource not found: " + RESOURCE_PATH);` |
| 141 | `if (type.equalsIgnoreCase("RNA"))` |
| 143 | `if (type.equalsIgnoreCase("DNA"))` |

## TaxonomyReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\biology\taxonomy\TaxonomyReader.java`

| Line | Content |
| --- | --- |
| 33 | `return "Biology";` |
| 37 | `return "Taxonomy Data Loader.";` |

## AcidBaseReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\AcidBaseReader.java`

| Line | Content |
| --- | --- |
| 44 | `return "Chemistry";` |
| 48 | `return "Acid-Base Data Reader.";` |
| 110 | `JsonNode acidsNode = root.get("acids");` |
| 114 | `acidNode.get("name").asText(),` |
| 115 | `acidNode.get("formula").asText(),` |
| 116 | `acidNode.get("pKa").asDouble(),` |
| 117 | `acidNode.get("strong").asBoolean()));` |
| 122 | `JsonNode basesNode = root.get("bases");` |
| 126 | `baseNode.get("name").asText(),` |
| 127 | `baseNode.get("formula").asText(),` |
| 128 | `baseNode.get("pKb").asDouble(),` |
| 129 | `baseNode.get("strong").asBoolean()));` |
| 134 | `JsonNode indicatorsNode = root.get("indicators");` |
| 138 | `indNode.get("name").asText(),` |
| 139 | `indNode.get("pHLow").asDouble(),` |
| 140 | `indNode.get("pHHigh").asDouble(),` |
| 141 | `indNode.get("colorAcid").asText(),` |
| 142 | `indNode.get("colorBase").asText()));` |

## CIFReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\CIFReader.java`

| Line | Content |
| --- | --- |
| 45 | `return "Chemistry";` |
| 49 | `return "Crystal Structure Reader (CIF).";` |
| 85 | `if (line.isEmpty() \|\| line.startsWith("#"))` |
| 88 | `if (line.startsWith("_chemical_formula_sum")) {` |
| 90 | `} else if (line.startsWith("_cell_length_a")) {` |
| 92 | `} else if (line.startsWith("_cell_length_b")) {` |
| 94 | `} else if (line.startsWith("_cell_length_c")) {` |
| 96 | `} else if (line.startsWith("_cell_angle_alpha")) {` |
| 98 | `} else if (line.startsWith("_cell_angle_beta")) {` |
| 100 | `} else if (line.startsWith("_cell_angle_gamma")) {` |
| 102 | `} else if (line.startsWith("loop_")) {` |
| 105 | `} else if (line.startsWith("_atom_site_")) {` |
| 111 | `String[] parts = line.split("\\s+");` |
| 113 | `String label = "Unk";` |
| 114 | `String symbol = "X";` |
| 122 | `if (header.contains("label"))` |
| 124 | `if (header.contains("type_symbol"))` |
| 126 | `if (header.contains("fract_x"))` |
| 128 | `if (header.contains("fract_y"))` |
| 130 | `if (header.contains("fract_z"))` |
| 147 | `String[] parts = line.split("\\s+", 2);` |
| 149 | `return parts[1].replace("'", "").replace("\"", "");` |
| 151 | `return "";` |
| 155 | `String[] parts = line.split("\\s+");` |

## ChEBIReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\ChEBIReader.java`

| Line | Content |
| --- | --- |
| 41 | `private static final String SEARCH_URL = "https://www.ebi.ac.uk/ols/api/search?ontology=chebi&q=";` |
| 47 | `return "Chemistry";` |
| 51 | `return "ChEBI Database Reader.";` |
| 60 | `String urlStr = SEARCH_URL + java.net.URLEncoder.encode(name, "UTF-8");` |
| 63 | `conn.setRequestMethod("GET");` |
| 64 | `conn.setRequestProperty("Accept", "application/json");` |
| 81 | `result.put("query", name);` |
| 82 | `result.put("raw_response", response.toString());` |
| 86 | `int labelIdx = json.indexOf("\"label\":\"");` |
| 89 | `int end = json.indexOf("\"", start);` |
| 91 | `result.put("name", json.substring(start, end));` |
| 105 | `int idx = jsonResponse.indexOf("CHEBI:");` |
| 107 | `int end = jsonResponse.indexOf("\"", idx);` |
| 119 | `return "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + chebiId;` |

## ChemistryDataReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\ChemistryDataReader.java`

| Line | Content |
| --- | --- |
| 53 | `return "Chemistry";` |
| 58 | `return "Generic Chemistry Data Reader (JSON).";` |
| 63 | `return "/org/jscience/chemistry/";` |
| 73 | `if ("elements".equals(id)) {` |
| 76 | `} else if ("molecules".equals(id)) {` |
| 110 | `System.err.println("CRITICAL ERROR: Could not initialize Jackson ObjectMapper. Check dependencies!");` |
| 120 | `System.err.println("ERROR: ChemistryDataReader cannot function because ObjectMapper failed to initialize.");` |
| 124 | `try (InputStream is = ChemistryDataReader.class.getResourceAsStream("/org/jscience/chemistry/elements.json")) {` |
| 135 | `String catStr = data.category.toUpperCase().replace(" ", "_").replace("-", "_");` |
| 136 | `if (catStr.contains("DIATOMIC") \|\| catStr.contains("POLYATOMIC")) {` |
| 137 | `catStr = "NONMETAL";` |
| 142 | `if (catStr.contains("NOBLE"))` |
| 144 | `else if (catStr.contains("ALKALI") && !catStr.contains("EARTH"))` |
| 146 | `else if (catStr.contains("ALKALINE"))` |
| 148 | `else if (catStr.contains("TRANSITION"))` |
| 150 | `else if (catStr.contains("LANTHANIDE"))` |
| 152 | `else if (catStr.contains("ACTINIDE"))` |
| 154 | `else if (catStr.contains("METALLOID"))` |
| 156 | `else if (catStr.contains("HALOGEN"))` |
| 158 | `else if (catStr.contains("NONMETAL"))` |
| 213 | `try (InputStream is = ChemistryDataReader.class.getResourceAsStream("/org/jscience/chemistry/molecules.json")) {` |

## IUPACGoldBookReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\IUPACGoldBookReader.java`

| Line | Content |
| --- | --- |
| 41 | `private static final String API_URL = "https://goldbook.iupac.org/terms/";` |
| 42 | `private static final String SEARCH_URL = "https://goldbook.iupac.org/indexes/autocomplete?term=";` |
| 48 | `return "Chemistry";` |
| 52 | `return "IUPAC Gold Book Reader.";` |
| 60 | `String urlStr = SEARCH_URL + java.net.URLEncoder.encode(term, "UTF-8");` |
| 63 | `conn.setRequestMethod("GET");` |
| 64 | `conn.setRequestProperty("Accept", "application/json");` |
| 81 | `result.put("query", term);` |
| 82 | `result.put("raw_json", response.toString());` |
| 100 | `public static final String MOLE = "M03980";` |
| 101 | `public static final String AVOGADRO_CONSTANT = "A00543";` |
| 102 | `public static final String MOLARITY = "A00295";` |
| 103 | `public static final String PH = "P04524";` |
| 104 | `public static final String CATALYST = "C00876";` |
| 105 | `public static final String ENTHALPY = "E02141";` |
| 106 | `public static final String ENTROPY = "E02149";` |
| 107 | `public static final String FREE_ENERGY = "G02629";` |
| 114 | `entry.put("term", term);` |
| 115 | `entry.put("definition", definition);` |
| 116 | `entry.put("code", termCode);` |
| 117 | `entry.put("url", getTermUrl(termCode));` |

## PeriodicTableReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\PeriodicTableReader.java`

| Line | Content |
| --- | --- |
| 53 | `return "Chemistry";` |
| 58 | `return "Periodic Table of Elements Loader.";` |
| 63 | `return "/";` |
| 102 | `is = PeriodicTableReader.class.getResourceAsStream("/" + resourcePath);` |
| 105 | `throw new IllegalArgumentException("Resource not found: " + resourcePath);` |
| 111 | `if (root.has("elements") && root.get("elements").isArray()) {` |
| 112 | `for (JsonNode node : root.get("elements")) {` |
| 119 | `throw new RuntimeException("Failed to load periodic table from " + resourcePath, e);` |
| 124 | `int atomicNumber = node.get("atomicNumber").asInt();` |
| 125 | `String symbol = node.get("symbol").asText();` |
| 126 | `String name = node.get("name").asText();` |
| 128 | `double atomicMassVal = node.get("atomicMass").asDouble();` |
| 131 | `int group = node.has("group") ? node.get("group").asInt() : 0;` |
| 132 | `int period = node.has("period") ? node.get("period").asInt() : 0;` |
| 134 | `String catStr = node.has("category") ? node.get("category").asText().toUpperCase().replace(" ", "_")` |
| 135 | `: "UNKNOWN";` |
| 143 | `double electro = node.has("electronegativity") ? node.get("electronegativity").asDouble() : 0.0;` |
| 146 | `if (node.has("melt")) {` |
| 147 | `melt = Quantities.create(node.get("melt").asDouble(), Units.KELVIN);` |
| 151 | `if (node.has("boil")) {` |
| 152 | `boil = Quantities.create(node.get("boil").asDouble(), Units.KELVIN);` |

## PubChemReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\chemistry\loaders\PubChemReader.java`

| Line | Content |
| --- | --- |
| 48 | `try (InputStream is = PubChemReader.class.getResourceAsStream("/jscience.properties")) {` |
| 53 | `BASE_URL = props.getProperty("api.pubchem.base", "https://pubchem.ncbi.nlm.nih.gov/rest/pug");` |
| 60 | `return "Chemistry";` |
| 64 | `return "PubChem Chemical Compound Reader.";` |
| 92 | `return String.format("%s (CID:%d) - %s, MW=%.2f",` |
| 105 | `String url = BASE_URL + "/compound/cid/" + cid + "/property/" +` |
| 106 | `"IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";` |
| 115 | `JsonNode props = root.path("PropertyTable").path("Properties");` |
| 121 | `System.err.println("PubChem fetch failed: " + e.getMessage());` |
| 135 | `String url = BASE_URL + "/compound/name/" +` |
| 136 | `java.net.URLEncoder.encode(name, "UTF-8") + "/cids/JSON";` |
| 145 | `JsonNode cidList = root.path("IdentifierList").path("CID");` |
| 153 | `System.err.println("PubChem search failed: " + e.getMessage());` |
| 163 | `String url = BASE_URL + "/compound/smiles/" +` |
| 164 | `java.net.URLEncoder.encode(smiles, "UTF-8") + "/property/" +` |
| 165 | `"IUPACName,MolecularFormula,MolecularWeight,CanonicalSMILES,InChI,InChIKey/JSON";` |
| 174 | `JsonNode props = root.path("PropertyTable").path("Properties");` |
| 180 | `System.err.println("PubChem SMILES fetch failed: " + e.getMessage());` |
| 189 | `return BASE_URL + "/compound/cid/" + cid + "/PNG";` |
| 194 | `node.path("CID").asLong(),` |
| 195 | `node.path("IUPACName").asText(null),` |
| 196 | `node.path("MolecularFormula").asText(null),` |
| 197 | `node.path("MolecularWeight").asDouble(0),` |
| 198 | `node.path("CanonicalSMILES").asText(null),` |
| 199 | `node.path("InChI").asText(null),` |
| 200 | `node.path("InChIKey").asText(null)));` |

## NMEAReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\device\loaders\nmea\NMEAReader.java`

| Line | Content |
| --- | --- |
| 55 | `return "Device";` |
| 60 | `return "NMEA GPS/Sensor Data Reader.";` |
| 80 | `if (line.startsWith("$") && NMEAMessage.validateChecksum(line)) {` |
| 118 | `if (line.startsWith("$")) {` |

## OpenWeatherReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\earth\loaders\OpenWeatherReader.java`

| Line | Content |
| --- | --- |
| 51 | `private static final String API_URL = org.jscience.io.Configuration.get("api.openweathermap.base");` |
| 52 | `private static final String SAMPLES_PATH = "/org/jscience/earth/loaders/weather_samples.json";` |
| 57 | `this(org.jscience.io.Configuration.get("api.openweathermap.key", "YOUR_API_KEY"));` |
| 67 | `if (resourceId.contains(",")) {` |
| 69 | `String[] parts = resourceId.split(",");` |
| 113 | `.location(node.path("location").asText("Unknown"))` |
| 114 | `.description(node.path("description").asText(""))` |
| 115 | `.temperatureCelsius(node.path("temperature").asDouble())` |
| 116 | `.humidityPercent(node.path("humidity").asDouble())` |
| 117 | `.windSpeedMps(node.path("windSpeed").asDouble())` |
| 118 | `.solarIrradianceWm2(node.path("solarIrradiance").asDouble());` |
| 130 | `return "Earth";` |
| 135 | `return "OpenWeather Map API Reader.";` |
| 153 | `String urlStr = API_URL + "?q=" + java.net.URLEncoder.encode(city, "UTF-8")` |
| 154 | `+ "&appid=" + apiKey + "&units=metric";` |
| 158 | `System.err.println("WARNING: Failed to fetch weather for '" + city + "': " + e.getMessage());` |
| 168 | `String urlStr = API_URL + "?lat=" + lat + "&lon=" + lon` |
| 169 | `+ "&appid=" + apiKey + "&units=metric";` |
| 173 | `System.err.println("WARNING: Failed to fetch weather: " + e.getMessage());` |
| 181 | `conn.setRequestMethod("GET");` |
| 186 | `throw new RuntimeException("HTTP " + conn.getResponseCode());` |
| 203 | `String temp = extractJsonValue(json, "temp");` |
| 208 | `String feelsLike = extractJsonValue(json, "feels_like");` |
| 213 | `String pressure = extractJsonValue(json, "pressure");` |
| 218 | `String humidity = extractJsonValue(json, "humidity");` |
| 223 | `String windSpeed = extractJsonValue(json, "speed");` |
| 228 | `String clouds = extractJsonValue(json, "all");` |
| 233 | `String desc = extractJsonValue(json, "description");` |
| 238 | `String name = extractJsonValue(json, "name");` |
| 246 | `int idx = json.indexOf("\"" + key + "\":");` |
| 253 | `if (json.charAt(start) == '"') {` |
| 254 | `int end = json.indexOf("\"", start + 1);` |

## USGSEarthquakesReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\earth\loaders\USGSEarthquakesReader.java`

| Line | Content |
| --- | --- |
| 56 | `private static final String API_BASE = org.jscience.io.Configuration.get("api.usgs.earthquakes.base",` |
| 57 | `"https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary");` |
| 60 | `return "Earth";` |
| 64 | `return "USGS Earthquakes API Reader.";` |
| 73 | `String cacheKey = "usgs_significant_month";` |
| 79 | `String urlStr = API_BASE + "/significant_month.geojson";` |
| 84 | `throw new RuntimeException("USGS API request failed", e);` |
| 93 | `"WARNING: Domain mapping for " + target.getSimpleName() + " is not yet implemented. Returning null.");` |
| 101 | `conn.setRequestMethod("GET");` |
| 104 | `throw new RuntimeException("HTTP error code : " + conn.getResponseCode());` |
| 117 | `throw new RuntimeException("Failed to fetch from USGS", e);` |

## DrugBankReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\medicine\loaders\DrugBankReader.java`

| Line | Content |
| --- | --- |
| 54 | `try (InputStream is = DrugBankReader.class.getResourceAsStream("/jscience.properties")) {` |
| 59 | `BASE_URL = props.getProperty("api.drugbank.base", "https://api.drugbank.com/v1");` |
| 60 | `API_KEY = props.getProperty("api.drugbank.key", "");` |
| 68 | `return "Medicine";` |
| 73 | `return "DrugBank Medication Data Reader.";` |
| 99 | `String url = BASE_URL + "/drugs/" + java.net.URLEncoder.encode(id, "UTF-8");` |
| 105 | `builder.header("Authorization", "Bearer " + API_KEY);` |
| 114 | `System.err.println("Drug fetch failed: " + e.getMessage());` |
| 150 | `String name = node.path("name").asText("Unknown Drug");` |
| 151 | `String formStr = node.path("form").asText("TABLET");` |
| 152 | `String routeStr = node.path("route").asText("ORAL");` |
| 166 | `m.setDosage(node.path("dosage").asText(""));` |
| 167 | `m.setGenericName(node.path("generic_name").asText(null));` |
| 168 | `m.setBrandName(node.path("brand_name").asText(null));` |
| 170 | `if (node.has("active_ingredients")) {` |
| 171 | `for(JsonNode ing : node.get("active_ingredients")) {` |

## ICD10Reader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\medicine\loaders\ICD10Reader.java`

| Line | Content |
| --- | --- |
| 55 | `try (InputStream is = ICD10Reader.class.getResourceAsStream("/jscience.properties")) {` |
| 60 | `BASE_URL = props.getProperty("api.icd.base", "https://id.who.int/icd/release/11/mms");` |
| 61 | `API_KEY = props.getProperty("api.icd.key", "");` |
| 69 | `return "Medicine";` |
| 74 | `return "ICD Disease Classification Reader.";` |
| 100 | `String url = BASE_URL + "/" + java.net.URLEncoder.encode(code, "UTF-8");` |
| 106 | `builder.header("Authorization", "Bearer " + API_KEY);` |
| 115 | `System.err.println("ICD fetch failed: " + e.getMessage());` |
| 152 | `String title = node.path("title").path("@value").asText("Unknown Disease");` |
| 153 | `String desc = node.path("definition").path("@value").asText(null);` |
| 154 | `String icd = code != null ? code : node.path("code").asText(null);` |

## SolarSystemReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\astronomy\SolarSystemReader.java`

| Line | Content |
| --- | --- |
| 62 | `if (!resourcePath.startsWith("/")) {` |
| 63 | `is = SolarSystemReader.class.getResourceAsStream("/" + resourcePath);` |
| 69 | `System.out.println("WARNING: Solar System resource not found: " + resourcePath + ". Using fallback.");` |
| 77 | `System.err.println("Failed to load solar system from " + resourcePath + ": " + e.getMessage());` |
| 84 | `String systemName = root.get("name").asText();` |
| 87 | `JsonNode starsNode = root.get("stars");` |
| 94 | `JsonNode planetsNode = starNode.get("planets");` |
| 101 | `JsonNode moonsNode = planetNode.get("moons");` |
| 117 | `StarSystem system = new StarSystem("Fallback Solar System");` |
| 120 | `Star sun = new Star("Sun",` |
| 124 | `sun.setTexturePath("2k_sun.jpg");` |
| 135 | `Planet earth = new Planet("Earth",` |
| 139 | `earth.setTexturePath("2k_earth_daymap.jpg");` |
| 151 | `String name = node.get("name").asText();` |
| 152 | `Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);` |
| 153 | `Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);` |
| 157 | `if (node.has("luminosity"))` |
| 158 | `star.setLuminosity(Quantities.create(node.get("luminosity").asDouble(), Units.WATT));` |
| 159 | `if (node.has("temperature"))` |
| 160 | `star.setTemperature(Quantities.create(node.get("temperature").asDouble(), Units.KELVIN));` |
| 166 | `String name = node.get("name").asText();` |
| 167 | `Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);` |
| 168 | `Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);` |
| 179 | `if (node.has("rings")) {` |
| 180 | `JsonNode ringsNode = node.get("rings");` |
| 181 | `RingSystem rings = new RingSystem(name + " Rings",` |
| 186 | `if (ringsNode.has("inner"))` |
| 187 | `rings.setInnerRadius(Quantities.create(ringsNode.get("inner").asDouble(), Units.METER));` |
| 188 | `if (ringsNode.has("outer"))` |
| 189 | `rings.setOuterRadius(Quantities.create(ringsNode.get("outer").asDouble(), Units.METER));` |
| 194 | `if (node.has("habitable"))` |
| 195 | `planet.setHabitable(node.get("habitable").asBoolean());` |
| 197 | `if (node.has("surface_temperature"))` |
| 199 | `Quantities.create(node.get("surface_temperature").asDouble(), Units.KELVIN));` |
| 201 | `if (node.has("surface_pressure"))` |
| 203 | `Quantities.create(node.get("surface_pressure").asDouble(), Units.PASCAL));` |
| 205 | `if (node.has("atmosphere")) {` |
| 206 | `JsonNode atmNode = node.get("atmosphere");` |
| 220 | `String name = node.get("name").asText();` |
| 221 | `Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);` |
| 222 | `Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);` |
| 239 | `if (node.has("texture")) {` |
| 240 | `JsonNode texNode = node.get("texture");` |
| 254 | `if (node.has("orbit")) {` |
| 255 | `JsonNode orbit = node.get("orbit");` |
| 256 | `double a = orbit.get("a").asDouble();` |
| 257 | `double e = orbit.get("e").asDouble();` |
| 258 | `double i = Math.toRadians(orbit.get("i").asDouble());` |
| 259 | `double Omega = Math.toRadians(orbit.get("Omega").asDouble());` |
| 260 | `double omega = Math.toRadians(orbit.get("omega").asDouble());` |
| 261 | `double M = Math.toRadians(orbit.get("M").asDouble());` |

## HorizonsEphemerisReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\loaders\HorizonsEphemerisReader.java`

| Line | Content |
| --- | --- |
| 54 | `return "Physics";` |
| 59 | `return "JPL Horizons Ephemeris Reader.";` |
| 64 | `return "/";` |
| 75 | `if (id.startsWith("http")) {` |
| 82 | `throw new java.io.IOException("Horizons resource not found: " + id);` |
| 130 | `if (line.trim().equals("$$SOE")) {` |
| 134 | `if (line.trim().equals("$$EOE")) {` |
| 141 | `String[] parts = line.split(",");` |

## NASAExoplanetsReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\loaders\NASAExoplanetsReader.java`

| Line | Content |
| --- | --- |
| 49 | `private static final String API_BASE = org.jscience.io.Configuration.get("api.nasa.exoplanets.tap",` |
| 50 | `"https://exoplanetarchive.ipac.caltech.edu/TAP/sync");` |
| 60 | `String query = "SELECT pl_name,hostname,pl_bmassj,pl_radj,pl_orbper,pl_eqt,sy_dist " +` |
| 61 | `"FROM ps WHERE pl_name='" + id.replace("'", "''") + "'";` |
| 69 | `return "loader.nasa.name";` |
| 74 | `return "Physics";` |
| 79 | `return "Access to NASA Exoplanet Archive.";` |
| 94 | `String encodedQuery = URLEncoder.encode(query, "UTF-8");` |
| 95 | `String urlStr = API_BASE + "?query=" + encodedQuery + "&format=csv";` |
| 99 | `conn.setRequestMethod("GET");` |
| 102 | `throw new RuntimeException("HTTP error code: " + conn.getResponseCode());` |
| 109 | `output.append(line).append("\n");` |
| 115 | `throw new RuntimeException("Failed to query NASA Exoplanet Archive", e);` |
| 121 | `String[] lines = csv.split("\n");` |
| 127 | `String[] parts = line.split(",");` |

## SIMBADReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\loaders\SIMBADReader.java`

| Line | Content |
| --- | --- |
| 54 | `return "http://simbad.u-strasbg.fr"; // API-based, not file-based` |
| 64 | `return "Physics";` |
| 69 | `return "Access to the SIMBAD astronomical database.";` |
| 73 | `private static final String TAP_URL = "http://simbad.u-strasbg.fr/simbad/sim-tap/sync";` |
| 77 | `try (InputStream is = SIMBADReader.class.getResourceAsStream("/jscience.properties")) {` |
| 82 | `BASE_URL = props.getProperty("api.simbad.base", "http://cdsweb.u-strasbg.fr/cgi-bin/nph-sesame");` |
| 117 | `return String.format("%02dh%02dm%.2fs", h, m, s);` |
| 124 | `String sign = dec >= 0 ? "+" : "-";` |
| 130 | `return String.format("%s%02d°%02d'%.2f\"", sign, d, m, s);` |
| 135 | `return String.format("%s (%s) at RA=%s, Dec=%s, V=%.2f",` |
| 150 | `String url = BASE_URL + "/-oI/A?" + java.net.URLEncoder.encode(name, "UTF-8");` |
| 161 | `System.err.println("SIMBAD resolve failed: " + e.getMessage());` |
| 181 | `"SELECT TOP %d main_id, otype, ra, dec, flux_v, sp_type " +` |
| 182 | `"FROM basic WHERE CONTAINS(POINT('ICRS', ra, dec), " +` |
| 183 | `"CIRCLE('ICRS', %.6f, %.6f, %.6f)) = 1",` |
| 186 | `String params = "REQUEST=doQuery&LANG=ADQL&FORMAT=json&QUERY=" +` |
| 187 | `java.net.URLEncoder.encode(query, "UTF-8");` |
| 191 | `.header("Content-Type", "application/x-www-form-urlencoded")` |
| 200 | `JsonNode data = root.path("data");` |
| 216 | `System.err.println("SIMBAD coordinate search failed: " + e.getMessage());` |
| 224 | `String[] lines = response.split("\n");` |
| 230 | `if (line.startsWith("%J ")) {` |
| 231 | `String[] parts = line.substring(3).trim().split("\\s+");` |
| 239 | `} else if (line.startsWith("%T ")) {` |

## StarReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\loaders\StarReader.java`

| Line | Content |
| --- | --- |
| 51 | `return "/org/jscience/physics/astronomy/data/";` |
| 62 | `return "Physics";` |
| 67 | `return "Generic Star Catalog Reader (CSV).";` |
| 73 | `throw new java.io.IOException("Star catalog not found: " + path);` |
| 109 | `String[] parts = line.split(",");` |

## VizieRReader.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\physics\loaders\VizieRReader.java`

| Line | Content |
| --- | --- |
| 47 | `private static final String API_URL = "https://vizier.cds.unistra.fr/viz-bin/votable";` |
| 54 | `return "Physics";` |
| 59 | `return "Access to VizieR astronomical catalogs.";` |
| 103 | `String urlStr = API_URL + "?-source=" + catalog` |
| 104 | `+ "&-c=" + java.net.URLEncoder.encode(objectName, "UTF-8")` |
| 105 | `+ "&-out.max=10";` |
| 108 | `conn.setRequestMethod("GET");` |
| 120 | `response.append(line).append("\n");` |
| 125 | `result.put("object", objectName);` |
| 126 | `result.put("catalog", catalog);` |
| 127 | `result.put("raw_votable", response.toString());` |
| 138 | `public static final String HIPPARCOS = "I/239/hip_main";` |
| 139 | `public static final String TYCHO2 = "I/259/tyc2";` |
| 140 | `public static final String GAIA_DR3 = "I/355/gaiadr3";` |
| 141 | `public static final String SIMBAD = "II/246/out";` |
| 142 | `public static final String USNO_B1 = "I/284/out";` |
| 148 | `return "https://vizier.cds.unistra.fr/viz-bin/VizieR?-source=" + catalog;` |
| 156 | `String urlStr = API_URL + "?-source=" + catalog` |
| 157 | `+ "&-c=" + ra + "+" + dec` |
| 158 | `+ "&-c.rm=" + radiusArcmin` |
| 159 | `+ "&-out.max=50";` |
| 162 | `conn.setRequestMethod("GET");` |
| 174 | `response.append(line).append("\n");` |
| 179 | `result.put("ra", String.valueOf(ra));` |
| 180 | `result.put("dec", String.valueOf(dec));` |
| 181 | `result.put("radius_arcmin", String.valueOf(radiusArcmin));` |
| 182 | `result.put("catalog", catalog);` |
| 183 | `result.put("raw_votable", response.toString());` |

## CentrifugeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\CentrifugeViewer.java`

| Line | Content |
| --- | --- |
| 47 | `rpmLabel = new Label("0 RPM");` |
| 60 | `rpmLabel.setText(String.format("%.0f RPM", newVal.doubleValue()));` |

## MicroscopeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\MicroscopeViewer.java`

| Line | Content |
| --- | --- |
| 44 | `magLabel = new Label("Magnification: -");` |
| 53 | `magLabel.setText("Magnification: " + device.getCurrentMagnification().toString());` |

## MultimeterViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\MultimeterViewer.java`

| Line | Content |
| --- | --- |
| 44 | `valueLabel = new Label("0.00 V");` |
| 45 | `valueLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 24px; -fx-text-fill: #222;");` |
| 54 | `valueLabel.setText("0.00 V"); // device.readValue() needs Exception handling` |

## OscilloscopeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\OscilloscopeViewer.java`

| Line | Content |
| --- | --- |
| 51 | `frame.setStyle("-fx-background-color: black; -fx-border-color: #555; -fx-border-width: 2;");` |

## PHMeterViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\PHMeterViewer.java`

| Line | Content |
| --- | --- |
| 44 | `phLabel = new Label("pH 7.0");` |
| 45 | `phLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");` |
| 51 | `phLabel.setText("pH 7.0");` |

## PressureGaugeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\PressureGaugeViewer.java`

| Line | Content |
| --- | --- |
| 55 | `private String unitLabel = "bar";` |
| 58 | `super("PressureGaugeViewer");` |
| 61 | `view.setStyle("-fx-background-color: #333; -fx-background-radius: 100;");` |
| 106 | `new Stop(0.85, Color.web("#444")),` |
| 107 | `new Stop(0.95, Color.web("#222")),` |
| 108 | `new Stop(1.0, Color.web("#111")));` |
| 114 | `new Stop(0, Color.web("#f5f5f5")),` |
| 115 | `new Stop(1, Color.web("#d0d0d0")));` |
| 122 | `gc.setFont(Font.font("Arial", 11));` |
| 148 | `gc.fillText(String.format("%.0f", value), lx, ly + 4);` |
| 196 | `gc.setFont(Font.font("Arial", 10));` |
| 201 | `gc.setFont(Font.font("Arial", 12));` |

## TelescopeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\TelescopeViewer.java`

| Line | Content |
| --- | --- |
| 80 | `getStyleClass().add("dark-viewer-root");` |
| 83 | `Label title = new Label(I18n.getInstance().get("telescope.title"));` |
| 84 | `title.setFont(Font.font("System", FontWeight.BOLD, 18));` |
| 98 | `canvasPane.getStyleClass().add("dark-viewer-root");` |
| 110 | `Label raTitle = new Label(I18n.getInstance().get("telescope.ra.title"));` |
| 112 | `raTitle.setFont(Font.font("System", 10));` |
| 113 | `raLabel = new Label(I18n.getInstance().get("telescope.ra.default"));` |
| 115 | `raLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));` |
| 121 | `Label decTitle = new Label(I18n.getInstance().get("telescope.dec.title"));` |
| 123 | `decTitle.setFont(Font.font("System", 10));` |
| 124 | `decLabel = new Label(I18n.getInstance().get("telescope.dec.default"));` |
| 126 | `decLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));` |
| 132 | `Label statusTitle = new Label(I18n.getInstance().get("telescope.status.title"));` |
| 134 | `statusTitle.setFont(Font.font("System", 10));` |
| 135 | `statusLabel = new Label(I18n.getInstance().get("telescope.status.disconnected"));` |
| 137 | `statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));` |
| 144 | `panel.getStyleClass().add("dark-viewer-sidebar");` |
| 151 | `Label targetLabel = new Label(I18n.getInstance().get("telescope.target"));` |
| 153 | `targetLabel.setFont(Font.font("System", FontWeight.BOLD, 12));` |
| 158 | `raInput = new TextField("12.0");` |
| 160 | `raInput.setPromptText("RA (h)");` |
| 162 | `decInput = new TextField("45.0");` |
| 164 | `decInput.setPromptText("Dec (Ã‚Â°)");` |
| 166 | `Button slewBtn = new Button(I18n.getInstance().get("telescope.btn.slew"));` |
| 167 | `slewBtn.getStyleClass().add("accent-button-blue");` |
| 170 | `Button stopBtn = new Button(I18n.getInstance().get("telescope.btn.stop"));` |
| 171 | `stopBtn.getStyleClass().add("accent-button-red");` |
| 175 | `new Label("RA:"), raInput,` |
| 176 | `new Label("Dec:"), decInput,` |
| 186 | `createPresetButton("Polaris", 2.53, 89.26),` |
| 187 | `createPresetButton("Vega", 18.62, 38.78),` |
| 188 | `createPresetButton("Betelgeuse", 5.92, 7.41),` |
| 189 | `createPresetButton("Sirius", 6.75, -16.72));` |
| 194 | `panel.getStyleClass().add("dark-viewer-sidebar");` |
| 201 | `btn.getStyleClass().add("accent-button-blue");` |
| 203 | `raInput.setText(String.format("%.2f", ra));` |
| 204 | `decInput.setText(String.format("%.2f", dec));` |
| 216 | `statusLabel.setText(I18n.getInstance().get("telescope.status.error"));` |
| 228 | `statusLabel.setText(I18n.getInstance().get("telescope.status.invalid"));` |
| 231 | `statusLabel.setText(I18n.getInstance().get("telescope.status.slew_error"));` |
| 260 | `case "TRACKING":` |
| 263 | `case "SLEWING":` |

## ThermometerViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\ThermometerViewer.java`

| Line | Content |
| --- | --- |
| 58 | `super("ThermometerViewer");` |
| 61 | `view.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10;");` |
| 98 | `gc.setFill(Color.web("#eee"));` |
| 132 | `new Stop(0, Color.web("#ff3030")),` |
| 133 | `new Stop(0.5, Color.web("#ff5050")),` |
| 134 | `new Stop(1, Color.web("#ff3030")));` |
| 141 | `gc.setFont(Font.font("Arial", 10));` |
| 148 | `gc.fillText(t + "Ã‚Â°", tubeX - 8, y + 4);` |
| 152 | `gc.setFont(Font.font("Arial", 12));` |
| 159 | `gc.setFont(Font.font("Arial", 11));` |
| 161 | `gc.fillText(String.format("%.1fÃ‚Â°C", temp), WIDTH / 2, HEIGHT - 10);` |

## VitalMonitorViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\devices\VitalMonitorViewer.java`

| Line | Content |
| --- | --- |
| 59 | `private static final Color HR_COLOR = Color.web("#00FF00");` |
| 60 | `private static final Color HR_VALUE_COLOR = Color.web("#FF69B4");` |
| 61 | `private static final Color BP_COLOR = Color.web("#FFFF00");` |
| 62 | `private static final Color SPO2_COLOR = Color.web("#00FFFF");` |
| 63 | `private static final Color RR_COLOR = Color.web("#FFFF00");` |
| 82 | `public String getName() { return I18n.getInstance().get("vital.title", "Vital Signs Monitor"); }` |
| 85 | `public String getCategory() { return "Medicine"; }` |
| 91 | `this(new SimulatedVitalSignsMonitor("SimMonitor"));` |
| 96 | `setStyle("-fx-background-color: black;");` |
| 105 | `I18n.getInstance().get("vital.param.hr", "Target HR"),` |
| 106 | `"Base heart rate in beats per minute",` |
| 111 | `I18n.getInstance().get("vital.param.spo2", "Target SpO2"),` |
| 112 | `"Base oxygen saturation level",` |
| 121 | `mainContent.setStyle("-fx-background-color: black;");` |
| 173 | `panel.setStyle("-fx-background-color: black;");` |
| 179 | `Label hrLabel = createSmallLabel("HR", HR_VALUE_COLOR);` |
| 180 | `Label ecgLeadLabel = createSmallLabel("II", HR_COLOR);` |
| 191 | `Label spo2Label = createSmallLabel("SpO2", SPO2_COLOR);` |
| 208 | `panel.setStyle("-fx-background-color: black;");` |
| 211 | `hrValueLabel = new Label("--");` |
| 212 | `VBox hrBox = createValueBox("Heart Rate", hrValueLabel, "bpm", HR_VALUE_COLOR);` |
| 214 | `bpValueLabel = new Label("--/--");` |
| 215 | `VBox bpBox = createValueBox("Blood Pressure", bpValueLabel, "mmHg", BP_COLOR);` |
| 217 | `spo2ValueLabel = new Label("--");` |
| 218 | `VBox spo2Box = createValueBox("Oxygen Saturation", spo2ValueLabel, "%", SPO2_COLOR);` |
| 220 | `rrValueLabel = new Label("--");` |
| 221 | `VBox rrBox = createValueBox("Respiration", rrValueLabel, "bpm", RR_COLOR);` |
| 223 | `tempValueLabel = new Label("--.-");` |
| 224 | `VBox tempBox = createValueBox("Temperature", tempValueLabel, "°F", TEMP_COLOR);` |
| 237 | `valueLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 36));` |
| 241 | `unitLabel.setFont(Font.font("Arial", 12));` |
| 247 | `titleLabel.setFont(Font.font("Arial", 11));` |
| 256 | `label.setFont(Font.font("Arial", FontWeight.BOLD, 12));` |
| 276 | `gc.setStroke(Color.web("#1a1a1a"));` |

## LotkaVolterraViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\biology\ecology\LotkaVolterraViewer.java`

| Line | Content |
| --- | --- |
| 71 | `public String getName() { return I18n.getInstance().get("lotka.title", "Lotka-Volterra Model"); }` |
| 74 | `public String getCategory() { return "Biology"; }` |
| 82 | `this.getStyleClass().add("dark-viewer-root");` |
| 87 | `xAxisTime.setLabel(I18n.getInstance().get("lotka.axis.time", "Time"));` |
| 89 | `yAxisPop.setLabel(I18n.getInstance().get("lotka.axis.pop", "Population"));` |
| 92 | `timeChart.setTitle(I18n.getInstance().get("lotka.chart.time", "Population Dynamics"));` |
| 94 | `preySeries.setName(I18n.getInstance().get("lotka.series.prey", "Prey"));` |
| 95 | `predSeries.setName(I18n.getInstance().get("lotka.series.pred", "Predator"));` |
| 100 | `xAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.prey", "Prey"));` |
| 102 | `yAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.pred", "Predator"));` |
| 105 | `phaseChart.setTitle(I18n.getInstance().get("lotka.chart.phase", "Phase Portrait"));` |
| 106 | `phaseSeries.setName(I18n.getInstance().get("lotka.series.traj", "Trajectory"));` |
| 116 | `sidebar.getStyleClass().add("dark-viewer-sidebar");` |
| 119 | `new Label(I18n.getInstance().get("lotka.header.params", "Parameters")),` |
| 120 | `createSliderLabel(I18n.getInstance().get("lotka.label.alpha", "Alpha"), 0, 5, alpha, v -> alpha = v),` |
| 121 | `createSliderLabel(I18n.getInstance().get("lotka.label.beta", "Beta"), 0, 5, beta, v -> beta = v),` |
| 122 | `createSliderLabel(I18n.getInstance().get("lotka.label.delta", "Delta"), 0, 5, delta, v -> delta = v),` |
| 123 | `createSliderLabel(I18n.getInstance().get("lotka.label.gamma", "Gamma"), 0, 5, gamma, v -> gamma = v));` |
| 125 | `Button resetBtn = new Button(I18n.getInstance().get("lotka.btn.reset", "Reset"));` |
| 187 | `Label lbl = new Label(name + ": " + String.format("%.2f", val));` |
| 191 | `lbl.setText(name + ": " + String.format("%.2f", nv.doubleValue()));` |

## SpeciesBrowserViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\biology\ecology\SpeciesBrowserViewer.java`

| Line | Content |
| --- | --- |
| 48 | `public String getName() { return I18n.getInstance().get("viewer.species"); }` |
| 51 | `public String getCategory() { return "Biology"; }` |
| 58 | `getStyleClass().add("dark-viewer-root");` |
| 66 | `searchField = new TextField("Panthera leo");` |
| 69 | `Button searchBtn = new Button(I18n.getInstance().get("species.btn.search", "Search GBIF"));` |
| 76 | `searchBar.getChildren().addAll(new Label(I18n.getInstance().get("species.label.species", "Species:")), searchField, searchBtn, progress);` |
| 86 | `detailArea = new TextArea(I18n.getInstance().get("species.prompt.search", "Search for a species to view details from GBIF..."));` |
| 89 | `detailArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");` |
| 95 | `details.getChildren().addAll(new Label(I18n.getInstance().get("species.label.taxonomy", "Taxonomy & Details:")), detailArea, imageView);` |
| 111 | `detailArea.setText(I18n.getInstance().get("species.msg.notfound", "No results found for: %s", query));` |
| 116 | `detailArea.setText(I18n.getInstance().get("species.error", "Error: %s", ex.getMessage()));` |
| 132 | `sb.append(I18n.getInstance().get("species.detail.name", "Scientific Name: %s", species.scientificName())).append("\n");` |
| 133 | `sb.append(I18n.getInstance().get("species.detail.rank", "Rank: %s", species.rank())).append("\n\n");` |
| 134 | `sb.append(I18n.getInstance().get("species.detail.classification", "Classification:")).append("\n");` |
| 135 | `sb.append("  Kingdom: ").append(species.kingdom()).append("\n");` |
| 136 | `sb.append("  Phylum:  ").append(species.phylum()).append("\n");` |
| 137 | `sb.append("  Class:   ").append(species.clazz()).append("\n");` |
| 138 | `sb.append("  Order:   ").append(species.order()).append("\n");` |
| 139 | `sb.append("  Family:  ").append(species.family()).append("\n");` |
| 140 | `sb.append("  Genus:   ").append(species.genus()).append("\n");` |
| 141 | `sb.append("\n").append(I18n.getInstance().get("species.source", "Source: Global Biodiversity Information Facility (GBIF)"));` |

## GeneticsViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\biology\genetics\GeneticsViewer.java`

| Line | Content |
| --- | --- |
| 67 | `public String getName() { return I18n.getInstance().get("viewer.genetics", "Genetics"); }` |
| 70 | `public String getCategory() { return "Biology"; }` |
| 78 | `tabPane.getStyleClass().add("dark-tab-pane");` |
| 80 | `Tab driftTab = new Tab(I18n.getInstance().get("genetics.tab.drift", "Genetic Drift"));` |
| 84 | `Tab mendelTab = new Tab(I18n.getInstance().get("genetics.tab.mendel", "Mendelian"));` |
| 96 | `Button runBtn = new Button(I18n.getInstance().get("genetics.run", "Run Simulation"));` |
| 116 | `new Label(I18n.getInstance().get("genetics.popsize", "Pop Size")), popSpinner,` |
| 117 | `new Label(I18n.getInstance().get("genetics.generations", "Generations")), genSpinner,` |
| 118 | `new Label(I18n.getInstance().get("genetics.initialfreq", "Initial Freq")), freqSlider,` |
| 125 | `infoPanel.getStyleClass().add("dark-viewer-sidebar");` |
| 128 | `Label titleLabel = new Label(I18n.getInstance().get("genetics.subtitle", "Genetic Drift"));` |
| 129 | `titleLabel.getStyleClass().add("dark-header");` |
| 131 | `Label explanationLabel = new Label(I18n.getInstance().get("genetics.explanation", "Simulates allele frequency changes in a population over generations due to random sampling."));` |
| 133 | `explanationLabel.setStyle("-fx-font-size: 11px;");` |
| 135 | `driftStatusLabel = new Label(I18n.getInstance().get("genetics.status.start", "Click Run to start"));` |
| 136 | `driftStatusLabel.setStyle("-fx-font-style: italic;");` |
| 144 | `root.getStyleClass().add("dark-viewer-root");` |
| 163 | `String outcome = freq >= 0.99 ? I18n.getInstance().get("genetics.outcome.fixed", "Fixed")` |
| 164 | `: freq <= 0.01 ? I18n.getInstance().get("genetics.outcome.lost", "Lost") : String.format("%.3f", freq);` |
| 165 | `driftStatusLabel.setText(String.format(I18n.getInstance().get("genetics.frequency", "Final Freq: %s"), outcome));` |
| 175 | `gc.setStroke(Color.web("#444444"));` |
| 180 | `gc.setFill(Color.web("#222222"));` |
| 181 | `gc.fillText(I18n.getInstance().get("genetics.axis.generation", "Generation"), 350, 385);` |
| 182 | `gc.fillText(I18n.getInstance().get("genetics.axis.allele", "Allele"), 5, 180);` |
| 183 | `gc.fillText(I18n.getInstance().get("genetics.axis.frequency", "Frequency"), 5, 195);` |
| 185 | `gc.setStroke(Color.web("#888888"));` |
| 190 | `gc.setFill(Color.web("#333333"));` |
| 191 | `gc.fillText(String.format("%.2f", f), 20, y + 4);` |
| 197 | `gc.setStroke(Color.web("#00d9ff"));` |
| 215 | `inputPanel.getStyleClass().add("dark-viewer-sidebar");` |
| 218 | `Label title = new Label(I18n.getInstance().get("genetics.tab.mendel", "Mendelian Inheritance"));` |
| 219 | `title.getStyleClass().add("dark-header");` |
| 221 | `parent1Field = new TextField("Aa");` |
| 222 | `parent2Field = new TextField("Aa");` |
| 224 | `Button calcBtn = new Button(I18n.getInstance().get("genetics.mendel.calculate", "Calculate"));` |
| 230 | `mendelResultsLabel.setStyle("-fx-font-size: 13px;");` |
| 234 | `new Label(I18n.getInstance().get("genetics.mendel.parent1", "Parent 1 Genotype")), parent1Field,` |
| 235 | `new Label(I18n.getInstance().get("genetics.mendel.parent2", "Parent 2 Genotype")), parent2Field,` |
| 241 | `root.getStyleClass().add("dark-viewer-root");` |
| 252 | `mendelResultsLabel.setText(I18n.getInstance().get("genetics.mendel.error", "Enter 2-character genotypes"));` |
| 270 | `StringBuilder results = new StringBuilder(I18n.getInstance().get("genetics.mendel.ratios", "Ratios:") + "\n");` |
| 273 | `results.append(String.format("  %s : %.0f%%\n", entry.getKey(), percent));` |
| 281 | `if (Character.isUpperCase(a) && Character.isLowerCase(b)) return "" + a + b;` |
| 282 | `if (Character.isLowerCase(a) && Character.isUpperCase(b)) return "" + b + a;` |
| 283 | `return (a < b) ? "" + a + b : "" + b + a;` |
| 299 | `gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));` |
| 306 | `gc.fillText("" + p1[0], startX + size / 4 - 8, startY - 20);` |
| 307 | `gc.fillText("" + p1[1], startX + 3 * size / 4 - 8, startY - 20);` |
| 308 | `gc.fillText("" + p2[0], startX - 30, startY + size / 4 + 8);` |
| 309 | `gc.fillText("" + p2[1], startX - 30, startY + 3 * size / 4 + 8);` |

## LSystemViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\biology\lsystems\LSystemViewer.java`

| Line | Content |
| --- | --- |
| 76 | `sb.append("Axiom: ").append(axiom).append("\n");` |
| 77 | `sb.append("Angle: ").append(angle).append("\n");` |
| 78 | `rules.forEach((k, v) -> sb.append(k).append(" -> ").append(v).append("\n"));` |
| 106 | `@Override public String getName() { return I18n.getInstance().get("viewer.lsystem", "L-System Visualizer"); }` |
| 107 | `@Override public String getCategory() { return "Biology"; }` |
| 110 | `LSystem plant = new LSystem("Fractal Plant", "X", 25, false);` |
| 111 | `plant.addRule('X', "F+[[X]-X]-F[-FX]+X"); plant.addRule('F', "FF");` |
| 113 | `LSystem dragon = new LSystem("Dragon Curve", "FX", 90, false);` |
| 114 | `dragon.addRule('X', "X+YF+"); dragon.addRule('Y', "-FX-Y");` |
| 116 | `LSystem sierpinski = new LSystem("Sierpinski Triangle", "F-G-G", 120, false);` |
| 117 | `sierpinski.addRule('F', "F-G+F+G-F"); sierpinski.addRule('G', "GG");` |
| 119 | `LSystem bush = new LSystem("Bush", "X", 25, false);` |
| 120 | `bush.addRule('X', "F-[[X]+X]+F[+FX]-X"); bush.addRule('F', "FF");` |
| 122 | `LSystem algae = new LSystem("Algae", "A", 0, false);` |
| 123 | `algae.addRule('A', "AB"); algae.addRule('B', "A");` |
| 125 | `LSystem koch = new LSystem("Koch Curve", "F", 90, false);` |
| 126 | `koch.addRule('F', "F+F-F-F+F");` |
| 128 | `LSystem tree3D = new LSystem("3D Tree", "F", 25, true);` |
| 129 | `tree3D.addRule('F', "F[+F]F[-F]F");` |
| 131 | `LSystem hilbert3D = new LSystem("Hilbert 3D", "X", 90, true);` |
| 132 | `hilbert3D.addRule('X', "^<XF^<XFX-F^>>XFX&F+>>XFX-F>X->");` |
| 137 | `this.getStyleClass().add("dark-viewer-root");` |
| 140 | `renderPane.setStyle("-fx-background-color: #222;");` |
| 162 | `sidebar.getStyleClass().add("dark-viewer-sidebar");` |
| 164 | `Label title = new Label("L-System Settings");` |
| 165 | `title.getStyleClass().add("dark-header");` |
| 169 | `presetCombo.setValue("Fractal Plant");` |
| 176 | `Label iterLabel = new Label("Iterations: 4");` |
| 177 | `iterSlider.valueProperty().addListener((obs, old, val) -> iterLabel.setText("Iterations: " + val.intValue()));` |
| 179 | `animCheck = new CheckBox(I18n.getInstance().get("lsystem.animate", "Animate Growth"));` |
| 182 | `Button renderBtn = new Button(I18n.getInstance().get("lsystem.generate", "Generate & Render"));` |
| 194 | `rulesArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px;");` |
| 196 | `statusLabel = new Label("Ready");` |
| 197 | `statusLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10px;");` |
| 199 | `sidebar.getChildren().addAll(title, new Label("Preset:"), presetCombo, iterLabel, iterSlider, animCheck, new Separator(), new Label("Rules:"), rulesArea, new Separator(), renderBtn, statusLabel);` |
| 202 | `loadPreset("Fractal Plant");` |
| 233 | `statusLabel.setText(String.format("Generated %d chars in %dms", commands.length(), dur));` |
| 263 | `if (currentSystem.name.contains("Dragon")) step = 5;` |

## PhylogeneticTreeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\biology\phylogeny\PhylogeneticTreeViewer.java`

| Line | Content |
| --- | --- |
| 74 | `public String getName() { return I18n.getInstance().get("viewer.phylogeny"); }` |
| 77 | `public String getCategory() { return "Biology"; }` |
| 80 | `this.getStyleClass().add("dark-viewer-root");` |
| 89 | `Label infoPanel = new Label(I18n.getInstance().get("phylogeny.info.default"));` |
| 90 | `infoPanel.getStyleClass().add("dark-viewer-sidebar");` |
| 92 | `javafx.scene.control.Button toggleBtn = new javafx.scene.control.Button(I18n.getInstance().get("phylogeny.toggle_view"));` |
| 110 | `String txt = I18n.getInstance().get("phylogeny.info.selected", clicked.name, clicked.coi, clicked.rna16s, clicked.cytb);` |
| 115 | `infoPanel.setText(I18n.getInstance().get("phylogeny.info.default"));` |
| 195 | `gc.setFill(Color.web("#333333"));` |
| 196 | `gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 12));` |
| 197 | `gc.fillText(I18n.getInstance().get("phylogeny.marker.coi"), 760, 20);` |
| 198 | `gc.fillText(I18n.getInstance().get("phylogeny.marker.rna16s"), 790, 20);` |
| 199 | `gc.fillText(I18n.getInstance().get("phylogeny.marker.cytb"), 820, 20);` |
| 203 | `gc.setStroke(Color.web("#666"));` |
| 220 | `gc.setFill(node.children.isEmpty() ? Color.web("#4CAF50") : Color.web("#2196F3"));` |
| 224 | `Color textColor = Color.web("#111111");` |
| 230 | `gc.setFont(javafx.scene.text.Font.font("SansSerif", 12));` |
| 267 | `Node root = new Node("Primates (Order)");` |
| 268 | `Node strepsirrhini = new Node("Strepsirrhini"); Node haplorhini = new Node("Haplorhini");` |
| 270 | `Node lemuriformes = new Node("Lemuriformes"); Node lorisiformes = new Node("Lorisiformes");` |
| 272 | `lemuriformes.add(new Node("Lemur catta", 0.72, 0.65, 0.45));` |
| 273 | `lemuriformes.add(new Node("Microcebus murinus", 0.70, 0.63, 0.48));` |
| 274 | `lorisiformes.add(new Node("Nycticebus coucang", 0.68, 0.60, 0.50));` |
| 275 | `Node tarsiiformes = new Node("Tarsiiformes"); Node simiiformes = new Node("Simiiformes");` |
| 277 | `tarsiiformes.add(new Node("Tarsius syrichta", 0.75, 0.68, 0.55));` |
| 278 | `Node platyrrhini = new Node("Platyrrhini (New World)"); Node catarrhini = new Node("Catarrhini (Old World)");` |
| 280 | `platyrrhini.add(new Node("Saimiri sciureus", 0.82, 0.75, 0.65));` |
| 281 | `platyrrhini.add(new Node("Alouatta palliata", 0.80, 0.73, 0.62));` |
| 282 | `Node cercopithecoidea = new Node("Cercopithecoidea"); Node hominoidea = new Node("Hominoidea (Apes)");` |
| 284 | `cercopithecoidea.add(new Node("Macaca mulatta", 0.88, 0.80, 0.75));` |
| 285 | `cercopithecoidea.add(new Node("Papio anubis", 0.87, 0.79, 0.74));` |
| 286 | `Node hylobatidae = new Node("Hylobatidae"); Node hominidae = new Node("Hominidae");` |
| 288 | `hylobatidae.add(new Node("Hylobates lar", 0.90, 0.85, 0.80));` |
| 289 | `Node ponginae = new Node("Ponginae"); Node homininae = new Node("Homininae");` |
| 291 | `ponginae.add(new Node("Pongo abelii", 0.92, 0.88, 0.82));` |
| 292 | `homininae.add(new Node("Gorilla gorilla", 0.95, 0.92, 0.88));` |
| 293 | `homininae.add(new Node("Pan troglodytes", 0.98, 0.96, 0.94));` |
| 294 | `homininae.add(new Node("Homo sapiens", 0.99, 0.98, 0.97));` |

## ChemicalReactionViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\chemistry\ChemicalReactionViewer.java`

| Line | Content |
| --- | --- |
| 49 | `public String getCategory() { return "Chemistry"; }` |
| 52 | `public String getName() { return I18n.getInstance().get("chemical.title.parser", "Chemical Reaction Parser"); }` |
| 59 | `this.getStyleClass().add("dark-viewer-root");` |
| 68 | `inputBox.getStyleClass().add("dark-viewer-sidebar");` |
| 70 | `Label inputLabel = new Label(I18n.getInstance().get("chemical.label.eqn"));` |
| 71 | `inputLabel.getStyleClass().add("dark-label-muted");` |
| 74 | `inputArea.setPromptText(I18n.getInstance().get("chemical.prompt.eqn", "Enter chemical equation, e.g.: 2H2 + O2 -> 2H2O"));` |
| 79 | `exampleCombo.setPromptText(I18n.getInstance().get("chemical.prompt.example", "Select an example..."));` |
| 81 | `"2H2 + O2 -> 2H2O",` |
| 82 | `"CH4 + 2O2 -> CO2 + 2H2O",` |
| 83 | `"HCl + NaOH -> NaCl + H2O",` |
| 84 | `"6CO2 + 6H2O -> C6H12O6 + 6O2",` |
| 85 | `"C8H18 + 12.5O2 -> 8CO2 + 9H2O",` |
| 86 | `"Fe2O3 + 3CO -> 2Fe + 3CO2"` |
| 96 | `Button loadFileBtn = new Button(I18n.getInstance().get("chemical.btn.load"));` |
| 100 | `fileChooser.setTitle(I18n.getInstance().get("chemical.file.open", "Open Reaction File"));` |
| 101 | `fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter(I18n.getInstance().get("chemical.file.type", "Text Files"), "*.txt", "*.rxn"));` |
| 109 | `outputArea.setText(I18n.getInstance().get("chemical.error.read", "Error reading file: %s", ex.getMessage()));` |
| 117 | `Button parseBtn = new Button(I18n.getInstance().get("chemical.btn.parse"));` |
| 118 | `parseBtn.getStyleClass().add("accent-button-green");` |
| 127 | `outputBox.getStyleClass().add("dark-viewer-sidebar");` |
| 129 | `Label outputLabel = new Label(I18n.getInstance().get("chemical.label.results"));` |
| 130 | `outputLabel.getStyleClass().add("dark-label-muted");` |
| 135 | `outputArea.setStyle("-fx-font-family: 'Consolas', monospace;");` |
| 137 | `statusLabel = new Label("");` |
| 138 | `statusLabel.setStyle("-fx-font-weight: bold;");` |
| 149 | `formulaPanel.setStyle("-fx-background-color: #222222; -fx-background-radius: 5;"); // Or dark-viewer-sidebar` |
| 152 | `Label formulaTitle = new Label(I18n.getInstance().get("chemical.label.quick"));` |
| 153 | `formulaTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #00d9ff;");` |
| 156 | `formulaInput.setPromptText(I18n.getInstance().get("chemical.prompt.formula", "e.g., Ca(OH)2"));` |
| 161 | `formulaOutput.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px; -fx-text-fill: #aaaaaa; -fx-control-inner-background: #333333;");` |
| 163 | `Button parseFormulaBtn = new Button(I18n.getInstance().get("chemical.btn.formula"));` |
| 169 | `sb.append(I18n.getInstance().get("chemical.label.formula", "Formula: %s", f.toString())).append("\n");` |
| 170 | `sb.append(I18n.getInstance().get("chemical.label.coefficient", "Coefficient: %d", (long) f.getCoefficient())).append("\n");` |
| 171 | `sb.append(I18n.getInstance().get("chemical.label.elements", "Elements:")).append("\n");` |
| 172 | `for (var entry : f.getElements().entrySet()) sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");` |
| 173 | `if (f.getState() != null) sb.append(I18n.getInstance().get("chemical.label.state", "State: %s", f.getState())).append("\n");` |
| 174 | `if (f.getCharge() != 0) sb.append(I18n.getInstance().get("chemical.label.charge", "Charge: %d", (long) f.getCharge())).append("\n");` |
| 177 | `formulaOutput.setText(I18n.getInstance().get("chemical.status.error", "Error: %s", ex.getMessage()));` |
| 188 | `outputArea.setText(I18n.getInstance().get("chemical.msg.enter"));` |
| 196 | `sb.append("══════════════════════════════════════\n");` |
| 197 | `sb.append(I18n.getInstance().get("chemical.header.parsed", "PARSED REACTION")).append("\n");` |
| 198 | `sb.append("══════════════════════════════════════\n\n");` |
| 199 | `sb.append(I18n.getInstance().get("chemical.label.formula", "Formatted: %s", reaction.toString())).append("\n\n");` |
| 201 | `sb.append("─── ").append(I18n.getInstance().get("chemical.header.reactants", "REACTANTS")).append(" ───\n");` |
| 203 | `sb.append("  ").append(formatFormula(f)).append("\n");` |
| 206 | `sb.append("\n─── ").append(I18n.getInstance().get("chemical.header.products", "PRODUCTS")).append(" ───\n");` |
| 208 | `sb.append("  ").append(formatFormula(f)).append("\n");` |
| 211 | `sb.append("\n─── ").append(I18n.getInstance().get("chemical.header.balance", "ELEMENT BALANCE")).append(" ───\n");` |
| 215 | `sb.append("\n══════════════════════════════════════\n");` |
| 216 | `sb.append(I18n.getInstance().get("chemical.result.prefix", "RESULT: "))` |
| 217 | `.append(balanced ? I18n.getInstance().get("chemical.result.balanced", "✓ BALANCED")` |
| 218 | `: I18n.getInstance().get("chemical.result.unbalanced", "✗ NOT BALANCED"))` |
| 219 | `.append("\n");` |
| 220 | `sb.append("══════════════════════════════════════\n");` |
| 225 | `statusLabel.setText(I18n.getInstance().get("chemical.status.balanced"));` |
| 226 | `statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");` |
| 228 | `statusLabel.setText(I18n.getInstance().get("chemical.status.unbalanced"));` |
| 229 | `statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");` |
| 233 | `outputArea.setText(I18n.getInstance().get("chemical.error.parse", "Error parsing equation:\n%s", e.getMessage()));` |
| 234 | `statusLabel.setText(I18n.getInstance().get("chemical.status.error"));` |
| 235 | `statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");` |
| 242 | `sb.append(I18n.getInstance().get("chemical.out.elements", " \u2192 Elements: "));` |
| 244 | `sb.append(e.getKey()).append("=").append(e.getValue()).append(" ");` |

## MolecularViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\chemistry\MolecularViewer.java`

| Line | Content |
| --- | --- |
| 56 | `private Label detailLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("molecule.select"));` |
| 65 | `return org.jscience.ui.i18n.I18n.getInstance().get("molecule.window.title");` |
| 70 | `return "Chemistry";` |
| 78 | `controls.setStyle("-fx-background-color: #eee;");` |
| 82 | `"Benzene", "DNA", "Water", "Protein Folding")); // Removed some for brevity` |
| 83 | `selector.setValue("Benzene");` |
| 94 | `new Label(org.jscience.ui.i18n.I18n.getInstance().get("molecule.label.load")),` |
| 97 | `new Label("Style"),` |
| 103 | `loadModel("Benzene");` |
| 111 | `if ("Benzene".equals(name))` |
| 113 | `else if ("DNA".equals(name))` |
| 115 | `else if ("Protein Folding".equals(name)) {` |
| 132 | `Atom c = new Atom(PeriodicTable.bySymbol("C"), vec(r * Math.cos(ang), r * Math.sin(ang), 0));` |
| 133 | `Atom h = new Atom(PeriodicTable.bySymbol("H"), vec((r + 2) * Math.cos(ang), (r + 2) * Math.sin(ang), 0));` |
| 147 | `mol.addAtom(new Atom(PeriodicTable.bySymbol("P"), vec(8 * Math.cos(ang), y, 8 * Math.sin(ang))));` |
| 148 | `mol.addAtom(new Atom(PeriodicTable.bySymbol("P"),` |

## PeriodicTableViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\chemistry\PeriodicTableViewer.java`

| Line | Content |
| --- | --- |
| 70 | `{ "H", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,` |
| 71 | `"He" },` |
| 72 | `{ "Li", "Be", null, null, null, null, null, null, null, null, null, null, "B", "C", "N", "O", "F", "Ne" },` |
| 73 | `{ "Na", "Mg", null, null, null, null, null, null, null, null, null, null, "Al", "Si", "P", "S", "Cl",` |
| 74 | `"Ar" },` |
| 75 | `{ "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br",` |
| 76 | `"Kr" },` |
| 77 | `{ "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I",` |
| 78 | `"Xe" },` |
| 79 | `{ "Cs", "Ba", "*", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At",` |
| 80 | `"Rn" },` |
| 81 | `{ "Fr", "Ra", "**", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts",` |
| 82 | `"Og" },` |
| 84 | `{ null, null, "*", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",` |
| 85 | `"Lu" },` |
| 86 | `{ null, null, "**", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No",` |
| 87 | `"Lr" }` |
| 103 | `zoomPane.getStyleClass().add("dark-viewer-root");` |
| 108 | `scrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");` |
| 120 | `HBox topBar = new HBox(10, new Label(I18n.getInstance().get("periodic.zoom", "Zoom")), zoomSlider);` |
| 123 | `topBar.getStyleClass().add("dark-viewer-sidebar");` |
| 136 | `grid.getStyleClass().add("dark-viewer-root");` |
| 141 | `if (sym == null \|\| sym.equals("*") \|\| sym.equals("**")) {` |
| 160 | `el = new Element("Unknown", symbol);` |
| 169 | `num.setFont(Font.font("Arial", 10));` |
| 171 | `symLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));` |
| 173 | `name.setFont(Font.font("Arial", 9));` |
| 182 | `t.setStyle("-fx-font-size: 12px;");` |
| 186 | `e -> cell.setStyle(style + " -fx-opacity: 0.8; -fx-border-color: white; -fx-border-width: 2;"));` |
| 195 | `panel.getStyleClass().add("dark-viewer-sidebar");` |
| 197 | `Label title = new Label(I18n.getInstance().get("periodic.details", "Element Details"));` |
| 198 | `title.setFont(Font.font("Arial", FontWeight.BOLD, 18));` |
| 199 | `title.getStyleClass().add("dark-header");` |
| 201 | `Label hint = new Label(I18n.getInstance().get("periodic.hint", "Select an element to view details"));` |
| 204 | `hint.setId("hint-label");` |
| 208 | `Tab elecTab = new Tab(I18n.getInstance().get("periodic.electronic", "Electronic"));` |
| 216 | `Tab nucTab = new Tab(I18n.getInstance().get("periodic.nuclear", "Nuclear"));` |
| 231 | `detailPanel.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("hint-label"));` |
| 239 | `sym.setFont(Font.font("Arial", FontWeight.BOLD, 48));` |
| 240 | `sym.setTextFill(Color.web("#54a0ff"));` |
| 242 | `name.setFont(Font.font("Arial", FontWeight.BOLD, 24));` |
| 251 | `props.getChildren().add(createPropLabel("Atomic Number", String.valueOf(element.getAtomicNumber())));` |
| 252 | `props.getChildren().add(createPropLabel("Category",` |
| 253 | `element.getCategory() != null ? element.getCategory().name() : "Unknown"));` |
| 255 | `props.getChildren().add(createPropLabel("Atomic Mass",` |
| 256 | `String.format("%.4f u", element.getAtomicMass().getValue().doubleValue())));` |
| 260 | `VBox atomBox = new VBox(5, new Label(I18n.getInstance().get("periodic.structure", "Atomic Structure")),` |
| 262 | `atomBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");` |
| 265 | `Label nucHeader = new Label(element.getName() + " (" + element.getSymbol() + ")");` |
| 266 | `nucHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));` |
| 270 | `VBox nucBox = new VBox(5, new Label("Nucleus Structure (Model)"), nucView);` |
| 271 | `nucBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");` |
| 273 | `Label isoLabel = new Label("Known Isotopes:");` |
| 274 | `isoLabel.setStyle("-fx-font-weight: bold;");` |
| 309 | `ss.setFill(Color.web("#222"));` |
| 326 | `PhongMaterial m = new PhongMaterial(Color.web("#3498db", 0.3));` |
| 334 | `createLobePair(root, new Point3D(0, 1, 0), s, Color.web("#e74c3c"));` |
| 335 | `createLobePair(root, new Point3D(1, 0, 0), s, Color.web("#2ecc71"));` |
| 336 | `createLobePair(root, new Point3D(0, 0, 1), s, Color.web("#f1c40f"));` |
| 341 | `createLobePair(root, new Point3D(0, 1, 0), s, Color.web("#3498db"));` |
| 343 | `PhongMaterial m = new PhongMaterial(Color.web("#e67e22"));` |
| 453 | `ss.setFill(Color.web("#222"));` |
| 471 | `iso.add(el.getSymbol() + "-" + a + " (Stable)"); // Mock` |
| 476 | `Label nl = new Label(n + ":");` |
| 487 | `return e.getName() + " (" + e.getSymbol() + ")\nZ: " + e.getAtomicNumber();` |
| 491 | `String base = "-fx-background-radius: 5; -fx-cursor: hand;";` |
| 493 | `return "-fx-background-color: #576574;" + base;` |
| 496 | `return "-fx-background-color: #ff6b6b;" + base;` |
| 498 | `return "-fx-background-color: #c8d6e5;" + base;` |
| 500 | `return "-fx-background-color: #8395a7;" + base; // Simplified mapping` |
| 504 | `@Override public String getName() { return "Periodic Viewer"; }` |
| 505 | `@Override public String getCategory() { return "Chemistry"; }` |

## DigitalLogicViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\computing\logic\DigitalLogicViewer.java`

| Line | Content |
| --- | --- |
| 177 | `root.setStyle("-fx-background-color: white;");` |
| 195 | `toolbar.getStyleClass().add("dark-viewer-sidebar"); // Or keep default` |
| 197 | `ToggleButton modeBtn = new ToggleButton(I18n.getInstance().get("digital.mode.simulation", "Simulation Mode"));` |
| 202 | `modeBtn.setText(simulationMode ? I18n.getInstance().get("digital.mode.simulation", "Simulation Mode")` |
| 203 | `: I18n.getInstance().get("digital.mode.design", "Design Mode"));` |
| 213 | `Label compLabel = new Label(I18n.getInstance().get("digital.components", "Components"));` |
| 214 | `compLabel.setStyle("-fx-font-weight: bold;");` |
| 216 | `Button btnAnd = new Button("AND Gate"); btnAnd.setMaxWidth(Double.MAX_VALUE);` |
| 219 | `Button btnOr = new Button("OR Gate"); btnOr.setMaxWidth(Double.MAX_VALUE);` |
| 222 | `Button btnNot = new Button("NOT Gate"); btnNot.setMaxWidth(Double.MAX_VALUE);` |
| 225 | `Button btnNand = new Button("NAND Gate"); btnNand.setMaxWidth(Double.MAX_VALUE);` |
| 228 | `Button btnIn = new Button("Input Switch"); btnIn.setMaxWidth(Double.MAX_VALUE);` |
| 231 | `Button btnOut = new Button("Output LED"); btnOut.setMaxWidth(Double.MAX_VALUE);` |
| 234 | `Label actionLabel = new Label(I18n.getInstance().get("digital.actions", "Actions"));` |
| 235 | `actionLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");` |
| 237 | `Button clear = new Button(I18n.getInstance().get("digital.clear", "Clear All"));` |
| 241 | `Button saveBtn = new Button(I18n.getInstance().get("digital.btn.save", "Save Circuit"));` |
| 247 | `Button loadBtn = new Button(I18n.getInstance().get("digital.btn.load", "Load Circuit"));` |
| 253 | `statusLabel = new Label("Ready");` |
| 255 | `statusLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 10px;");` |
| 271 | `if (simulationMode) statusLabel.setText("Simulation: Click Inputs to toggle.");` |
| 272 | `else statusLabel.setText("Design: Drag gates/wires. Select & Delete to remove.");` |
| 298 | `fileChooser.setTitle("Save Circuit");` |
| 299 | `fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));` |
| 303 | `for (Gate g : gates) pw.println("GATE " + g.type + " " + (int) g.x + " " + (int) g.y);` |
| 309 | `if (sGate != -1 && eGate != -1) pw.println("WIRE " + sGate + ":" + sPort + " " + eGate + ":" + ePort);` |
| 311 | `statusLabel.setText("Saved: " + file.getName());` |
| 312 | `} catch (Exception ex) { statusLabel.setText("Error saving: " + ex.getMessage()); }` |
| 318 | `fileChooser.setTitle("Load Circuit");` |
| 319 | `fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));` |
| 326 | `if (line.startsWith("GATE")) {` |
| 327 | `String[] parts = line.split(" ");` |
| 329 | `} else if (line.startsWith("WIRE")) {` |
| 330 | `String[] parts = line.split(" ");` |
| 331 | `String[] s = parts[1].split(":");` |
| 332 | `String[] e = parts[2].split(":");` |
| 337 | `simulate(); draw(); statusLabel.setText("Loaded: " + file.getName());` |
| 338 | `} catch (Exception ex) { statusLabel.setText("Error: " + ex.getMessage()); }` |
| 359 | `gc.setStroke(Color.web("#eee")); gc.setLineWidth(1);` |
| 382 | `@Override public String getName() { return "Digital Logic Viewer"; }` |
| 383 | `@Override public String getCategory() { return "Computing"; }` |
| 452 | `gc.setFill(Color.BLACK); gc.fillText(isOn ? "ON" : "OFF", x + 5, y + 20);` |

## MapViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\earth\MapViewer.java`

| Line | Content |
| --- | --- |
| 104 | `gc.setFill(Color.web("#0a1929"));` |
| 112 | `gc.setStroke(Color.web("#1a3a5c"));` |
| 121 | `gc.fillText(lat + "°", 5, y - 2);` |
| 130 | `gc.fillText(lon + "°", x + 2, h - 5);` |
| 134 | `gc.setStroke(Color.web("#2a5a8c"));` |

## EarthquakeMapViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\earth\seismology\EarthquakeMapViewer.java`

| Line | Content |
| --- | --- |
| 67 | `coordLabel.setText(String.format(I18n.getInstance().get("earthquake.coord.fmt", "Lat: %.2f  Lon: %.2f"), lat, lon));` |
| 74 | `infoLabel.setText(String.format(I18n.getInstance().get("earthquake.info.format", "Mag: %.1f\nLat: %.2f\nLon: %.2f\nDepth: %.0f km"),` |
| 80 | `if(!hover) infoLabel.setText(I18n.getInstance().get("earthquake.hover", "Hover over a quake for info."));` |
| 94 | `sidebar.getStyleClass().add("dark-viewer-sidebar");` |
| 97 | `Label titleLabel = new Label(I18n.getInstance().get("earthquake.label.title", "Earthquake Map"));` |
| 98 | `titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));` |
| 100 | `Label explainLabel = new Label(I18n.getInstance().get("earthquake.explanation", "Real-time visualization of seismic activity."));` |
| 103 | `Label legendLabel = new Label(I18n.getInstance().get("earthquake.legend", "Magnitude"));` |
| 104 | `legendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));` |
| 108 | `createLegendItem(Color.hsb(120, 1, 1), "Minor (< 3.0)"),` |
| 109 | `createLegendItem(Color.hsb(80, 1, 1), "Light (3.0 - 5.0)"),` |
| 110 | `createLegendItem(Color.hsb(40, 1, 1), "Moderate (5.0 - 6.0)"),` |
| 111 | `createLegendItem(Color.RED, "Strong (> 6.0)"));` |
| 113 | `coordLabel = new Label("Lat: 0.00 Lon: 0.00");` |
| 114 | `coordLabel.setFont(Font.font("Consolas", 12));` |
| 116 | `infoLabel = new Label(I18n.getInstance().get("earthquake.hover", "Hover over a quake for info."));` |
| 126 | `Label l = new Label("- " + text);` |
| 128 | `l.setFont(Font.font("Arial", 11));` |
| 156 | `gc.fillText(String.format("Zoom: %.1fx", zoom), 10, 20);` |
| 157 | `gc.fillText(String.format("Quakes: %d", quakes.size()), 10, 35);` |
| 185 | `@Override public String getName() { return "Earthquake Viewer"; }` |
| 186 | `@Override public String getCategory() { return "Earth Sciences"; }` |

## HumanBodyViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\medicine\anatomy\HumanBodyViewer.java`

| Line | Content |
| --- | --- |
| 64 | `return "Biology";` |
| 69 | `return "Human Body Viewer (JavaFX)";` |
| 153 | `if (item == null) item = idToTreeItem.get(id.replace("_", " "));` |
| 160 | `titleLabel.setText("Unknown Part");` |
| 161 | `descriptionArea.setText("No ID found for this mesh.");` |
| 164 | `titleLabel.setText("Human Body");` |
| 165 | `descriptionArea.setText("Select a part to see its description.");` |
| 179 | `loadingLabel = new Label("Loading Anatomy...");` |
| 180 | `loadingLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 10; -fx-background-radius: 5;");` |
| 187 | `Label attributionLabel = new Label("Models: Z-Anatomy (CC BY-SA 4.0)");` |
| 188 | `attributionLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-background-color: rgba(0,0,0,0.3); -fx-padding: 5; -fx-font-size: 10px;");` |
| 198 | `leftToolbar.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #3f3f3f; -fx-border-width: 0 1 0 0;");` |
| 201 | `Label layersHeader = new Label("SYSTEMS");` |
| 202 | `layersHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");` |
| 206 | `createLayerWithLoading("Skin", skinLayer, "/org/jscience/medicine/anatomy/models/Regions of human body100.fbx", Color.rgb(255, 218, 185, 0.4)),` |
| 207 | `createLayerWithLoading("Muscles", muscleLayer, "/org/jscience/medicine/anatomy/models/MuscularSystem100.fbx", Color.INDIANRED),` |
| 208 | `createLayerWithLoading("Skeleton", skeletonLayer, "/org/jscience/medicine/anatomy/models/SkeletalSystem100.fbx", Color.IVORY),` |
| 209 | `createLayerWithLoading("Organs", organLayer, "/org/jscience/medicine/anatomy/models/VisceralSystem100.fbx", Color.CORAL),` |
| 210 | `createLayerWithLoading("Nervous", nervousLayer, "/org/jscience/medicine/anatomy/models/NervousSystem100.fbx", Color.GOLD),` |
| 211 | `createLayerWithLoading("Circulatory", circulatoryLayer, "/org/jscience/medicine/anatomy/models/CardioVascular41.fbx", Color.DARKRED),` |
| 213 | `new Label("ACTIONS"),` |
| 214 | `createButton("Center View", () -> {` |
| 225 | `System.out.println("Centering on " + mesh.getId() + " at " + center);` |
| 230 | `createButton("Reset Camera", () -> cameraController.reset())` |
| 237 | `rightPanel.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #3f3f3f; -fx-border-width: 0 0 0 1;");` |
| 242 | `searchField.setPromptText("Search...");` |
| 244 | `searchField.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white;");` |
| 311 | `titleLabel = new Label("Human Body");` |
| 312 | `titleLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 18px; -fx-font-weight: bold; -fx-wrap-text: true;");` |
| 314 | `descriptionArea = new TextArea("Select a part to view details.");` |
| 317 | `descriptionArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: #b0b0b0; -fx-background-color: transparent;");` |
| 323 | `hierarchyTree.setStyle("-fx-background-color: #2b2b2b; -fx-control-inner-background: #2b2b2b; -fx-text-fill: white;");` |
| 383 | `cb.setText(name + " (Error)");` |
| 416 | `loadingLabel.setText(loadingLabel.getText().replace("...", ".") + ".");` |
| 424 | `System.err.println("Async Load Failed: " + resourcePath);` |
| 478 | `InputStream is = HumanBodyViewer.class.getResourceAsStream("/org/jscience/medicine/anatomy/z-anatomy/hierarchy.txt");` |
| 480 | `System.err.println("Hierarchy file not found!");` |
| 484 | `TreeItem<String> rootItem = new TreeItem<>("Root");` |
| 623 | `return name.replace(" ", "_");` |
| 638 | `if (cleanName.matches(".*\\.[a-z]+$")) {` |
| 642 | `String descFolder = "/z-anatomy/descriptions/";` |
| 646 | `id + "-FR.txt",` |
| 647 | `cleanName + "-FR.txt",` |
| 648 | `"(" + cleanName + ")-FR.txt",` |
| 649 | `cleanName + ".txt"` |
| 659 | `.lines().collect(Collectors.joining("\n"));` |
| 668 | `descriptionArea.setText("No description found for: " + cleanName);` |
| 673 | `Platform.runLater(() -> System.err.println("Failed to load: " + model));` |

## BioMotionViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\medicine\biomecanics\BioMotionViewer.java`

| Line | Content |
| --- | --- |
| 60 | `public String getName() { return I18n.getInstance().get("biomotion.title", "BioMotion"); }` |
| 63 | `public String getCategory() { return "Biology"; }` |
| 70 | `this.getStyleClass().add("dark-viewer-root");` |
| 79 | `sidebar.setStyle("-fx-padding: 10; -fx-background-color: #2b2b2b;");` |
| 82 | `Label title = new Label(I18n.getInstance().get("biomotion.controls", "BioMotion Controls"));` |
| 83 | `title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");` |
| 85 | `Button resetBtn = new Button(I18n.getInstance().get("biomotion.reset", "Reset Walker"));` |
| 90 | `Label gravLabel = new Label(String.format("Gravity: %.2f", 9.81));` |
| 91 | `gravLabel.setStyle("-fx-text-fill: white;");` |
| 94 | `gravLabel.setText(String.format("Gravity: %.2f", gravity));` |
| 98 | `Label muscleLabel = new Label(String.format("Muscle Tone: %.1f", 1.0));` |
| 99 | `muscleLabel.setStyle("-fx-text-fill: white;");` |
| 102 | `muscleLabel.setText(String.format("Muscle Tone: %.1f", muscleStrength));` |

## StarSystemViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\astronomy\StarSystemViewer.java`

| Line | Content |
| --- | --- |
| 138 | `box.getStyleClass().add("dark-viewer-sidebar");` |
| 140 | `Label title = new Label(I18n.getInstance().get("starsystem.presets", "Presets"));` |
| 142 | `title.getStyleClass().add("dark-header");` |
| 151 | `dateLabel.getStyleClass().add("dark-label-muted");` |
| 174 | `if (body.getName().contains("Black Hole")) {` |
| 191 | `if (body.getName().contains("Black Hole")) r = 5.0;` |
| 195 | `if (body.getName().contains("Black Hole")) { mat.setDiffuseColor(Color.BLACK); mat.setSpecularColor(Color.WHITE); }` |
| 205 | `if (n.contains("sun")) return Color.YELLOW;` |
| 206 | `if (n.contains("earth")) return Color.BLUE;` |
| 207 | `if (n.contains("mars")) return Color.RED;` |
| 208 | `if (n.contains("supergiant")) return Color.ALICEBLUE;` |
| 218 | `if (body.getName().contains("Black Hole")) { x=0; }` |
| 219 | `else if (body.getName().contains("Supergiant") \|\| body.getName().contains("Companion")) {` |
| 221 | `} else if (body.getName().equalsIgnoreCase("Sun")) { x=0; }` |
| 222 | `else if (body.getName().equalsIgnoreCase("Earth")) {` |
| 224 | `} else if (body.getName().equalsIgnoreCase("Mars")) {` |
| 229 | `if (updateCounter % 10 == 0 && !body.getName().contains("Black Hole") && !body.getName().contains("Sun")) {` |
| 239 | `private void updateLabels() { if(dateLabel!=null) dateLabel.setText(I18n.getInstance().get("starsystem.date", "Date") + ": " + String.format("%.2f", currentDate.getValue())); }` |
| 253 | `StarSystem s = new StarSystem("Solar System");` |
| 256 | `Star sun = new Star("Sun", org.jscience.measure.Quantities.create(Real.of(1.989e30), Units.KILOGRAM),` |
| 259 | `s.addBody(new Planet("Earth", org.jscience.measure.Quantities.create(Real.of(5.9e24), Units.KILOGRAM),` |
| 261 | `s.addBody(new Planet("Mars", org.jscience.measure.Quantities.create(Real.of(6.4e23), Units.KILOGRAM),` |
| 267 | `StarSystem s = new StarSystem("Cygnus X-1");` |
| 270 | `s.addBody(new Star("Black Hole", org.jscience.measure.Quantities.create(Real.of(2e31), Units.KILOGRAM),` |
| 272 | `s.addBody(new Planet("Companion", org.jscience.measure.Quantities.create(Real.of(4e31), Units.KILOGRAM),` |

## StellarSkyViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\astronomy\StellarSkyViewer.java`

| Line | Content |
| --- | --- |
| 97 | `this.setStyle("-fx-background-color: #000;"); // Black background` |
| 105 | `timeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));` |
| 106 | `timeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 5;");` |
| 140 | `sidebar.getStyleClass().add("dark-viewer-sidebar");` |
| 142 | `Label title = new Label(I18n.getInstance().get("sky.title", "Sky Controls"));` |
| 143 | `title.getStyleClass().add("dark-header");` |
| 145 | `Label locLabel = new Label(I18n.getInstance().get("sky.location", "Location"));` |
| 146 | `locLabel.setStyle("-fx-font-weight: bold;");` |
| 148 | `Slider latSlider = createLabeledSlider(I18n.getInstance().get("sky.lat", "Lat"), -90, 90, observerLat, val -> { observerLat = val; drawSky(); });` |
| 149 | `Slider lonSlider = createLabeledSlider(I18n.getInstance().get("sky.lon", "Lon"), -180, 180, observerLon, val -> { observerLon = val; drawSky(); });` |
| 151 | `Label timeCtrlLabel = new Label(I18n.getInstance().get("starsystem.date", "Time"));` |
| 152 | `timeCtrlLabel.setStyle("-fx-font-weight: bold;");` |
| 155 | `Slider hourSlider = createLabeledSlider(I18n.getInstance().get("sky.hour", "Hour"), 0, 23, simulationTime.getHour(), val -> {` |
| 159 | `showConstellations = new CheckBox(I18n.getInstance().get("sky.stars", "Constellations")); showConstellations.setSelected(true); showConstellations.setOnAction(e -> drawSky());` |
| 160 | `showPlanets = new CheckBox(I18n.getInstance().get("sky.planets", "Solar System")); showPlanets.setSelected(true); showPlanets.setOnAction(e -> drawSky());` |
| 161 | `showDSO = new CheckBox(I18n.getInstance().get("sky.dso", "Deep Sky")); showDSO.setSelected(true); showDSO.setOnAction(e -> drawSky());` |
| 162 | `showTrails = new CheckBox(I18n.getInstance().get("sky.trails", "Trails")); showTrails.setSelected(false); showTrails.setOnAction(e -> drawSky());` |
| 164 | `infoLabel = new Label(I18n.getInstance().get("sky.info.select", "Select an object..."));` |
| 166 | `infoLabel.getStyleClass().add("dark-label-muted");` |
| 176 | `Label label = new Label(String.format("%s: %.2f", name, val));` |
| 178 | `slider.valueProperty().addListener((o, ov, nv) -> { action.accept(nv.doubleValue()); label.setText(String.format("%s: %.2f", name, nv.doubleValue())); });` |
| 185 | `InputStream isStar = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/stars.csv");` |
| 187 | `try (InputStream isConst = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/constellations.csv")) {` |
| 192 | `String[] parts = line.split(",");` |
| 194 | `String name = (parts.length >= 3) ? parts[2].trim() : "Unknown";` |
| 211 | `planets.add(new PlanetData("Mercury", Color.LIGHTGRAY, 48.331, 7.005, 29.124, 0.3871, 0.2056, 168.656, 3.24587e-5, 3.2e-7, 1.014e-5, 0, 0, 4.0923344));` |
| 212 | `planets.add(new PlanetData("Venus", Color.LIGHTYELLOW, 76.680, 3.394, 54.884, 0.7233, 0.0068, 48.005, 3.24587e-5, 2.75e-7, 1.383e-5, 0, 0, 1.602136));` |
| 213 | `planets.add(new PlanetData("Mars", Color.RED, 49.558, 1.850, 286.502, 1.5237, 0.0934, 18.602, 2.122e-5, 6.7e-7, 1.76e-5, 0, 0, 0.5240208));` |
| 214 | `planets.add(new PlanetData("Jupiter", Color.ORANGE, 100.556, 1.303, 274.197, 5.2026, 0.0485, 20.020, 2.768e-5, 4.3e-7, 4.41e-6, 0, 0, 0.0830853));` |
| 215 | `planets.add(new PlanetData("Saturn", Color.GOLDENROD, 113.715, 2.484, 339.392, 9.5549, 0.0555, 317.020, 2.39e-5, 1.6e-7, 8.1e-6, 0, 0, 0.033444));` |
| 216 | `planets.add(new PlanetData("Uranus", Color.LIGHTBLUE, 74.006, 0.773, 96.661, 19.1817, 0.0473, 142.590, 4.04e-5, 4.1e-7, 4e-6, 0, 0, 0.0117258));` |
| 217 | `planets.add(new PlanetData("Neptune", Color.BLUE, 131.780, 1.770, 272.846, 30.0583, 0.0086, 260.247, 3e-5, 9e-8, 1e-6, 0, 0, 0.0059951));` |
| 221 | `deepSkyObjects.add(new DeepSkyObject("M31", "Galaxy", (0 + 42.0/60 + 44.0/3600)*15, (41 + 16.0/60 + 9.0/3600), 3.4));` |
| 222 | `deepSkyObjects.add(new DeepSkyObject("M42", "Nebula", (5 + 35.0/60 + 17.0/3600)*15, -(5 + 23.0/60 + 28.0/3600), 4.0));` |
| 223 | `deepSkyObjects.add(new DeepSkyObject("M45", "Cluster", (3 + 47.0/60 + 24.0/3600)*15, (24 + 7.0/60), 1.6));` |
| 224 | `deepSkyObjects.add(new DeepSkyObject("M1", "Nebula", (5 + 34.0/60 + 31.0/3600)*15, (22 + 0.0/60 + 52.0/3600), 8.4));` |
| 225 | `deepSkyObjects.add(new DeepSkyObject("M13", "Cluster", (16 + 41.0/60 + 41.0/3600)*15, (36 + 27.0/60 + 35.0/3600), 5.8));` |
| 295 | `if (selectedPlanet != null) infoLabel.setText("Planet: " + selectedPlanet.name);` |
| 296 | `else if (selectedStar != null) infoLabel.setText("Star: " + selectedStar.name + " (" + selectedStar.mag + ")");` |
| 297 | `else if (selectedConstellation != null) infoLabel.setText("Constellation: " + selectedConstellation.name);` |
| 298 | `else infoLabel.setText(I18n.getInstance().get("sky.info.select", "Select an object"));` |
| 342 | `PlanetData earth = new PlanetData("Earth", null, 174.873, 0.00005, 102.947, 1.0, 0.0167, 100.464, 0,0,0,0,0, 0.9856);` |
| 362 | `timeLabel.setText(simulationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));` |
| 367 | `drawCardinal(gc, "N", 0, cx, cy, radius); drawCardinal(gc, "E", 90, cx, cy, radius);` |
| 368 | `drawCardinal(gc, "S", 180, cx, cy, radius); drawCardinal(gc, "W", 270, cx, cy, radius);` |
| 438 | `@Override public String getName() { return "Stellar Viewer"; }` |
| 439 | `@Override public String getCategory() { return "Physics"; }` |

## RigidBodyViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\mechanics\RigidBodyViewer.java`

| Line | Content |
| --- | --- |
| 83 | `public String getName() { return I18n.getInstance().get("rigid.window", "Rigid Body Physics"); }` |
| 86 | `public String getCategory() { return "Physics"; }` |
| 93 | `this.setId("root");` |
| 105 | `sidebar.getStyleClass().add("dark-viewer-sidebar");` |
| 107 | `Label title = new Label(I18n.getInstance().get("rigid.title", "Rigid Bodies"));` |
| 108 | `title.getStyleClass().add("dark-label-accent");` |
| 110 | `countLabel = new Label(I18n.getInstance().get("rigid.bodies", "Bodies: 0"));` |
| 111 | `countLabel.getStyleClass().add("dark-label-muted");` |
| 115 | `Label gravLabel = new Label(I18n.getInstance().get("rigid.gravity", "Gravity"));` |
| 116 | `gravLabel.getStyleClass().add("dark-label-muted");` |
| 121 | `gravLabel.setText(String.format(I18n.getInstance().get("rigid.gravity") + ": %.1f", gravityVal));` |
| 124 | `Label bounceLabel = new Label(I18n.getInstance().get("rigid.bounciness", "Bounciness"));` |
| 125 | `bounceLabel.getStyleClass().add("dark-label-muted");` |
| 130 | `bounceLabel.setText(String.format(I18n.getInstance().get("rigid.bounciness") + ": %.1f", bouncinessVal));` |
| 135 | `Button addBtn = new Button(I18n.getInstance().get("rigid.add", "Add Body"));` |
| 137 | `addBtn.getStyleClass().add("accent-button-green");` |
| 140 | `Button add5Btn = new Button(I18n.getInstance().get("rigid.add5", "Add 5 Bodies"));` |
| 144 | `Button clearBtn = new Button(I18n.getInstance().get("rigid.clear", "Clear"));` |
| 146 | `clearBtn.getStyleClass().add("accent-button-red");` |
| 149 | `countLabel.setText(I18n.getInstance().get("rigid.bodies", "Bodies: 0"));` |
| 199 | `countLabel.setText(java.text.MessageFormat.format(I18n.getInstance().get("rigid.count.fmt", "Bodies: {0}"), bodies.size()));` |
| 308 | `gc.setStroke(Color.web("#444"));` |

## AudioViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\waves\AudioViewer.java`

| Line | Content |
| --- | --- |
| 66 | `contentBox.setStyle("-fx-background-color: #f0f0f0;");` |
| 80 | `box.getStyleClass().add("dark-viewer-sidebar");` |
| 82 | `Label title = new Label(I18n.getInstance().get("audio.control", "Audio Control"));` |
| 83 | `title.getStyleClass().add("dark-header");` |
| 85 | `Button openBtn = new Button(I18n.getInstance().get("audio.open", "Open File"));` |
| 95 | `fileChooser.setTitle("Open Audio File");` |
| 97 | `new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aif", "*.au", "*.snd")` |
| 133 | `gc.setFill(Color.web("#f0f0f0"));` |
| 205 | `@Override public String getName() { return "Audio Viewer"; }` |
| 206 | `@Override public String getCategory() { return "Physics"; }` |

## SpectrographViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\waves\SpectrographViewer.java`

| Line | Content |
| --- | --- |
| 70 | `private String currentPattern = "Voice";` |
| 86 | `public String getName() { return "Spectrograph Viewer"; }` |
| 89 | `public String getCategory() { return "Physics"; }` |
| 93 | `I18n.getInstance().get("spectrograph.sensitivity", "Sensitivity"),` |
| 94 | `I18n.getInstance().get("spectrograph.sensitivity.desc", "Adjusts the signal responsiveness"),` |
| 99 | `I18n.getInstance().get("spectrograph.source", "Source"),` |
| 100 | `I18n.getInstance().get("spectrograph.source.desc", "Selects the input signal pattern"),` |
| 101 | `"Voice",` |
| 109 | `I18n.getInstance().get("spectrograph.mode", "Scientific Mode"),` |
| 110 | `I18n.getInstance().get("spectrograph.mode.desc", "Toggles between primitive and object-based engines"),` |
| 125 | `vbox.getStyleClass().add("dark-viewer-root");` |
| 133 | `fpsLabel = new Label(I18n.getInstance().get("spectrograph.fps", "FPS: --"));` |
| 134 | `fpsLabel.getStyleClass().add("dark-label-muted");` |
| 142 | `new Stop(0, Color.web("#00ff00")),` |
| 143 | `new Stop(0.5, Color.web("#ffff00")),` |
| 144 | `new Stop(1.0, Color.web("#ff0000")));` |
| 158 | `fpsLabel.setText(I18n.getInstance().get("spectrograph.fps.fmt", "FPS: %.1f", fps));` |
| 251 | `private String sourcePattern = "Voice";` |
| 265 | `if ("Voice".equals(sourcePattern) \|\| I18n.getInstance().get("spectrograph.source.voice", "Voice").equals(sourcePattern)) {` |
| 269 | `} else if ("White Noise".equals(sourcePattern) \|\| I18n.getInstance().get("spectrograph.source.noise", "White Noise").equals(sourcePattern)) {` |
| 271 | `} else if ("Sine Wave".equals(sourcePattern) \|\| I18n.getInstance().get("spectrograph.source.sine", "Sine Wave").equals(sourcePattern)) {` |

## CircuitSimulatorViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\waves\electromagnetism\circuit\CircuitSimulatorViewer.java`

| Line | Content |
| --- | --- |
| 50 | `public String getCategory() { return "Engineering"; }` |
| 53 | `public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.circuit"); }` |
| 57 | `RESISTOR("circuit.component.resistor"),` |
| 58 | `CAPACITOR("circuit.component.capacitor"),` |
| 59 | `INDUCTOR("circuit.component.inductor"),` |
| 60 | `BATTERY("circuit.component.battery"),` |
| 61 | `WIRE("circuit.component.wire"),` |
| 62 | `GROUND("circuit.component.ground"),` |
| 63 | `METER("circuit.component.meter");` |
| 114 | `toolbar.getStyleClass().add("viewer-toolbar");` |
| 127 | `new Button(org.jscience.ui.i18n.I18n.getInstance().get("circuit.btn.delete")) {` |
| 138 | `new Button(org.jscience.ui.i18n.I18n.getInstance().get("circuit.btn.clear")) {` |
| 186 | `sidebar.getStyleClass().add("viewer-sidebar");` |
| 188 | `new Label(org.jscience.ui.i18n.I18n.getInstance().get("circuit.hint.click")),` |
| 189 | `new Label(org.jscience.ui.i18n.I18n.getInstance().get("circuit.hint.snap")),` |
| 190 | `new Label(org.jscience.ui.i18n.I18n.getInstance().get("circuit.hint.jscience")));` |
| 217 | `ToggleButton btn = new ToggleButton(org.jscience.ui.i18n.I18n.getInstance().get("circuit.btn.select"));` |
| 264 | `gc.setStroke(Color.web("#ddd"));` |

## ResistorColorCodeViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\waves\electromagnetism\circuit\ResistorColorCodeViewer.java`

| Line | Content |
| --- | --- |
| 49 | `public String getCategory() { return "Engineering"; }` |
| 52 | `public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.resistor"); }` |
| 65 | `private final Label resultLabel = new Label("Resistance: ");` |
| 70 | `private static final String[] COLORS = { "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet",` |
| 71 | `"Gray", "White" };` |
| 91 | `root.getStyleClass().add("dark-viewer-root");` |
| 92 | `resultLabel.getStyleClass().add("dark-label-accent");` |
| 112 | `band4.getItems().addAll("Gold", "Silver");` |
| 113 | `band4.setValue("Gold");` |
| 114 | `colorMap.put("Gold", Color.GOLD);` |
| 115 | `colorMap.put("Silver", Color.SILVER);` |
| 134 | `cb.setValue("Brown");` |
| 153 | `resultLabel.setText(String.format("Resistance: %.0f Ohms +/- %s", ohms, c4.equals("Gold") ? "5%" : "10%"));` |

## MagneticFieldViewer.java (jscience-natural)
`C:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience\ui\viewers\physics\classical\waves\electromagnetism\field\MagneticFieldViewer.java`

| Line | Content |
| --- | --- |
| 41 | `public String getCategory() { return "Physics"; }` |
| 44 | `public String getName() { return "Magnetic Field Viewer"; }` |
| 53 | `this.setStyle("-fx-background-color: #1a1a2e;"); // Dark background` |
| 57 | `subScene.setFill(Color.web("#1a1a2e"));` |

## HelpViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\architecture\traffic\HelpViewer.java`

| Line | Content |
| --- | --- |
| 89 | `super("GLD Online Help");` |
| 110 | `File file = new File("");` |
| 111 | `String base = "file:/" + file.getAbsolutePath();` |
| 113 | `if (!base.endsWith("/org/jscience/architecture/traffic")) {` |
| 114 | `base += "/org/jscience/architecture/traffic";` |
| 117 | `base += "/docs/";` |
| 133 | `return base + "index.html";` |
| 136 | `return base + "specs/index.html";` |
| 139 | `return base + "license.html";` |
| 142 | `return "http://www.students.cs.uu.nl/swp/2001/isg";` |
| 145 | `return base + "about.html";` |
| 148 | `return "";` |
| 174 | `controller.showError("Couldn't view help item \"" + s + "\" : " +` |
| 188 | `controller.showError("Couldn't view help item \"" + url + "\" : " +` |

## XMLInputReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\architecture\traffic\xml\XMLInputReader.java`

| Line | Content |
| --- | --- |
| 140 | `String result = "";` |

## XMLLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\architecture\traffic\xml\XMLLoader.java`

| Line | Content |
| --- | --- |
| 115 | `System.out.println("Warning : could not close XMLLoader : " + e);` |
| 159 | `("EOF at non-terminated tree. File is probably truncated.");` |
| 165 | `if (tag.startsWith("</")) {` |
| 170 | `("Unbalanced tags while loading XML file :" + ref + "/" + tag);` |
| 195 | `("Can't find more XMLElements inside " + parent.getXMLName());` |
| 226 | `("Can't find more XMLElements inside " + parent.getXMLName());` |
| 230 | `("Can't find next element named " + name + " in parent " +` |
| 313 | `System.out.println("XML stack dump " + stack.getBranchName());` |

## XMLReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\architecture\traffic\xml\XMLReader.java`

| Line | Content |
| --- | --- |
| 140 | `String result = "";` |

## XMLWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\architecture\traffic\xml\XMLWriter.java`

| Line | Content |
| --- | --- |
| 104 | `println(StringUtils.repeat(' ', indent) + element.getOpenTag() + " " +` |

## YaleSkyViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\catalogs\yale\YaleSkyViewer.java`

| Line | Content |
| --- | --- |
| 153 | `JMenuItem b_hemisphere = new JMenuItem("Cut Hemisphere");` |
| 156 | `JMenuItem b_sphere = new JMenuItem("Full Sky");` |
| 159 | `JMenuItem b_optimal = new JMenuItem("Optimal View");` |
| 162 | `JMenuItem b_center = new JMenuItem("Center View");` |
| 165 | `JRadioButton b_sun = new JRadioButton("Loot at Sun", true);` |
| 168 | `JRadioButton b_opposite = new JRadioButton("Look Opposite Sun");` |
| 171 | `JMenuItem b_stars_all = new JMenuItem("Display All Stars");` |
| 174 | `JMenuItem b_stars_none = new JMenuItem("Hide All Stars");` |
| 177 | `JMenuItem b_brightest = new JMenuItem("Display Brightest Stars");` |
| 180 | `JCheckBox cb_rotate = new JCheckBox("Rotate", false);` |
| 183 | `JCheckBox cb_grid = new JCheckBox("Display Grid", true);` |
| 186 | `JMenuItem b_ss_all = new JMenuItem("Display Solar System");` |
| 189 | `JMenuItem b_ss_none = new JMenuItem("Hide Solar System");` |
| 192 | `JCheckBox cb_sun = new JCheckBox("Sun", true);` |
| 195 | `JCheckBox cb_mercury = new JCheckBox("Mercury", true);` |
| 198 | `JCheckBox cb_venus = new JCheckBox("Venus", true);` |
| 201 | `JCheckBox cb_mars = new JCheckBox("Mars", true);` |
| 204 | `JCheckBox cb_jupiter = new JCheckBox("Jupiter", true);` |
| 207 | `JCheckBox cb_saturn = new JCheckBox("Saturn", true);` |
| 252 | `JLabel l_position = new JLabel("", JLabel.RIGHT);` |
| 255 | `JLabel l_bright = new JLabel("", JLabel.CENTER);` |
| 383 | `"org/jscience/astronomy/milkyway/yale/yalestars.txt");` |
| 422 | `if (s.equals("-")) {` |
| 431 | `if(!s.equals("."))` |
| 453 | `"org/jscience/astronomy/solarsystem/constellations/sun.png")` |
| 456 | `"org/jscience/astronomy/solarsystem/constellations/mercury.png")` |
| 459 | `"org/jscience/astronomy/solarsystem/constellations/venus.png")` |
| 462 | `"org/jscience/astronomy/solarsystem/constellations/mars.png")` |
| 465 | `"org/jscience/astronomy/solarsystem/constellations/saturn.png")` |
| 468 | `"org/jscience/astronomy/solarsystem/constellations/jupiter.png")` |
| 521 | `latitude.setText("0.0");` |
| 546 | `l_bright.setText(star_count + " Brightest Stars");` |
| 553 | `p_bright.add("North", s_bright);` |
| 554 | `p_bright.add("South", l_bright);` |
| 558 | `JMenu sky = new JMenu("Sky Objects");` |
| 564 | `JMenu bright = new JMenu("Brightest Stars");` |
| 576 | `JMenu ss = new JMenu("Select Solar System");` |
| 593 | `JMenu v = new JMenu("View");` |
| 604 | `JMenu fov = new JMenu("Field of View");` |
| 609 | `JMenu rotate = new JMenu("Rotation");` |
| 613 | `controls.add("West", menubar);` |
| 614 | `controls.add("East", l_position);` |
| 619 | `top_frame.getContentPane().add("Center", canvas3D);` |
| 622 | `top_frame.getContentPane().add("North", controls);` |
| 1086 | `l_position.setText(round(l_ra) + "   " + round(l_dec));` |
| 1277 | `System.out.println("Deleted");` |
| 1312 | `System.out.println("Added ["+sao[index1]+", "+sao[index2]+"]"+'\n');` |
| 1317 | `System.out.println(p.x+" "+p.y);` |
| 1403 | `tf_brightest.setText("");` |
| 1512 | `l_bright.setText(brightest + " Brightest Stars");` |

## HtmlEphemerisWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\artificialsatellites\gui\HtmlEphemerisWriter.java`

| Line | Content |
| --- | --- |
| 70 | `out.println("<html>");` |
| 71 | `out.println("<body>");` |
| 73 | `out.println("<p>Generated on " + dateGenerated.toString() +` |
| 74 | `"&nbsp;</p>");` |
| 76 | `out.print("<h2>");` |
| 78 | `out.println("</h2>");` |
| 80 | `out.println("<table align='center' border='1'>");` |
| 82 | `out.println("<tr>");` |
| 84 | `"<th bgcolor='black'><font color='white'>TSINCE</font></th>");` |
| 85 | `out.println("<th bgcolor='black'><font color='white'>X</font></th>");` |
| 86 | `out.println("<th bgcolor='black'><font color='white'>Y</font></th>");` |
| 87 | `out.println("<th bgcolor='black'><font color='white'>Z</font></th>");` |
| 88 | `out.println("<th bgcolor='black'><font color='white'>XDOT</font></th>");` |
| 89 | `out.println("<th bgcolor='black'><font color='white'>YDOT</font></th>");` |
| 90 | `out.println("<th bgcolor='black'><font color='white'>ZDOT</font></th>");` |
| 91 | `out.println("</tr>");` |
| 98 | `out.println("<tr>");` |
| 99 | `out.println("<td align='right'>");` |
| 101 | `out.println("</td>");` |
| 102 | `out.println("<td>");` |
| 104 | `out.println("</td>");` |
| 105 | `out.println("<td>");` |
| 107 | `out.println("</td>");` |
| 108 | `out.println("<td>");` |
| 110 | `out.println("</td>");` |
| 111 | `out.println("<td>");` |
| 113 | `out.println("</td>");` |
| 114 | `out.println("<td>");` |
| 116 | `out.println("</td>");` |
| 117 | `out.println("<td>");` |
| 119 | `out.println("</td>");` |
| 120 | `out.println("</tr>");` |
| 123 | `out.println("</table>");` |
| 124 | `out.println("</body>");` |
| 125 | `out.println("</html>");` |
| 127 | `destination.setContentType("text/html");` |

## SwingApp.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\artificialsatellites\gui\SwingApp.java`

| Line | Content |
| --- | --- |
| 50 | `"org.jscience.astronomy.solarsystem.artificialsatellites");` |

## TextEphemerisWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\artificialsatellites\gui\TextEphemerisWriter.java`

| Line | Content |
| --- | --- |
| 70 | `out.println("Generated on " + dateGenerated.toString());` |
| 74 | `out.print("       TSINCE");` |
| 75 | `out.print("             X");` |
| 76 | `out.print("                Y");` |
| 77 | `out.print("                Z");` |
| 78 | `out.print("                XDOT");` |
| 79 | `out.print("             YDOT");` |
| 80 | `out.print("             ZDOT");` |
| 90 | `destination.setContentType("text/plain");` |

## XmlEphemerisWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\artificialsatellites\gui\XmlEphemerisWriter.java`

| Line | Content |
| --- | --- |
| 70 | `out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");` |
| 72 | `out.println("<ephemeris>");` |
| 74 | `out.println("<two-line-element-set>");` |
| 75 | `out.println("<line1>" + tle.getLine1() + "<line1>");` |
| 76 | `out.println("<line2>" + tle.getLine2() + "<line2>");` |
| 77 | `out.println("</two-line-element-set>");` |
| 79 | `out.print("<description>");` |
| 80 | `out.print("<title>" + title + "</title>");` |
| 81 | `out.println("<date-generated>" + dateGenerated.toString() +` |
| 82 | `"</date-generated>");` |
| 83 | `out.println("</description>");` |
| 90 | `out.println("<node time-since-epoch=\"" + sv.TSINCE + "\">");` |
| 91 | `out.println("<position x=\"" + sv.X + "\" y=\"" + sv.Y + "\" z=\"" +` |
| 92 | `sv.Z + "\">");` |
| 93 | `out.println("<velocity x=\"" + sv.XDOT + "\" y=\"" + sv.YDOT +` |
| 94 | `"\" z=\"" + sv.ZDOT + "\">");` |
| 95 | `out.println("</node>");` |
| 98 | `out.println("</ephemeris>");` |
| 100 | `destination.setContentType("text/plain");` |

## ConstellationsSkyViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\constellations\ConstellationsSkyViewer.java`

| Line | Content |
| --- | --- |
| 154 | `JMenuItem b_hemisphere = new JMenuItem("Cut Hemisphere");` |
| 157 | `JMenuItem b_sphere = new JMenuItem("Full Sky");` |
| 160 | `JMenuItem b_optimal = new JMenuItem("Optimal View");` |
| 163 | `JMenuItem b_center = new JMenuItem("Center View");` |
| 166 | `JRadioButton b_sun = new JRadioButton("Loot at Sun", true);` |
| 169 | `JRadioButton b_opposite = new JRadioButton("Look Opposite Sun");` |
| 172 | `JMenuItem b_con_all = new JMenuItem("Display All Constellations");` |
| 175 | `JMenuItem b_con_none = new JMenuItem("Hide All Constellations");` |
| 178 | `JMenuItem b_stars_all = new JMenuItem("Display All Stars");` |
| 181 | `JMenuItem b_stars_none = new JMenuItem("Hide All Stars");` |
| 184 | `JMenuItem b_brightest = new JMenuItem("Display Brightest Stars");` |
| 187 | `JCheckBox cb_rotate = new JCheckBox("Rotate", false);` |
| 190 | `JCheckBox cb_constellations = new JCheckBox("Constellations", true);` |
| 193 | `JCheckBox cb_grid = new JCheckBox("Display Grid", true);` |
| 196 | `JMenuItem b_ss_all = new JMenuItem("Display Solar System");` |
| 199 | `JMenuItem b_ss_none = new JMenuItem("Hide Solar System");` |
| 202 | `JCheckBox cb_sun = new JCheckBox("Sun", true);` |
| 205 | `JCheckBox cb_mercury = new JCheckBox("Mercury", true);` |
| 208 | `JCheckBox cb_venus = new JCheckBox("Venus", true);` |
| 211 | `JCheckBox cb_mars = new JCheckBox("Mars", true);` |
| 214 | `JCheckBox cb_jupiter = new JCheckBox("Jupiter", true);` |
| 217 | `JCheckBox cb_saturn = new JCheckBox("Saturn", true);` |
| 262 | `JLabel l_position = new JLabel("", JLabel.RIGHT);` |
| 265 | `JLabel l_bright = new JLabel("", JLabel.CENTER);` |
| 392 | `text = new URL(getCodeBase(), "../../milkyway/yalestars.txt");` |
| 431 | `if (s.equals("-")) {` |
| 440 | `if(!s.equals("."))` |
| 447 | `text = new URL(getCodeBase(), "constellations.txt");` |
| 464 | `if (s.startsWith("#")) {` |
| 517 | `symbols[0] = getImage(getCodeBase(), "sun.png")` |
| 519 | `symbols[1] = getImage(getCodeBase(), "mercury.png")` |
| 521 | `symbols[2] = getImage(getCodeBase(), "venus.png")` |
| 523 | `symbols[3] = getImage(getCodeBase(), "mars.png")` |
| 525 | `symbols[4] = getImage(getCodeBase(), "saturn.png")` |
| 527 | `symbols[5] = getImage(getCodeBase(), "jupiter.png")` |
| 580 | `latitude.setText("0.0");` |
| 610 | `l_bright.setText(star_count + " Brightest Stars");` |
| 617 | `p_bright.add("North", s_bright);` |
| 618 | `p_bright.add("South", l_bright);` |
| 622 | `JMenu sky = new JMenu("Sky Objects");` |
| 628 | `JMenu bright = new JMenu("Brightest Stars");` |
| 632 | `select_con = new JMenu("Select Constellations");` |
| 653 | `JMenu ss = new JMenu("Select Solar System");` |
| 670 | `JMenu v = new JMenu("View");` |
| 681 | `JMenu fov = new JMenu("Field of View");` |
| 686 | `JMenu rotate = new JMenu("Rotation");` |
| 690 | `controls.add("West", menubar);` |
| 691 | `controls.add("East", l_position);` |
| 696 | `top_frame.getContentPane().add("Center", canvas3D);` |
| 699 | `top_frame.getContentPane().add("North", controls);` |
| 1162 | `l_position.setText(round(l_ra) + "   " + round(l_dec));` |
| 1353 | `System.out.println("Deleted");` |
| 1388 | `System.out.println("Added ["+sao[index1]+", "+sao[index2]+"]"+'\n');` |
| 1393 | `System.out.println(p.x+" "+p.y);` |
| 1479 | `tf_brightest.setText("");` |
| 1667 | `l_bright.setText(brightest + " Brightest Stars");` |

## StarLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\astronomy\solarsystem\ephemeris\gui\StarLoader.java`

| Line | Content |
| --- | --- |
| 96 | `(new Thread(this, "StarLoader")).start();` |
| 112 | `System.out.println("VSOP already loaded");` |
| 117 | `URL url = new URL(app.getCodeBase(), "VSOP87bin.zip");` |
| 131 | `System.out.println("VSOP " + i);` |
| 138 | `System.out.println("Error reading VSOP data: " +` |
| 189 | `System.out.println("DataFile already loaded");` |
| 201 | `URL url = new URL(app.getCodeBase(), "cspheredata.zip");` |
| 207 | `System.out.println("StarsArray gets a count of: " + i);` |
| 251 | `System.out.println("Messier objects: " + i);` |
| 293 | `System.out.println("Attempting to load ELP2000");` |
| 296 | `System.out.println("Got ELP2000");` |
| 307 | `System.out.println("Error reading data file: " +` |

## MoleculeViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\chemistry\gui\basic\MoleculeViewer.java`

| Line | Content |
| --- | --- |
| 173 | `sFilename = getParameter("model");` |
| 175 | `String sMoleculeRadius = getParameter("mole_rad");` |
| 184 | `String sLabels = getParameter("labels");` |
| 187 | `if (sLabels.equals("1")) {` |
| 192 | `String sFog = getParameter("fog");` |
| 195 | `if (sFog.equals("1")) {` |
| 202 | `System.out.println("get URL");` |
| 211 | `System.out.println("read atoms");` |
| 245 | `System.out.println("Reading a line...");` |
| 250 | `if (sKeyword.equals("ATOM")) {` |
| 264 | `System.out.println("Adding " + nIndex + " '" + sAtomType +` |
| 265 | `"' " + x + " " + y + " " + z);` |
| 276 | `} else if (sKeyword.equals("TER")) {` |
| 277 | `System.out.println("TER");` |
| 278 | `} else if (sKeyword.equals("CONECT")) {` |
| 295 | `System.out.print("-->" + (int) nNextBond);` |
| 302 | `System.out.println("");` |
| 304 | `System.out.println("Can't handle " + sKeyword);` |
| 310 | `System.out.println("Exception...");` |
| 315 | `System.out.println("Find BoundingBox");` |
| 319 | `System.out.println("x " + xmin + " " + xmax);` |
| 320 | `System.out.println("y " + ymin + " " + ymax);` |
| 321 | `System.out.println("z " + zmin + " " + zmax);` |
| 335 | `System.out.println("Atom 1");` |
| 349 | `System.out.println("Atom " + i + "(" + atom.m_x + "," + atom.m_y +` |
| 350 | `"," + atom.m_z + ")");` |
| 656 | `System.out.println("bond null");` |
| 661 | `System.out.println("atom null");` |
| 665 | `System.out.println("zrange = " + _minz + ", " + _maxz + "min/max = [" +` |
| 666 | `min_range + "," + max_range);` |

## SimpleViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\chemistry\gui\extended\SimpleViewer.java`

| Line | Content |
| --- | --- |
| 77 | `lookup.addObject("CentralDisplay", c);` |
| 79 | `panel.add("Center", molPanel);` |
| 80 | `add("Center", panel);` |
| 94 | `Menu m = new Menu("File");` |
| 95 | `m.add("Open");` |
| 97 | `m.add("Exit");` |
| 100 | `m = new Menu("View");` |
| 101 | `m.add("Ball and Stick");` |
| 102 | `m.add("Stick");` |
| 103 | `m.add("Wire");` |
| 104 | `m.add("Spacefill");` |
| 108 | `m = new Menu("Help");` |
| 109 | `m.add("Usage");` |
| 110 | `m.add("About");` |
| 135 | `String vers = System.getProperty("java.version");` |
| 137 | `if (vers.compareTo("1.2") < 0) {` |
| 139 | `"!!!WARNING: SimpleViewer must be run with a " +` |
| 140 | `"1.2 or higher version VM!!!");` |
| 146 | `frame.setTitle("Tripos SimpleViewer");` |
| 158 | `frame.add("Center", mv);` |
| 164 | `System.out.println("uncaught exception: " + t);` |
| 190 | `throw new IOException("Resource could not be found !!!");` |
| 215 | `box.add("Center", text);` |
| 217 | `Button b = new Button("OK");` |
| 218 | `box.add("South", b);` |
| 242 | `if (command.equalsIgnoreCase("Usage")) {` |
| 243 | `showMessage("Usage.def");` |
| 244 | `} else if (command.equalsIgnoreCase("About")) {` |
| 245 | `showMessage("About.def");` |
| 266 | `if (command.equalsIgnoreCase("Exit")) {` |
| 268 | `} else if (command.equalsIgnoreCase("Open")) {` |
| 269 | `FileDialog fd = new FileDialog(parent, "Open Mol2 File",` |
| 310 | `if (command.equalsIgnoreCase("Ball and Stick")) {` |
| 312 | `} else if (command.equalsIgnoreCase("Stick")) {` |
| 314 | `} else if (command.equalsIgnoreCase("Spacefill")) {` |
| 316 | `} else if (command.equalsIgnoreCase("Wire")) {` |

## JViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\chemistry\gui\extended\jviewer\JViewer.java`

| Line | Content |
| --- | --- |
| 241 | `"Argument sent to tripos.jviewer.JViewer::setTransformMode(int) " +` |
| 242 | `"is not one of TRANSLATE, ROTATE_XY, ROTATE_Z, SCALE, or " +` |
| 243 | `"NO_TRANSFORM defined in tripos.jviewer.JViewer ";` |

## BasisReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\chemistry\quantum\basis\BasisReader.java`

| Line | Content |
| --- | --- |
| 60 | `private String element = new String("element");` |
| 106 | `System.err.println("Warning : " + e);` |
| 111 | `System.err.println("Error : " + e);` |
| 116 | `System.err.println("Error : " + e);` |
| 122 | `Document basisDoc = db.parse(getClass().getResourceAsStream("/org.jscience.chemistry.quantum/basis/basis_" + basisName + ".xml"));` |
| 141 | `if (nodeName.equals("name")) {` |
| 152 | `if (element.equals("atom")) {` |
| 154 | `atomicBasis = new AtomicBasis(atts.getNamedItem("symbol").getNodeValue(),` |
| 155 | `Integer.parseInt(atts.getNamedItem("atomicNumber").getNodeValue()));` |
| 157 | `} else if (element.equals("orbital")) {` |
| 159 | `orbital = new Orbital(atts.getNamedItem("type").getNodeValue());` |
| 161 | `} else if (element.equals("entry")) {` |
| 163 | `orbital.addEntry(Double.parseDouble(atts.getNamedItem("coeff").getNodeValue()),` |
| 164 | `Double.parseDouble(atts.getNamedItem("exp").getNodeValue()));` |

## QueryReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\computing\ai\casebasedreasoning\QueryReader.java`

| Line | Content |
| --- | --- |
| 36 | `private static final String FIELD_DELIMITER = "\|";` |
| 37 | `private static final String CONSTRAINT_LINE_INDICATOR = "c";` |
| 38 | `private static final String WEIGHT_LINE_INDICATOR = "w";` |
| 39 | `private static final String DEFAULT_FILE_NAME = "query.txt";` |
| 137 | `lineType = "";` |
| 194 | `boolean isAHardConstraint = (!((operator.equals("~")) \|\|` |
| 195 | `(operator.equals("%")) \|\| (operator.equals("!%"))));` |
| 241 | `String methodName = "ItemManager::loadItems";` |
| 243 | `System.out.println(methodName + " error: " + e);` |

## TextWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\io\TextWriter.java`

| Line | Content |
| --- | --- |
| 122 | `write(data[i] + " " + delimiter + " ");` |
| 124 | `write(data[i] + "\n");` |
| 137 | `write(data[i][j] + " " + delimiter + " ");` |
| 139 | `write(data[i][j] + "\n");` |
| 154 | `write(data[i] + " " + delimiter + " ");` |
| 156 | `write(data[i] + "\n");` |
| 169 | `write(data[i][j] + " " + delimiter + " ");` |
| 171 | `write(data[i][j] + "\n");` |
| 188 | `write(matrix.getElement(i, j) + " " + delimiter + " ");` |
| 190 | `write(matrix.getElement(i, j) + "\n");` |
| 207 | `write(matrix.getElement(i, j) + " " + delimiter + " ");` |
| 209 | `write(matrix.getElement(i, j) + "\n");` |

## OMDOMReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\io\openmath\OMDOMReader.java`

| Line | Content |
| --- | --- |
| 46 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",` |
| 47 | `"OMS", "OMSTR", "OMV", "OMBVAR", "OMATP"` |
| 135 | `throw new IOException("Unable to construct DOM");` |
| 141 | `NodeList tNodeList = tDocument.getElementsByTagName("OMOBJ");` |
| 156 | `throw new IOException("Unable to read OMObject from DOM");` |
| 447 | `throw new IOException("Unable to read OMObject from DOM: " +` |
| 459 | `tObject.setAttribute(tNode.getPrefix() + ":" +` |

## OMDOMWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\io\openmath\OMDOMWriter.java`

| Line | Content |
| --- | --- |
| 100 | `Node tNode = mDocument.createElement("OMOBJ");` |
| 108 | `throw new IOException("Unable to write out DOM tree:" +` |
| 131 | `tNode = mDocument.createElement("OMA");` |
| 142 | `tNode = mDocument.createElement("OMATTR");` |
| 147 | `Element tPairs = mDocument.createElement("OMATP");` |
| 165 | `tNode = mDocument.createElement("OMB");` |
| 172 | `tNode = mDocument.createElement("OMBIND");` |
| 175 | `Element tVariables = mDocument.createElement("OMBVAR");` |
| 194 | `tNode = mDocument.createElement("OME");` |
| 204 | `tNode = mDocument.createElement("OMF");` |
| 211 | `tNode = mDocument.createElement("OMI");` |
| 219 | `tNode = mDocument.createElement("OMSTR");` |
| 224 | `tNode = mDocument.createElement("OMS");` |
| 228 | `tNode = mDocument.createElement("OMV");` |
| 252 | `throw new IOException("Unable to write out DOM node");` |

## OMXMLReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\io\openmath\OMXMLReader.java`

| Line | Content |
| --- | --- |
| 51 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",` |
| 52 | `"OMS", "OMSTR", "OMV", "OMBVAR", "OMATP"` |
| 185 | `if (fLocalName.equals("OMOBJ")) {` |
| 426 | `if (fAttributes.getIndex("dec") != -1) {` |
| 427 | `tFloat.setFloat(fAttributes.getValue("dec"));` |
| 430 | `if (fAttributes.getIndex("hex") != -1) {` |
| 431 | `tFloat.setFloat(fAttributes.getValue("hex"), "hex");` |
| 447 | `if (fAttributes.getIndex("cd") != -1) {` |
| 448 | `tSymbol.setCD(fAttributes.getValue("cd"));` |
| 451 | `if (fAttributes.getIndex("name") != -1) {` |
| 452 | `tSymbol.setName(fAttributes.getValue("name"));` |
| 468 | `if (fAttributes.getIndex("name") != -1) {` |
| 469 | `tVariable.setName(fAttributes.getValue("name"));` |
| 484 | `throw new SAXException("Element <" + fLocalName +` |
| 485 | `"> not recognized");` |
| 569 | `return "OMVCT";` |
| 578 | `return "<OMVCT/>";` |

## OMXMLWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\io\openmath\OMXMLWriter.java`

| Line | Content |
| --- | --- |
| 48 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",` |
| 49 | `"OMS", "OMSTR", "OMV", "OMBVAR", "OMATP"` |
| 193 | `mPrintWriter.print(" " + tKey.toString() + "=\"");` |
| 194 | `mPrintWriter.print(fAttributes.get(tKey).toString() + "\"");` |
| 208 | `mPrintWriter.print("<OMA");` |
| 210 | `mPrintWriter.println(">");` |
| 220 | `mPrintWriter.print("</OMA>");` |
| 235 | `mPrintWriter.print("<OMATTR");` |
| 237 | `mPrintWriter.print(">");` |
| 246 | `mPrintWriter.print("<OMATP>");` |
| 263 | `mPrintWriter.print("</OMATP>");` |
| 273 | `mPrintWriter.print("</OMATTR>");` |
| 289 | `mPrintWriter.print("<OMBIND");` |
| 291 | `mPrintWriter.print(">");` |
| 300 | `mPrintWriter.print("<OMBVAR>");` |
| 315 | `mPrintWriter.print("</OMBVAR>");` |
| 324 | `mPrintWriter.print("</OMBIND>");` |
| 340 | `mPrintWriter.print("<OMB");` |
| 342 | `mPrintWriter.print(">");` |
| 344 | `mPrintWriter.print("</OMB>");` |
| 425 | `mPrintWriter.print("<OME");` |
| 427 | `mPrintWriter.print(">");` |
| 444 | `mPrintWriter.print("</OME>");` |
| 460 | `mPrintWriter.print("<OMF");` |
| 462 | `mPrintWriter.print("/>");` |
| 477 | `mPrintWriter.print("  ");` |
| 491 | `mPrintWriter.print("<OMI");` |
| 493 | `mPrintWriter.print(">");` |
| 495 | `mPrintWriter.print(fInteger.getInteger() + "</OMI>");` |
| 509 | `mPrintWriter.print("<OMOBJ>");` |
| 519 | `mPrintWriter.print("</OMOBJ>");` |
| 538 | `mPrintWriter.print("<OMSTR");` |
| 540 | `mPrintWriter.print(">");` |
| 542 | `mPrintWriter.print(fString.getString() + "</OMSTR>");` |
| 558 | `mPrintWriter.print("<OMS");` |
| 560 | `mPrintWriter.print("/>");` |
| 576 | `mPrintWriter.print("<OMV");` |
| 578 | `mPrintWriter.print("/>");` |

## SchemaReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\mathematics\axiomatic\SchemaReader.java`

| Line | Content |
| --- | --- |
| 169 | `System.out.println("Couldn't find '" + name + "'");` |
| 226 | `System.out.println("5 null");` |
| 238 | `System.out.println("4 null");` |
| 249 | `System.out.println("3 null");` |
| 259 | `System.out.println("2 null");` |
| 268 | `System.out.println("1 null");` |
| 281 | `stack.push("1");` |
| 283 | `stack.push("2");` |
| 285 | `stack.push("3");` |
| 287 | `stack.push("4");` |
| 289 | `stack.push("5");` |
| 291 | `stack.push("6");` |
| 293 | `stack.push("7");` |
| 295 | `stack.push("8");` |
| 297 | `stack.push("9");` |
| 299 | `stack.push("a");` |
| 301 | `stack.push("b");` |
| 303 | `stack.push("c");` |
| 305 | `stack.push("d");` |
| 307 | `stack.push("e");` |
| 309 | `stack.push("f");` |
| 311 | `stack.push("g");` |
| 313 | `stack.push("h");` |
| 315 | `stack.push("i");` |
| 317 | `stack.push("j");` |
| 319 | `stack.push("k");` |
| 321 | `stack.push("l");` |
| 323 | `stack.push("m");` |
| 325 | `stack.push("n");` |
| 327 | `stack.push("<");` |
| 329 | `stack.push("\|");` |
| 331 | `stack.push("&");` |
| 333 | `stack.push("%");` |
| 335 | `stack.push("/");` |
| 337 | `stack.push("[");` |
| 339 | `stack.push("E");` |
| 341 | `stack.push("!");` |
| 343 | `stack.push("*");` |
| 345 | `stack.push("@");` |
| 347 | `stack.push("V");` |
| 349 | `stack.push("{");` |
| 351 | `stack.push("}");` |
| 353 | `stack.push("A");` |
| 355 | `stack.push("X");` |
| 357 | `stack.push("\\");` |
| 359 | `stack.push("?");` |
| 361 | `stack.push("]");` |
| 363 | `stack.push("~");` |
| 365 | `stack.push("-");` |
| 367 | `stack.push("^");` |
| 369 | `stack.push("U");` |
| 381 | `stack.push("D" + stack.pop() + stack.pop());` |
| 383 | `stack.push("G" + stack.pop());` |
| 385 | `stack.push("=" + stack.pop());` |
| 387 | `System.out.println("What's a '" + c + "'?");` |
| 401 | `throw new IllegalStateException("TOO LONG (" + proof.length() +` |
| 402 | `")");` |
| 419 | `System.out.print("Reading...");` |
| 421 | `System.out.println("DONE");` |
| 464 | `section[proofLength] = "";` |
| 467 | `section[proofLength] = section[proofLength] + "~" + key +` |
| 468 | `";\r\n" + proof + ";\r\n\r\n";` |
| 472 | `System.out.println("TOO LONG!");` |

## AnselInputStreamReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\gedcom\AnselInputStreamReader.java`

| Line | Content |
| --- | --- |
| 121 | `return "ANSEL";` |

## AnselOutputStreamWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\gedcom\AnselOutputStreamWriter.java`

| Line | Content |
| --- | --- |
| 103 | `return "ANSEL";` |

## ConfigLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\om\util\ConfigLoader.java`

| Line | Content |
| --- | --- |
| 60 | `private static final String MANIFEST_FILENAME = "META-INF/SCHEMATYPE";` |
| 62 | `private static final String CONFIG_FILE_ENTRY_TYPE_ENDING = "XSI_Relation_Type";` |
| 64 | `private static final String CONFIG_FILE_ENTRY_CLASSNAME_ENDING = "XSI_Relation_Class";` |
| 109 | `\|\| ("".equals(classname.trim()))` |
| 111 | `throw new ConfigException("No class defined for type: " + type + ". Please check plugin Manifest files, or download new extension. ");` |
| 134 | `String sep = System.getProperty("path.separator");` |
| 135 | `String path = System.getProperty("java.class.path");` |
| 142 | `&& (token.getName().endsWith(".jar"))` |
| 149 | `String extPath = System.getProperty("java.ext.dirs");` |
| 155 | `if (name.toLowerCase().endsWith(".jar"))` |
| 177 | `throw new ConfigException("Error while accessing JAR file. ", zipEx);` |
| 179 | `throw new ConfigException("Error while accessing JAR file. ", ioe);` |
| 194 | `throw new ConfigException("Error while accessing entry from JAR file. ", ioe);` |
| 231 | `&& (!"".equals(classname.trim()))` |
| 232 | `&& (!"".equals(type.trim()))` |

## SchemaLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\om\util\SchemaLoader.java`

| Line | Content |
| --- | --- |
| 211 | `throw new FGCAException("XML file is null, does not exist or is directory. ");` |
| 217 | `System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");` |
| 221 | `dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");` |
| 222 | `dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", schemaFile.getAbsoluteFile());` |
| 233 | `throw new SchemaException("Unable to parse xml file: " + xmlFile.getAbsolutePath(), sax);` |
| 235 | `throw new FGCAException("Unable to access xml file: " + xmlFile.getAbsolutePath(), ioe);` |
| 239 | `throw new SchemaException("Parser configuration wrong: " + xmlFile.getAbsolutePath(), pce);` |
| 260 | `throw new FGCAException("XML Schema is NULL or has no child nodes. ");` |
| 275 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_OBSERVER_CONTAINER + " element. ");` |
| 283 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_TARGET_CONTAINER + " element. ");` |
| 291 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_SITE_CONTAINER + " element. ");` |
| 299 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_SCOPE_CONTAINER + " element. ");` |
| 307 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_EYEPIECE_CONTAINER + " element. ");` |
| 315 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_IMAGER_CONTAINER + " element. ");` |
| 324 | `throw new FGCAException("Schema XML can only have one " + RootElement.XML_SESSION_CONTAINER + " element. ");` |
| 365 | `throw new SchemaException("Unable to get classname from xsi:type.\n" + ce.getMessage());` |
| 373 | `throw new SchemaException("Unable to find class: " + classname + "\n" + cnfe.getMessage());` |
| 402 | `throw new SchemaException("Unable to instantiate class: " + classname + "\n" + ie.getMessage());` |
| 404 | `throw new SchemaException("Unable to invocate class: " + classname + "\n" + ite.getMessage());` |
| 406 | `throw new SchemaException("Unable to access class: " + classname + "\n" + iae.getMessage());` |
| 409 | `throw new SchemaException("Unable to load class: " + classname + "\nMaybe class has no default constructor. ");` |
| 445 | `System.err.println(se + "\nContinue loading next observation...\n");` |
| 487 | `System.err.println(se + "\nContinue with next target element...\n");` |
| 496 | `throw new SchemaException("Unable to load class of type: " + xsiType + "\nClass seems not to be of type ITarget. ");` |
| 499 | `throw new SchemaException("Unable to load class of type: " + xsiType);` |
| 502 | `throw new SchemaException("No attribute specified: " + ITarget.XML_XSI_TYPE);` |
| 505 | `throw new SchemaException("No attribute specified: " + ITarget.XML_XSI_TYPE);` |
| 638 | `System.err.println("XML Schema error: " + exception);` |
| 644 | `System.err.println("XML Schema fatal error: " + exception);` |
| 650 | `System.out.println("XML Schema warning: " + exception);` |

## OMDOMReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\openmath\io\OMDOMReader.java`

| Line | Content |
| --- | --- |
| 46 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI", "OMS",` |
| 47 | `"OMSTR", "OMV", "OMBVAR", "OMATP", "OMR", "OMFOREIGN"` |
| 136 | `throw new IOException("Unable to construct DOM: " +` |
| 143 | `NodeList tNodeList = tDocument.getElementsByTagName("OMOBJ");` |
| 160 | `throw new IOException("Unable to read OMObject from DOM");` |
| 455 | `throw new IOException("Unable to read OMObject from DOM: "` |
| 468 | `tObject.setAttribute(tNode.getPrefix() + ":"` |

## OMDOMWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\openmath\io\OMDOMWriter.java`

| Line | Content |
| --- | --- |
| 104 | `Node node = document.createElement("OMOBJ");` |
| 117 | `throw new IOException("Unable to write out DOM tree:" +` |
| 140 | `node = document.createElement("OMA");` |
| 152 | `node = document.createElement("OMATTR");` |
| 158 | `Element pairs = document.createElement("OMATP");` |
| 173 | `node = document.createElement("OMB");` |
| 182 | `node = document.createElement("OMBIND");` |
| 185 | `Element variables = document.createElement("OMBVAR");` |
| 200 | `node = document.createElement("OME");` |
| 214 | `node = document.createElement("OMF");` |
| 218 | `node = document.createElement("OMFOREIGN");` |
| 224 | `node = document.createElement("OMI");` |
| 233 | `node = document.createElement("OMR");` |
| 237 | `node = document.createElement("OMOBJ");` |
| 241 | `node = document.createElement("OMSTR");` |
| 250 | `node = document.createElement("OMS");` |
| 254 | `node = document.createElement("OMV");` |
| 278 | `throw new IOException("Unable to write out DOM node");` |

## OMXMLReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\openmath\io\OMXMLReader.java`

| Line | Content |
| --- | --- |
| 51 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI", "OMS",` |
| 52 | `"OMSTR", "OMV", "OMBVAR", "OMATP", "OMR", "OMFOREIGN"` |
| 205 | `if (fLocalName.equals("OMOBJ")) {` |
| 299 | `if (fLocalName.equals("OMFOREIGN"))` |
| 441 | `if (fAttributes.getIndex("dec") != -1) {` |
| 442 | `tFloat.setFloat(fAttributes.getValue("dec"));` |
| 444 | `if (fAttributes.getIndex("hex") != -1) {` |
| 445 | `tFloat.setFloat(fAttributes.getValue("hex"), "hex");` |
| 457 | `if (fAttributes.getIndex("cd") != -1) {` |
| 458 | `tSymbol.setCD(fAttributes.getValue("cd"));` |
| 460 | `if (fAttributes.getIndex("name") != -1) {` |
| 461 | `tSymbol.setName(fAttributes.getValue("name"));` |
| 473 | `if (fAttributes.getIndex("name") != -1) {` |
| 474 | `tVariable.setName(fAttributes.getValue("name"));` |
| 497 | `throw new SAXException("Element <" + fLocalName +` |
| 498 | `"> not recognized");` |
| 521 | `if (fLocalName.equals("OMFOREIGN"))` |
| 586 | `return "OMVCT";` |
| 595 | `return "<OMVCT/>";` |

## OMXMLWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\openmath\io\OMXMLWriter.java`

| Line | Content |
| --- | --- |
| 48 | `"OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",` |
| 49 | `"OMS", "OMSTR", "OMV", "OMBVAR", "OMATP", "OMR", "OMFOREIGN"` |
| 193 | `mPrintWriter.print(" " + tKey.toString() + "=\"");` |
| 194 | `mPrintWriter.print(fAttributes.get(tKey).toString() + "\"");` |
| 208 | `mPrintWriter.print("<OMA");` |
| 210 | `mPrintWriter.println(">");` |
| 220 | `mPrintWriter.print("</OMA>");` |
| 235 | `mPrintWriter.print("<OMATTR");` |
| 237 | `mPrintWriter.print(">");` |
| 246 | `mPrintWriter.print("<OMATP>");` |
| 263 | `mPrintWriter.print("</OMATP>");` |
| 273 | `mPrintWriter.print("</OMATTR>");` |
| 289 | `mPrintWriter.print("<OMBIND");` |
| 291 | `mPrintWriter.print(">");` |
| 300 | `mPrintWriter.print("<OMBVAR>");` |
| 315 | `mPrintWriter.print("</OMBVAR>");` |
| 324 | `mPrintWriter.print("</OMBIND>");` |
| 340 | `mPrintWriter.print("<OMB");` |
| 342 | `mPrintWriter.print(">");` |
| 344 | `mPrintWriter.print("</OMB>");` |
| 435 | `mPrintWriter.print("<OME");` |
| 437 | `mPrintWriter.print(">");` |
| 454 | `mPrintWriter.print("</OME>");` |
| 470 | `mPrintWriter.print("<OMF");` |
| 472 | `mPrintWriter.print("/>");` |
| 487 | `mPrintWriter.print("<OMFOREIGN");` |
| 489 | `mPrintWriter.print(">");` |
| 496 | `mPrintWriter.print("</OMFOREIGN>");` |
| 511 | `mPrintWriter.print("  ");` |
| 525 | `mPrintWriter.print("<OMI");` |
| 527 | `mPrintWriter.print(">");` |
| 529 | `mPrintWriter.print(fInteger.getInteger() + "</OMI>");` |
| 543 | `mPrintWriter.print("<OMOBJ>");` |
| 553 | `mPrintWriter.print("</OMOBJ>");` |
| 572 | `mPrintWriter.print("<OMR");` |
| 574 | `mPrintWriter.print("/>");` |
| 590 | `mPrintWriter.print("<OMSTR");` |
| 592 | `mPrintWriter.print(">");` |
| 594 | `mPrintWriter.print(fString.getString() + "</OMSTR>");` |
| 610 | `mPrintWriter.print("<OMS");` |
| 612 | `mPrintWriter.print("/>");` |
| 628 | `mPrintWriter.print("<OMV");` |
| 630 | `mPrintWriter.print("/>");` |

## SBMLLevel2Reader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\ml\sbml\SBMLLevel2Reader.java`

| Line | Content |
| --- | --- |
| 59 | `private final static String NAMESPACE_MATHML = "http://www.w3.org/1998/Math/MathML";` |
| 60 | `private final static String NAMESPACE_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";` |
| 61 | `private final static String NAMESPACE_SBML = "http://www.sbml.org/sbml/level2";` |
| 85 | `StringBuffer text = new StringBuffer("");` |
| 88 | `text.append(" " + attributes.getQName(attributesIndex) + "=\"" + attributes.getValue(attributesIndex) + "\"");` |
| 94 | `text.append((key.length() == 0 ? " xmlns" : (" xmlns:" + key)) + "=\"" + entry.getValue() + "\"");` |
| 100 | `return "[" + spe.getSystemId() + "] Line " + spe.getLineNumber() + ": " + spe.getMessage();` |
| 124 | `if (localName.equals("RDF") && namespace.equals(NAMESPACE_RDF)) {` |
| 125 | `((SBase) stack.peek()).setRDF(localText.toString() + "</rdf:RDF>");` |
| 128 | `localText.append("</" + qName + ">");` |
| 130 | `if (localName.equals("notes") && namespace.equals(NAMESPACE_SBML)) {` |
| 131 | `((SBase) stack.peek()).getNotes().add(localText.toString() + "</notes>");` |
| 134 | `localText.append("</" + qName + ">");` |
| 136 | `if (localName.equals("annotation") && namespace.equals(NAMESPACE_SBML)) {` |
| 137 | `((SBase) stack.peek()).getAnnotations().add(localText.toString() + "</annotation>");` |
| 140 | `localText.append("</" + qName + ">");` |
| 144 | `if (localName.equals("math")) {` |
| 145 | `setTextOfMathElement(localText.toString() + "</math:math>\n");` |
| 148 | `localText.append(JEP.isMathMLFunction(localName) ? ("<math:" + localName + "/>\n") : ("</math:" + localName + ">\n"));` |
| 150 | `throw new SAXException("Found unknown element [" + namespace + "]" + localName + ".");` |
| 154 | `String message = "Error: " + getParseExceptionInfo(spe);` |
| 159 | `String message = "Fatal Error: " + getParseExceptionInfo(spe);` |
| 165 | `localText.append("<" + qName + createAttributesText(attributes, namespacePrefixToURI) + ">");` |
| 168 | `throw new SAXException("SBML tag " + localName + " encountered while in MathML mode.");` |
| 171 | `if (localName.equals("math")) {` |
| 173 | `localText = new StringBuffer("<math:math>\n");` |
| 176 | `localText.append("<math:" + localName + createAttributesText(attributes, null) + ">");` |
| 177 | `if (!localName.equals("ci") && !localName.equals("cn"))` |
| 178 | `localText.append("\n");` |
| 181 | `throw new SAXException("Math tag " + localName + " encountered when not in MathML mode.");` |
| 184 | `throw new SAXException("RDF tag " + localName + " encountered while in MathML mode.");` |
| 185 | `if (localName.equals("RDF"))` |
| 188 | `throw new SAXException("RDF tag " + localName + " encountered when not in RDF mode.");` |
| 189 | `((SBase) stack.peek()).setRDF("<rdf:RDF" + createAttributesText(attributes, namespacePrefixToURI) + ">");` |
| 191 | `throw new SAXException("Found unknown element [" + namespace + "]" + localName + ".");` |
| 200 | `errorStream.println("Warning: " + getParseExceptionInfo(spe));` |
| 211 | `if (localName.equals("functionDefinition"))` |
| 213 | `else if (localName.equals("compartment"))` |
| 215 | `else if (localName.equals("parameter")) {` |
| 221 | `} else if (localName.equals("reaction"))` |
| 223 | `else if (localName.equals("speciesReference")) {` |
| 229 | `throw new SAXException("Mode " + mode + " not allowed for speciesReference.");` |
| 231 | `} else if (localName.equals("modifierSpeciesReference")) {` |
| 234 | `} else if (localName.equals("kineticLaw")) {` |
| 237 | `} else if (localName.equals("species"))` |
| 240 | `if (localName.equals("algebraicRule") \|\| localName.equals("rateRule") \|\| localName.equals("assignmentRule"))` |
| 242 | `else if (localName.equals("unit")) {` |
| 245 | `} else if (localName.equals("unitDefinition"))` |
| 247 | `else if (localName.equals("event"))` |
| 249 | `else if (localName.equals("eventAssignment")) {` |
| 253 | `} else if (localName.equals("delay")) {` |
| 256 | `} else if (localName.equals("trigger")) {` |
| 267 | `throw new SAXException("Cannot set math for a null element.");` |
| 279 | `throw new SAXException("Can only set math for an event when in delay, trigger, or assignment.");` |
| 283 | `throw new SAXException("Cannot set math for SBML element " + localElement.getClass().getName() + ".");` |
| 287 | `if (localName.equals("model")) {` |
| 290 | `model.setMetaid(attributes.getValue("metaid"));` |
| 291 | `model.setId(attributes.getValue("id"));` |
| 292 | `model.setName(attributes.getValue("name"));` |
| 293 | `} else if (localName.equals("notes")) {` |
| 295 | `localText = new StringBuffer("<notes" + createAttributesText(attributes, namespacePrefixToURI) + ">");` |
| 296 | `} else if (localName.equals("annotation")) {` |
| 298 | `localText = new StringBuffer("<annotation" + createAttributesText(attributes, namespacePrefixToURI) + ">");` |
| 299 | `} else if (localName.equals("delay"))` |
| 301 | `else if (localName.equals("trigger"))` |
| 303 | `else if (localName.equals("unit")) {` |
| 304 | `Unit unit = new Unit(UnitManager.findBaseUnit(attributes.getValue("kind")));` |
| 307 | `unit.setMetaid(attributes.getValue("metaid"));` |
| 308 | `if (attributes.getIndex("exponent") != -1)` |
| 309 | `unit.setExponent(Integer.parseInt(attributes.getValue("exponent")));` |
| 310 | `if (attributes.getIndex("multiplier") != -1)` |
| 311 | `unit.setMultiplier(Double.parseDouble(attributes.getValue("multiplier")));` |
| 312 | `if (attributes.getIndex("scale") != -1)` |
| 313 | `unit.setScale(Integer.parseInt(attributes.getValue("scale")));` |
| 314 | `if (attributes.getIndex("offset") != -1)` |
| 315 | `unit.setOffset(Double.parseDouble(attributes.getValue("offset")));` |
| 316 | `} else if (localName.equals("unitDefinition")) {` |
| 317 | `localElement = new UnitDefinition(attributes.getValue("id"), attributes.getValue("name"));` |
| 319 | `((UnitDefinition) localElement).setMetaid(attributes.getValue("metaid"));` |
| 320 | `} else if (localName.equals("compartment")) {` |
| 323 | `((Compartment) localElement).setMetaid(attributes.getValue("metaid"));` |
| 324 | `((Compartment) localElement).setId(attributes.getValue("id"));` |
| 325 | `((Compartment) localElement).setName(attributes.getValue("name"));` |
| 326 | `if (attributes.getIndex("constant") != -1)` |
| 327 | `((Compartment) localElement).setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);` |
| 328 | `if (attributes.getIndex("size") != -1)` |
| 329 | `((Compartment) localElement).setSize(attributes.getValue("size"));` |
| 330 | `if (attributes.getIndex("outside") != -1)` |
| 331 | `((Compartment) localElement).setOutside(attributes.getValue("outside"));` |
| 332 | `if (attributes.getIndex("spatialDimensions") != -1)` |
| 333 | `((Compartment) localElement).setSpatialDimensions(attributes.getValue("spatialDimensions"));` |
| 334 | `if (attributes.getIndex("units") != -1)` |
| 335 | `((Compartment) localElement).setUnits(attributes.getValue("units"));` |
| 336 | `} else if (localName.equals("parameter")) {` |
| 343 | `parameter.setMetaid(attributes.getValue("metaid"));` |
| 344 | `parameter.setId(attributes.getValue("id"));` |
| 345 | `parameter.setName(attributes.getValue("name"));` |
| 346 | `if (attributes.getIndex("constant") != -1)` |
| 347 | `parameter.setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);` |
| 348 | `if (attributes.getIndex("units") != -1)` |
| 349 | `parameter.setUnits(attributes.getValue("units"));` |
| 350 | `if (attributes.getIndex("value") != -1)` |
| 351 | `parameter.setValue(Double.parseDouble(attributes.getValue("value")));` |
| 352 | `} else if (localName.equals("species")) {` |
| 355 | `((Species) localElement).setMetaid(attributes.getValue("metaid"));` |
| 356 | `((Species) localElement).setId(attributes.getValue("id"));` |
| 357 | `((Species) localElement).setName(attributes.getValue("name"));` |
| 358 | `if (attributes.getIndex("boundaryCondition") != -1)` |
| 359 | `((Species) localElement).setBoundaryCondition(Boolean.valueOf(attributes.getValue("boundaryCondition")) == Boolean.TRUE);` |
| 360 | `if (attributes.getIndex("charge") != -1)` |
| 361 | `((Species) localElement).setCharge(attributes.getValue("charge"));` |
| 362 | `if (attributes.getIndex("compartment") != -1)` |
| 363 | `((Species) localElement).setCompartment(attributes.getValue("compartment"));` |
| 364 | `if (attributes.getIndex("constant") != -1)` |
| 365 | `((Species) localElement).setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);` |
| 366 | `if (attributes.getIndex("hasOnlySubstanceUnits") != -1)` |
| 367 | `((Species) localElement).setHasOnlySubstanceUnits(Boolean.valueOf(attributes.getValue("hasOnlySubstanceUnits")) == Boolean.TRUE);` |
| 368 | `if (attributes.getIndex("initialAmount") != -1)` |
| 369 | `((Species) localElement).setInitialAmount(Double.parseDouble(attributes.getValue("initialAmount")));` |
| 370 | `if (attributes.getIndex("initialConcentration") != -1)` |
| 371 | `((Species) localElement).setInitialConcentration(Double.parseDouble(attributes.getValue("initialConcentration")));` |
| 372 | `if (attributes.getIndex("spatialSizeUnits") != -1)` |
| 373 | `((Species) localElement).setSpatialSizeUnits(attributes.getValue("spatialSizeUnits"));` |
| 374 | `if (attributes.getIndex("substanceUnits") != -1)` |
| 375 | `((Species) localElement).setSubstanceUnits(attributes.getValue("substanceUnits"));` |
| 376 | `} else if (localName.equals("reaction")) {` |
| 380 | `((Reaction) localElement).setMetaid(attributes.getValue("metaid"));` |
| 381 | `((Reaction) localElement).setId(attributes.getValue("id"));` |
| 382 | `((Reaction) localElement).setName(attributes.getValue("name"));` |
| 383 | `if (attributes.getIndex("fast") != -1)` |
| 384 | `((Reaction) localElement).setFast(Boolean.valueOf(attributes.getValue("fast")) == Boolean.TRUE);` |
| 385 | `if (attributes.getIndex("reversible") != -1)` |
| 386 | `((Reaction) localElement).setReversible(Boolean.valueOf(attributes.getValue("reversible")) == Boolean.TRUE);` |
| 387 | `} else if (localName.equals("kineticLaw")) {` |
| 391 | `localKineticLaw.setMetaid(attributes.getValue("metaid"));` |
| 392 | `if (attributes.getIndex("substanceUnits") != -1)` |
| 393 | `localKineticLaw.setSubstanceUnits(attributes.getValue("substanceUnits"));` |
| 394 | `if (attributes.getIndex("timeUnits") != -1)` |
| 395 | `localKineticLaw.setTimeUnits(attributes.getValue("timeUnits"));` |
| 396 | `} else if (localName.equals("speciesReference")) {` |
| 400 | `reference.setMetaid(attributes.getValue("metaid"));` |
| 401 | `if (attributes.getIndex("species") != -1)` |
| 402 | `reference.setSpecies(attributes.getValue("species"));` |
| 403 | `if (attributes.getIndex("stoichiometry") != -1)` |
| 404 | `reference.setStoichiometry(Double.parseDouble(attributes.getValue("stoichiometry")));` |
| 405 | `} else if (localName.equals("stoichiometryMath")) {` |
| 409 | `} else if (localName.equals("modifierSpeciesReference")) {` |
| 413 | `reference.setMetaid(attributes.getValue("metaid"));` |
| 414 | `if (attributes.getIndex("species") != -1)` |
| 415 | `reference.setSpecies(attributes.getValue("species"));` |
| 416 | `} else if (localName.equals("event")) {` |
| 419 | `((Event) localElement).setMetaid(attributes.getValue("metaid"));` |
| 420 | `((Event) localElement).setId(attributes.getValue("id"));` |
| 421 | `((Event) localElement).setName(attributes.getValue("name"));` |
| 422 | `if (attributes.getIndex("timeUnits") != -1)` |
| 423 | `((Event) localElement).setTimeUnits(attributes.getValue("timeUnits"));` |
| 424 | `} else if (localName.equals("eventAssignment")) {` |
| 429 | `assignment.setMetaid(attributes.getValue("metaid"));` |
| 430 | `if (attributes.getIndex("variable") != -1)` |
| 431 | `assignment.setVariable(attributes.getValue("variable"));` |
| 432 | `} else if (localName.equals("algebraicRule")) {` |
| 435 | `} else if (localName.equals("assignmentRule")) {` |
| 438 | `if (attributes.getIndex("variable") != -1)` |
| 439 | `((AssignmentRule) localElement).setVariable(attributes.getValue("variable"));` |
| 440 | `} else if (localName.equals("rateRule")) {` |
| 443 | `if (attributes.getIndex("variable") != -1)` |
| 444 | `((RateRule) localElement).setVariable(attributes.getValue("variable"));` |
| 445 | `} else if (localName.equals("sbml"))` |
| 447 | `else if (localName.equals("listOfEventAssignments"))` |
| 449 | `else if (localName.equals("listOfReactants")) {` |
| 452 | `} else if (localName.equals("listOfProducts")) {` |
| 455 | `} else if (localName.equals("listOfModifiers")) {` |
| 458 | `} else if (localName.equals("listOfFunctionDefinitions")) {` |
| 461 | `} else if (localName.equals("listOfCompartments")) {` |
| 464 | `} else if (localName.equals("listOfUnitDefinitions")) {` |
| 466 | `} else if (localName.equals("listOfSpecies")) {` |
| 469 | `} else if (localName.equals("listOfParameters")) {` |
| 476 | `} else if (localName.equals("listOfRules")) {` |
| 479 | `} else if (localName.equals("listOfEvents")) {` |
| 482 | `} else if (localName.equals("listOfReactions")) {` |
| 485 | `} else if (localName.equals("listOfUnits"))` |
| 487 | `else if (localName.equals("functionDefinition")) {` |
| 490 | `((FunctionDefinition) localElement).setId(attributes.getValue("id"));` |
| 491 | `((FunctionDefinition) localElement).setName(attributes.getValue("name"));` |
| 493 | `throw new SAXException("Found unknown SBML element " + localName + ".");` |

## Downloader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\net\Downloader.java`

| Line | Content |
| --- | --- |
| 173 | `throw new IllegalStateException("download already started");` |
| 264 | `throw new IllegalStateException("download is running");` |
| 322 | `e = new IOException("download interrupted by calling cancel()");` |
| 352 | `throw new IllegalStateException("download has already been started");` |

## JDownloader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\net\JDownloader.java`

| Line | Content |
| --- | --- |
| 92 | `super("JDownloader thread for " + uc.getURL());` |
| 188 | `String note = "saving " + file.getName() + " (bytes: " +` |
| 189 | `((length > 0) ? (length + ")") : "unknown)");` |
| 218 | `"download complete (" + file.getName() + ")");` |
| 225 | `"download failed", JOptionPane.ERROR_MESSAGE);` |

## NumericTextFileReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\physics\nuclear\kinematics\NumericTextFileReader.java`

| Line | Content |
| --- | --- |
| 69 | `System.err.println(getClass().getName() + " constructor: " + ioe);` |
| 84 | `".read(): Wrong token type: " + tokenizer.ttype);` |

## SimpleTokenReader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\physics\nuclear\kinematics\SimpleTokenReader.java`

| Line | Content |
| --- | --- |
| 58 | `st.quoteChar('\"');` |
| 75 | `".readInteger(): Wrong token type: " + st.ttype);` |
| 93 | `".readInteger(): Wrong token type: " + st.ttype);` |
| 112 | `".readString(): Wrong token type: " + st.ttype);` |

## AtlasViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\physics\solids\gui\AtlasViewer.java`

| Line | Content |
| --- | --- |
| 198 | `JFrame frame = new JFrame("ATLAS Viewer");` |
| 230 | `JButton fitButton = new JButton("Fit");` |
| 239 | `JButton shadeButton = new JButton("Shade");` |
| 247 | `JButton elButton = new JButton("EL");` |
| 255 | `JButton nlButton = new JButton("NL");` |

## OpticalApp.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\physics\waves\optics\gui\OpticalApp.java`

| Line | Content |
| --- | --- |
| 118 | `return "";` |

## FontViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\swing\FontViewer.java`

| Line | Content |
| --- | --- |
| 58 | `super("FontViewer", true, true);` |
| 74 | `super("FontViewer", true, true);` |
| 119 | `return "no font selected";` |
| 122 | `String s = font.getName() + " (";` |
| 126 | `s += "plain";` |
| 131 | `s += "italic";` |
| 136 | `s += "bold";` |
| 141 | `s += "bold, italic";` |
| 146 | `s += (", size: " + font.getSize() + ")");` |

## ImageLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\swing\ImageLoader.java`

| Line | Content |
| --- | --- |
| 61 | `super("ImageViewer", true, false);` |
| 64 | `filter = new ExtensionFileFilter("jpg", "Images");` |
| 65 | `filter.addType("jpeg");` |
| 66 | `filter.addType("gif");` |
| 67 | `filter.addType("tif");` |
| 68 | `filter.addType("tiff");` |
| 74 | `final JButton button = new JButton("view image from file");` |

## ImageViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\swing\ImageViewer.java`

| Line | Content |
| --- | --- |
| 68 | `"maintain proportions");` |
| 71 | `private JCheckBoxMenuItem sizeToFit = new JCheckBoxMenuItem("size to fit");` |
| 151 | `System.out.println("width : " + image.getWidth(this));` |
| 152 | `System.out.println("height: " + image.getHeight(this));` |
| 288 | `JMenuItem item = new JMenuItem("reset image");` |
| 298 | `item = new JMenuItem("copy image");` |

## JTextViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\swing\JTextViewer.java`

| Line | Content |
| --- | --- |
| 82 | `this(lang.getString("textViewer"), text);` |
| 102 | `this(lang.getString("textViewer"), parent, text);` |
| 124 | `this(lang.getString("textViewer"), null, text, width, height);` |
| 166 | `"org/jscience/awt/icons/general/Save16.gif"));` |
| 168 | `button.setToolTipText(lang.getString("save to file"));` |
| 169 | `button.setMnemonic(((Integer) lang.getObject("keySave")).intValue());` |
| 182 | `fc.getSelectedFile() + "\n" +` |
| 183 | `lang.getString("file exists; overwrite?"),` |
| 184 | `lang.getString("overwrite warning"),` |
| 203 | `"org/jscience/awt/icons/general/Copy16.gif"));` |
| 205 | `button.setToolTipText(lang.getString("copyAll"));` |
| 206 | `button.setMnemonic(((Integer) lang.getObject("keyCopy")).intValue());` |
| 216 | `"org/jscience/awt/icons/general/Print16.gif"));` |
| 218 | `button.setToolTipText(lang.getString("print"));` |
| 219 | `button.setMnemonic(((Integer) lang.getObject("keyPrint")).intValue());` |
| 241 | `"org/jscience/awt/icons/general/Search16.gif"));` |
| 243 | `button.setToolTipText(lang.getString("search"));` |
| 244 | `button.setMnemonic(((Integer) lang.getObject("keySearch")).intValue());` |
| 248 | `lang.getString("search"), searchString);` |
| 261 | `"not found", searchString,` |
| 273 | `"org/jscience/awt/icons/general/Edit16.gif"));` |
| 276 | `toggleButton.setToolTipText(lang.getString("editable"));` |
| 277 | `toggleButton.setMnemonic(((Integer) lang.getObject("keyEditable")).intValue());` |

## FitsImageViewer.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\tests\io\fitsbrowser\FitsImageViewer.java`

| Line | Content |
| --- | --- |
| 408 | `label.setText("gamma=" + format.format(gamma));` |

## RandomApp.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\tests\measure\random\RandomApp.java`

| Line | Content |
| --- | --- |
| 53 | `static String generators[] = {"ranmar", "ranecu", "ranlux", "randomjava", "null", "ranmt"};` |
| 54 | `static String distributions[] = {"flat", "gaussian", "choose1", "choose2", "coin1", "coin2"};` |
| 84 | `if (a == "noprint") {` |
| 90 | `if (a == "seed") {` |
| 92 | `die("RandomApp: only one seed can be passed");` |
| 94 | `die("RandomApp: missing seed.");` |
| 102 | `die("RandomApp: seed is not a valid number.");` |
| 111 | `if (a == "luxury") {` |
| 113 | `die("RandomApp: only one luxury level can be passed");` |
| 115 | `die("RandomApp: missing luxury level.");` |
| 123 | `die("RandomApp: luxury level is not a valid number.");` |
| 130 | `die("RandomApp: luxury level must be between 0 and " + Ranlux.maxlev);` |
| 138 | `die("RandomApp: only one generator can be selected.");` |
| 148 | `die("RandomApp: only one distribution can be selected.");` |
| 159 | `die("RandomApp: only one number of random numbers can be selected.");` |
| 162 | `die("RandomApp: syntax error <" + a + ">");` |
| 198 | `die("Invalid distribution: " + distribution);` |

## FieldLensApp.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\tests\physics\optics\FieldLensApp.java`

| Line | Content |
| --- | --- |
| 102 | `add("Center", canvas);` |
| 105 | `add("West", controls);` |
| 117 | `vaccum = new Material("Vaccum", new ConstantParameter(1.0));` |
| 132 | `ds.addDevice("With", fl);` |
| 133 | `ds.addDevice("Without", nofl);` |
| 134 | `ds.setCurrentDevice("Without");` |
| 158 | `sb1.setName("RaysPos");` |
| 163 | `Checkbox c = new Checkbox("field lens", false);` |
| 168 | `Button Reset = new Button("Reset");` |
| 169 | `Reset.setName("Reset");` |
| 182 | `if (name.equals("RaysPos")) {` |
| 203 | `ds.setCurrentDevice("With");` |
| 205 | `ds.setCurrentDevice("Without");` |
| 218 | `if (name.equals("Reset")) {` |
| 224 | `ds.setCurrentDevice("Without");` |

## LicenseLoader.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\util\LicenseLoader.java`

| Line | Content |
| --- | --- |
| 159 | `String[] options = new String[]{"from file", "from URL", "cancel"};` |
| 161 | `"license required for " + licensee.getClass().getName(),` |
| 162 | `"LicenseLoader", JOptionPane.YES_NO_CANCEL_OPTION,` |
| 169 | `fc.setDialogTitle("aquire license");` |
| 184 | `"enter URL for license");` |
| 198 | `assert false : "unexpected option encountered";` |

## JDBCLogWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\util\logging\JDBCLogWriter.java`

| Line | Content |
| --- | --- |
| 99 | `throw new LogException("no appropriate LogEntryFormatter used", ex);` |

## StackTraceFileWriter.java (jscience-old-v1)
`C:\Silvere\Encours\Developpement\JScience\jscience-old-v1\src\org\jscience\util\logging\StackTraceFileWriter.java`

| Line | Content |
| --- | --- |
| 55 | `String s = "logged exception at " + new Date(entry.getTime()) +` |
| 56 | `":" + StringUtils.lb +` |

## FinancialMarketReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\economics\loaders\FinancialMarketReader.java`

| Line | Content |
| --- | --- |
| 66 | `return String.format("%s [O:%s H:%s L:%s C:%s V:%s]", time, open, high, low, close, volume);` |
| 78 | `throw new java.io.IOException("Resource not found: " + id);` |
| 79 | `return loadCSV(ctxIs, "USD"); // Default currency` |
| 81 | `return loadCSV(is, "USD"); // Default currency` |
| 93 | `return "loader.financial.name";` |
| 98 | `return "Economics";` |
| 103 | `return "Financial Market Data Reader.";` |
| 108 | `return "financial_market_data.csv";` |
| 124 | `String[] parts = line.split(",");` |

## ETOPOElevationReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\geography\loaders\ETOPOElevationReader.java`

| Line | Content |
| --- | --- |
| 39 | `return "Geography";` |
| 44 | `return "ETOPO1 Global Relief Model Reader.";` |
| 49 | `return org.jscience.io.Configuration.get("data.etopo.path", "/data/etopo/");` |
| 60 | `String[] parts = coordinates.split(",");` |
| 64 | `String dataPath = org.jscience.io.Configuration.get("data.etopo.path", "/data/etopo/");` |
| 65 | `java.io.File file = new java.io.File(dataPath, "ETOPO1_Ice_g_int.bin");` |
| 84 | `try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "r")) {` |

## ElevationReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\geography\loaders\ElevationReader.java`

| Line | Content |
| --- | --- |
| 58 | `private static final String GOOGLE_API_BASE = "https://maps.googleapis.com/maps/api/elevation/json";` |
| 68 | `this.googleKey = Configuration.get("api.google.elevation.key", "");` |
| 73 | `return "Geography";` |
| 78 | `return "Elevation Data Reader (Altimetry/Bathymetry).";` |
| 83 | `return "N/A";` |
| 94 | `String[] parts = coordinates.split(",");` |
| 116 | `throw new UnsupportedOperationException("Source not implemented: " + defaultSource);` |
| 122 | `System.err.println("WARNING: Google Elevation API key not set.");` |
| 127 | `String url = String.format("%s?locations=%f,%f&key=%s", GOOGLE_API_BASE, lat, lon, googleKey);` |
| 139 | `int idx = body.indexOf("\"elevation\" :");` |
| 142 | `int end = body.indexOf(",", start);` |

## GeoJSONReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\geography\loaders\GeoJSONReader.java`

| Line | Content |
| --- | --- |
| 57 | `return "Geography";` |
| 62 | `return "GeoJSON Geographic Data Reader.";` |
| 67 | `return "/";` |
| 82 | `throw new Exception("Resource not found: " + id);` |
| 123 | `JsonNode features = root.path("features");` |
| 133 | `else if ("Feature".equals(root.path("type").asText())) {` |
| 145 | `JsonNode properties = feature.path("properties");` |
| 146 | `JsonNode geometry = feature.path("geometry");` |
| 148 | `String name = properties.path("name").asText(null);` |
| 150 | `name = properties.path("NAME").asText(null);` |
| 153 | `name = "Unknown";` |
| 157 | `String typeStr = properties.path("type").asText("");` |
| 168 | `if (properties.has("population")) {` |
| 169 | `region.setPopulation(properties.path("population").asLong(0));` |
| 172 | `String geometryType = geometry.path("type").asText("");` |
| 173 | `JsonNode coordinates = geometry.path("coordinates");` |
| 175 | `if ("Point".equals(geometryType) && coordinates.isArray() && coordinates.size() >= 2) {` |
| 180 | `} else if ("Polygon".equals(geometryType) \|\| "MultiPolygon".equals(geometryType)) {` |
| 196 | `if ("MultiPolygon".equals(type)) {` |
| 265 | `} else if (node.has("coordinates")) {` |
| 266 | `extractCoordinates(node.path("coordinates"), coords);` |
| 267 | `} else if (node.has("features")) {` |
| 268 | `for (JsonNode feature : node.path("features")) {` |
| 269 | `extractCoordinates(feature.path("geometry"), coords);` |

## GoogleElevationReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\geography\loaders\GoogleElevationReader.java`

| Line | Content |
| --- | --- |
| 44 | `private static final String API_BASE = Configuration.get("api.google.elevation.base", "https://maps.googleapis.com/maps/api/elevation/json");` |
| 48 | `this.apiKey = Configuration.get("api.google.elevation.key", "");` |
| 53 | `return "Geography";` |
| 58 | `return "Google Elevation API Reader.";` |
| 74 | `String[] parts = coordinates.split(",");` |
| 82 | `System.err.println("WARNING: Google Elevation API key not set.");` |
| 87 | `String url = String.format("%s?locations=%f,%f&key=%s", API_BASE, lat, lon, apiKey);` |
| 98 | `int idx = body.indexOf("\"elevation\" :");` |
| 101 | `int end = body.indexOf(",", start);` |

## SRTMElevationReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\geography\loaders\SRTMElevationReader.java`

| Line | Content |
| --- | --- |
| 39 | `return "Geography";` |
| 44 | `return "SRTM Data Reader (HGT format).";` |
| 49 | `return org.jscience.io.Configuration.get("data.srtm.path", "/data/srtm/");` |
| 60 | `String[] parts = coordinates.split(",");` |
| 68 | `String latStr = (latInt >= 0 ? "N" : "S") + String.format("%02d", Math.abs(latInt));` |
| 69 | `String lonStr = (lonInt >= 0 ? "E" : "W") + String.format("%03d", Math.abs(lonInt));` |
| 70 | `String filename = latStr + lonStr + ".hgt";` |
| 72 | `String dataPath = org.jscience.io.Configuration.get("data.srtm.path", "/data/srtm/");` |
| 92 | `try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "r")) {` |

## CSVTimeSeriesReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\history\loaders\CSVTimeSeriesReader.java`

| Line | Content |
| --- | --- |
| 56 | `return "/data/history/";` |
| 67 | `return "History";` |
| 72 | `return "Historical Time Series Reader (CSV).";` |
| 124 | `String[] parts = line.split(",");` |

## FactbookReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\politics\loaders\FactbookReader.java`

| Line | Content |
| --- | --- |
| 63 | `return "Politics";` |
| 68 | `return "CIA World Factbook Reader (XML).";` |
| 73 | `return "/data/politics/";` |
| 86 | `throw new Exception("Resource not found: " + id);` |
| 142 | `LOG.debug("Parsing Factbook XML, root element: {}", doc.getDocumentElement().getNodeName());` |
| 148 | `LOG.warn("No country nodes found in Factbook XML");` |
| 152 | `LOG.info("Found {} countries in Factbook data", countryNodes.getLength());` |
| 162 | `LOG.warn("Failed to parse country at index {}: {}", i, e.getMessage());` |
| 166 | `LOG.info("Successfully loaded {} countries from Factbook", countries.size());` |
| 169 | `LOG.error("Failed to parse Factbook XML", e);` |
| 181 | `String[] possibleNames = { "country", "Country", "nation", "state" };` |
| 186 | `LOG.debug("Found countries using element name: {}", name);` |
| 192 | `if ("countries".equalsIgnoreCase(doc.getDocumentElement().getNodeName()) \|\|` |
| 193 | `"factbook".equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {` |
| 197 | `return doc.getElementsByTagName("country");` |
| 210 | `String name = getTextValue(elem, "name");` |
| 212 | `name = elem.getAttribute("name");` |
| 215 | `LOG.debug("Skipping element without name: {}", elem.getNodeName());` |
| 220 | `String code = getTextValue(elem, "code");` |
| 222 | `code = elem.getAttribute("code");` |
| 225 | `code = "";` |
| 244 | `String capital = getTextValue(elem, "capital");` |
| 250 | `String area = getTextValue(elem, "area");` |
| 256 | `LOG.debug("Could not parse area value: {}", area);` |
| 261 | `String population = getTextValue(elem, "population");` |
| 267 | `LOG.debug("Could not parse population value: {}", population);` |
| 272 | `String government = getTextValue(elem, "government");` |
| 278 | `String region = getTextValue(elem, "region");` |
| 284 | `String independence = getTextValue(elem, "independence");` |
| 288 | `String yearStr = independence.replaceAll("[^0-9]", "");` |
| 293 | `LOG.debug("Could not parse independence year: {}", independence);` |
| 323 | `String cleaned = value.replaceAll("[,\\s]", "")` |
| 324 | `.replaceAll("sq\\s*km", "")` |
| 325 | `.replaceAll("kmÂ²", "")` |
| 334 | `Country france = new Country("France", "FR", "FRA", 250, "Paris", "Europe", 67_750_000L, 643_801.0);` |
| 335 | `france.setGovernmentType("Semi-presidential republic");` |
| 339 | `france.setCurrencyCode("EUR");` |
| 342 | `france.getMajorIndustries().addAll(List.of("aerospace", "automotive", "pharmaceuticals", "tourism"));` |
| 343 | `france.getNaturalResources().addAll(List.of("coal", "iron ore", "bauxite", "zinc"));` |
| 344 | `france.getBorderCountries().addAll(List.of("BEL", "LUX", "DEU", "CHE", "ITA", "ESP", "AND", "MCO"));` |
| 348 | `Country usa = new Country("United States", "US", "USA", 840, "Washington, D.C.", "North America", 331_900_000L,` |
| 350 | `usa.setGovernmentType("Federal presidential constitutional republic");` |
| 354 | `usa.setCurrencyCode("USD");` |
| 357 | `usa.getMajorIndustries().addAll(List.of("technology", "aerospace", "automotive", "healthcare"));` |
| 358 | `usa.getNaturalResources().addAll(List.of("coal", "copper", "lead", "uranium", "natural gas"));` |
| 359 | `usa.getBorderCountries().addAll(List.of("CAN", "MEX"));` |
| 363 | `Country china = new Country("China", "CN", "CHN", 156, "Beijing", "Asia", 1_411_750_000L, 9_596_960.0);` |
| 364 | `china.setGovernmentType("Communist party-led state");` |
| 368 | `china.setCurrencyCode("CNY");` |
| 371 | `china.getMajorIndustries().addAll(List.of("manufacturing", "mining", "electronics", "textiles"));` |
| 372 | `china.getNaturalResources().addAll(List.of("coal", "iron ore", "rare earths", "tungsten"));` |
| 373 | `china.getBorderCountries().addAll(List.of("AFG", "BTN", "IND", "KAZ", "PRK", "KGZ", "LAO", "MNG"));` |
| 377 | `Country brazil = new Country("Brazil", "BR", "BRA", 76, "Brasília", "South America", 214_000_000L, 8_515_767.0);` |
| 378 | `brazil.setGovernmentType("Federal presidential constitutional republic");` |
| 382 | `brazil.setCurrencyCode("BRL");` |
| 385 | `brazil.getMajorIndustries().addAll(List.of("agriculture", "mining", "manufacturing", "services"));` |
| 386 | `brazil.getNaturalResources().addAll(List.of("iron ore", "manganese", "bauxite", "gold", "timber"));` |
| 388 | `.addAll(List.of("ARG", "BOL", "COL", "GUF", "GUY", "PRY", "PER", "SUR", "URY", "VEN"));` |
| 392 | `Country russia = new Country("Russia", "RU", "RUS", 643, "Moscow", "Europe", 144_100_000L, 17_098_242.0);` |
| 393 | `russia.setGovernmentType("Semi-presidential federation");` |
| 399 | `Country india = new Country("India", "IN", "IND", 356, "New Delhi", "Asia", 1_380_000_000L, 3_287_263.0);` |
| 400 | `india.setGovernmentType("Federal parliamentary republic");` |
| 406 | `Country canada = new Country("Canada", "CA", "CAN", 124, "Ottawa", "North America", 38_000_000L, 9_984_670.0);` |
| 407 | `canada.setGovernmentType("Federal parliamentary constitutional monarchy");` |

## WorldBankReader.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\politics\loaders\WorldBankReader.java`

| Line | Content |
| --- | --- |
| 55 | `private static final String RESOURCE_PATH = "/org/jscience/politics/worldbank-fallback.json";` |
| 56 | `private static final String WB_API_BASE = "https://api.worldbank.org/v2";` |
| 93 | `this.rateLimiter = registry.rateLimiter("worldbank");` |
| 105 | `return "Politics";` |
| 110 | `return "World Bank Data (Indicators & Countries).";` |
| 125 | `String url = String.format("%s/country/%s?format=json", WB_API_BASE, id);` |
| 191 | `LOG.warn("Failed to fetch indicator data for {}/{}: {}",` |
| 202 | `String url = String.format("%s/country?format=json&per_page=%d", WB_API_BASE, PER_PAGE);` |
| 215 | `throw new RuntimeException("World Bank API returned status: " + response.statusCode());` |
| 222 | `Map<String, Double> populations = fetchIndicator("all", "SP.POP.TOTL");` |
| 232 | `LOG.warn("Failed to fetch population data to enrich countries: " + e.getMessage());` |
| 250 | `String regionType = node.path("region").path("value").asText("");` |
| 251 | `if ("Aggregates".equals(regionType)) {` |
| 270 | `String name = node.path("name").asText(null);` |
| 275 | `String code = node.path("id").asText(null); // ISO3 code` |
| 279 | `String alpha2 = node.path("iso2Code").asText(code.length() >= 2 ? code.substring(0, 2) : "XX");` |
| 285 | `String capital = node.path("capitalCity").asText(null);` |
| 291 | `LOG.debug("Loaded country {}: capital={}, region={}, income={}",` |
| 294 | `node.path("region").path("value").asText(""),` |
| 295 | `node.path("incomeLevel").path("value").asText(""));` |
| 306 | `LOG.warn("Local resource not found: {}, using sample data", RESOURCE_PATH);` |
| 325 | `LOG.error("Failed to load from local resource", e);` |
| 334 | `String name = node.path("name").asText(null);` |
| 339 | `String code = node.path("code").asText("XX");` |
| 341 | `country.setPopulation(node.path("population").asLong(0));` |
| 359 | `LOG.info("Using built-in sample data");` |
| 390 | `String codeFilter = (countryCodes == null \|\| countryCodes.isEmpty()) ? "all"` |
| 391 | `: String.join(";", countryCodes);` |
| 396 | `String targetCountries = (countryCodes != null && countryCodes.size() > 20) ? "all" : codeFilter;` |
| 409 | `LOG.error("Failed to fetch indicator {}", indicator, e);` |
| 425 | `String url = String.format("%s/country/%s/indicator/%s?format=json&per_page=%d",` |
| 436 | `throw new RuntimeException("API Error " + response.statusCode());` |
| 450 | `String countryId = entry.path("countryiso3code").asText("");` |
| 452 | `countryId = entry.path("country").path("id").asText("");` |
| 462 | `double val = entry.path("value").asDouble(Double.NaN);` |
| 478 | `LOG.debug("WorldBankReader: manual cache clear requested (delegated to system cache)");` |

## CarTrafficViewer.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\ui\viewers\architecture\traffic\CarTrafficViewer.java`

| Line | Content |
| --- | --- |
| 72 | `public String getName() { return I18n.getInstance().get("traffic.title", "Traffic Simulation"); }` |
| 75 | `public String getCategory() { return "Social"; }` |
| 82 | `this.setStyle("-fx-background-color: #f8f8f8;");` |
| 87 | `sidebar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 0 1 0 0;");` |
| 89 | `Label title = new Label(I18n.getInstance().get("traffic.label.header", "Traffic Simulation"));` |
| 90 | `title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");` |
| 92 | `Label desc = new Label(I18n.getInstance().get("traffic.label.desc", "Intelligent Driver Model simulation"));` |
| 94 | `desc.setStyle("-fx-text-fill: #666;");` |
| 97 | `Label densityLabel = new Label(String.format("Cars: %.0f", 30.0));` |
| 98 | `densitySlider.valueProperty().addListener((o, old, val) -> densityLabel.setText(String.format("Cars: %.0f", val.doubleValue())));` |
| 101 | `Label speedLabel = new Label(String.format("Target Speed: %.0f m/s", 30.0));` |
| 104 | `speedLabel.setText(String.format("Target Speed: %.0f m/s", val.doubleValue()));` |
| 108 | `Label gapLabel = new Label(String.format("Time Gap: %.1f s", 1.5));` |
| 111 | `gapLabel.setText(String.format("Time Gap: %.1f s", val.doubleValue()));` |
| 114 | `Button resetBtn = new Button(I18n.getInstance().get("traffic.button.reset", "Reset"));` |
| 116 | `resetBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");` |
| 119 | `Button perturbBtn = new Button(I18n.getInstance().get("traffic.button.perturb", "Perturb"));` |
| 121 | `perturbBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");` |
| 124 | `jamStatusLabel = new Label("Status: Free Flow");` |
| 125 | `jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");` |
| 127 | `HBox legendFast = createLegendItem(Color.GREEN, "Fast (>25 m/s)");` |
| 128 | `HBox legendSlow = createLegendItem(Color.ORANGE, "Slow");` |
| 129 | `HBox legendStop = createLegendItem(Color.RED, "Stopped (<5 m/s)");` |
| 131 | `sidebar.getChildren().addAll(title, desc, new Separator(), densityLabel, densitySlider, speedLabel, speedSlider, gapLabel, gapSlider, new Separator(), resetBtn, perturbBtn, new Separator(), jamStat...` |
| 237 | `jamStatusLabel.setText("Status: JAMMED");` |
| 238 | `jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");` |
| 240 | `jamStatusLabel.setText("Status: Congested");` |
| 241 | `jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");` |
| 243 | `jamStatusLabel.setText("Status: Free Flow");` |
| 244 | `jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");` |
| 256 | `gc.setFill(Color.web("#f8f8f8"));` |
| 259 | `gc.setStroke(Color.web("#e0e0e0"));` |

## ArtsColorTheoryViewer.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\ui\viewers\arts\ArtsColorTheoryViewer.java`

| Line | Content |
| --- | --- |
| 106 | `.add(new Label(SocialI18n.getInstance().get("arts.color.label.comp")));` |
| 112 | `.add(new Label(SocialI18n.getInstance().get("arts.color.label.analog")));` |
| 118 | `.add(new Label(SocialI18n.getInstance().get("arts.color.label.triad")));` |
| 121 | `VBox main = new VBox(10, new Label(SocialI18n.getInstance().get("arts.color.label.hue")),` |
| 130 | `return String.format("#%02X%02X%02X",` |
| 138 | `return "Arts";` |
| 143 | `return SocialI18n.getInstance().get("ArtsColorTheory.title");` |
| 148 | `return SocialI18n.getInstance().get("ArtsColorTheory.desc");` |

## MapViewer.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\ui\viewers\geography\MapViewer.java`

| Line | Content |
| --- | --- |
| 41 | `this.setStyle("-fx-background-color: #e0f0ff;");` |
| 63 | `new Label("Active Map Backend: " + provider.get().getName()),` |
| 69 | `this.getChildren().add(new Label("No Map Backend Available (Install Unfolding, GeoTools, etc.)"));` |
| 75 | `return "Geography";` |
| 80 | `return "Map Viewer";` |
| 85 | `return "Displays geographic data.";` |

## TimelineViewer.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\ui\viewers\history\TimelineViewer.java`

| Line | Content |
| --- | --- |
| 40 | `this.setStyle("-fx-background-color: white;");` |
| 45 | `return "History";` |
| 50 | `return "Timeline Viewer";` |
| 55 | `return "Visualizes events on a timeline.";` |

## SportsResultsViewer.java (jscience-social)
`C:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java\org\jscience\ui\viewers\sports\SportsResultsViewer.java`

| Line | Content |
| --- | --- |
| 57 | `public String getName() { return i18n.get("sports.title", "Sports Results"); }` |
| 60 | `public String getCategory() { return "Sports"; }` |
| 68 | `Label headerVal = new Label(i18n.get("sports.header", "League Table"));` |
| 69 | `headerVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");` |
| 79 | `TableColumn<Team, Integer> rankCol = new TableColumn<>("#");` |
| 89 | `TableColumn<Team, String> nameCol = new TableColumn<>(i18n.get("sports.col.team", "Team"));` |
| 90 | `nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));` |
| 92 | `TableColumn<Team, Integer> playedCol = new TableColumn<>(i18n.get("sports.col.played", "Played"));` |
| 93 | `playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));` |
| 95 | `TableColumn<Team, Number> pointsCol = new TableColumn<>(i18n.get("sports.col.pts", "Points"));` |
| 97 | `pointsCol.setStyle("-fx-font-weight: bold;");` |
| 99 | `TableColumn<Team, String> avgCol = new TableColumn<>("Trend (SMA)");` |
| 104 | `VBox centerBox = new VBox(10, new Label(i18n.get("sports.label.standings", "Standings")), table);` |
| 113 | `homeBox.setPromptText(i18n.get("sports.form.home", "Home Team"));` |
| 117 | `awayBox.setPromptText(i18n.get("sports.form.away", "Away Team"));` |
| 121 | `hScore.setPromptText("Home");` |
| 124 | `aScore.setPromptText("Away");` |
| 126 | `HBox scoreBox = new HBox(10, hScore, new Label("-"), aScore);` |
| 129 | `Button addBtn = new Button(i18n.get("sports.button.add", "Add Match"));` |
| 140 | `Button simBtn = new Button(i18n.get("sports.button.simulate", "Simulate"));` |
| 147 | `rightPanel.getChildren().addAll(new Label(i18n.get("sports.form.title", "Add Match")), homeBox, awayBox, scoreBox, addBtn, simBtn, new Separator(), historyView);` |
| 154 | `teams.addAll(new Team("Man City"), new Team("Arsenal"), new Team("Liverpool"), new Team("Aston Villa"), new Team("Tottenham"));` |
| 159 | `matchHistory.add(0, home.name.get() + " " + hs + " - " + as + " " + away.name.get());` |
| 176 | `public final SimpleStringProperty trend = new SimpleStringProperty("--");` |
| 197 | `trend.set(String.format("%.1f", sma[sma.length - 1].doubleValue()));` |

## App.java (jscience-worker)
`C:\Silvere\Encours\Developpement\JScience\jscience-worker\src\main\java\org\App.java`

| Line | Content |
| --- | --- |
| 34 | `System.out.println( "Hello World!" );` |


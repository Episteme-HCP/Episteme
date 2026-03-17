package org.episteme.benchmarks.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

import java.util.ServiceLoader;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.natural.physics.classical.mechanics.collision.MechanicsBackend;

public class EnvironmentController {

    @FXML private TableView<ProviderItem> providerTable;
    @FXML private TableColumn<ProviderItem, String> typeColumn;
    @FXML private TableColumn<ProviderItem, String> nameColumn;
    @FXML private TableColumn<ProviderItem, String> statusColumn;
    @FXML private TableColumn<ProviderItem, String> classColumn;
    @FXML private Label systemInfoLabel;

    private final ObservableList<ProviderItem> providers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        classColumn.setCellValueFactory(data -> data.getValue().classNameProperty());

        providerTable.setItems(providers);
        providerTable.setPlaceholder(new Label("Loading providers..."));
        
        systemInfoLabel.setText("Java: " + System.getProperty("java.version") + " | OS: " + System.getProperty("os.name") + " | Arch: " + System.getProperty("os.arch"));

        refreshProviders();
    }

    @FXML private javafx.scene.control.Button refreshBtn;
    
    public void updateUI(java.util.ResourceBundle resources) {
        if (refreshBtn != null) refreshBtn.setText(resources.getString("btn.refresh"));
        if (typeColumn != null) typeColumn.setText(resources.getString("col.type"));
        if (nameColumn != null) nameColumn.setText(resources.getString("col.name"));
        if (statusColumn != null) statusColumn.setText(resources.getString("col.status"));
        if (classColumn != null) classColumn.setText(resources.getString("col.implementation"));
        if (providerTable != null) {
            String placeholderKey = providers.isEmpty() ? "msg.loading_providers" : "msg.no_providers";
            try {
                providerTable.setPlaceholder(new Label(resources.getString(placeholderKey)));
            } catch (Exception e) {
                providerTable.setPlaceholder(new Label("Loading..."));
            }
        }
    }

    @FXML
    private void handleRefresh() {
        NativeFFMLoader.clearCache();
        refreshProviders();
    }

    private Thread refreshThread = null;

    private void refreshProviders() {
        if (refreshThread != null && refreshThread.isAlive()) {
            return; // Already refreshing
        }
        
        providers.clear();
        refreshThread = new Thread(() -> {
            java.util.List<AlgorithmProvider> allProviders = new java.util.ArrayList<>();
            java.util.Set<String> seenClasses = new java.util.HashSet<>();
            
            // Safe discovery for each category
            safeDiscover(AlgorithmProvider.class, allProviders);
            
            try {
                ServiceLoader.load(org.episteme.core.technical.backend.Backend.class).forEach(b -> {
                    try {
                        allProviders.addAll(b.getAlgorithmProviders());
                    } catch (Throwable t) {
                        System.err.println("Error getting providers from backend " + b.getName() + ": " + t.getMessage());
                    }
                });
            } catch (Throwable t) { System.err.println("Error loading Backends: " + t.getMessage()); }

            safeDiscover(org.episteme.core.mathematics.linearalgebra.tensors.TensorBackend.class, allProviders);
            safeDiscover(org.episteme.core.media.VisionBackend.class, allProviders);
            safeDiscover(org.episteme.core.media.AudioBackend.class, allProviders);
            safeDiscover(MechanicsBackend.class, allProviders);

            if (allProviders.isEmpty()) {
                Platform.runLater(() -> providerTable.setPlaceholder(new Label("No providers found.")));
            }

            for (AlgorithmProvider p : allProviders) {
                try {
                    if (!seenClasses.add(p.getClass().getName())) {
                        continue; // Skip duplicates
                    }
                    String type = p.getAlgorithmType();
                    if (type != null && !type.isEmpty()) {
                        type = type.substring(0, 1).toUpperCase() + type.substring(1);
                    }
                    
                    final String capitalizedType = type;
                    String status = p.isAvailable() ? "Available" : "Unavailable";
                    ProviderItem item = new ProviderItem(
                        capitalizedType,
                        p.getName(),
                        status,
                        p.getClass().getName()
                    );
                    Platform.runLater(() -> providers.add(item));
                } catch (Throwable t) {
                    System.err.println("Error processing provider: " + t.getMessage());
                }
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.setName("ProviderDiscoveryThread");
        refreshThread.start();
    }

    private <T> void safeDiscover(Class<T> serviceClass, java.util.List<AlgorithmProvider> targetList) {
        try {
            ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
            java.util.Iterator<T> iterator = loader.iterator();
            while (true) {
                try {
                    if (!iterator.hasNext()) break;
                    T provider = iterator.next();
                    if (provider instanceof AlgorithmProvider) {
                        targetList.add((AlgorithmProvider) provider);
                    }
                } catch (Throwable t) {
                    System.err.println("Failed to load a provider for " + serviceClass.getSimpleName() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("Critical error initializing ServiceLoader for " + serviceClass.getSimpleName() + ": " + t.getMessage());
        }
    }
    
    public static class ProviderItem {
        private final javafx.beans.property.StringProperty type = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty name = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty status = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty className = new javafx.beans.property.SimpleStringProperty();
        
        public ProviderItem(String type, String name, String status, String className) {
            this.type.set(type);
            this.name.set(name);
            this.status.set(status);
            this.className.set(className);
        }
        
        public javafx.beans.property.StringProperty typeProperty() { return type; }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty statusProperty() { return status; }
        public javafx.beans.property.StringProperty classNameProperty() { return className; }
    }
}

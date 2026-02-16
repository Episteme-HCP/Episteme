package org.jscience.benchmarks.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

import java.util.ServiceLoader;

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
        
        systemInfoLabel.setText("Java: " + System.getProperty("java.version") + " | OS: " + System.getProperty("os.name") + " | Arch: " + System.getProperty("os.arch"));

        refreshProviders();
    }

    @FXML private javafx.scene.control.Button refreshBtn;
    
    public void updateUI(java.util.ResourceBundle resources) {
        if (refreshBtn != null) refreshBtn.setText(resources.getString("btn.refresh"));
        if (typeColumn != null) typeColumn.setText(resources.getString("col.type")); // Need key
        if (nameColumn != null) nameColumn.setText(resources.getString("col.name"));
        if (statusColumn != null) statusColumn.setText(resources.getString("col.status"));
        if (classColumn != null) classColumn.setText(resources.getString("col.implementation")); // Need key? Or use col.result?
    }

    @FXML
    private void handleRefresh() {
        refreshProviders();
    }

    private void refreshProviders() {
        providers.clear();
        new Thread(() -> {
            ServiceLoader<AlgorithmProvider> loader = ServiceLoader.load(AlgorithmProvider.class);
            for (AlgorithmProvider p : loader) {
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
            }
        }).start();
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

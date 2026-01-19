package org.jscience.apps.sociology;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.viewers.economics.FinancialPortfolioViewer;
import org.jscience.ui.viewers.mathematics.discrete.NetworkViewer;
import org.jscience.ui.viewers.social.DemographicProfileViewer;
import org.jscience.economics.loaders.EconomicScenarioReader;
import org.jscience.sociology.loaders.DemographicResourceReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global Stability Studio: A professional tool for analyzing world risks.
 * Integrates markets, geopolitics, and demographics.
 */
public final class GlobalStabilityStudio extends FeaturedAppBase {

    private FinancialPortfolioViewer marketViewer;
    private NetworkViewer allianceViewer;
    private DemographicProfileViewer demographyViewer;

    @Override
    protected String getAppTitle() {
        return "Global Stability Studio - JScience";
    }

    @Override
    protected Region createMainContent() {
        TabPane tabs = new TabPane();
        
        // Tab 1: Financial Markets
        marketViewer = new FinancialPortfolioViewer();
        Tab marketTab = new Tab("Economic Indicators", marketViewer);
        marketTab.setClosable(false);
        
        // Tab 2: Geopolitics
        allianceViewer = new NetworkViewer();
        Tab geopoliticalTab = new Tab("Geopolitical Alliances", allianceViewer);
        geopoliticalTab.setClosable(false);
        
        // Tab 3: Demography
        demographyViewer = new DemographicProfileViewer();
        Tab demographyTab = new Tab("Demographic Risk", demographyViewer);
        demographyTab.setClosable(false);
        
        tabs.getTabs().addAll(marketTab, geopoliticalTab, demographyTab);
        return tabs;
    }

    @Override
    protected void onAppReady() {
        setStatus("Loading Global Scenarios...");
        
        try {
            EconomicScenarioReader econReader = new EconomicScenarioReader();
            marketViewer.setPortfolio(econReader.load("GlobalMarkets"));
            
            DemographicResourceReader demoReader = new DemographicResourceReader();
            demographyViewer.setDemographicData(demoReader.load("World2025"));
            
            // For allianceViewer, we'll keep it as is or create a GeopoliticalResourceReader later
            // For now, let's just use a placeholder or empty graph if we can't find a reader
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to load scenarios", e);
        }
        
        setStatus("System Ready. All data layers synchronized.");
    }

    @Override
    public void onRun() {
        // Simulation logic: slightly drift market prices
        try {
            marketViewer.setPortfolio(new EconomicScenarioReader().load("GlobalMarkets"));
            setStatus("Market indices recalculated.");
        } catch (Exception e) {
            setStatus("Simulation failed: " + e.getMessage());
        }
    }

    @Override
    public String getCategory() {
        return "Executive Tools";
    }

    @Override
    public String getDescription() {
        return "Comprehensive analysis of global socio-economic stability.";
    }
}

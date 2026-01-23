package org.jscience.ui.viewers.sociology;

import org.jscience.ui.viewers.mathematics.discrete.NetworkViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import java.util.List;

/**
 * Specialized Network Viewer for Social Dynamics.
 * Visualizes genealogies, political alliances, and opinion interaction graphs.
 */
public final class NetworkDynamicsViewer extends NetworkViewer {

    public NetworkDynamicsViewer() {
        super();
        getStyleClass().add("network-dynamics-viewer");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = super.getViewerParameters();
        params.add(new RealParameter(
            I18n.getInstance().get("viewer.social.opinion_threshold", "Opinion Influence Threshold"),
            "Minimum relation strength to show interaction", 0.0, 1.0, 0.05, 0.5, val -> {
                // Future: add filtering logic to renderer
            }));
        return params;
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.social", "Social Sciences"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.social.network.name", "Network Dynamics Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.social.network.desc", "Universal graph motor for opinion and social relation networks."); }
}

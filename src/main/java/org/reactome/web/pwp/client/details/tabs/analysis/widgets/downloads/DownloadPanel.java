package org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;

/**
 * A panel that displays all the available download options
 * related to the performed analysis
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DownloadPanel extends DockLayoutPanel {

    private FlowPanel main;

    private String species;

    public DownloadPanel() {
        super(Style.Unit.EM);
        main = new FlowPanel();
        main.setStyleName(AnalysisTabStyleFactory.RESOURCES.css().downloadContainer());
        add(main);
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void showDownloadOptions(final AnalysisResult analysisResult, final String resource) {
        main.clear();

        String token = analysisResult.getSummary().getToken();
        for(AnalysisDownloadType type : AnalysisDownloadType.values()) {
            main.add(new AnalysisDownloadItem(type, token, resource, species));
        }
    }
}

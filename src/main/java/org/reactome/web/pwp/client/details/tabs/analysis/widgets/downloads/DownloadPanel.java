package org.reactome.web.pwp.client.details.tabs.analysis.widgets.downloads;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Report;

import java.util.List;

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
        boolean isGsa = analysisResult.getSummary().getGsaToken() != null && !analysisResult.getSummary().getGsaToken().isEmpty(); //true
        for (AnalysisDownloadType type : AnalysisDownloadType.values()) {
            if (type.equals(AnalysisDownloadType.GSA_REPORT)) continue; // skip it.
            // skip traditional PDF report when GSA has been performed. GSA Reports will be added #showGsaReportOptions
            if(type.equals(AnalysisDownloadType.PDF_REPORT) && isGsa) continue;
            main.add(new AnalysisDownloadItem(type, token, resource, species));
        }
    }

    public void showGsaReportOptions(List<Report> reportList, String gsaToken) {
        if (reportList == null || reportList.isEmpty()) return;
        for (Report report : reportList) {
            // insert GSA report before the default
            main.insert(new GSAAnalysisDownloadItem(report, gsaToken), 0);
        }
    }
}

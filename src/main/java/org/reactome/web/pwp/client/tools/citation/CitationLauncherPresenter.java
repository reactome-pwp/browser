package org.reactome.web.pwp.client.tools.citation;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.events.BrowserReadyEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.events.ToolSelectedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class CitationLauncherPresenter extends AbstractPresenter implements CitationLauncher.Presenter, BrowserReadyHandler, AnalysisCompletedHandler {

    private CitationLauncher.Display display;
    private static final Map<String, String> CITATIONS;
    private static final String PATHWAY_ANALYSIS_CITATION_ID = "28249561";
    private static final String PATHWAY_GSA_ANALYSIS_CITATION_ID = "32907876";
    private static final String DIAGRAM_VIEWER_CITATION_ID = "29186351";
    private String analysisType;


    static {
        Map<String, String> citations = new HashMap<>();
        citations.put("28249561", "Fabregat A, Sidiropoulos K, Viteri G, Forner O, Marin-Garcia P, Arnau V, D'Eustachio P, Stein L, Hermjakob H. Reactome pathway analysis: a high-performance in-memory approach. BMC Bioinformatics. 2017 Mar 2;18(1):142. doi: 10.1186/s12859-017-1559-2. PubMed PMID: 28249561");
        citations.put("32907876", "Griss J, Viteri G, Sidiropoulos K, Nguyen V, Fabregat A, Hermjakob H. ReactomeGSA - Efficient Multi-Omics Comparative Pathway Analysis. Mol Cell Proteomics. 2020 Dec;19(12):2115-2125. doi: 10.1074/mcp.TIR120.002155. Epub 2020 Sep 9. PMID: 32907876; PMCID: PMC7710148.");
        citations.put("29186351", "Fabregat A, Sidiropoulos K, Viteri G, Marin-Garcia P, Ping P, Stein L, D'Eustachio P, Hermjakob H. Reactome diagram viewer: data structures and strategies to boost performance. Bioinformatics. 2018 Apr 1;34(7):1208-1214. doi: 10.1093/bioinformatics/btx752. PubMed PMID: 29186351");
        CITATIONS = Collections.unmodifiableMap(citations);
    }


    public CitationLauncherPresenter(EventBus eventBus, CitationLauncher.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.eventBus.addHandler(BrowserReadyEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisCompletedEvent.TYPE, this);
    }

    @Override
    public void displayClosed() {
        this.eventBus.fireEventFromSource(new ToolSelectedEvent(PathwayPortalTool.NONE), this);
    }

    @Override
    public void onBrowserReady(BrowserReadyEvent event) {
        // do your stuff
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        PathwayPortalTool tool = event.getState().getTool();
        if (tool.equals(PathwayPortalTool.CITATION)) {
            if (event.getState().getAnalysisStatus().getToken() != null) {
                if (analysisType.equals("GSA_REGULATION")) {
                    getCitation(PATHWAY_GSA_ANALYSIS_CITATION_ID);
                } else {
                    getCitation(PATHWAY_ANALYSIS_CITATION_ID);
                }
            } else getCitation(DIAGRAM_VIEWER_CITATION_ID);
        } else {
            display.hide();
        }
    }

    private void showDisplay() {
        display.show();
        display.center();
    }

    private void getCitation(String id) {
        ContentClient.getStaticCitation(id, new ContentClientHandler.Citation() {
            @Override
            public void onCitationTextLoaded(String citation) {
                // happy flow
                display.setCitation(citation);
                // call method for including the export buttons on display
                display.setExportBar(id);
                showDisplay();
            }

            @Override
            public void onContentClientException(Type type, String message) {
                display.setCitation(CITATIONS.get(id));
                showDisplay();
                //TODO add code for showing banner
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                display.setCitation(CITATIONS.get(id));
                showDisplay();
                //TODO add code for showing banner
            }
        });
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        analysisType = event.getAnalysisResult().getSummary().getType();
    }
}



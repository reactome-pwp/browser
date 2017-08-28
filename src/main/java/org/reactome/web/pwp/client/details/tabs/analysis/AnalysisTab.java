package org.reactome.web.pwp.client.details.tabs.analysis;

import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisTab {

    interface Presenter extends DetailsTab.Presenter {
        void onResourceSelected(ResourceChangedEvent event);
        void onPathwayHovered(Long dbId);
        void onPathwayHoveredReset();
        void onPathwaySelected(Long dbId);
    }

    interface Display extends DetailsTab.Display<Presenter>{
        void clearSelection();
        void selectPathway(Pathway pathway);
        void showResult(AnalysisResult analysisResult, String resource);
    }
}

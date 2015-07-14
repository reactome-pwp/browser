package org.reactome.web.pwp.client.details.tabs.analysis;

import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisTab {

    interface Presenter extends DetailsTab.Presenter {
        void onResourceSelected(String resource);
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

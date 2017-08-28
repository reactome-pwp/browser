package org.reactome.web.pwp.client.viewport.fireworks;

import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Fireworks {

    interface Presenter extends BrowserModule.Presenter {
        void selectPathway(Long dbId);
        void resetPathwaySelection();
        void highlightPathway(Long dbId);
        void profileChanged(String profileName);
        void resetAnalysis();
        void resetFlag();
        void resetPathwayHighlighting();
        void showPathwayDiagram(Long dbId);
    }

    interface Display extends BrowserModule.Display {
        boolean isVisible();

        void setPresenter(Presenter presenter);

        void loadSpeciesFireworks(String speciesJson);

        void flag(String flag);

        void highlightPathway(Pathway pathway);

        void resetHighlight();

        void openPathway(Pathway pathway);

        void setAnalysisToken(AnalysisStatus analysisStatus);

        void selectPathway(Pathway pathway);
        void resetSelection();

        void resetView();

        void setVisible(boolean visible);
    }
}

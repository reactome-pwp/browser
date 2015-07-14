package org.reactome.web.pwp.client.viewport.diagram;

import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Diagram {

    interface Presenter extends BrowserModule.Presenter {
        void analysisReset();

        void databaseObjectSelected(Long dbId);

        void databaseObjectHovered(Long dbId);

        void diagramLoaded(Long dbId);

        void fireworksOpened(Long dbId);
    }

    interface Display extends BrowserModule.Display {
        boolean isVisible();

        void loadPathway(Pathway pathway);

        void highlight(DatabaseObject databaseObject);

        void select(DatabaseObject databaseObject);

        void setAnalysisToken(AnalysisStatus analysisStatus);

        void setPresenter(Presenter presenter);
    }
}

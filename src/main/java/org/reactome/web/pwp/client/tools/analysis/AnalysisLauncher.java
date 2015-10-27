package org.reactome.web.pwp.client.tools.analysis;

import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.model.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisLauncher {

    interface Presenter extends BrowserModule.Presenter {
        void displayClosed();
        void analysisCompleted(AnalysisCompletedEvent event);
        void analysisError(AnalysisErrorEvent event);
    }

    interface Display extends BrowserModule.Display {
        void hide();
        void center();
        void show();
        void setPresenter(Presenter presenter);
        void setSpeciesList(List<Species> speciesList);
    }
}

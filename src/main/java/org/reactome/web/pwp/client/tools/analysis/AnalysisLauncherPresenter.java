package org.reactome.web.pwp.client.tools.analysis;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.PathwayPortalTool;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.model.client.classes.Species;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisLauncherPresenter extends AbstractPresenter implements AnalysisLauncher.Presenter, BrowserReadyHandler {

    private AnalysisLauncher.Display display;

    private List<Species> speciesList;

    public AnalysisLauncherPresenter(EventBus eventBus, AnalysisLauncher.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(BrowserReadyEvent.TYPE, this);
    }

    @Override
    public void displayClosed() {
        this.eventBus.fireEventFromSource(new ToolSelectedEvent(PathwayPortalTool.NONE), this);
    }

    @Override
    public void analysisCompleted(AnalysisCompletedEvent event) {
        this.display.hide();
        this.eventBus.fireEventFromSource(event, this);
    }

    @Override
    public void onBrowserReady(BrowserReadyEvent event) {
        retrieveSpeciesList();
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        PathwayPortalTool tool = event.getState().getTool();
        if (tool.equals(PathwayPortalTool.ANALYSIS)) {
            display.show();
            display.center();
        } else {
            display.hide();
        }
    }

    private void retrieveSpeciesList() {
        ContentClient.getSpeciesList(new ContentClientHandler.ObjectListLoaded<Species>() {
            @Override
            public void onObjectListLoaded(List<Species> list) {
                display.setSpeciesList(list);
            }

            @Override
            public void onContentClientException(Type type, String message) {
                display.setSpeciesList(new LinkedList<>());
                eventBus.fireEventFromSource(new ErrorMessageEvent(message), this);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                display.setSpeciesList(new LinkedList<>());
                //TODO
                eventBus.fireEventFromSource(new ErrorMessageEvent(error.getMessage().get(0)), this);
            }
        });
    }
}

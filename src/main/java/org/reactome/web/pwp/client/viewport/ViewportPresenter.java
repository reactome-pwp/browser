package org.reactome.web.pwp.client.viewport;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.AppConfig;
import org.reactome.web.pwp.client.common.events.FireworksOpenedEvent;
import org.reactome.web.pwp.client.common.events.PathwayDiagramOpenedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.events.ViewportChangedEvent;
import org.reactome.web.pwp.client.common.handlers.FireworksOpenedHandler;
import org.reactome.web.pwp.client.common.handlers.PathwayDiagramOpenedHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ViewportPresenter extends AbstractPresenter implements Viewport.Presenter, PathwayDiagramOpenedHandler, FireworksOpenedHandler {

    private Viewport.Display display;
    private ViewportToolType currentViewportTool = ViewportToolType.FIREWORKS;

    public ViewportPresenter(EventBus eventBus, Viewport.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(PathwayDiagramOpenedEvent.TYPE, this);
        this.eventBus.addHandler(FireworksOpenedEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        if(event.getState().getPathway()==null){
            if(currentViewportTool.equals(ViewportToolType.FIREWORKS)) return;
            if(AppConfig.getIsCurator()) {
                display.showWelcome();
            } else {
                display.showFireworks();
                Scheduler.get().scheduleDeferred(() -> {
                    currentViewportTool = ViewportToolType.FIREWORKS;
                    eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.FIREWORKS), ViewportPresenter.this);
                });
            }
        } else if(AppConfig.getIsCurator()) {
            //This needs to be done here because Fireworks is not present when the curator pathway browser is shown
            //so this event will never happen (and then the diagram will never be open)
            eventBus.fireEventFromSource(new PathwayDiagramOpenedEvent(event.getState().getPathway()), this);
        }
    }

    @Override
    public void onPathwayDiagramOpened(PathwayDiagramOpenedEvent event) {
        Scheduler.get().scheduleDeferred(() -> {
            currentViewportTool = ViewportToolType.DIAGRAM;
            eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.DIAGRAM), ViewportPresenter.this);
            display.showDiagram(); //Moved here so the user doesn't see the previous diagram before loading the new one
        });
    }

    @Override
    public void onFireworksOpened(FireworksOpenedEvent event) {
        display.showFireworks();
        Scheduler.get().scheduleDeferred(() -> {
            currentViewportTool = ViewportToolType.FIREWORKS;
            eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.FIREWORKS), ViewportPresenter.this);
        });
    }
}

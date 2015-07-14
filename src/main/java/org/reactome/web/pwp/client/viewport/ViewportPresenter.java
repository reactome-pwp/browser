package org.reactome.web.pwp.client.viewport;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
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
            display.showFireworks();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.FIREWORKS), ViewportPresenter.this);
                }
            });
        }
    }

    @Override
    public void onPathwayDiagramOpened(PathwayDiagramOpenedEvent event) {
        display.showDiagram();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.DIAGRAM), ViewportPresenter.this);
            }
        });
    }

    @Override
    public void onFireworksOpened(FireworksOpenedEvent event) {
        display.showFireworks();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.fireEventFromSource(new ViewportChangedEvent(ViewportToolType.FIREWORKS), ViewportPresenter.this);
            }
        });
    }
}

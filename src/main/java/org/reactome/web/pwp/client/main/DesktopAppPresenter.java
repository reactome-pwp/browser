package org.reactome.web.pwp.client.main;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.LayoutSelectorChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.LayoutSelectorChangedHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DesktopAppPresenter extends AbstractPresenter implements DesktopApp.Presenter, LayoutSelectorChangedHandler {

    private DesktopApp.Display display;

    public DesktopAppPresenter(EventBus eventBus, DesktopApp.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(LayoutSelectorChangedEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {

    }

    @Override
    public void onLayoutSelectorChanged(LayoutSelectorChangedEvent event) {
        switch (event.getType()){
            case HIERARCHY:
                this.display.toggleHierarchy();
                break;
            case DETAILS:
                this.display.toggleDetails();
                break;
            case VIEWPORT:
                this.display.toggleViewport();
                break;
        }
    }
}

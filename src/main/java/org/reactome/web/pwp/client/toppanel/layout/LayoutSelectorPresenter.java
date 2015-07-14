package org.reactome.web.pwp.client.toppanel.layout;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.LayoutSelectorChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LayoutSelectorPresenter extends AbstractPresenter implements LayoutSelector.Presenter {

    private LayoutSelector.Display display;

    public LayoutSelectorPresenter(EventBus eventBus, LayoutSelector.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        //Nothing to do here
    }

    @Override
    public void layoutSelectorChanged(LayoutSelectorType selector) {
        this.eventBus.fireEventFromSource(new LayoutSelectorChangedEvent(selector), this);
    }
}

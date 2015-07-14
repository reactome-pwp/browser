package org.reactome.web.pwp.client.common.module;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AbstractPresenter implements BrowserModule.Presenter {

    protected EventBus eventBus;

    public AbstractPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
    }
}

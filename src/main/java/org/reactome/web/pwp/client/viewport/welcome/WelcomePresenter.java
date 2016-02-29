package org.reactome.web.pwp.client.viewport.welcome;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class WelcomePresenter extends AbstractPresenter implements Welcome.Presenter {

    private Welcome.Display display;

    public WelcomePresenter(EventBus eventBus, Welcome.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {

    }
}

package org.reactome.web.pwp.client.messages;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.ErrorMessageHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MessagesPresenter extends AbstractPresenter implements Messages.Presenter,
        ErrorMessageHandler {

    private Messages.Display display;

    public MessagesPresenter(EventBus eventBus, Messages.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);

        this.eventBus.addHandler(ErrorMessageEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) { /*Nothing here*/ }

    @Override
    public void onInternalMessage(ErrorMessageEvent event) {
        this.display.showErrorMessage(event.getMessage());
    }
}

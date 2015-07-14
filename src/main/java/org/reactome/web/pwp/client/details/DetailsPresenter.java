package org.reactome.web.pwp.client.details;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.State;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsPresenter extends AbstractPresenter implements Details.Presenter {

    private Details.Display display;
    private DetailsTabType currentTab;

    public DetailsPresenter(EventBus eventBus, Details.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.currentTab = DetailsTabType.getDefault();
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        if(!state.getDetailsTab().equals(this.currentTab)){
            this.currentTab = state.getDetailsTab();
            this.display.setTabVisible(this.currentTab);
        }
    }

    @Override
    public void tabChanged(DetailsTabType tabType) {
        if(!tabType.equals(this.currentTab)) {
            this.eventBus.fireEventFromSource(new DetailsTabChangedEvent(tabType), this);
        }
    }
}

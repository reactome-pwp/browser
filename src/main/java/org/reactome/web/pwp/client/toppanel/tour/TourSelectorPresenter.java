package org.reactome.web.pwp.client.toppanel.tour;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public class TourSelectorPresenter extends AbstractPresenter implements TourSelector.Presenter {

    public TourSelectorPresenter(EventBus eventBus, TourSelector.Display display) {
        super(eventBus);
        display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        //Nothing to do here
    }

    @Override
    public void tour() {
        TourContainer tour = new TourContainer();
//        tour.show();
//        this.eventBus.fireEventFromSource(new TourSelectedEvent(), this);
    }
}

package org.reactome.web.pwp.client.toppanel.tour;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public interface TourSelector {

    interface Presenter extends BrowserModule.Presenter {
        void tour();
    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);
    }
}

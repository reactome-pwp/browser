package org.reactome.web.pwp.client.details;

import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Details {

    interface Presenter extends BrowserModule.Presenter {
        void tabChanged(DetailsTabType tabType);
    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);
        void setTabVisible(DetailsTabType tabType);
    }
}

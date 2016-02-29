package org.reactome.web.pwp.client.viewport.welcome;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Welcome {

    interface Presenter extends BrowserModule.Presenter {

    }

    interface Display extends BrowserModule.Display {
        boolean isVisible();
        void setPresenter(Presenter presenter);
        void setVisible(boolean visible);
    }
}

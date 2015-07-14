package org.reactome.web.pwp.client.main;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DesktopApp {

    interface Presenter extends BrowserModule.Presenter {

    }

    interface Display extends BrowserModule.Display {
        void toggleDetails();

        void toggleHierarchy();

        void toggleViewport();

        void setPresenter(Presenter presenter);
    }
}

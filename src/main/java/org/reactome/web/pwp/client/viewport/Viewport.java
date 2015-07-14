package org.reactome.web.pwp.client.viewport;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Viewport {

    interface Presenter extends BrowserModule.Presenter {

    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);

        void showDiagram();

        void showFireworks();
    }
}

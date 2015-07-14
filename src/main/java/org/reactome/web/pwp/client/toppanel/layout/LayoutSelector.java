package org.reactome.web.pwp.client.toppanel.layout;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface LayoutSelector {

    interface Presenter extends BrowserModule.Presenter {
        void layoutSelectorChanged(LayoutSelectorType selector);
    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);
    }
}

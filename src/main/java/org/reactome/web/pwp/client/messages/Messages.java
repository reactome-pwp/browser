package org.reactome.web.pwp.client.messages;

import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Messages {

    interface Presenter extends BrowserModule.Presenter {
    }

    interface Display extends BrowserModule.Display {
        void showErrorMessage(String errorMsg);

        void setPresenter(Presenter presenter);
    }
}

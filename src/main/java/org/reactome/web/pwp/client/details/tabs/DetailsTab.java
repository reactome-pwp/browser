package org.reactome.web.pwp.client.details.tabs;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DetailsTab {

    interface Presenter extends BrowserModule.Presenter {

    }

    interface Display<P extends Presenter> extends BrowserModule.Display {

        DetailsTabType getDetailTabType();

        Widget getTitleContainer();

        void setInitialState();

        void setPresenter(P presenter);

        void showLoadingMessage();

        void showErrorMessage(String message);
    }
}
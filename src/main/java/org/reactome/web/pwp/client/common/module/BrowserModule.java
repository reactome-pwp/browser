package org.reactome.web.pwp.client.common.module;

import com.google.gwt.user.client.ui.IsWidget;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface BrowserModule {

    interface Display extends IsWidget {}

    interface Presenter extends StateChangedHandler {}

    interface Manager {}

}

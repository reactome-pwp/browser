package org.reactome.web.pwp.client.manager.title.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TitleChangedHandler extends EventHandler {

    void onTitleChanged(TitleChangedEvent event);

}

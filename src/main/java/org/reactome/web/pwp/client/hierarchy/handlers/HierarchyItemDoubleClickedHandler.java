package org.reactome.web.pwp.client.hierarchy.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemDoubleClickedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface HierarchyItemDoubleClickedHandler extends EventHandler {

    void onHierarchyItemDoubleClicked(HierarchyItemDoubleClickedEvent e);
}

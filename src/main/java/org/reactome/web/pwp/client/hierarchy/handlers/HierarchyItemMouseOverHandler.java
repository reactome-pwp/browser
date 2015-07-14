package org.reactome.web.pwp.client.hierarchy.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface HierarchyItemMouseOverHandler extends EventHandler {

    void onHierarchyItemMouseOver(HierarchyItemMouseOverEvent e);
}

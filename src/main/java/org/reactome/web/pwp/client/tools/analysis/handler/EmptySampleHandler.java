package org.reactome.web.pwp.client.tools.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.event.EmptySampleEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface EmptySampleHandler extends EventHandler {

    void onEmptySample(EmptySampleEvent event);

}

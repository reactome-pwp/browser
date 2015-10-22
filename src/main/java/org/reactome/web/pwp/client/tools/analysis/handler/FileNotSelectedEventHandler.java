package org.reactome.web.pwp.client.tools.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.event.FileNotSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface FileNotSelectedEventHandler extends EventHandler {
    void onFileNotSelectedEvent(FileNotSelectedEvent event);
}

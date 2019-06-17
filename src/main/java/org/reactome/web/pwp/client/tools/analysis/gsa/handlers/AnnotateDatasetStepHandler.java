package org.reactome.web.pwp.client.tools.analysis.gsa.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.events.AnnotateDatasetStepEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@Deprecated
public interface AnnotateDatasetStepHandler extends EventHandler {
    void onAnnotateStepSelected(AnnotateDatasetStepEvent event);
}

package org.reactome.web.pwp.client.tools.analysis.gsa.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.handlers.AnnotateDatasetStepHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@Deprecated
public class AnnotateDatasetStepEvent extends GwtEvent<AnnotateDatasetStepHandler> {
    public static final Type<AnnotateDatasetStepHandler> TYPE = new Type<>();

    private GSADataset dataset;

    public AnnotateDatasetStepEvent(GSADataset dataset) {
        this.dataset = dataset;
    }

    @Override
    protected void dispatch(AnnotateDatasetStepHandler handler) {
        handler.onAnnotateStepSelected(this);
    }

    @Override
    public Type<AnnotateDatasetStepHandler> getAssociatedType() {
        return TYPE;
    }

    public GSADataset getDataset() {
        return dataset;
    }

    @Override
    public String toString() {
        return "AnnotateDatasetStepEvent{" +
                "dataset=" + dataset +
                '}';
    }
}

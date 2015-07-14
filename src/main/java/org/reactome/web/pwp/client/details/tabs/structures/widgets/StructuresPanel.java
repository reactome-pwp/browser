package org.reactome.web.pwp.client.details.tabs.structures.widgets;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.pwp.client.details.tabs.structures.handlers.StructureLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class StructuresPanel<T> extends Composite implements ProvidesResize, HasHandlers {
    protected VerticalPanel container;

    protected Integer structuresRequired = 0;
    protected Integer structuresLoaded = 0;

    public StructuresPanel() {
        this.container = new VerticalPanel();
        this.container.setWidth("99%");
        this.container.add(DisclosurePanelFactory.getLoadingMessage());

        this.initWidget(new ScrollPanel(this.container));
        this.setStyleName("elv-Details-OverviewPanel");
    }

    public abstract void add(T element);

    public HandlerRegistration addStructureLoadedHandler(StructureLoadedHandler handler){
        return addHandler(handler, StructureLoadedEvent.TYPE);
    }

    public final Integer getNumberOfRequiredStructures() {
        return structuresRequired;
    }

    public final Integer getNumberOfLoadedStructures() {
        return structuresLoaded;
    }

    public abstract void setEmpty();
}
package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.events.FilterRemovedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.handlers.FilterRemovedHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AppliedFiltersPanel extends FlowPanel {

    private Label title;
    private FlowPanel container;
    private Filter filter;


    public AppliedFiltersPanel() {
        setStyleName(RESOURCES.getCSS().main());

        title = new Label("Filters applied:");
        title.setStyleName(RESOURCES.getCSS().title());

        container = new FlowPanel();
        container.setStyleName(RESOURCES.getCSS().container());

        add(title);
        add(container);
    }

    public HandlerRegistration addFilterRemovedHandler(FilterRemovedHandler handler) {
        return this.addHandler(handler, FilterRemovedEvent.TYPE);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        if (filter == null || filter.getAppliedFilters().isEmpty()) {
            setVisible(false);
            container.clear();
        } else {
            container.clear();
            for(Filter.Type f : filter.getAppliedFilters()) {
                container.add(getChip(f));
            }
        }
    }

    private Widget getChip(Filter.Type filterType) {
        FlowPanel chip = new FlowPanel();
        chip.setStyleName(RESOURCES.getCSS().chip());
        chip.getElement().getStyle().setBackgroundColor(filterType.chipColour());
        chip.add(new Label(filterType.displayName()));

        IconButton btn = new IconButton(RESOURCES.cancelIcon(), RESOURCES.getCSS().removeBtn(), "remove this filter", e -> {
            filter.removeFilter(filterType);
            chip.removeFromParent();
            setVisible(container.getWidgetCount() > 0);
            fireEvent(new FilterRemovedEvent(filterType));
        });

        chip.add(btn);

        return chip;
    }



    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {

        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../images/cancel.png")
        ImageResource cancelIcon();

    }

    @CssResource.ImportedWithPrefix("pwp-AppliedFiltersPanel")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/pwp/client/details/tabs/analysis/widgets/filtering/AppliedFiltersPanel.css";

        String main();

        String title();

        String container();

        String chip();

        String removeBtn();

    }
}

package org.reactome.web.pwp.client.toppanel.tour;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourSelectorDisplay extends Composite implements TourSelector.Display, ClickHandler {

    private TourSelector.Presenter presenter;

    public TourSelectorDisplay() {
        IconButton tourBtn = new IconButton("", RESOURCES.tourIcon());
        tourBtn.setTitle("Starts the tour...");
        tourBtn.setStyleName(RESOURCES.getCSS().tourBtn());
        tourBtn.addClickHandler(this);

        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName(RESOURCES.getCSS().layoutPanel());
        flowPanel.add(new SimplePanel(new InlineLabel("Tour:")));
        flowPanel.add(tourBtn);

        initWidget(flowPanel);
    }

    @Override
    public void setPresenter(TourSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(ClickEvent event) {
        this.presenter.tour();
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResoruceCSS.CSS)
        ResoruceCSS getCSS();

        @Source("images/tour.png")
        ImageResource tourIcon();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-TourSelector")
    public interface ResoruceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/toppanel/tour/Tour.css";

        String layoutPanel();

        String tourBtn();
    }
}

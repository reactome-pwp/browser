package org.reactome.web.pwp.client.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.AppController;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsDisplay extends ResizeComposite implements Details.Display, SelectionHandler<Integer> {

    private Details.Presenter presenter;

    private TabLayoutPanel tabPanel;
    private List<Widget> tabs = new LinkedList<>();

    private Map<DetailsTabType, Integer> indexMap = new HashMap<>();

    public DetailsDisplay() {
        this.tabPanel = new TabLayoutPanel(30, Style.Unit.PX);
        this.tabPanel.setAnimationDuration(500);
        this.tabPanel.addStyleName("elv-Details-Content");
        initWidget(this.tabPanel);

        for (DetailsTab.Display detailsTab : AppController.DETAILS_TABS) {
            Widget title = detailsTab.getTitleContainer();

            this.tabPanel.add(detailsTab, title);
            indexMap.put(detailsTab.getDetailTabType(), this.tabPanel.getWidgetCount() - 1);

            tabs.add(title.getParent());
            title.getParent().addStyleName(RESOURCES.getCSS().detailsTab());
        }

        tabPanel.selectTab(DetailsTabType.getDefaultIndex());
        tabs.get(DetailsTabType.getDefaultIndex()).addStyleName(RESOURCES.getCSS().detailsTabSelected());
        tabPanel.addSelectionHandler(this);
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) {
        for (Widget tab : this.tabs) {
            tab.removeStyleName(RESOURCES.getCSS().detailsTabSelected());
        }
        tabs.get(this.tabPanel.getSelectedIndex()).addStyleName(RESOURCES.getCSS().detailsTabSelected());

        for (DetailsTabType tabType : indexMap.keySet()) {
            if(indexMap.get(tabType).equals(this.tabPanel.getSelectedIndex())){
                presenter.tabChanged(tabType);
                return;
            }
        }
        Console.error("Oops! Problems notifying the selected tab :(", this);
    }

    @Override
    public void setPresenter(Details.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTabVisible(DetailsTabType tabType) {
        if(indexMap.containsKey(tabType)){
            this.tabPanel.selectTab(indexMap.get(tabType));
        }else{
            this.tabPanel.selectTab(0);
            Console.error("Ops! Problems selecting the specified tab", this);
        }
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
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-Details")
    public interface ResoruceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/DetailsTab.css";

        String detailsTab();

        String detailsTabSelected();

        String initialStateMessage();

    }
}

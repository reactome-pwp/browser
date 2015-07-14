package org.reactome.web.pwp.client.main;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DesktopAppDisplay extends DockLayoutPanel implements DesktopApp.Display, DesktopSplitPanel.WestPanelResizedHandler, DesktopSplitPanel.SouthPanelResizedHandler {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private DesktopApp.Presenter presenter;

    private final String HIERARCHY_WIDTH_COOKIE = "pwp-HierarchyPanelWidth";
    private final String DETAILS_HEIGHT_COOKIE = "pwp-DetailsPanelHeight";

    private static int HIERARCHY_WIDTH_STD = 275;

    private static int DETAILS_HEIGHT_STD = 225;

    private IsWidget hierarchy;
    private IsWidget details;
    private DesktopSplitPanel slp;

    public DesktopAppDisplay(IsWidget topPanel, final IsWidget hierarchy, final IsWidget details, IsWidget viewport) {
        super(Style.Unit.PX);

        int hierarchyWidth = getCookieValue(HIERARCHY_WIDTH_COOKIE, HIERARCHY_WIDTH_STD);
        int detailsHeight = getCookieValue(DETAILS_HEIGHT_COOKIE, DETAILS_HEIGHT_STD);

        this.hierarchy = hierarchy;
        this.details = details;

        this.slp = new DesktopSplitPanel();
        this.slp.addWestPanelResizedHandler(this);
        this.slp.addSouthPanelResizedHandler(this);
        this.slp.addWest(hierarchy, hierarchyWidth);
        this.slp.addSouth(details, detailsHeight);
        this.slp.add(viewport);

        this.addNorth(topPanel, 45);
        this.add(this.slp);
    }

    @Override
    public void onSouthPanelResized(int size) {
        this.setCookieValue(DETAILS_HEIGHT_COOKIE, size);
    }

    @Override
    public void onWestPanelResized(int size) {
        this.setCookieValue(HIERARCHY_WIDTH_COOKIE, size);
    }

    @Override
    public void toggleDetails() {
        Double size = this.slp.getWidgetSize(this.details.asWidget());
        this.slp.setWidgetSize(this.details.asWidget(), size == 0 ? DETAILS_HEIGHT_STD : 0);
        this.slp.forceLayout();
    }

    @Override
    public void toggleHierarchy() {
        Double size = this.slp.getWidgetSize(this.hierarchy.asWidget());
        this.slp.setWidgetSize(this.hierarchy.asWidget(), size == 0 ? HIERARCHY_WIDTH_STD : 0);
        this.slp.forceLayout();
    }

    @Override
    public void toggleViewport() {
        Double detailsSize = this.slp.getWidgetSize(this.details.asWidget());
        Double hierarchySize = this.slp.getWidgetSize(this.hierarchy.asWidget());
        if (detailsSize > 0 || hierarchySize > 0) {
            this.slp.setWidgetSize(this.details.asWidget(), 0);
            this.slp.setWidgetSize(this.hierarchy.asWidget(), 0);
        } else {
            this.slp.setWidgetSize(this.details.asWidget(), DETAILS_HEIGHT_STD);
            this.slp.setWidgetSize(this.hierarchy.asWidget(), HIERARCHY_WIDTH_STD);
        }
        this.slp.forceLayout();
    }

    @Override
    public void setPresenter(DesktopApp.Presenter presenter) {
        this.presenter = presenter;
    }

    private Integer getCookieValue(String cookie, Integer defaultValue) {
        String value = Cookies.getCookie(cookie);
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.valueOf(value);
        }
    }

    private void setCookieValue(String cookie, Integer value) {
        Cookies.setCookie(cookie, value.toString());
    }
}

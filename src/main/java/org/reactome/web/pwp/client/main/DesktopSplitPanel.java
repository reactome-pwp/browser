package org.reactome.web.pwp.client.main;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DesktopSplitPanel extends SplitLayoutPanel {

    public interface WestPanelResizedHandler {
        void onWestPanelResized(int size);
    }

    public interface SouthPanelResizedHandler {
        void onSouthPanelResized(int size);
    }

    private Widget westWidget;
    private Widget southWidget;

    private double westPanelSize;
    private double southPanelSize;

    private WestPanelResizedHandler westPanelResizedHandler;
    private SouthPanelResizedHandler southPanelResizedHandler;

    public DesktopSplitPanel() {
        super(10);
    }

    public void addWestPanelResizedHandler(WestPanelResizedHandler handler){
        this.westPanelResizedHandler = handler;
    }

    public void addSouthPanelResizedHandler(SouthPanelResizedHandler handler){
        this.southPanelResizedHandler = handler;
    }

    @Override
    public void addSouth(Widget widget, double size) {
        super.addSouth(widget, size);
        this.southPanelSize = size;
        this.southWidget = widget;
    }

    @Override
    public void addSouth(IsWidget widget, double size) {
        super.addSouth(widget, size);
        this.southPanelSize = size;
        this.southWidget = widget.asWidget();
    }

    @Override
    public void addWest(Widget widget, double size) {
        super.addWest(widget, size);
        this.westPanelSize = size;
        this.westWidget = widget;
    }

    @Override
    public void addWest(IsWidget widget, double size) {
        super.addWest(widget, size);
        this.westPanelSize = size;
        this.westWidget = widget.asWidget();
    }

    @Override
    public void forceLayout() {
        super.forceLayout();

        int size = westWidget.getOffsetWidth();
        if(size!=westPanelSize){
            if(westPanelResizedHandler!=null) {
                westPanelResizedHandler.onWestPanelResized(size);
            }
        }

        size = southWidget.getOffsetHeight();
        if(size!=southPanelSize){
            if(southPanelResizedHandler!=null) {
                southPanelResizedHandler.onSouthPanelResized(size);
            }
        }
    }
}

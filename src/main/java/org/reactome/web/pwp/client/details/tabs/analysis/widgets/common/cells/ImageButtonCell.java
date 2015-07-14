package org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.cells;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ImageButtonCell extends AbstractCell<ImageResource> {
    private String title;
    private ImageResource imageResource;
    private ImageButtonCellClickedHandler clickHandler;

    public interface ImageButtonCellClickedHandler {
        public void onImageButtonCellClicked();
    }

    public ImageButtonCell(String title, ImageResource imageResource, ImageButtonCellClickedHandler clickHandler) {
        this.title = title;
        this.imageResource = imageResource;
        this.clickHandler = clickHandler;
    }

    @Override
    public Set<String> getConsumedEvents() {
        Set<String> set = new HashSet<String>();
        set.add(BrowserEvents.CLICK);
        return set;
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, ImageResource value, NativeEvent event, ValueUpdater<ImageResource> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if(BrowserEvents.CLICK.equals(event.getType())){
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }
            if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                if(clickHandler!=null){
                    clickHandler.onImageButtonCellClicked();
                }
            }
        }
    }

    @Override
    public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
        Button button = new Button();
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //on click
            }
        });
        button.setTitle(this.title);
        button.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisRowSelector());
        Image img = new Image(imageResource);
        DOM.insertBefore(button.getElement(), img.getElement(), DOM.getFirstChild(button.getElement()));
        sb.appendHtmlConstant(button.getElement().toString());
    }
}

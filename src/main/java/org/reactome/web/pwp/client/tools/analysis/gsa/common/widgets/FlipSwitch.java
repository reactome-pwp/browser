package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * A simple On/Off flipswitch with animated transitions
 *
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FlipSwitch extends FlowPanel implements ClickHandler {
    private Label slider;
    private Boolean isChecked = Boolean.FALSE;

    private Handler handler;

    public interface Handler {
        void onValueChange(boolean value);
    }

    public FlipSwitch() {
        init();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onClick(ClickEvent event) {
        isChecked = !isChecked;
        update();
        fireEvent();
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked, boolean notifyAboutChange) {
        isChecked = checked;
        update();

        if (notifyAboutChange)
            fireEvent();
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().main());

        slider = new Label();
        slider.setStyleName(RESOURCES.getCSS().slider());
        slider.addStyleName(RESOURCES.getCSS().round());
        slider.addClickHandler(this);
        add(slider);
    }

    private void update() {
        if (isChecked) {
            slider.addStyleName(RESOURCES.getCSS().checked());
        } else {
            slider.removeStyleName(RESOURCES.getCSS().checked());
        }
    }

    private void fireEvent() {
        if (handler != null)
            handler.onValueChange(isChecked);
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("pwp-FlipSwitch")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/FlipSwitch.css";

        String main();

        String slider();

        String checked();

        String round();

    }
}

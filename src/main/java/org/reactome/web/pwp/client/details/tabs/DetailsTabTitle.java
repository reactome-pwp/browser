package org.reactome.web.pwp.client.details.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsTabTitle extends Composite {

    private String title;
    private InlineLabel counter;

    public DetailsTabTitle(ImageResource icon, String title) {
        this.title = title;
        FlowPanel fp = new FlowPanel();
        fp.add(new Image(icon));
        fp.add(new InlineLabel(title));
        fp.add(this.counter = new InlineLabel());
        initWidget(fp);
        setStyleName(RESOURCES.getCSS().titleContainer());
    }

    public void setCounter(String counter){
        this.counter.addStyleName(RESOURCES.getCSS().counter());
        this.counter.setText(counter);
    }

    public void resetCounter(){
        this.counter.setText("");
        this.counter.removeStyleName(RESOURCES.getCSS().counter());
    }

    @Override
    public String toString() {
        return title;
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
    @CssResource.ImportedWithPrefix("pwp-MoleculesTab")
    public interface ResoruceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/details/tabs/DetailsTabTitle.css";

        String titleContainer();

        String counter();

    }
}

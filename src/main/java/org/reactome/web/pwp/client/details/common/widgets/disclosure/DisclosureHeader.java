package org.reactome.web.pwp.client.details.common.widgets.disclosure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.panels.DetailsPanel;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class DisclosureHeader extends Composite implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel>, HasText {

    private final SimplePanel buttonContainer;
    private final Widget openedIcon;
    private final Widget closedIcon;
    private InlineLabel title = new InlineLabel();
    private InlineLabel primaryMessage = new InlineLabel();
    private InlineLabel numberMessage = new InlineLabel();

    public DisclosureHeader(String text, ClickHandler instanceSelector) {
        this(getDefaultResources(), text, instanceSelector);
    }

    public DisclosureHeader(String text, ClickHandler instanceSelector, Widget openIcon, Widget closeIcon){
        this(getDefaultResources(), text, instanceSelector, openIcon, closeIcon);
    }

    public DisclosureHeader(Resources resources, String text, ClickHandler instanceSelector){
        this(resources, text, instanceSelector, new Image(DisclosureImages.INSTANCE.getCollapseImage()), new Image(DisclosureImages.INSTANCE.getExpandImage()));
    }

    public DisclosureHeader(FlowPanel panel, ClickHandler instanceSelector) {
        this.openedIcon =  new Image(DisclosureImages.INSTANCE.getCollapseImage());
        this.closedIcon = new Image(DisclosureImages.INSTANCE.getExpandImage());

        // Inject the styles used by this widget.
        Style style = getDefaultResources().customHeaderStyle();
        style.ensureInjected();

        panel.setWidth("100%");
        panel.setStyleName(style.customHeaderContainer());
        //noinspection GWTStyleCheck
        panel.addStyleName("clearfix");

        Button go = getIcon(instanceSelector);
        if(go!=null){
            go.addStyleName(style.customHeaderIcon());
            panel.insert(go, 0);
        }

        this.buttonContainer = new SimplePanel();
        this.buttonContainer.add(this.closedIcon);
        this.buttonContainer.setStyleName(style.customHeaderButton());
        panel.add(this.buttonContainer);

        this.initWidget(panel);
        this.setWidth("100%");
    }

    public DisclosureHeader(Resources resources, String text, ClickHandler instanceSelector, Widget openIcon, Widget closeIcon) {
        this.openedIcon = openIcon;
        this.closedIcon = closeIcon;

        // Inject the styles used by this widget.
        Style style = resources.customHeaderStyle();
        style.ensureInjected();

        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setWidth("100%");
        flowPanel.setStyleName(style.customHeaderContainer());
        //noinspection GWTStyleCheck
        flowPanel.addStyleName("clearfix");

        Button go = getIcon(instanceSelector);
        if(go!=null){
            go.addStyleName(style.customHeaderIcon());
            flowPanel.add(go);
        }

        this.title.setText(text);
        this.title.setStyleName(style.customHeaderTitle());
        flowPanel.add(this.title);

        this.primaryMessage.setStyleName(style.customHeaderPrimaryMessage());
        flowPanel.add(this.primaryMessage);

        this.numberMessage.setStyleName(style.customHeaderSecondaryMessage());
        flowPanel.add(this.numberMessage);

        this.buttonContainer = new SimplePanel();
        this.buttonContainer.add(this.closedIcon);
        this.buttonContainer.setStyleName(style.customHeaderButton());
        flowPanel.add(this.buttonContainer);

        this.initWidget(flowPanel);
        this.setWidth("100%");
    }

    private Button getIcon(ClickHandler instanceSelector){
        Button go = null;
        if(instanceSelector!=null){
            DetailsPanel p = (DetailsPanel) instanceSelector;
            DatabaseObject databaseObject = p.getDatabaseObject();
            SchemaClass schemaClass = databaseObject.getSchemaClass();
            ImageResource ir = databaseObject.getImageResource();
            Image goIcon = new Image(ir);
            go = new Button();
            go.setStyleName("elv-Disclosure-Button");
            go.setTitle("Select the " + schemaClass.name.toLowerCase() + " and show more information");
            go.getElement().appendChild(goIcon.getElement());
            go.addClickHandler(instanceSelector);
        }
        return go;
    }

    /**
     * Get the default {@link Resources} for this widget.
     */
    private static Resources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(Resources.class);
        }
        return DEFAULT_RESOURCES;
    }

    @Override
    public void onClose(CloseEvent<DisclosurePanel> event) {
        this.buttonContainer.clear();
        this.buttonContainer.add(this.closedIcon);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        this.buttonContainer.clear();
        this.buttonContainer.add(this.openedIcon);
    }

    @Override
    public String getText() {
        return this.title.getText();
    }

    @Override
    public void setText(String s) {
        setDisplayName(s);
    }

    public void setDisplayName(String text){
        this.title.setText(text);
    }

    public void setPrimaryMessage(String text){
        this.primaryMessage.setText(text);
    }

    public void setSecondaryMessage(String text){
        this.numberMessage.setText(text);
    }




    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(Style.DEFAULT_CSS)
        Style customHeaderStyle();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("reactome-CustomHeader")
    public interface Style extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/pwp/client/details/common/widgets/disclosure/css/DisclosureHeader.css";

        /**
         * Applied to the widget.
         */
        String customHeaderContainer();

        /**
         * Applied to the left icon
         */
        String customHeaderIcon();

        /**
         * Applied to the text label
         */
        String customHeaderTitle();

        /**
         * Applied to the selection text labels
         */
        String customHeaderPrimaryMessage();

        /**
         * Applied to the number of items labels
         */
        String customHeaderSecondaryMessage();

        /**
         * Applied to the widget button
         */
        String customHeaderButton();
    }

    private static Resources DEFAULT_RESOURCES;

}
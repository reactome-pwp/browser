package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class AbstractParameterWidget<T> extends FlowPanel {
    protected static final String HINT_INTEGER = "Please enter an integer value";
    protected static final String HINT_FLOAT = "Please enter a decimal number";

    protected Parameter parameter;

    protected String name;
    protected T value;

    private FlowPanel rightGroup;
    protected Image validationIcon;

    protected AbstractParameterWidget.Handler<T> handler;

    public interface Handler<T> {
        void onParameterChange(T value);
    }

    public AbstractParameterWidget(Parameter parameter) {
        this.parameter = parameter;
        this.name = parameter.getName();
        baseInit();
    }

    public String getName() {
        return name;
    }

    public abstract String getValue();

    public abstract void setValue(String value);

    public Parameter getParameter() {
        return parameter;
    }

    protected abstract void setDefault();

    public abstract boolean validate();

    public void showValidationError(boolean visible) {
        if (validationIcon != null) {
            validationIcon.setVisible(visible);
            if (visible) {
                rightGroup.addStyleName(RESOURCES.getCSS().validationError());
            } else {
                rightGroup.removeStyleName(RESOURCES.getCSS().validationError());
            }
        }
    }

    private void baseInit() {
        setStyleName(RESOURCES.getCSS().main());

        Label title = parameter.getDisplayName() == null ? new Label(parameter.getName()) : new Label(parameter.getDisplayName());
        title.setStyleName(RESOURCES.getCSS().title());

        Image description = new Image(RESOURCES.infoIcon());
        description.setStyleName(RESOURCES.getCSS().description());
        description.setTitle(parameter.getDescription());

        FlowPanel leftGroup = new FlowPanel();
        leftGroup.setStyleName(RESOURCES.getCSS().leftGroup());
        leftGroup.add(title);
        leftGroup.add(description);

        rightGroup = new FlowPanel();
        rightGroup.setStyleName(RESOURCES.getCSS().rightGroup());

        add(leftGroup);
        add(rightGroup);
    }

    protected void includeWidget(Widget widget) {
        rightGroup.add(widget);
    }

    protected void addValidationWidget(String hint) {
        validationIcon = new Image(RESOURCES.errorIcon());
        validationIcon.setStyleName(RESOURCES.getCSS().validationIcon());
        validationIcon.setTitle(hint);
        validationIcon.setVisible(false);

        rightGroup.add(validationIcon);
    }

    public void setParameterChangeHandler(Handler<T> handler) {
        this.handler = handler;
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../../images/info.png")
        ImageResource infoIcon();

        @Source("../../../images/error.png")
        ImageResource errorIcon();

    }

    @CssResource.ImportedWithPrefix("pwp-MethodItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/parameters/AbstractParameterWidget.css";

        String main();

        String flipSwitch();

        String leftGroup();

        String rightGroup();

        String title();

        String description();

        String validationIcon();

        String validationError();

    }
}

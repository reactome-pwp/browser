package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class AbstractParameterWidget<T> extends FlowPanel {
    protected Parameter parameter;

    protected String name;
    protected T value;

    public AbstractParameterWidget(Parameter parameter) {
        this.parameter = parameter;
        this.name = parameter.getName();
        baseInit();
    }

    public String getName() {
        return name;
    }

    public abstract String getValue();

    public Parameter getParameter() {
        return parameter;
    }

    protected abstract void setDefault();

    public abstract boolean validate();


    private void baseInit() {
        setStyleName(RESOURCES.getCSS().main());

        Label title = new Label(parameter.getDisplayName());
        title.setStyleName(RESOURCES.getCSS().title());

        Image description = new Image(RESOURCES.infoIcon());
        description.setStyleName(RESOURCES.getCSS().description());
        description.setTitle(parameter.getDescription());

        FlowPanel leftGroup = new FlowPanel();
        leftGroup.setStyleName(RESOURCES.getCSS().leftGroup());
        leftGroup.add(title);
        leftGroup.add(description);

        add(leftGroup);
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

    }

    @CssResource.ImportedWithPrefix("pwp-MethodItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/parameters/ParameterWidget.css";

        String main();

        String leftGroup();

        String title();

        String description();

    }
}

package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.details.common.widgets.button.IconButton;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters.ParametersPanel;

import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class MethodItem extends FocusPanel implements ClickHandler {
    private Method method;
    private boolean isChecked;
    private boolean isExpanded;

    private FlowPanel mainPanel;
    private ParametersPanel parametersPanel;
    private Image checkboxImage;

    public interface Handler {
        void onCheckedChanged(MethodItem source, boolean isChecked);
        void onOptionsExpanded(MethodItem source);
    }

    private Handler handler;

    public MethodItem(final Method method, Handler handler) {
        this.method = method;
        this.handler = handler;
        init();

        addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        handler.onCheckedChanged(this, !isChecked);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void collapse() {
        if (isExpanded) {
            removeStyleName(RESOURCES.getCSS().itemExpanded());
            isExpanded = false;
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        updateCheckBox();
    }

    public Method getMethod() {
        return method;
    }

    public boolean validateAllParameters() {
        return parametersPanel.validateAllParameters();
    }

    public Map<String, String> getParameterValues() {
        return parametersPanel.getParameterValues();
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().item());

        FlowPanel content = new FlowPanel();
        content.setStyleName(RESOURCES.getCSS().contentPanel());

        mainPanel = getMainPanel();
        content.add(mainPanel);

        parametersPanel = createParametersPanel();
        content.add(parametersPanel);

        add(content);
    }

    private void expandCollapse() {
        if (isExpanded) {
            removeStyleName(RESOURCES.getCSS().itemExpanded());
        } else {
            handler.onOptionsExpanded(this);
            addStyleName(RESOURCES.getCSS().itemExpanded());
        }
        isExpanded = !isExpanded;
    }

    private FlowPanel getMainPanel() {
        FlowPanel main = new FlowPanel();
        main.setStyleName(RESOURCES.getCSS().mainPanel());

        checkboxImage = new Image(RESOURCES.uncheckedIcon());
        checkboxImage.setStyleName(RESOURCES.getCSS().checkBoxImage());
        checkboxImage.addStyleName(RESOURCES.getCSS().undraggable());
        checkboxImage.addStyleName(RESOURCES.getCSS().unselectable());

        FlowPanel detailsPanel = new FlowPanel();
        detailsPanel.setStyleName(RESOURCES.getCSS().detailsPanel());
        detailsPanel.addStyleName(RESOURCES.getCSS().unselectable());

        Label title = new Label(method.getName());
        title.setStyleName(RESOURCES.getCSS().title());
        HTMLPanel description = new HTMLPanel(method.getDescription());
        description.setStyleName(RESOURCES.getCSS().description());

        detailsPanel.add(title);
        detailsPanel.add(description);

        Button parametersBtn = new IconButton(RESOURCES.parametersIcon(), RESOURCES.getCSS().parametersBtn(), "Change default analysis parameters", event -> {
            event.preventDefault();
            event.stopPropagation();
            expandCollapse();
        });

        main.add(checkboxImage);
        main.add(detailsPanel);
        main.add(parametersBtn);
        return main;
    }

    private ParametersPanel createParametersPanel() {
        ParametersPanel parameters = new ParametersPanel(method);
        parameters.setStyleName(RESOURCES.getCSS().parametersPanel());

        return parameters;
    }

    private void updateCheckBox() {
        if (checkboxImage != null) {
            checkboxImage.setResource(isChecked ? RESOURCES.checkedIcon() : RESOURCES.uncheckedIcon());
        }
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../images/unchecked.png")
        ImageResource uncheckedIcon();

        @Source("../../images/checked.png")
        ImageResource checkedIcon();

        @Source("../../images/parameters.png")
        ImageResource parametersIcon();
    }

    @CssResource.ImportedWithPrefix("pwp-MethodItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/MethodItem.css";

        String item();

        String itemExpanded();

        String contentPanel();

        String mainPanel();

        String parametersPanel();

        String checkBoxImage();

        String parametersBtn();

        String unselectable();

        String undraggable();

        String detailsPanel();

        String title();

        String description();


    }
}

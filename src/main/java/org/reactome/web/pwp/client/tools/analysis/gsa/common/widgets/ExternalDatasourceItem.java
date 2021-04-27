package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.ExternalDatasource;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters.ParametersPanel;

import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ExternalDatasourceItem extends FocusPanel implements ClickHandler {
    private ExternalDatasource externalDatasource;
    private boolean isExpanded;
    private ParametersPanel parametersPanel;

    public interface Handler {
        void onOptionsExpanded(ExternalDatasourceItem source);
        void onOptionSubmitted(ExternalDatasourceItem source);
    }

    private Handler handler;

    public ExternalDatasourceItem(final ExternalDatasource externalDatasource, Handler handler) {
        this.externalDatasource = externalDatasource;
        this.handler = handler;
        init();

        addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        event.preventDefault();
        event.stopPropagation();
        if (event.getSource().equals(this) && isExpanded) {
            collapse();
        } else {
            handler.onOptionsExpanded(this);
            expandCollapse();
        }
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().item());
        getElement().setId(externalDatasource.getId());
        getElement().getStyle().setBackgroundColor(externalDatasource.getColour());

        FlowPanel content = new FlowPanel();

        Label name = new Label(externalDatasource.getName());
        name.setStyleName(RESOURCES.getCSS().typeName());
        content.add(name);

        HTMLPanel description = new HTMLPanel(externalDatasource.getDescription());
        description.setStyleName(RESOURCES.getCSS().typeDescription());
        content.add(description);

        parametersPanel = createParametersPanel();
        content.add(parametersPanel);

        FlowPanel buttonDiv = new FlowPanel();
        buttonDiv.setStyleName(RESOURCES.getCSS().buttonPanel());
        buttonDiv.add(createSubmitButton());
        content.add(buttonDiv);

        add(content);
    }

    private Button createSubmitButton() {
        Button retBtn = new Button();
        retBtn.setStyleName(RESOURCES.getCSS().importBtn());
        retBtn.getElement().getStyle().setBackgroundColor(externalDatasource.getDarkerColour());
        retBtn.setText("Import");
        retBtn.setTitle("Import a dataset from another resource");
        retBtn.setEnabled(true);

        retBtn.addClickHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            if (validateAllParameters()) {
                handler.onOptionSubmitted(this);
            }
        });
        return retBtn;
    }

    private ParametersPanel createParametersPanel() {
        ParametersPanel parameters = new ParametersPanel(externalDatasource);
        parameters.setStyleName(RESOURCES.getCSS().parametersPanel());
        return parameters;
    }

    public void expandCollapse() {
        if (isExpanded) {
            removeStyleName(RESOURCES.getCSS().itemExpanded());
        } else {
            addStyleName(RESOURCES.getCSS().itemExpanded());
        }
        isExpanded = !isExpanded;
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

    public ExternalDatasource getExternalDatasource() {
        return externalDatasource;
    }

    public boolean validateAllParameters() {
        return parametersPanel.validateAllParameters();
    }

    public Map<String, String> getParameterValues() {
        return parametersPanel.getParameterValues();
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

    @CssResource.ImportedWithPrefix("pwp-ExternalDatasourceItem")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/pwp/client/tools/analysis/gsa/common/widgets/ExternalDatasourceItem.css";

        String item();

        String itemExpanded();

        String parametersPanel();

        String title();

        String description();

        String typeName();

        String typeDescription();

        String buttonPanel();

        String importBtn();

    }
}

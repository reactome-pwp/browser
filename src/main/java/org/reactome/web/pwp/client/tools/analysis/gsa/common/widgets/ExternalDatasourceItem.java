package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
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
    private Button submitBtn;

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
        Console.info("ExternalDatasourceItem > onClick [" + isExpanded + "]");
        handler.onOptionsExpanded(this);
        expandCollapse();
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

    private void init() {
        setStyleName(RESOURCES.getCSS().item());

//        FlowPanel content = new FlowPanel();
//        content.setStyleName(RESOURCES.getCSS().contentPanel());


        //content.setStyleName(RESOURCES.getCSS().externalTypeContent());
        getElement().setId(externalDatasource.getId());
        getElement().getStyle().setBackgroundColor(externalDatasource.getColour());

        FlowPanel content = new FlowPanel();

        Label name = new Label(externalDatasource.getName());
        name.setStyleName(RESOURCES.getCSS().typeName());
        content.add(name);

        HTMLPanel description = new HTMLPanel(externalDatasource.getName());
        description.setStyleName(RESOURCES.getCSS().typeDescription());
        content.add(description);

//        FocusPanel aPanel = new FocusPanel();
//        aPanel.getElement().setId(externalDatasource.getId());
//        aPanel.getElement().getStyle().setBackgroundColor(externalDatasource.getColour());
//        aPanel.addStyleName(RESOURCES.getCSS().externalTypePanel());
//
//        addClickHandler(event -> {
//            event.preventDefault();
//            event.stopPropagation();
//            expandCollapse();
//        });

        parametersPanel = createParametersPanel();
        content.add(createParametersPanel());

        FlowPanel buttonDiv = new FlowPanel();
        buttonDiv.setStyleName(RESOURCES.getCSS().buttonPanel());
        buttonDiv.add(submitBtn = createSubmitButton());

        content.add(buttonDiv);
        add(content);
    }

    private Button createSubmitButton() {
        Button retBtn = new Button();
        retBtn.setStyleName(RESOURCES.getCSS().importBtn());
        retBtn.setText("Import");
        retBtn.setTitle("Import a dataset from another resource");
        retBtn.setEnabled(true);

        retBtn.addClickHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            handler.onOptionSubmitted(this);
        });
        return retBtn;
    }

    public void expandCollapse() {
        Console.info("ExternalDatasourceItem > expandCollapse ["+ isExpanded + "]");
        if (isExpanded) {
            removeStyleName(RESOURCES.getCSS().itemExpanded());
        } else {
//            handler.onOptionsExpanded(this);
            addStyleName(RESOURCES.getCSS().itemExpanded());
        }
        isExpanded = !isExpanded;
        Console.info("ExternalDatasourceItem > expandCollapse final ["+ isExpanded + "]");
    }

    private ParametersPanel createParametersPanel() {
        ParametersPanel parameters = new ParametersPanel(externalDatasource);
        parameters.setStyleName(RESOURCES.getCSS().parametersPanel());
        return parameters;
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

        String typeName();
        String typeDescription();
        String externalTypeContent();
        String externalTypePanel();
        String parametersPanel();


        String title();

        String description();

        String buttonPanel();
        String importBtn();

    }
}

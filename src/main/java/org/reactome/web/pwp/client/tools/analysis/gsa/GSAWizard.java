package org.reactome.web.pwp.client.tools.analysis.gsa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClient;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.GSAClientHandler;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.GSAError;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.Parameter;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizard extends DockLayoutPanel {
    EventBus eventBus = new GSAWizardEventBus();

    private SimplePanel top;
    private TabLayoutPanel panels;

    private List<Method> availableMethods;
    private AnalysisCompletedHandler handler;

    public GSAWizard(AnalysisCompletedHandler handler) {
        super(Style.Unit.PX);
        this.handler = handler;

        initUI();
    }

    public void setAvailableMethods(List<Method> methods) {
        availableMethods = methods;

        for (Method m : methods) {
            Console.info(m.getName() + " - " + m.getDescription());
            for (Parameter p : m.getParameters()) {
                Console.info(">>> " + p.getName() + " >>> " + p.getDisplayName());
            }
        }

        GSAClient.getDatasetTypes(new GSAClientHandler.GSADatasetTypesHandler() {
            @Override
            public void onTypesSuccess(List<DatasetType> types) {
                for (DatasetType type : types) {
                    Console.info(type.getId() + " -> " + type.getDescription());
                }
            }

            @Override
            public void onError(GSAError error) {

            }

            @Override
            public void onException(String msg) {

            }
        });
    }

    private void initUI() {
        top = new SimplePanel();
        top.addStyleName(AnalysisStyleFactory.getAnalysisStyle().wizardTop());
        top.add(new Image(RESOURCES.step01()));
        this.addNorth(top, 55);

        this.panels = new TabLayoutPanel(0, Style.Unit.PX);
        this.panels.setAnimationDuration(250);
        this.panels.add(new Label("step 1"));
        this.panels.add(new Label("step 1"));
        this.add(panels);
    }

    public static GSAResource RESOURCES = GWT.create(GSAResource.class);
    public interface GSAResource extends ClientBundle {

        @Source("images/step1.png")
        ImageResource step01();

        @Source("images/step2.png")
        ImageResource step02();
    }

}

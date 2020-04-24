package org.reactome.web.pwp.client.tools.analysis.gsa.steps;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardContext;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.GSAWizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class AbstractGSAStep extends DockLayoutPanel {

    protected GSAWizardEventBus wizardEventBus;
    protected GSAWizardContext wizardContext;

    protected FlowPanel navigationPanel;

    public AbstractGSAStep(GSAWizardEventBus wizardEventBus, GSAWizardContext wizardContext) {
        super(Style.Unit.PX);
        this.wizardEventBus = wizardEventBus;
        this.wizardContext = wizardContext;

        baseInit();
    }

    private void baseInit() {
        navigationPanel = new FlowPanel();
        navigationPanel.setStyleName(GSAStyleFactory.getStyle().navigationPanel());
        addSouth(navigationPanel, 34);
    }

    protected void addRightButton(Button btn) {
        btn.addStyleName(GSAStyleFactory.getStyle().rightButton());
        navigationPanel.add(btn);
    }

    protected void addLeftButton(Button btn) {
        btn.addStyleName(GSAStyleFactory.getStyle().leftButton());
        navigationPanel.add(btn);
    }
}

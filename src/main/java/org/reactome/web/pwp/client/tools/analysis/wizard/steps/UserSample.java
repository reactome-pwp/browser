package org.reactome.web.pwp.client.tools.analysis.wizard.steps;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardSelection;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;
import org.reactome.web.pwp.client.tools.analysis.wizard.submitters.FileSubmitter;
import org.reactome.web.pwp.client.tools.analysis.wizard.submitters.PostSubmitter;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class UserSample extends ScrollPanel implements GoHandler {

    private WizardEventBus wizardEventBus;
    //wizardSelection is shared by all the steps in the wizard
    private WizardSelection wizardSelection;

    public UserSample(WizardEventBus wizardEventBus, WizardSelection wizardSelection) {
        this.wizardEventBus = wizardEventBus;
        this.wizardSelection = wizardSelection;
        init();
    }

    public void init(){
        FlowPanel container = new FlowPanel();

        SimplePanel explanation = new SimplePanel();
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        explanation.getElement().setInnerHTML(AnalysisWizard.UserSampleResource.INSTANCE.analysisInfo().getText());
        container.add(explanation);

        FileSubmitter fileSubmitter = new FileSubmitter();
        fileSubmitter.addGoHandler(this);
        container.add(fileSubmitter);

        PostSubmitter postSubmitter = new PostSubmitter();
        postSubmitter.addGoHandler(this);
        container.add(postSubmitter);

        add(container);
    }


    @Override
    public void onGoSelected(GoEvent event) {
        if(event.getForm()!=null){
            wizardSelection.setForm(event.getForm());
        } else {
            wizardSelection.setPostData(event.getTextArea().getText());
        }
        wizardEventBus.fireEventFromSource(new NextStepSelectedEvent(AnalysisWizard.Step.OPTIONS), UserSample.this);
    }
}

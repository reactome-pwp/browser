package org.reactome.web.pwp.client.tools.analysis.wizard.steps;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.exceptions.AnalysisModelException;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.factory.AnalysisModelFactory;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardSelection;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.AnalysisErrorEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.NextStepSelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Analysis extends FlowPanel implements NextStepSelectedHandler, FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler {

    private WizardEventBus wizardEventBus;
    private WizardSelection wizardSelection;

    public Analysis(WizardEventBus wizardEventBus, WizardSelection wizardSelection) {
        this.wizardEventBus = wizardEventBus;
        this.wizardSelection = wizardSelection;
        initHandlers();

        add(new InlineLabel("Step 3: Analysing your data"));
    }

    @Override
    public void onNextStepSelected(NextStepSelectedEvent event) {
        if (!event.getStep().equals(AnalysisWizard.Step.ANALYSIS)) return;

        //It's me!
        switch (wizardSelection.getSampleType()){
            case FILE:
                analyseFile(wizardSelection.getForm());
                break;
            default:
                analyseText(wizardSelection.getPostData());
        }
    }

    @Override
    public void onSubmit(FormPanel.SubmitEvent event) {
        //TODO: Icons?
    }

    @Override
    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
        //Work around to extract the content in case it's included in a HTML tag
        Element label = DOM.createLabel();
        label.setInnerHTML( event.getResults() );
        String json = label.getInnerText();
        try {
            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, json);
            Console.info(result.getSummary().getToken());
            wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result), this);
        } catch (AnalysisModelException e) {
            try {
                AnalysisError analysisError = AnalysisModelFactory.getModelObject(AnalysisError.class, json);
                wizardEventBus.fireEventFromSource(new AnalysisErrorEvent(analysisError), this);
            } catch (AnalysisModelException e1) {
                Console.error("Oops! This is unexpected", this);
            }
        }
    }

    private static final String FORM_ANALYSIS = "/AnalysisService/identifiers/form";

    private void analyseFile(FormPanel form){
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setAction(FORM_ANALYSIS + (wizardSelection.isProjectToHuman() ? "/projection" : "") + "?page=1&interactors=" + wizardSelection.isIncludeInteractors());
        form.addSubmitHandler(this);
        form.addSubmitCompleteHandler(this);
        form.submit();
    }

    private void analyseText(String data){
        AnalysisClient.analyseData(data, wizardSelection.isProjectToHuman(), wizardSelection.isIncludeInteractors(),  AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {

            @Override
            public void onAnalysisServerException(String s) {
//                wizardEventBus.fireEventFromSource(new AnalysisErrorEvent(), this);
                Console.error(s);
            }

            @Override
            public void onAnalysisResult(AnalysisResult result, long l) {
                wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result), Analysis.this);
            }

            @Override
            public void onAnalysisError(AnalysisError analysisError) {
                wizardEventBus.fireEventFromSource(new AnalysisErrorEvent(analysisError), Analysis.this);
            }
        });
    }

    private void initHandlers(){
        wizardEventBus.addHandler(NextStepSelectedEvent.TYPE, this);
    }
}

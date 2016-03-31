package org.reactome.web.pwp.client.tools.analysis.wizard.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.exceptions.AnalysisModelException;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.factory.AnalysisModelFactory;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardSelection;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.NextStepSelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Analysis extends ScrollPanel implements NextStepSelectedHandler, FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler, ClickHandler {

    private WizardEventBus wizardEventBus;
    private WizardSelection wizardSelection;

    private FlowPanel progress;
    private FlowPanel error;
    private Button back;

    public Analysis(WizardEventBus wizardEventBus, WizardSelection wizardSelection) {
        this.wizardEventBus = wizardEventBus;
        this.wizardSelection = wizardSelection;
        initHandlers();

        FlowPanel container = new FlowPanel();

        SimplePanel explanation = new SimplePanel();
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        explanation.getElement().setInnerHTML("Step 3: Analysing your data...");
        container.add(explanation);

        container.add(progress = getAnalysisProgressPanel());
        container.add(error = getErrorPanel());

        container.add(back = new Button("Back to your data", this));
        back.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisErrorButton());

        add(container);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        wizardEventBus.fireEventFromSource(new NextStepSelectedEvent(AnalysisWizard.Step.SAMPLE), this);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                progress.getElement().getStyle().setOpacity(1);
                error.getElement().getStyle().setOpacity(0);
                back.getElement().getStyle().setOpacity(0);
            }
        });
    }

    @Override
    public void onNextStepSelected(NextStepSelectedEvent event) {
        if (!event.getStep().equals(AnalysisWizard.Step.ANALYSIS)) return;

        //It's me!
        switch (wizardSelection.getSampleType()) {
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
        label.setInnerHTML(event.getResults());
        String json = label.getInnerText();
        try {
            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, json);
            analysisCompleted(result);
        } catch (AnalysisModelException e) {
            try {
                AnalysisError analysisError = AnalysisModelFactory.getModelObject(AnalysisError.class, json);
                analysisError(analysisError);
            } catch (AnalysisModelException e1) {
                Console.error("Oops! This is unexpected", this);
            }
        }
    }

    private void analysisNotAvailable() {
        progress.getElement().getStyle().setOpacity(0);
        error.getElement().getStyle().setOpacity(1);
        back.getElement().getStyle().setOpacity(1);
        error.add(new Label("Service not available. Please wait or contact help@reactome.org"));
    }

    private void analysisError(AnalysisError analysisError) {
        progress.getElement().getStyle().setOpacity(0);
        error.getElement().getStyle().setOpacity(1);
        back.getElement().getStyle().setOpacity(1);
        for (String message : analysisError.getMessages()) {
            error.add(new Label(message));
        }
    }

    private void analysisCompleted(AnalysisResult result) {
        wizardEventBus.fireEventFromSource(new AnalysisCompletedEvent(result), this);
    }

    private void analyseFile(FormPanel form) {
        String url = "/AnalysisService/identifiers/form";
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setAction(url + (wizardSelection.isProjectToHuman() ? "/projection" : "") + "?page=1&interactors=" + wizardSelection.isIncludeInteractors());
        form.addSubmitHandler(this);
        form.addSubmitCompleteHandler(this);
        form.submit();
    }

    private void analyseText(String data) {
        AnalysisClient.analyseData(data, wizardSelection.isProjectToHuman(), wizardSelection.isIncludeInteractors(), AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {

            @Override
            public void onAnalysisServerException(String s) {
                analysisNotAvailable();
            }

            @Override
            public void onAnalysisResult(AnalysisResult result, long l) {
                analysisCompleted(result);
            }

            @Override
            public void onAnalysisError(AnalysisError analysisError) {
                analysisError(analysisError);
            }
        });
    }

    private FlowPanel getAnalysisProgressPanel() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisProgress());
        fp.add(new Image(RESOURCES.loader()));
        fp.add(new Label("Wait while analysing your data"));
        return fp;
    }

    private FlowPanel getErrorPanel(){
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisError());
        return fp;
    }

    private void initHandlers() {
        wizardEventBus.addHandler(NextStepSelectedEvent.TYPE, this);
    }


    public UserSampleResource RESOURCES = GWT.create(UserSampleResource.class);
    public interface UserSampleResource extends ClientBundle {
        @Source("analysis/loader_lines.gif")
        ImageResource loader();
    }
}

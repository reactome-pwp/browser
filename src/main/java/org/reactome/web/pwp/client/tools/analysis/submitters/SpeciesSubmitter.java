package org.reactome.web.pwp.client.tools.analysis.submitters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.*;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.client.tools.analysis.event.ServiceUnavailableEvent;
import org.reactome.web.pwp.client.tools.analysis.examples.AnalysisExamples;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorHandler;
import org.reactome.web.pwp.client.tools.analysis.notifications.ErrorPanel;
import org.reactome.web.pwp.model.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSubmitter extends FlowPanel implements ClickHandler {

    private ListBox species;
    private Image statusIcon;

    private ErrorPanel errorPanel;

    public SpeciesSubmitter() {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().unselectable());
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisBlock());

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Species Comparison"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisExamples.EXAMPLES.speciesComparisonInfo().getText());
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        add(explanation);

        FlowPanel submissionPanel = new FlowPanel();
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        submissionPanel.add(new Label("Compare "));

        Label hsaLabel = new Label("Homo sapiens ");
        hsaLabel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().emphasis());
        submissionPanel.add(hsaLabel);

        submissionPanel.add(new Label("with "));
        submissionPanel.add(new Button("GO", this));
        this.statusIcon = new Image(CommonImages.INSTANCE.loader());
        this.statusIcon.setStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIcon());
        setStatusIcon(null, false, false);
        submissionPanel.add(this.statusIcon);

        this.species = new ListBox();
        this.species.setMultipleSelect(false);
        submissionPanel.add(this.species);
        add(submissionPanel);

        errorPanel = new ErrorPanel();
        add(errorPanel);
    }

    public HandlerRegistration addAnalysisCompletedEventHandler(AnalysisCompletedHandler handler){
        return this.addHandler(handler, AnalysisCompletedEvent.TYPE);
    }

    public HandlerRegistration addAnalysisErrorEventHandler(AnalysisErrorHandler handler){
        return this.addHandler(handler, AnalysisErrorEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent event) {
        Long dbId = Long.valueOf(species.getValue(species.getSelectedIndex()));
        if(dbId==-1) {
//            DialogBoxFactory.alert("Species comparison", "Please select a species to compare with");
            setStatusIcon(CommonImages.INSTANCE.error(), true, true);
            errorPanel.setErrorMessage("No species selected", "Please select a species to compare with and then press GO");
            return;
        }

        setStatusIcon(CommonImages.INSTANCE.loader(), true, false);
        String url = AnalysisHelper.URL_PREFIX + "/species/homoSapiens/" + dbId + "?page=1";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if(!response.getStatusText().equals("OK")){
                        setStatusIcon(CommonImages.INSTANCE.error(), true, true);
                        try {
                            AnalysisError analysisError = AnalysisModelFactory.getModelObject(AnalysisError.class, response.getText());
                            fireEvent(new AnalysisErrorEvent(analysisError));
                        } catch (AnalysisModelException e) {
                            Console.error("Oops! This is unexpected", this);
                        }
                    }else{
                        setStatusIcon(CommonImages.INSTANCE.success(), true, true);
                        try {
                            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                            fireEvent(new AnalysisCompletedEvent(result));
                        } catch (AnalysisModelException e) {
                            Console.error("Oops! This is unexpected", this);
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    setStatusIcon(CommonImages.INSTANCE.error(), true, true);
                    fireEvent(new ServiceUnavailableEvent());
                }
            });
        }catch (RequestException ex) {
            setStatusIcon(CommonImages.INSTANCE.error(), true, true);
            fireEvent(new ServiceUnavailableEvent());
        }
    }

    public void setSpeciesList(List<Species> speciesList) {
//        this.speciesList = speciesList;
        this.species.addItem("Select a species...", "-1");
        for (Species species : speciesList) {
            if(!species.getDbId().equals(Token.DEFAULT_SPECIES_ID)){
                this.species.addItem(species.getDisplayName(), species.getDbId().toString());
            }
        }
    }

    private void setStatusIcon(final ImageResource resource, boolean visible, boolean schedule) {
        if (resource != null) {
            statusIcon.setResource(resource);
        }
        if (visible) {
            statusIcon.addStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
            if(schedule) {
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        statusIcon.removeStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
                    }
                };
                timer.schedule(2000);
            }
        } else {
            statusIcon.removeStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
        }
    }
}

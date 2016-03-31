package org.reactome.web.pwp.client.tools.analysis.species;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.AnalysisResultTable;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.model.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesComparison extends FlowPanel implements ClickHandler {

    private ListBox species;
    private Image statusIcon;

//    private ErrorPanel errorPanel;

    public SpeciesComparison() {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().unselectable());
//        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisBlock());

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Species Comparison"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisWizard.UserSampleResource.INSTANCE.speciesComparisonInfo().getText());
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
//        setStatusIcon(null, false, false);
        submissionPanel.add(this.statusIcon);

        this.species = new ListBox();
        this.species.setMultipleSelect(false);
        submissionPanel.add(this.species);
        add(submissionPanel);

//        errorPanel = new ErrorPanel();
//        add(errorPanel);
    }

//    public HandlerRegistration addAnalysisCompletedEventHandler(AnalysisCompletedHandler handler) {
//        return this.addHandler(handler, AnalysisCompletedEvent.TYPE);
//    }
//
//    public HandlerRegistration addAnalysisErrorEventHandler(AnalysisErrorHandler handler) {
//        return this.addHandler(handler, AnalysisErrorEvent.TYPE);
//    }

    @Override
    public void onClick(ClickEvent event) {
        Long dbId = Long.valueOf(species.getValue(species.getSelectedIndex()));
        if (dbId == -1) {
//            DialogBoxFactory.alert("Species comparison", "Please select a species to compare with");
//            setStatusIcon(CommonImages.INSTANCE.error(), true, true);
//            errorPanel.setErrorMessage("No species selected", "Please select a species to compare with and then press GO");
            return;
        }


        AnalysisClient.speciesComparison(dbId, AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(AnalysisResult result, long time) {
                fireEvent(new AnalysisCompletedEvent(result));
            }

            @Override
            public void onAnalysisError(AnalysisError error) {
//                fireEvent(new AnalysisErrorEvent(error));
            }

            @Override
            public void onAnalysisServerException(String message) {
//                setStatusIcon(CommonImages.INSTANCE.error(), true, true);
//                errorPanel.setErrorMessage("The Analysis Service is temporarily unavailable",
//                        "We have trouble performing the analysis. Please check your internet connection and try again in a while");
//                fireEvent(new ServiceUnavailableEvent());
            }
        });
    }

    public void setSpeciesList(List<Species> speciesList) {
//        this.speciesList = speciesList;
        this.species.addItem("Select a species...", "-1");
        for (Species species : speciesList) {
            if (!species.getDbId().equals(Token.DEFAULT_SPECIES_ID)) {
                this.species.addItem(species.getDisplayName(), species.getDbId().toString());
            }
        }
    }

//    private void setStatusIcon(final ImageResource resource, boolean visible, boolean schedule) {
//        if (resource != null) {
//            statusIcon.setResource(resource);
//        }
//        if (visible) {
//            statusIcon.addStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
//            if (schedule) {
//                Timer timer = new Timer() {
//                    @Override
//                    public void run() {
//                        statusIcon.removeStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
//                    }
//                };
//                timer.schedule(2000);
//            }
//        } else {
//            statusIcon.removeStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIconVisible());
//        }
//    }
}

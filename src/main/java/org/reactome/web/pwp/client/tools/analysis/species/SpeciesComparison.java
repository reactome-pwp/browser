package org.reactome.web.pwp.client.tools.analysis.species;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
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

    private FlowPanel errorPanel;
    private InlineLabel errorHolder;

    private Image loading;

    private AnalysisCompletedHandler handler;

    public SpeciesComparison(AnalysisCompletedHandler handler) {
        this.handler = handler;
        getElement().getStyle().setMargin(5, Style.Unit.PX);

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Species Comparison"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisWizard.RESOURCES.speciesComparisonInfo().getText());
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        add(explanation);

        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        fp.add(new InlineLabel("Compare"));

        InlineLabel hs = new InlineLabel("Homo sapiens");
        hs.addStyleName(AnalysisStyleFactory.getAnalysisStyle().emphasis());
        fp.add(hs);

        fp.add(new InlineLabel("with"));
        fp.add(species = new ListBox());
        species.setMultipleSelect(false);

        fp.add(new Button("Go!", this));
        fp.add(loading = new Image(CommonImages.INSTANCE.loader()));
        loading.getElement().getStyle().setFloat(Style.Float.RIGHT);
        loading.setVisible(false);

        add(fp);

        add(errorPanel = getErrorHolder());
    }

    @Override
    public void onClick(ClickEvent event) {
        errorPanel.getElement().getStyle().setOpacity(0);
        Long dbId = Long.valueOf(species.getValue(species.getSelectedIndex()));
        if (dbId == -1) {
            errorHolder.setText("Please select a species to compare with and then press GO");
            errorPanel.getElement().getStyle().setOpacity(1);
            (new Timer() {
                @Override
                public void run() {
                    errorPanel.getElement().getStyle().setOpacity(0);
                }
            }).schedule(4000);
            return;
        }

        loading.setVisible(true);
        AnalysisClient.speciesComparison(dbId, AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisResult(AnalysisResult result, long time) {
                loading.setVisible(false);
                handler.onAnalysisCompleted(new AnalysisCompletedEvent(result));
            }

            @Override
            public void onAnalysisError(AnalysisError error) {
                if (error.getMessages() != null && !error.getMessages().isEmpty()) {
                    showErrorMessage(error.getMessages().get(0));
                } else {
                    showErrorMessage(error.getReason());
                }
            }

            @Override
            public void onAnalysisServerException(String message) {
                showErrorMessage(message);
            }
        });
    }

    public void setSpeciesList(List<Species> speciesList) {
        this.species.addItem("Select a species...", "-1");
        for (Species species : speciesList) {
            if (!species.getDbId().equals(Token.DEFAULT_SPECIES_ID)) {
                this.species.addItem(species.getDisplayName(), species.getDbId().toString());
            }
        }
    }

    protected void showErrorMessage(String error) {
        loading.setVisible(false);
        errorHolder.setText(error);
        errorPanel.getElement().getStyle().setOpacity(1);
        (new Timer() {
            @Override
            public void run() {
                errorPanel.getElement().getStyle().setOpacity(0);
            }
        }).schedule(4000);
    }

    @SuppressWarnings("Duplicates")
    private FlowPanel getErrorHolder() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().errorMessage());
        fp.add(new Image(CommonImages.INSTANCE.error()));
        fp.add(errorHolder = new InlineLabel());
        return fp;
    }
}

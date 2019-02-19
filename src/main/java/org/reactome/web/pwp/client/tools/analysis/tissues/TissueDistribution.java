package org.reactome.web.pwp.client.tools.analysis.tissues;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.ExperimentSummary;
import org.reactome.web.pwp.client.tools.analysis.tissues.listselector.ListSelector;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class TissueDistribution extends FlowPanel implements ClickHandler, ChangeHandler,
        ListSelector.Handler<String> {

    private static String SAMPLE_URL = "https://127.0.0.1/ExperimentDigester/experiments/##EXPERIMENT_ID##/sample";

    private Map<Integer, ExperimentSummary> summaries;
    private ExperimentSummary selectedSummary;

    private FlowPanel selectionPanel;
    private ListBox experimentSelector;
    private ListSelector tissueSelector;
    private Button goButton;

    private FlowPanel errorPanel;
    private InlineLabel errorHolder;

    private Image loading;

    private AnalysisCompletedHandler handler;

    public TissueDistribution(AnalysisCompletedHandler handler) {
        this.handler = handler;
        initialiseUI();
    }

    public void setExperimentSummaries(List<ExperimentSummary> summaries){
        if(summaries.isEmpty()) {
            noExperimentsLoaded();
            this.summaries = new HashMap<>();
        } else {
            this.summaries = summaries.stream().collect(Collectors.toMap(ExperimentSummary::getId, Function.identity(), (a, b) -> a));
            setExperimentsList(summaries);

            if(this.summaries.size() == 1) {
                experimentSelector.setSelectedIndex(1);
                Integer id = Integer.parseInt(experimentSelector.getSelectedValue());
                onChange(id);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if(event.getSource().equals(goButton)) {
            List<String> selectedTissueNames = tissueSelector.getSelectedItems();
            if (selectedTissueNames == null || selectedTissueNames.isEmpty()) {
                showErrorMessage("Please select at least one tissue and press Go");
            } else {
                String includedColumns = selectedTissueNames.stream()
                                                      .map(tissue -> selectedSummary.getTissuesMap().get(tissue).toString())
                                                      .collect(Collectors.joining(","));

                String targetUrl = SAMPLE_URL.replaceFirst("##EXPERIMENT_ID##", selectedSummary.getId().toString())
                                    + "?included=" + includedColumns;

                loading.setVisible(true);
                goButton.setEnabled(false);
                AnalysisClient.analyseURL(targetUrl, true, false, AnalysisResultTable.PAGE_SIZE, 1, new AnalysisHandler.Result() {
                    @Override
                    public void onAnalysisResult(AnalysisResult result, long time) {
                        loading.setVisible(false);
                        goButton.setEnabled(true);
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
        }
    }

    @Override
    public void onChange(ChangeEvent event) {
        Integer id = Integer.parseInt(experimentSelector.getSelectedValue());
        onChange(id);
    }

    private void onChange(Integer selectedId) {
        selectedSummary = summaries.getOrDefault(selectedId, null);
        updateUI();
    }

    @Override
    public void onSelectedListChanged(List<String> selectedItems) {
        goButton.setEnabled(selectedItems!=null && !selectedItems.isEmpty());
    }

    protected void showErrorMessage(String error) {
        loading.setVisible(false);
        goButton.setEnabled(true);
        errorHolder.setText(error);
        errorPanel.getElement().getStyle().setOpacity(1);
        (new Timer() {
            @Override
            public void run() {
                errorPanel.getElement().getStyle().setOpacity(0);
            }
        }).schedule(6000);
    }

    private void initialiseUI() {
        getElement().getStyle().setMargin(5, Style.Unit.PX);

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Tissue Distribution"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisWizard.RESOURCES.tissueDistributionInfo().getText());
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        add(explanation);

        selectionPanel = new FlowPanel();
        selectionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        selectionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        selectionPanel.add(new InlineLabel("Selected experiment:"));

        selectionPanel.add(experimentSelector = new ListBox());
        experimentSelector.setMultipleSelect(false);

        loading = new Image(CommonImages.INSTANCE.loader());
        loading.setStyleName(AnalysisStyleFactory.getAnalysisStyle().tissuesLoading());

        goButton = new Button("Go!", this);
        goButton.setStyleName(AnalysisStyleFactory.getAnalysisStyle().tissuesGoButton());


        add(selectionPanel);
        add(tissueSelector = new ListSelector<String>(
                "Select at least one of the available tissues",
                "Available Tissues:",
                "Selected Tissues:",
                this)
        );
        add(goButton);
        add(loading);
        add(errorPanel = getErrorHolder());
        errorHolder.setStyleName(AnalysisStyleFactory.getAnalysisStyle().tissuesError());

        tissueSelector.setVisible(false);
        loading.setVisible(false);
        goButton.setVisible(false);
        goButton.setEnabled(false);
    }

    private void noExperimentsLoaded() {
        selectionPanel.setVisible(false);
        errorHolder.setText("Error retrieving the experiments from the Rectome server");
        errorPanel.getElement().getStyle().setOpacity(1);
        errorPanel.getElement().getStyle().setOpacity(1);
    }

    private void updateUI() {
        List<String> list = null;
        if(selectedSummary != null && selectedSummary.getTissuesMap() != null) {
            list = new ArrayList<>(selectedSummary.getTissuesMap().keySet());
        }
        tissueSelector.setAvailableListItems(list);
        goButton.setVisible(selectedSummary != null);
    }

    @SuppressWarnings("Duplicates")
    private FlowPanel getErrorHolder() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().errorMessage());
        fp.add(new Image(CommonImages.INSTANCE.error()));
        fp.add(errorHolder = new InlineLabel());
        return fp;
    }

    private void setExperimentsList(List<ExperimentSummary> experimentsList) {
        experimentSelector.addItem("Choose one of the available experiments...", "-1");
        for (ExperimentSummary exp : experimentsList) {
            experimentSelector.addItem(exp.getName() + " - " + exp.getResource(), exp.getId().toString());
        }

        experimentSelector.addChangeHandler(this);
    }
}
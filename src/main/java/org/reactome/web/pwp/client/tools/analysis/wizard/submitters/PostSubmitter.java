package org.reactome.web.pwp.client.tools.analysis.wizard.submitters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PostSubmitter extends DockLayoutPanel implements ClickHandler {

    private TextArea textArea;
    private Integer height = 320;

    private FlowPanel errorPanel;
    private InlineLabel errorHolder;

    public PostSubmitter() {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        setHeight(this.height + "px");

        addEast(getExampleButtons(), 230);

        FlowPanel submissionPanel = new FlowPanel();
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterButtons());
        Button clear = new Button("Clear", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText("");
            }
        });
        clear.setStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterClear());
        submissionPanel.add(clear);
        submissionPanel.add(errorPanel = getErrorHolder());
        submissionPanel.add(new Button("Continue", this));
        addSouth(submissionPanel, 50);

        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitter());
        fp.add(new Label("Paste your data to analyse or try example data sets:"));
        fp.add(textArea = new TextArea());
        textArea.getElement().setAttribute("style", "font-family: Consolas;");
        textArea.getElement().setAttribute("placeholder", "Paste your data here or select an example from the right >>");
        add(fp);
    }

    public HandlerRegistration addGoHandler(GoHandler handler) {
        return addHandler(handler, GoEvent.TYPE);
    }

    public Integer getHeight() {
        return height;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        errorPanel.getElement().getStyle().setOpacity(0);
        if (textArea.getValue().isEmpty()) {
            errorHolder.setText("Please paste the content to analyse or select an example to continue...");
            errorPanel.getElement().getStyle().setOpacity(1);
            (new Timer() {
                @Override
                public void run() {
                    errorPanel.getElement().getStyle().setOpacity(0);
                }
            }).schedule(4000);
        } else {
            fireEvent(new GoEvent(textArea));
        }
    }

    private Widget getExampleButtons() {
        FlowPanel examples = new FlowPanel();
        examples.addStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterExamples());
        examples.add(new Label("Some examples:"));
        examples.add(new Button("UniProt accession list", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getUniprot().getText());
            }
        }));
        examples.add(new Button("Gene name list", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getGeneNames().getText());
            }
        }));
        examples.add(new Button("Gene NCBI / Entrez list", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.geneNCBI().getText());
            }
        }));
        examples.add(new Button("Small molecules (ChEBI)", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getChEBI().getText());
            }
        }));
        examples.add(new Button("Small molecules (KEGG)", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getKegg().getText());
            }
        }));
        examples.add(new Button("Microarray data", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getExpression().getText());
            }
        }));
        examples.add(new Button("Metabolomics data", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getMetabolomics().getText());
            }
        }));
        examples.add(new Button("Cancer Gene Census (COSMIC)", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getCancerGeneCensus().getText());
            }
        }));
        examples.add(new Button("Normal Tissue Expression (HPA)", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText(AnalysisExamples.EXAMPLES.getHPANormalTissueExpression().getText());
            }
        }));
        return examples;
    }

    @SuppressWarnings("Duplicates")
    private FlowPanel getErrorHolder() {
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().errorMessage());
        fp.add(new Image(CommonImages.INSTANCE.error()));
        fp.add(errorHolder = new InlineLabel());
        return fp;
    }

    public interface AnalysisExamples extends ClientBundle {

        AnalysisExamples EXAMPLES = GWT.create(AnalysisExamples.class);

        @Source("uniprot.txt")
        TextResource getUniprot();

        @Source("geneNames.txt")
        TextResource getGeneNames();

        @Source("GeneNCBI_Entrez.txt")
        TextResource geneNCBI();

        @Source("chEBI.txt")
        TextResource getChEBI();

        @Source("kegg.txt")
        TextResource getKegg();

        @Source("CancerGeneCensus.txt")
        TextResource getCancerGeneCensus();

        @Source("expression.txt")
        TextResource getExpression();

        @Source("metabolomics.txt")
        TextResource getMetabolomics();

        @Source("HPANormalTissueExpressionIHC.txt")
        TextResource getHPANormalTissueExpression();

    }
}

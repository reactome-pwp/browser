package org.reactome.web.pwp.client.tools.analysis.wizard.submitters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PostSubmitter extends DockLayoutPanel implements ClickHandler {

    private TextArea textArea;
    private Integer height = 280;

    public PostSubmitter() {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        setHeight(this.height + "px");

        addEast(getExampleButtons(), 210);

        FlowPanel submissionPanel = new FlowPanel();
        Button clear = new Button("Clear", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText("");
            }
        });
        clear.setStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterClear());
        submissionPanel.add(clear);
        submissionPanel.add(new Button("Go", this));
        addSouth(submissionPanel, 40);

        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitter());
        fp.add(new Label("Paste your data to analyse or try example data sets:"));
        this.textArea = new TextArea();
        this.textArea.getElement().setAttribute("style", "font-family: Consolas;");
        fp.add(this.textArea);

        add(fp);
    }

    public HandlerRegistration addGoHandler(GoHandler handler){
        return addHandler(handler, GoEvent.TYPE);
    }

    public Integer getHeight() {
        return height;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        //TODO: Check for errors!
        fireEvent(new GoEvent(textArea));
    }

    private Widget getExampleButtons(){
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
        return examples;
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

        @Source("expression.txt")
        TextResource getExpression();

        @Source("metabolomics.txt")
        TextResource getMetabolomics();
    }
}

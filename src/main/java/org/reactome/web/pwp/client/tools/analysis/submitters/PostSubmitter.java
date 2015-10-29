package org.reactome.web.pwp.client.tools.analysis.submitters;

import com.google.gwt.dom.client.Style;
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
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.client.tools.analysis.event.EmptySampleEvent;
import org.reactome.web.pwp.client.tools.analysis.event.ServiceUnavailableEvent;
import org.reactome.web.pwp.client.tools.analysis.examples.AnalysisExamples;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorHandler;
import org.reactome.web.pwp.client.tools.analysis.handler.EmptySampleHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PostSubmitter extends DockLayoutPanel implements ClickHandler {
    private static final String POST_ANALYSIS = "/AnalysisService/identifiers/?page=1";
    private static final String POST_ANALYSIS_PROJECTION = "/AnalysisService/identifiers/projection?page=1";

    private CheckBox projection;
    private TextArea textArea;
    private Image statusIcon;
    private Integer height = 310;

    public PostSubmitter() {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisBlock());
        setHeight(this.height + "px");
        getElement().getStyle().setPadding(0, Style.Unit.PX);

        FlowPanel submissionPanel = new FlowPanel();
        submissionPanel.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
        submissionPanel.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        submissionPanel.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        Button clear = new Button("Clear", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setText("");
            }
        });
        clear.setStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterClear());
        submissionPanel.add(clear);
        submissionPanel.add(new Button("GO", this));
        this.statusIcon = new Image(CommonImages.INSTANCE.loader());
        this.statusIcon.setStyleName(AnalysisStyleFactory.getAnalysisStyle().statusIcon());
        setStatusIcon(null, false, false);
        submissionPanel.add(this.statusIcon);
        this.projection = new CheckBox("Project to human");
        this.projection.setStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterCheckBox());
        this.projection.setValue(true);
        submissionPanel.add(this.projection);
        addSouth(submissionPanel, 40);

        addEast(this.getExampleButtons(), 210);

        FlowPanel north = new FlowPanel();
//        SimplePanel explanation = new SimplePanel();
//        explanation.getElement().setInnerHTML(AnalysisExamples.EXAMPLES.analysisExamples().getText());
//        north.add(explanation);
        Label label = new Label("Paste the data to analyse");
        label.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        label.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        label.getElement().getStyle().setColor("rgb(14, 124, 179)");
        north.add(label);
        addNorth(north, 30);

        this.textArea = new TextArea();
        this.textArea.getElement().setAttribute("style", "font-family: Consolas;");
        add(this.textArea);
    }

    public HandlerRegistration addAnalysisCompletedEventHandler(AnalysisCompletedHandler handler){
        return this.addHandler(handler, AnalysisCompletedEvent.TYPE);
    }

    public HandlerRegistration addAnalysisErrorEventHandler(AnalysisErrorHandler handler){
        return this.addHandler(handler, AnalysisErrorEvent.TYPE);
    }

    public HandlerRegistration addEmptySampleEventHandler(EmptySampleHandler handler){
        return this.addHandler(handler, EmptySampleEvent.TYPE);
    }

    public Integer getHeight() {
        return height;
    }

    @Override
    public void onClick(ClickEvent event) {
        if(this.textArea.getText().isEmpty()) {
            setStatusIcon(CommonImages.INSTANCE.error(),true, true);
            fireEvent(new EmptySampleEvent());
            return;
        }
        setStatusIcon(CommonImages.INSTANCE.loader(), true, false);

        String url = this.projection.getValue() ? POST_ANALYSIS_PROJECTION : POST_ANALYSIS;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(this.getData(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if(response.getStatusCode() != Response.SC_OK){
                        setStatusIcon(CommonImages.INSTANCE.error(), true, true);
                        try {
                            AnalysisError analysisError= AnalysisModelFactory.getModelObject(AnalysisError.class, response.getText());
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
            fireEvent(new ServiceUnavailableEvent());
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

    private String getData(){
        return this.textArea.getText();
    }

    private Widget getExampleButtons(){
        FlowPanel examples = new FlowPanel();
        examples.addStyleName(AnalysisStyleFactory.getAnalysisStyle().postSubmitterExamples());
        Label label = new Label("Some examples:");
        label.getElement().getStyle().setMargin(5, Style.Unit.PX);
        label.getElement().getStyle().setColor("rgb(14, 124, 179)");
        examples.add(label);
        examples.add(new Button("Uniprot accession list", new ClickHandler() {
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
}

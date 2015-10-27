package org.reactome.web.pwp.client.tools.analysis.submitters;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.pwp.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisError;
import org.reactome.web.pwp.client.common.analysis.model.AnalysisResult;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.tools.analysis.event.AnalysisErrorEvent;
import org.reactome.web.pwp.client.tools.analysis.event.FileNotSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.examples.AnalysisExamples;
import org.reactome.web.pwp.client.common.handlers.AnalysisCompletedHandler;
import org.reactome.web.pwp.client.tools.analysis.handler.AnalysisErrorHandler;
import org.reactome.web.pwp.client.tools.analysis.handler.FileNotSelectedEventHandler;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FileSubmitter extends FlowPanel  implements FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler,
        ClickHandler, HasHandlers {

    private static final String FORM_ANALYSIS = "/AnalysisService/identifiers/form?page=1";
    private static final String FORM_ANALYSIS_PROJECTION = "/AnalysisService/identifiers/form/projection?page=1";

    private FileUpload fileUpload;
    private FormPanel form;
    private CheckBox projection;
    private Image loading;

    public FileSubmitter(PostSubmitter postSubmitter) {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisBlock());

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Analyse your data"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisExamples.EXAMPLES.analysisInfo().getText());
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        add(explanation);

        FlowPanel submissionPanel = new FlowPanel();
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        submissionPanel.add(new InlineLabel("Select data file for analysis"));
        this.form = getFormPanel();
        submissionPanel.add(form);
        this.projection = new CheckBox("Project to human");
        this.projection.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisCheckBox());
        this.projection.setValue(true);
        submissionPanel.add(this.projection);
        submissionPanel.add(new Button("GO", this));
        this.loading = new Image(CommonImages.INSTANCE.loader());
        this.loading.setVisible(false);
        submissionPanel.add(this.loading);
        add(submissionPanel);

        addPostSubmitter(postSubmitter);
    }

    private void addPostSubmitter(final PostSubmitter postSubmitter){
        postSubmitter.getElement().getStyle().setDisplay(Style.Display.NONE);

        final PostSubmitterAnimation postSubmitterAnimation = new PostSubmitterAnimation(postSubmitter);
        DisclosurePanel dp = new DisclosurePanel("Click here to paste your data or try example data sets..."); dp.setWidth("100%");
        dp.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        dp.addOpenHandler(postSubmitterAnimation);
        dp.addCloseHandler(postSubmitterAnimation);
        add(dp);
        add(postSubmitter);
    }

    public HandlerRegistration addAnalysisCompletedEventHandler(AnalysisCompletedHandler handler){
        return this.addHandler(handler, AnalysisCompletedEvent.TYPE);
    }

    public HandlerRegistration addAnalysisErrorEventHandler(AnalysisErrorHandler handler){
        return this.addHandler(handler, AnalysisErrorEvent.TYPE);
    }

    public HandlerRegistration addFileNotSelectedEventHandler(FileNotSelectedEventHandler handler){
        return this.addHandler(handler, FileNotSelectedEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent event) {
        String fileName = this.fileUpload.getFilename();
        if(fileName==null || fileName.isEmpty()){
            fireEvent(new FileNotSelectedEvent());
            return;
        }

        if(this.projection.getValue()){
            form.setAction(FORM_ANALYSIS_PROJECTION);
        }else{
            form.setAction(FORM_ANALYSIS);
        }
        this.form.submit();
    }

    @Override
    public void onSubmit(FormPanel.SubmitEvent event) {
        this.loading.setVisible(true);
    }

    @Override
    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
        //Work around to extract the content in case it's included in a HTML tag
        Element label = DOM.createLabel();
        label.setInnerHTML( event.getResults() );
        String json = label.getInnerText();
        try {
            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, json);
            fireEvent(new AnalysisCompletedEvent(result));
            this.loading.setVisible(false);
        } catch (AnalysisModelException e) {
            this.loading.setVisible(false);
            try {
                AnalysisError analysisError = AnalysisModelFactory.getModelObject(AnalysisError.class, json);
                fireEvent(new AnalysisErrorEvent(analysisError));
            } catch (AnalysisModelException e1) {
                Console.error("Oops! This is unexpected", this);
            }
//            switch (analysisError.getCode()){
//                case 413:
//                    fireEvent(new AnalysisErrorEvent(AnalysisErrorType.FILE_SIZE_ERROR));
//                    break;
//                case 415:
//                    fireEvent(new AnalysisErrorEvent(AnalysisErrorType.PROCESSING_DATA));
//                    break;
//                case 500:
//                    fireEvent(new AnalysisErrorEvent(AnalysisErrorType.SERVICE_UNAVAILABLE));
//                    break;
//                default:
//                    fireEvent(new AnalysisErrorEvent(AnalysisErrorType.RESULT_FORMAT));
//            }
        }
    }

    private FormPanel getFormPanel(){
        FormPanel form = new FormPanel();
        this.fileUpload = new FileUpload();
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        this.fileUpload.setName("file");
        Console.info( this.fileUpload.getElement().getInnerHTML());
//        this.fileUpload.getElement().setAttribute("accept", ".txt");
        this.fileUpload.setTitle("Select a file to analyse");
        form.add(this.fileUpload);


        this.fileUpload.setEnabled(true);
        form.addSubmitHandler(this);
        form.addSubmitCompleteHandler(this);
        return form;
    }

    private class PostSubmitterAnimation extends Animation implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel> {
        private PostSubmitter widget;
        private Integer height;
        private boolean open;

        PostSubmitterAnimation(PostSubmitter postSubmitter) {
            this.widget = postSubmitter;
            this.height = postSubmitter.getHeight();
        }

        @Override
        protected void onUpdate(double progress) {
            progress = open ? progress : 1.0 - progress;
            double aux = Math.floor(progress * height);
            if(aux>50 && !this.widget.getElement().getStyle().getDisplay().equals(Style.Display.INLINE.name())){
                this.widget.getElement().getStyle().setDisplay(Style.Display.INLINE);
            }else if(!this.widget.getElement().getStyle().getDisplay().equals(Style.Display.NONE.name())){
                this.widget.getElement().getStyle().setDisplay(Style.Display.NONE);
            }
            this.widget.setHeight(aux + "px");
        }

        @Override
        public void onClose(CloseEvent<DisclosurePanel> event) {
            this.cancel();
            this.open = false;
            run(500);
        }

        @Override
        public void onOpen(OpenEvent<DisclosurePanel> event) {
            this.cancel();
            this.open = true;
            run(500);
        }
    }
}

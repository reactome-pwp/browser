package org.reactome.web.pwp.client.tools.analysis.wizard.submitters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FileSubmitter extends FlowPanel implements ClickHandler {

    private FormPanel form;
    private FileUpload fileUpload;

    private FlowPanel errorPanel;
    private InlineLabel errorHolder;

    public FileSubmitter() {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());

        InlineLabel submissionPanel = new InlineLabel("Select data file for analysis:");
        add(submissionPanel);

        form = new FormPanel();
        fileUpload = new FileUpload();
        fileUpload.setName("file");
        fileUpload.setTitle("Select a file to analyse");
        form.add(fileUpload);

        add(form);

        add(errorPanel = getErrorHolder());

        add(new Button("Continue", this));
    }

    public HandlerRegistration addGoHandler(GoHandler handler) {
        return addHandler(handler, GoEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        errorPanel.getElement().getStyle().setOpacity(0);
        if (fileUpload.getFilename() == null || fileUpload.getFilename().isEmpty()) {
            errorHolder.setText("Please choose a file to continue...");
            errorPanel.getElement().getStyle().setOpacity(1);
            (new Timer(){
                @Override
                public void run() {
                    errorPanel.getElement().getStyle().setOpacity(0);
                }
            }).schedule(4000);
        } else {
            fireEvent(new GoEvent(form));
        }
    }

    @SuppressWarnings("Duplicates")
    private FlowPanel getErrorHolder(){
        FlowPanel fp = new FlowPanel();
        fp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().errorMessage());
        fp.add(new Image(CommonImages.INSTANCE.error()));
        fp.add(errorHolder = new InlineLabel());
        return fp;
    }
}

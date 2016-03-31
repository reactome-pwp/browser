package org.reactome.web.pwp.client.tools.analysis.wizard.submitters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.GoEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.GoHandler;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FileSubmitter extends FlowPanel implements ClickHandler {

    private FormPanel form;

    public FileSubmitter() {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());

        InlineLabel submissionPanel = new InlineLabel("Select data file for analysis:");

        add(submissionPanel);

        form = new FormPanel();
        FileUpload fileUpload = new FileUpload();
        fileUpload.setName("file");
        fileUpload.setTitle("Select a file to analyse");
        form.add(fileUpload);

        add(form);
        add(new Button("GO", this));
    }

    public HandlerRegistration addGoHandler(GoHandler handler){
        return addHandler(handler, GoEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        //TODO: Check for errors!
        fireEvent(new GoEvent(form));
    }
}

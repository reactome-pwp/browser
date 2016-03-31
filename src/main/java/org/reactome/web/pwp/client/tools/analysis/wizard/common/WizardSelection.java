package org.reactome.web.pwp.client.tools.analysis.wizard.common;

import com.google.gwt.user.client.ui.FormPanel;

/**
 * This object is shared by all the steps in the wizard. Each step will read from it in order to
 * set the view up and will also write in it when the user continues to the next step.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class WizardSelection {

    public enum UserSampleType {FILE, POST}

    private UserSampleType sampleType;

    private FormPanel form;

    private String postData;

    private boolean projectToHuman;

    private boolean includeInteractors;

    public WizardSelection() {
        this.projectToHuman = true;
        this.includeInteractors = false;
    }

    public UserSampleType getSampleType() {
        return sampleType;
    }

    public FormPanel getForm() {
        return form;
    }

    public void setForm(FormPanel form) {
        this.sampleType = UserSampleType.FILE;
        this.form = form;
        this.postData = null;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.sampleType = UserSampleType.POST;
        this.postData = postData;
        this.form = null;
    }

    public boolean isProjectToHuman() {
        return projectToHuman;
    }

    public void setProjectToHuman(boolean projectToHuman) {
        this.projectToHuman = projectToHuman;
    }

    public boolean isIncludeInteractors() {
        return includeInteractors;
    }

    public void setIncludeInteractors(boolean includeInteractors) {
        this.includeInteractors = includeInteractors;
    }
}

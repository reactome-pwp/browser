package org.reactome.web.pwp.client.tools.analysis.event;

import com.google.gwt.http.client.Response;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum AnalysisErrorType {

//    SERVICE_UNAVAILABLE ("The analysis service is temporarily unavailable. Please wait a moment and resubmit your data"),
    FILE_SIZE_ERROR("Maximum file size exceeded. Please send a file up to 10MB"),
    PROCESSING_DATA ("The file format is incorrect. Please check it and resubmit your data again"),
    RESULT_FORMAT ("Error processing the result. Please get in touch with our help desk at help@reactome.org"),
//    FILE_NOT_SELECTED ("Please select a file to analyse"),
    FROM_RESPONSE ("Unknown");

    private String message;

    AnalysisErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public AnalysisErrorType setMessage(Response response){
        this.message = response.getStatusText();
        return this;
    }

    @Override
    public String toString() {
        return message;
    }
}
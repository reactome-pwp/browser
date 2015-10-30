package org.reactome.web.pwp.client.common.analysis.model;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisError {

    Integer getCode();

    String getReason();

    List<String> getMessages();
}

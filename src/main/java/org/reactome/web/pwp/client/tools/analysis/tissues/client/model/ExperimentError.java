package org.reactome.web.pwp.client.tools.analysis.tissues.client.model;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ExperimentError {

    /**
     * The error code
     */
    Integer getCode();

    /**
     * The general reason of this error
     */
    String getReason();

    /**
     * A list of detailed errors
     */
    List<String> getMessages();

}

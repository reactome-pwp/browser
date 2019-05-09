package org.reactome.web.pwp.client.tools.analysis.gsa.client.model;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface GSAError {

    /**
     * The status code
     */
    Integer getStatus();

    /**
     * The title of the error
     */
    String getTitle();

    /**
     * The details of the error
     */
    String getDetail();


}

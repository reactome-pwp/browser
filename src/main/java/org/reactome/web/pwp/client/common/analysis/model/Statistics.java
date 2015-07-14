package org.reactome.web.pwp.client.common.analysis.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Statistics {

    String getResource();

    Integer getTotal();

    Integer getFound();

    Double getRatio();
}

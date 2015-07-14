package org.reactome.web.pwp.client.common.analysis.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisSummary {

    String getToken();

    String getType();

    Long getSpecies();

    String getFileName();

    boolean isText();

    String getSampleName();

    //IMPORTANT: next methods are not in the Analysis RESTFul service data model
    //definition, but we need them to set the name after retrieval so the UI
    //can use it later one for the summary
    void setSpeciesName(String name);
    String getSpeciesName();
}

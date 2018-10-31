package org.reactome.web.pwp.client.tools.analysis.tissues.client.model;

import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ExperimentSummary {
    Integer getId();

    String getName();

    String getDescription();

    String getResource();

    String getUrl();

    String getTimestamp();

    Integer getNumberOfGenes();

    Map<String, Integer> getTissuesMap();
}

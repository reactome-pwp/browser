package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface Status {

    Double getCompleted();

    String getDescription();

    String getId();

    String getStatus();

    List<Report> getReports();

}

package org.reactome.web.pwp.client.tools.analysis.gsa.client.model;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface Method {

    String getDescription();

    String getName();

    List<Parameter> getParameters();
}

package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ResultLinks {

    String getRelease();

    @PropertyName("reactome_links")
    List<Link> getReactomeLinks();
}

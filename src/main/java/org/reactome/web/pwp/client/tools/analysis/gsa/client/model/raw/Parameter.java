package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface Parameter {

    String getDefault();

    String getDescription();

    String getName();

    @AutoBean.PropertyName("display_name")
    String getDisplayName();

    String getScope();

    String getType();

    List<String> getValues();

}

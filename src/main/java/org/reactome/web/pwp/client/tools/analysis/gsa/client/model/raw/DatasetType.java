package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface DatasetType {

    String getDescription();

    @AutoBean.PropertyName("id")
    String getType();

    String getName();

    default String getColour() {
        return DatasetTypesPalette.get().colourFromType(getType());
    }
}

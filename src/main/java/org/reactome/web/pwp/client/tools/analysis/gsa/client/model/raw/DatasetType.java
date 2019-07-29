package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface DatasetType {

    String getDescription();

    String getId();

    String getName();

    default String getColour() {
        return DatasetTypesPalette.get().colourFromType(getId());
    }
}

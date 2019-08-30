package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ExampleDataset {

    String getId();

    String getTitle();

    String getDescription();

    String getGroup();

    String getType();

    default String getColour() {
        return DatasetTypesPalette.get().colourFromType(getType());
    }

}

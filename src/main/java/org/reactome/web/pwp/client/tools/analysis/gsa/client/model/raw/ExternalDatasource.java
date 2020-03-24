package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ExternalDatasource {

    String getId();

    String getName();

    List<Parameter> getParameters();

    default String getColour() {
        return DatasetTypesPalette.get().colourFromType(getName());
    }

}

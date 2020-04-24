package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface ExampleDatasetSummary {

    String getId();

    String getTitle();

    String getDescription();

    String getGroup();

    String getType();

    default String getColour() {
        return DatasetTypesPalette.get().colourFromType(getType());
    }

    @AutoBean.PropertyName("sample_ids")
    List<String> getSampleIds();

    @AutoBean.PropertyName("sample_metadata")
    List<ExampleMetadata> getMetadata();

    @AutoBean.PropertyName("default_parameters")
    List<ExampleParameter> getDefaultParameters();

}

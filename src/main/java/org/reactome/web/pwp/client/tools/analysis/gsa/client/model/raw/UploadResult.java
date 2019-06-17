package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface UploadResult {

    @AutoBean.PropertyName("sample_names")
    List<String> getSampleNames();

    @AutoBean.PropertyName("top_identifiers")
    List<String> getTopIdentifiers();

    @AutoBean.PropertyName("n_lines")
    Integer getNumberOfLines();

    @AutoBean.PropertyName("data_token")
    String getDataToken();

}

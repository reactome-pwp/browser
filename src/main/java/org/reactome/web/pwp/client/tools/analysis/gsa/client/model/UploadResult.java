package org.reactome.web.pwp.client.tools.analysis.gsa.client.model;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface UploadResult {

//    @AutoBean.PropertyName("sample_names")
    List<String> getSample_names();

//    @AutoBean.PropertyName("top_identifiers")
    List<String> getTop_identifiers();

//    @AutoBean.PropertyName("n_lines")
    Integer getN_lines();

//    @AutoBean.PropertyName("data_token")
    String getData_token();

}

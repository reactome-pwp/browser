package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface Status {

    Double getCompleted();

    String getDescription();

    String getId();

    String getStatus();

    List<Report> getReports();

    @AutoBean.PropertyName("dataset_id")
    String getDatasetId();

}

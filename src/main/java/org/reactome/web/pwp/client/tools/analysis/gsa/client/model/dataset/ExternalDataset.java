package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.dtos.ParameterDTO;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.JSON;

import java.util.HashMap;
import java.util.Map;

public class ExternalDataset {

    private static final String KEY = "dataset_id";

    private String resourceId;
    private Map<String, String> parameters;

    public ExternalDataset(String resourceId) {
        this(resourceId, null);
    }

    public ExternalDataset(String resourceId, Map<String, String> parameters) {
        this.resourceId = resourceId;
        this.parameters = (parameters == null) ? new HashMap<>() : parameters;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void addDatasetId(String datasetId) {
        parameters.put(KEY, datasetId);
    }

    public String getDatasetId() {
        return parameters.get(KEY);
    }

    /**
     * Convert parameters map into a JSON key,value format
     *
     * @return the json
     */
    public String getPostData() {
        ParameterDTO[] params = (ParameterDTO[]) parameters.entrySet().stream().map(par -> ParameterDTO.create(par.getKey(), par.getValue())).toArray();
        return JSON.stringify(params);
    }
}

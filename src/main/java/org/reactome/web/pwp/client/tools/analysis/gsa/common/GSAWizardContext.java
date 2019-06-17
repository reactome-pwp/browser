package org.reactome.web.pwp.client.tools.analysis.gsa.common;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;

import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizardContext {
    private String method;
    private Map<String, String> parameters;
    private GSADataset datasetToAnnotate;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public GSADataset getDatasetToAnnotate() {
        return datasetToAnnotate;
    }

    public void setDatasetToAnnotate(GSADataset datasetToAnnotate) {
        this.datasetToAnnotate = datasetToAnnotate;
    }

    @Override
    public String toString() {
        return "GSAWizardContext{" +
                "method='" + method + '\'' +
                ", parameters=" + parameters +
                ", datasetToAnnotate=" + datasetToAnnotate +
                '}';
    }
}

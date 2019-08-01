package org.reactome.web.pwp.client.tools.analysis.gsa.common;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.dtos.AnalysisDTO;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps the context during all the steps of the GSA wizard, including
 * information about the selected analysis method, the set parameters,
 * the annotated datasets.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAWizardContext {
    private Method method;
    private Map<String, String> parameters = new HashMap<>();
    private GSADataset datasetToAnnotate;
    private List<GSADataset> annotatedDatasets = new ArrayList<>();

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setParameter(String key, String value) {
        parameters.put(key, value);
    }

    public GSADataset getDatasetToAnnotate() {
        return GSADataset.create(datasetToAnnotate);
    }

    public void setDatasetToAnnotate(GSADataset datasetToAnnotate) {
        this.datasetToAnnotate = datasetToAnnotate;
    }

    public List<GSADataset> getAnnotatedDatasets() {
        return annotatedDatasets;
    }

    public void addDataset(GSADataset dataset) {
        annotatedDatasets.add(dataset);
    }

    @Override
    public String toString() {
        return "GSAWizardContext{" +
                "method='" + (method != null ? method.getName() : null) + '\'' +
                ", parameters=" + parameters +
                ", datasetToAnnotate=" + datasetToAnnotate +
                ", datasets=" + annotatedDatasets.size() +
                '}';
    }

    public String toJSON() {
        AnalysisDTO dto = AnalysisDTO.create(method, annotatedDatasets, parameters);
        return JSON.stringify(dto);
    }
}

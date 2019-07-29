package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.dtos;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;

import java.util.List;
import java.util.Map;

import static jsinterop.annotations.JsPackage.GLOBAL;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@JsType(namespace = GLOBAL, name = "Object", isNative = true)
public class AnalysisDTO {
    private String methodName;
    private DatasetDTO[] datasets;
    private ParameterDTO[] parameters;

    private AnalysisDTO() {
        // Nothing here
    }

    @JsOverlay
    @SuppressWarnings("Duplicates")
    public static AnalysisDTO create(Method method, List<GSADataset> annotatedDatasets, Map<String, String> parameters) {
        if (method == null) return null;

        AnalysisDTO rtn = new AnalysisDTO();
        rtn.methodName = method.getName();

        rtn.datasets = new DatasetDTO[annotatedDatasets.size()];

        for (int i = 0; i < annotatedDatasets.size(); i++) {
            GSADataset dataset = annotatedDatasets.get(i);
            rtn.datasets[i] = DatasetDTO.create(dataset);
        }

        if (parameters != null) {
            rtn.parameters = (ParameterDTO[]) parameters.entrySet().stream()
                                                                   .map(par -> ParameterDTO.create(par.getKey(), par.getValue()))
                                                                   .toArray();
        }
        return rtn;
    }
}

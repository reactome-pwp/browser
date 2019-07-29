package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.dtos;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;

import static jsinterop.annotations.JsPackage.GLOBAL;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@JsType(namespace = GLOBAL, name = "Object", isNative = true)
public class DatasetDTO {
    private String name;
    private String type;
    private String data;

    private JsPropertyMap<Object> design;

    private ParameterDTO[] parameters;

    protected DatasetDTO() {
        // Nothing here
    }

    @JsOverlay
    @SuppressWarnings("Duplicates")
    public static DatasetDTO create(GSADataset dataset) {
        Console.info("dataset: " + dataset.toString());
        DatasetDTO rtn = new DatasetDTO();
        rtn.name = dataset.getName();
        rtn.type = dataset.getType();
        rtn.data = dataset.getDataToken();

        String selectedComparisonFactor = dataset.getAnnotations().getSelectedComparisonFactor();
        AnnotationProperty comparisonProperty = dataset.getAnnotations().findAnnotationPropertyByName(selectedComparisonFactor);

        if (comparisonProperty != null) {
            rtn.design = JsPropertyMap.of();
            rtn.design.set("analysisGroup", comparisonProperty.getValues());
        }

        if (dataset.getSampleNames() != null && !dataset.getSampleNames().isEmpty()) {
            rtn.design.set("samples", dataset.getSampleNames().toArray());
        }

        if (dataset.getAnnotations().getGroupOne() != null && dataset.getAnnotations().getGroupTwo() != null) {
            JsPropertyMap<Object> comparison = JsPropertyMap.of();
            comparison.set("group1", dataset.getAnnotations().getGroupOne());
            comparison.set("group2", dataset.getAnnotations().getGroupTwo());
            rtn.design.set("comparison", comparison);
        }


        if (dataset.getAnnotations().getCovariates() != null) {
            for (AnnotationProperty covariate : dataset.getAnnotations().getCovariates()) {
                rtn.design.set(covariate.getName(), covariate.getValues());
            }
        }

        if (dataset.getParameters() != null && !dataset.getParameters().isEmpty()) {
            rtn.parameters = (ParameterDTO[]) dataset.getParameters().entrySet().stream()
                                                                          .map(par -> ParameterDTO.create(par.getKey(), par.getValue()))
                                                                          .toArray();
        }

        return rtn;
    }
}

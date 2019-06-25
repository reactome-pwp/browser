package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Annotations {
    private static Map<String, Integer> index = new HashMap<>();
    private Map<String, AnnotationProperty> annotationPropertyMap = new LinkedHashMap<>();

    public Annotations(List<String> sampleNames) {
        for (int i = 0; i < sampleNames.size(); i++) {
            index.put(sampleNames.get(i), i);
        }
    }

    public boolean isAnnotationNameValid(String name) {
        return !annotationPropertyMap.containsKey(name);
    }

    public void createAnnotationProperty(String name) {
        if (!isAnnotationNameValid(name)) return;

        AnnotationProperty annotationProperty = new AnnotationProperty(name, index.size());
        annotationPropertyMap.put(name, annotationProperty);
    }

    public void deleteAnnotationProperty(String name) {
        annotationPropertyMap.remove(name);
    }

}

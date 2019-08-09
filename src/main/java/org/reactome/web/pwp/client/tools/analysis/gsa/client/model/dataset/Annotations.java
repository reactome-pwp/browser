package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Annotations {
    private static RegExp regExp = RegExp.compile("^[a-zA-Z\\d_]+$");

    private Map<String, Integer> index = new HashMap<>();
    private List<AnnotationProperty> annotationPropertyList = new ArrayList<>();

    private String selectedComparisonFactor;
    private String groupOne;
    private String groupTwo;
    private List<AnnotationProperty> covariates = new ArrayList<>();

    public Annotations(List<String> sampleNames) {
        index.clear();
        if (sampleNames != null) {
            for (int i = 0; i < sampleNames.size(); i++) {
                index.put(sampleNames.get(i), i);
            }
        }
    }

    private Annotations() {

    }

    @SuppressWarnings("unchecked")
    public static Annotations create(Annotations annotations) {
        Annotations copy = new Annotations();
        copy.index = new HashMap(annotations.index);
        copy.annotationPropertyList = new ArrayList<>(annotations.annotationPropertyList);
        copy.selectedComparisonFactor = annotations.selectedComparisonFactor;
        copy.groupOne = annotations.groupOne;
        copy.groupTwo = annotations.groupTwo;
        copy.covariates = new ArrayList<>(annotations.covariates);

        return copy;
    }

    public boolean isAnnotationNameValid(AnnotationProperty targetAnnotationProperty, String newName) {
        boolean rtn = true;

        if (regExp.exec(newName) == null) {
            rtn = false;
            return rtn;
        }

        for (AnnotationProperty annotationProperty : annotationPropertyList) {
            if(targetAnnotationProperty.equals(annotationProperty)) continue;
            if (annotationProperty.getName().equalsIgnoreCase(newName)) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }

    public boolean annotationNameExists(String newName) {
        boolean rtn = false;
        for (AnnotationProperty annotationProperty : annotationPropertyList) {
            if (annotationProperty.getName().equalsIgnoreCase(newName)) {
                rtn = true;
                break;
            }
        }
        return rtn;
    }

    public AnnotationProperty createAnnotationProperty(String name) {
        if (annotationNameExists(name)) return null;

        AnnotationProperty annotationProperty = new AnnotationProperty(name, index.size());
        annotationPropertyList.add(annotationProperty);

        return annotationProperty;
    }

    public AnnotationProperty findAnnotationPropertyByName(String name) {
        AnnotationProperty rtn = null;
        for (AnnotationProperty annotationProperty : annotationPropertyList) {
            if (annotationProperty.getName().equalsIgnoreCase(name)) {
                rtn = annotationProperty;
                break;
            }
        }
        return rtn;
    }

    public void deleteAnnotationProperty(AnnotationProperty annotationProperty) {
        annotationPropertyList.remove(annotationProperty);
    }

    public List<AnnotationProperty> getAllAnnotations() {
        return annotationPropertyList;
    }


    public String getSelectedComparisonFactor() {
        return selectedComparisonFactor;
    }

    public void setSelectedComparisonFactor(String selectedComparisonFactor) {
        this.selectedComparisonFactor = selectedComparisonFactor;
    }

    public String getGroupOne() {
        return groupOne;
    }

    public void setGroupOne(String groupOne) {
        this.groupOne = groupOne;
    }

    public String getGroupTwo() {
        return groupTwo;
    }

    public void setGroupTwo(String groupTwo) {
        this.groupTwo = groupTwo;
    }

    public List<AnnotationProperty> getCovariates() {
        return covariates;
    }

    public void setCovariates(List<AnnotationProperty> covariates) {
        this.covariates = covariates;
    }
}

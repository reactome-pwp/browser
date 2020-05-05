package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class AnnotationProperty {
    public static final String EMPTY = "";

    private String name;
    private String[] values;
    private boolean isChecked;
    private Set<String> uniqueValues;

    public AnnotationProperty(String name, int size) {
        this.name = name;
        values = new String[size];

        for (int i = 0; i < size; i++) {
            values[i] = EMPTY;
        }
    }

    public AnnotationProperty(String name, String[] values) {
        this.name = name;
        this.values = values;
    }

    public void resetAtIndex(int index) {
        if (isIndexCorrect(index)) return;
        values[index] = EMPTY;
    }

    public void resetAll() {
        for (int i = 0; i < values.length ; i++) {
            resetAtIndex(i);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return values;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isIndexCorrect(int index) {
        return !(index < 0 || index >= values.length);
    }

    /**
     * Property should only be visible if it contains more than one unique value;
     * e.g values [p1,p1,p1,p1] = visible false;
     * e.g values [p1,p2,p1,p2] = visible true;
     */
    public boolean isVisible() {
        uniqueValues = new HashSet<>(Arrays.asList(values));
        uniqueValues.removeAll(Arrays.asList("", null));
        return uniqueValues.size() > 1;
    }

    @Override
    public String toString() {
        return "AnnotationProperty{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                ", isChecked=" + isChecked +
                '}';
    }
}

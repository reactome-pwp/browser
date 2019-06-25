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
    private Set<String> dictionary;

    public AnnotationProperty(String name, int size) {
        this.name = name;
        values = new String[size];
        dictionary = new HashSet<>();

        for (int i = 0; i < size; i++) {
            values[i] = EMPTY;
        }
    }

    public boolean setValueAtIndex(int index, String value) {
        if (isIndexCorrect(index)) return false;
        boolean rtn;
        if (rtn = dictionary.add(value)) {
            values[index] = value;
        }
        return rtn;
    }

    public String getValueAtIndex(int index) {
        String rtn = null;
        if (isIndexCorrect(index)) {
            rtn = values[index];
        }
        return rtn;
    }

    public void resetAtIndex(int index) {
        if (isIndexCorrect(index)) return;
        dictionary.remove(values[index]);
        values[index] = EMPTY;
    }

    public void resetAll() {
        dictionary.clear();
        for (int i = 0; i < values.length ; i++) {
            resetAtIndex(i);
        }
    }

    public String getName() {
        return name;
    }

    public String[] getValues() {
        return values;
    }

    private boolean isIndexCorrect(int index) {
        return !(index < 0 || index >= values.length);
    }

    @Override
    public String toString() {
        return "AnnotationProperty{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                ", dictionary=" + dictionary +
                '}';
    }
}

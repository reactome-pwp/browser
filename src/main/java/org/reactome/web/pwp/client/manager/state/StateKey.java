package org.reactome.web.pwp.client.manager.state;

import java.util.Arrays;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum StateKey {
    SPECIES     ("SPECIES" , "FOCUS_SPECIES_ID"),
    PATHWAY     ("PATHWAY", "DIAGRAM", "FOCUS_PATHWAY_ID"),
    SELECTED    ("SEL", "ID"),
    PATH        ("PATH"),
    DETAILS_TAB ("DTAB", "DETAILS_TAB"),
    TOOL        ("TOOL"),
    ANALYSIS    ("ANALYSIS", "ANALYSIS_ID"),
    FLAG        ("FLG", "FLAG", "ACC", "UNIPROT", "GENE", "CHEBI", "ENSEMBL");

    List<String> keys;

    StateKey(String... keys) {
        this.keys = Arrays.asList(keys);
    }

    public static StateKey getAdvancedStateKey(String key) {
        for (StateKey advancedStateKey : values()) {
            if (advancedStateKey.keys.contains(key)) {
                return advancedStateKey;
            }
        }
        return null;
    }

    public String getDefaultKey() {
        return keys.get(0);
    }
}

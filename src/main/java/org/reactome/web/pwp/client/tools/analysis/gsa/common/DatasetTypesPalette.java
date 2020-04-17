package org.reactome.web.pwp.client.tools.analysis.gsa.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a standard colour for each dataset type
 * E.g. Proteomics, Microarray, RNA-seq etc.
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DatasetTypesPalette {

    private static DatasetTypesPalette instance;
    private static int index = 0;

    // Color name: https://abouts.co/color/FC6E51
    private List<String> colourPalette = Arrays.asList(
            "#ED5565", // GRAPEFRUIT
            "#A0D468", // GRASS
            "#48CFAD", // MINT
            "#4FC1E9", // AQUA
            "#4A89DC", // BLUE JEANS
            "#AC92EC", // LAVANDER
            "#D770AD", // PINK ROSE
            "#FC6E51", // BITTERSWEET
            "#FFCE54", // SUNFLOWER
            "#C6AA76", // LATTE
            "#e3adb5", // ROSEFOG
            "#684771", // CYBERGRAPE
            "#587a8f"  // SMALTBLUE
    );

    // 20% darker - https://www.hexcolortool.com/
    private  Map<String, String> darkerColourPalette  = new HashMap<String, String>() {{
        put("#ED5565", "#BE4451");
        put("#A0D468", "#80AA53");
        put("#48CFAD", "#3AA68A");
        put("#4FC1E9", "#3F9ABA");
        put("#4A89DC", "#3B6EB0");
        put("#AC92EC", "#8A75BD");
        put("#D770AD", "#AC5A8A");
        put("#FC6E51", "#CA5841");
        put("#FFCE54", "#CCA543");
        put("#C6AA76", "#937743");
        put("#e3adb5", "#B07A82");
        put("#684771", "#35143E");
        put("#587a8f", "#25475C");
    }};

    private Map<String, String> typesMap = new HashMap<>();

    public static DatasetTypesPalette get() {
        if (instance == null) {
            instance = new DatasetTypesPalette();
        }
        return instance;
    }

    public String colourFromType(String type) {
        return typesMap.computeIfAbsent(type, key -> {
            if (index >= colourPalette.size()) {
                index = colourPalette.size() - 1;
            }

            return colourPalette.get(index++);
        });
    }

    public String darkerColourFromType(String type) {
        return darkerColourPalette.get(typesMap.get(type));
    }

}

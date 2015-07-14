package org.reactome.web.pwp.client.details.tabs.molecules.model.type;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */

/**
 * PropertyType provides a type for each possible group of molecules and a download-type.
 */
public enum PropertyType {

    PROTEINS("Proteins"),
    CHEMICAL_COMPOUNDS("Chemical Compounds"),
    SEQUENCES("DNA/RNA"),
    OTHERS("Others"),
    DOWNLOAD("Download");

    private final String title;

    PropertyType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Get property type for a string.
     * @param type name of required PropertyType
     * @return PropertyType or null if string does not correspond to a type
     */
    public static PropertyType getPropertyType(String type){
        for (PropertyType pt : values()) {
            if(pt.title.equals(type)){
                return pt;
            }
        }
        return null;
    }

}
package org.reactome.web.pwp.client.details.tabs.molecules.model.type;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public enum PathwayType {

    OGB("only green boxes"),
    NGB("no green boxes"),
    CGB("contains green boxes");

    private final String title;

    PathwayType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Get pathway type for a string.
     * @param type name of required PathwayType
     * @return PathwayType or null if string does not correspond to a type
     */
    public static PathwayType getPathwayType(String type){
        for (PathwayType pt : values()) {
            if(pt.title.equals(type)){
                return pt;
            }
        }
        return null;
    }

}

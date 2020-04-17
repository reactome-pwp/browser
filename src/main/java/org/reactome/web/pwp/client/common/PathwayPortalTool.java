package org.reactome.web.pwp.client.common;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum PathwayPortalTool {

    NONE        ("XX", "No Tool"),
    ANALYSIS    ("AT", "Analysis Tool"),
    CITATION     ("CT", "Cite Us!");

    private String code;
    private String title;

    PathwayPortalTool(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static PathwayPortalTool getByCode(String code){
        for (PathwayPortalTool pathwayPortalTool : values()) {
            if(pathwayPortalTool.getCode().equals(code)){
                return pathwayPortalTool;
            }
        }
        return getDefault();
    }

    public String getCode() {
        return code;
    }

    public static PathwayPortalTool getDefault(){
        return NONE;
    }

    public static int getDefaultIndex(){
        return getIndex(getDefault());
    }

    public static int getIndex(PathwayPortalTool type){
        int index = 0;
        for (PathwayPortalTool pathwayPortalTool : values()) {
            if(pathwayPortalTool.equals(type))
                return index;
            index++;
        }
        return PathwayPortalTool.getDefaultIndex();
    }

    public String getTitle() {
        return title;
    }

}

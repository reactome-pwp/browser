package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public enum ThumbStatus {
    NORMAL("#02a0e3"),
    HOVERED("#35c1fd"),
    CLICKED("#02a0e3");

    String colour;

    ThumbStatus(String colour) {
        this.colour = colour;
    }
}

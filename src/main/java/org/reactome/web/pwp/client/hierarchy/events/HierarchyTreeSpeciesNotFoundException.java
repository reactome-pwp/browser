package org.reactome.web.pwp.client.hierarchy.events;

/**
 * Hierarchy tree species not available associated exception
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyTreeSpeciesNotFoundException extends Exception {

    public HierarchyTreeSpeciesNotFoundException() {
        super("Species not available");
    }
}

package org.reactome.web.pwp.client.details.tabs.processes.model.widgets.factory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum PropertyType {

    IN_PATHWAYS("Involved in pathways"),
    IN_REACTIONS_AS_INPUT("Is an input in reactions"),
    IN_REACTIONS_AS_OUTPUT("Is an output in reactions"),
    IN_MODIFIED_RESIDUE("Has modified residues"),
    IN_COMPLEX_AS_COMPONENT("Is present in complexes"),
    IN_ENTITY_SET_AS_COMPONENT("Is present in sets"),
    OTHER_FORMS_FOR_EWAS("Other forms of this molecule");

    private String title;

    PropertyType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

package org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum PropertyType {

    DOI("DOI"),
    SUMMATION("Summation"),
    DISEASE("Disease"),
    CROSS_REFERENCES("External identifiers"),
    REFERENCE_ENTITY("Links to this entry in other databases"),
    RELATED_IDENTIFIER("Other identifiers related to this sequence"),
    ENTITY_FUNCTIONAL_STATUS("Functional status"),
    INPUT("Input"),
    OUTPUT("Output"),
    CATALYST("Catalyst Activity"),
    POSITIVELY_REGULATED("Positively regulated by"),
    NEGATIVELY_REGULATED("Negatively regulated by"),
    PRECEDING_EVENTS("Preceding Event(s)"),
    FOLLOWING_EVENTS("Following Event(s)"),
    MODIFICATION("Post-translational modification"),
    COORDINATES("Coordinates in the Reference Sequence"),
    CELLULAR_COMPARTMENT("Cellular compartment"),
    HAS_COMPONENT("Components"),
    TEMPLATE("Template event"),
    NORMAL_REACTION("Normal reaction"),
    NORMAL_PATHWAY("Normal parthway"),

    HAS_MEMBER("Members"),
    HAS_CANDIDATE("Candidates"),

    INFERRED_FROM("Inferred from another species"),
    INFERRED_TO("View computationally predicted event in"),
    GO_BIOLOGICAL_PROCESS("Represents GO Biological Process"),
    //GO_MOLECULAR_FUNCTION("Represents GO Molecular Function"),

    PRODUCED_BY("Produced by"),
    CONSUMED_BY("Consumed by"),
    DEDUCED_FROM("Deduced from the existence of"),
    DEDUCED_ON("Computationally inferred orthologues"), //DO NOT CHANGE BEFORE ASKING Steve
    ENTITY_ON_OTHER_CELL("Entity on other cell"),
    REQUIRED_INPUT("Required input components"),

    REGULATOR("Has regulators"), //NOTE: REGULATOR and REGULATION are different concepts

    MAX_UNITS("Max units count"),
    MIN_UNITS("Min units count"),
    REPEATED_UNITS("Repeated units"),

    REVERSE_REACTION("Reverse reaction"),

    //CATALYSES_EVENTS("Catalyses events"),
    REFERENCES("References"),

    //Keep these at the end, no so important flags :)
    CREATED("Created"),
    MODIFIED("Modified"),
    AUTHORED("Authored"),
    REVIEWED("Reviewed"),
    REVISED("Revised");

    private String title;

    PropertyType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
package org.reactome.web.pwp.client.details.tabs.analysis.widgets.common;


import org.reactome.web.analysis.client.model.IdentifierSummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InteractorAux {

    private IdentifierSummary identifier;
    private String interactsWith;

    public InteractorAux(IdentifierSummary identifier, String interactsWith) {
        this.identifier = identifier;
        this.interactsWith = interactsWith;
    }

    public IdentifierSummary getIdentifier() {
        return identifier;
    }

    public String getInteractsWith() {
        return interactsWith;
    }
}

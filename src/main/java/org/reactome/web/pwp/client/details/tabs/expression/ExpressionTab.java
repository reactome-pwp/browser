package org.reactome.web.pwp.client.details.tabs.expression;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.classes.ReferenceSequence;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ExpressionTab {

    interface Presenter extends DetailsTab.Presenter {
        void getReferenceSequences(DatabaseObject databaseObject);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(DatabaseObject databaseObject);
        void showProteins(DatabaseObject databaseObject);
        void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceSequence> referenceSequenceList);
        void showPathway(Pathway pathway);
    }
}

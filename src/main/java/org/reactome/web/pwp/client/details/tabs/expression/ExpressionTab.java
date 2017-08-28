package org.reactome.web.pwp.client.details.tabs.expression;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;

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
        void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceEntity> referenceSequenceList);
        void showPathway(Pathway pathway);
    }
}

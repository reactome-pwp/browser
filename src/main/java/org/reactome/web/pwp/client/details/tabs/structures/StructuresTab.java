package org.reactome.web.pwp.client.details.tabs.structures;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface StructuresTab {

    interface Presenter extends DetailsTab.Presenter {
        void getReferenceSequences(DatabaseObject databaseObject);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(DatabaseObject databaseObject);
        void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceEntity> referenceSequenceList);
        void updateTitle(DatabaseObject databaseObject);
    }
}

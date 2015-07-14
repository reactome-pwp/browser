package org.reactome.web.pwp.client.details.tabs.description;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.util.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DescriptionTab {

    interface Presenter extends DetailsTab.Presenter {
        void selectObject(DatabaseObject databaseObject);
        void selectEvent(Path path, Pathway pathway, Event event);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(DatabaseObject databaseObject);
    }
}

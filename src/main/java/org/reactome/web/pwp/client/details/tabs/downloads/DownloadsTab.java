package org.reactome.web.pwp.client.details.tabs.downloads;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DownloadsTab {

    interface Presenter extends DetailsTab.Presenter {
        void swapToMolecules(Pathway pathway);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void setDbName(String dbName);

        void showDetails(DatabaseObject databaseObject);
    }
}

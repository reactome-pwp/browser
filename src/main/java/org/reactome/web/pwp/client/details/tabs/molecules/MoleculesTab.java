package org.reactome.web.pwp.client.details.tabs.molecules;

import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface MoleculesTab {

    interface Presenter extends DetailsTab.Presenter {
        void getMoleculesData();
        void updateMoleculesData();
        void moleculeDownloadStarted();
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(Pathway pathway, DatabaseObject databaseObject);
        void updateDetailsIfLoaded(Pathway pathway, DatabaseObject databaseObject);

        void setMoleculesData(Result result);
        void updateMoleculesData(Result result);

        void refreshTitle(Integer highlightedMolecules, Integer loadedMolecules);
        void moleculesDownloadRequired();

        void setLoadingMsg(String msg);
        void clearLoadingMsg();
    }
}

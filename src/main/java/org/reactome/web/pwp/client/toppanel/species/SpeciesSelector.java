package org.reactome.web.pwp.client.toppanel.species;

import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.model.client.classes.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SpeciesSelector {

    interface Presenter extends BrowserModule.Presenter {
        void setSpeciesSelected(String dbId);
    }

    interface Display extends BrowserModule.Display {
        void setPresenter(Presenter presenter);
        void setData(List<Species> speciesList);
        void setSelectedIndex(int index);
    }
}

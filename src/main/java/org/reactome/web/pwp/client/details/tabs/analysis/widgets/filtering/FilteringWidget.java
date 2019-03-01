package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.species.Species;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface FilteringWidget {

    interface Handler {

        Filter getFilter();

        AnalysisResult getAnalysisResult();

        void onResourceChanged(String resource);

        void onSizeChanged(int min, int max, int filterMin, int filterMax);

        void onSpeciesChanged(List<Species> speciesList);

        void onPValueChanged(double pValue);

        void onIncludeDiseaseChanged(boolean includeDisease);

        void loadAnalysisData();
    }

    Widget initUI();

    void updateUI();

}

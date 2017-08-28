package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;
import org.reactome.web.pwp.model.client.classes.CandidateSet;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CandidateSetTable extends EntitySetTable {
    private CandidateSet candidateSet;

    public CandidateSetTable(CandidateSet candidateSet) {
        super(candidateSet);
        this.candidateSet = candidateSet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case HAS_CANDIDATE:
                return TableRowFactory.getPhysicalEntityRow(title, this.candidateSet.getHasCandidate());
            default:
                return super.getTableRow(propertyType);
        }
    }
}

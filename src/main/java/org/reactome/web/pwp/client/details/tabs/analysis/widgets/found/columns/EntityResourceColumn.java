package org.reactome.web.pwp.client.details.tabs.analysis.widgets.found.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.analysis.client.model.FoundEntity;
import org.reactome.web.analysis.client.model.IdentifierMap;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityResourceColumn extends AbstractColumn<FoundEntity, String> {

    private static final String explanation = "Mapped identifier in Reactome for ";
    private String resource;

    public EntityResourceColumn(String resource, String group, String title) {
        super(new TextCell(), Style.TextAlign.LEFT, group, title, explanation + title);
        this.resource = resource;
        setHorizontalAlignment(ALIGN_LEFT);
    }

    @Override
    public String getValue(FoundEntity object) {
        StringBuilder sb = new StringBuilder();
        for (IdentifierMap identifierMap : object.getMapsTo()) {
            if(identifierMap.getResource().equals(resource)){
                for (String s : identifierMap.getIds()) {
                    sb.append(s).append(", ");
                }
            }
        }
        if(sb.length()>0){
            sb.delete(sb.length()-2, sb.length());
        }
        return sb.toString();
    }
}

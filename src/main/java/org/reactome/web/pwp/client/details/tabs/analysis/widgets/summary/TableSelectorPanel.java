package org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.pwp.client.details.tabs.analysis.style.AnalysisTabStyleFactory;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.events.OptionSelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.summary.handlers.OptionSelectedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TableSelectorPanel extends FlowPanel implements ValueChangeHandler<Boolean>, ClickHandler {

    private ToggleButton pathways;
    private ToggleButton notFound;
    private List<ToggleButton> btns = new LinkedList<ToggleButton>();

    public TableSelectorPanel(Integer notFound, boolean speciesComparison) {
        this.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTableSelector());

        this.notFound = new ToggleButton("Identifiers not found: " + NumberFormat.getDecimalFormat().format(notFound));
        this.notFound.setEnabled( notFound > 0 );
        if(!speciesComparison){
            this.btns.add(this.notFound);
        }

        this.pathways = new ToggleButton("Results"); this.pathways.setTitle("Pathways found");
        this.pathways.setWidth("100px");
        this.pathways.setDown(true);
        this.btns.add(this.pathways);

        for (ToggleButton btn : btns) {
            btn.addStyleName(AnalysisTabStyleFactory.RESOURCES.css().analysisTableSelectorButton());
            btn.addClickHandler(this);
            btn.addValueChangeHandler(this);
            this.add(btn);
        }
    }

    public HandlerRegistration addOptionSelectedHandler(OptionSelectedHandler handler){
        return this.addHandler(handler, OptionSelectedEvent.TYPE);
    }

    public void setSelected(AnalysisInfoType type){
        switch (type){
            case PATHWAYS_FOUND:
                this.pathways.setValue(true, true);
                break;
            case NOT_FOUND:
                this.notFound.setValue(true, true);
                break;
        }
    }

    public void setDownAll(boolean down){
        for (ToggleButton toggleButton : this.btns) {
            toggleButton.setDown(down);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        ToggleButton btn = (ToggleButton) event.getSource();
        if(!btn.getValue()){
            event.stopPropagation();
            btn.setValue(true);
        }

    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        ToggleButton btn = (ToggleButton) event.getSource();
        for (ToggleButton toggleButton : this.btns) {
            if(!toggleButton.equals(btn)){
                toggleButton.setDown(false);
            }
        }
        if(btn.equals(this.pathways)){
            fireEvent(new OptionSelectedEvent(AnalysisInfoType.PATHWAYS_FOUND));
        }else if(btn.equals(this.notFound)){
            fireEvent(new OptionSelectedEvent(AnalysisInfoType.NOT_FOUND));
        }
    }
}

package org.reactome.web.pwp.client.details.tabs.molecules.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.client.details.tabs.molecules.model.type.PropertyType;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
class TablePanel extends Composite implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel>{
    private final DisclosurePanel disclosurePanel;
    private final PropertyType propertyType;
    private Result result;
    private int size;
    private MoleculesTable moleculesTable;
    private String displayText;

    public TablePanel(PropertyType category, int size, Result result) {
        this.propertyType = category;
        this.size = size;
        this.result = result;
        this.moleculesTable = null;

        int toHighlight = result.getNumHighlight(propertyType);
        if(toHighlight == this.size){
            displayText = propertyType.getTitle() + " (" + this.size + ")"; // size/size will be replaced by size only
        }else{
            displayText = propertyType.getTitle() + " (" + result.getNumHighlight(propertyType) + "/" + this.size + ")";
        }

        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayText, null);
        this.disclosurePanel.removeStyleName("elv-Details-OverviewDisclosure-Advanced");
        this.disclosurePanel.addStyleName("elv-Details-OverviewDisclosure-Molecules");

        //set loadingMessage
        disclosurePanel.setContent(getLoadingMessage());
        disclosurePanel.setAnimationEnabled(true);

        //set all the handlers
        this.disclosurePanel.addOpenHandler(this);
        this.disclosurePanel.addCloseHandler(this);
        this.initWidget(this.disclosurePanel);
    }

    /**
     * If disclosurePanel is opened for the first time the data is set,
     * for any following openEvent the data will be updated instead.
     * @param event OpenEvent
     */
    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        disclosurePanel.setContent(getLoadingMessage());
        disclosurePanel.setAnimationEnabled(true);

        /**
         * Instead of using GWT.runAsync which must we placed in a Timer's run method (but schedule time can be 0)
         * I also looked into Class Scheduler (scheduleDeferred, scheduleFinally, scheduleFeixedDelay) and it looks
         * like the more elegant solution BUT it does not (always) show the loading symbol.
         */
        Timer timer = new Timer(){
            @Override
            public void run() {
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Console.warn(getClass() + ": The data for the moleculesTable could not be set or updated");
                    }

                    public void onSuccess() {
                        if (moleculesTable == null) {//loading/opening the first time
                            moleculesTable = new MoleculesTable(result);
                            moleculesTable.setMoleculesData(result.getSorted(propertyType));
                        } else {
                            moleculesTable.updateMoleculesData(result.getSorted(propertyType));
                        }
                        disclosurePanel.setContent(moleculesTable.asWidget());
                    }
                });
            }
        };
        timer.schedule(0);//If timer is not in place, the loading symbol never appears (excpet in the debug modus)

    }

    /**
     * If disclosurePanel is closed, first all the content is cleared away,
     * otherwise the content will be displayed until the panel is closed (which is not the right behaviour)!
     * @param event CloseEvent
     */
    @Override
    public void onClose(CloseEvent<DisclosurePanel> event) {
        this.disclosurePanel.clear();
    }

    /**
     * Updating the result, the data of the disclosurePanel and the text in disclosureHeader.
     * @param size new number of molecules of one category
     * @param result updated version of result.
     */
    public void update(int size, final Result result){
        this.result = result;
        this.size = size;

        if(this.disclosurePanel.isOpen()){
            moleculesTable.updateMoleculesData(result.getSorted(propertyType));
            disclosurePanel.setContent(moleculesTable.asWidget());
        }

        int toHighlight = result.getNumHighlight(propertyType);
        if(toHighlight == this.size){
            displayText = propertyType.getTitle() + " (" + this.size + ")";
        }else{
            displayText = propertyType.getTitle() + " (" + result.getNumHighlight(propertyType) + "/" + this.size + ")";
        }
        this.disclosurePanel.getHeaderTextAccessor().setText(displayText);
    }

    /**
     * Getting a panel with loading message and symbol.
     * @return Widget
     */
    private static Widget getLoadingMessage(){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel("Loading molecules..."));
        hp.setSpacing(5);

        return hp;
    }
}

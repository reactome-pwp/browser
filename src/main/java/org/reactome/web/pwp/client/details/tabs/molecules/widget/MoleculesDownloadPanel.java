package org.reactome.web.pwp.client.details.tabs.molecules.widget;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.common.widgets.DialogBoxFactory;
import org.reactome.web.pwp.client.details.common.widgets.button.CustomButton;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTab;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.pwp.client.details.tabs.molecules.model.type.PropertyType;
import org.reactome.web.pwp.client.details.common.widgets.panels.TextPanel;

import java.util.HashSet;
import java.util.Random;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesDownloadPanel extends DockLayoutPanel {
    private Result result;
    private final CheckBox typeTB;
    private final CheckBox nameTB;
    private final CheckBox identifierTB;
    private final CheckBox chemTB;
    private final CheckBox protTB;
    private final CheckBox sequTB;
    private final CheckBox otheTB;
    private TextArea textArea;
    private Random random = new Random();

    private final CustomButton startDownloadBtn = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Start Download");
    private final CustomButton startGenomeSpaceDownloadBtn = new CustomButton(CommonImages.INSTANCE.downloadFile(), "Save to GenomeSpace");
    private final MoleculesTab.Presenter presenter;

    public MoleculesDownloadPanel(Result result, MoleculesTab.Presenter presenter) {
        super(Style.Unit.PX);
        this.result = result;
        this.presenter = presenter;
        this.setWidth("99%");
        this.textArea = new TextArea();

        // Initialising and setting all the currently available checkboxes.
        chemTB = new CheckBox(PropertyType.CHEMICAL_COMPOUNDS.getTitle());
        chemTB.setTitle("Show or hide " + PropertyType.CHEMICAL_COMPOUNDS.getTitle());
        chemTB.setValue(true);

        protTB = new CheckBox(PropertyType.PROTEINS.getTitle());
        protTB.setTitle("Show or hide " + PropertyType.PROTEINS.getTitle());
        protTB.setValue(true);

        sequTB = new CheckBox(PropertyType.SEQUENCES.getTitle());
        sequTB.setTitle("Show or hide " + PropertyType.SEQUENCES.getTitle());
        sequTB.setValue(true);

        otheTB = new CheckBox(PropertyType.OTHERS.getTitle());
        otheTB.setTitle("Show or hide " + PropertyType.OTHERS.getTitle());
        otheTB.setValue(true);

        typeTB = new CheckBox("Type");
        typeTB.setTitle("Show or hide type column");
        typeTB.setValue(true);

        identifierTB = new CheckBox("Identifier");
        identifierTB.setTitle("Show or hide identifier column");
        identifierTB.setValue(true);

        nameTB = new CheckBox("Name");
        nameTB.setTitle("Show or hide name column");
        nameTB.setValue(true);
    }

    public void initialise(final Result result){
        this.clear(); //if not cleared then updated panels are added under old ones

        TextPanel information = new TextPanel(" Here you can download the information the Molecules Tab provides.\n" +
                "The format will be TSV which can easily be handled with most word processors.");
        information.setStyleName("elv-InformationPanel-Download");
        this.addNorth(information, 25);

        //Creating ToggleButton for each possible category of molecules.
        VerticalPanel requiredType = new VerticalPanel();
        requiredType.add(new TextPanel("Please select the type of Molecules you are interested in:"));

        //Allow immediate changes to Preview by adding ClickHandler to every CheckBox.
        ClickHandler updateText = new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                textArea.setText(resultToText());
                textArea.setStyleName("elv-PreviewPanel-Download");
            }
        };

        //Checking for each category of molecules if they are present before adding Handler.
        if(result.getChemicals().size() > 0){
            requiredType.add(chemTB);
            chemTB.addClickHandler(updateText);
        }

        if(result.getProteins().size() > 0){
            requiredType.add(protTB);
            protTB.addClickHandler(updateText);
        }

        if(result.getSequences().size() > 0){
            requiredType.add(sequTB);
            sequTB.addClickHandler(updateText);
        }

        if(result.getOthers().size() > 0){
            requiredType.add(otheTB);
            otheTB.addClickHandler(updateText);
        }

        requiredType.setStyleName("elv-SelectionPanels-Download");


        //Creating ToggleButton for each available attribute of molecules and adding Handler.
        VerticalPanel requiredFields = new VerticalPanel();
        requiredFields.add(new TextPanel("Please select the fields you are interested in:"));

        typeTB.addClickHandler(updateText);
        requiredFields.add(typeTB);

        identifierTB.addClickHandler(updateText);
        requiredFields.add(identifierTB);

        nameTB.addClickHandler(updateText);
        requiredFields.add(nameTB);

        requiredFields.setStyleName("elv-SelectionPanels-Download");

        //Creating button for download.
        //Browsers that fully support Blob/Download:
        //Chrome, Chrome for Android, Firefox 20+, IE 10+, Opera 15+, Safari 6.1+
        VerticalPanel buttonField = new VerticalPanel();
        buttonField.add(startDownloadBtn);
        startDownloadBtn.setStyleName("elv-Download-Button");
        buttonField.setStyleName("elv-ButtonPanel-Download");
        startDownloadBtn.setTitle("Depending on your browser you can either download your file by clicking on this button" +
        		" or your will be redirected to a new tab in your browser where you can right click and" +
        		" save the data.");
        startDownloadBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if((chemTB.getValue() || protTB.getValue() || sequTB.getValue() || otheTB.getValue())
                        && (typeTB.getValue() || nameTB.getValue() || identifierTB.getValue())){
                	alertDownload(textArea.getText());
                }else{
                    DialogBoxFactory.alert("Molecules Download", "You are trying to download an empty file.\n" +
                            "Please select at least one type of molecules AND one field for the download.");
                }
                presenter.moleculeDownloadStarted();
            }
        });
        buttonField.add(startGenomeSpaceDownloadBtn);
        startGenomeSpaceDownloadBtn.setStyleName("elv-Download-Button");
        startGenomeSpaceDownloadBtn.setTitle("Clicking this button should open a window to import the data to GenomeSpace. " +
        		"Depending on your browser configuration, you may have to enable popups for the Reactome website");
        startGenomeSpaceDownloadBtn.addClickHandler(new ClickHandler() {
        	@Override
        	public void onClick(ClickEvent event) {
        		if((chemTB.getValue() || protTB.getValue() || sequTB.getValue() || otheTB.getValue())
        				&& (typeTB.getValue() || nameTB.getValue() || identifierTB.getValue())){
        			Integer randomInt = random.nextInt(1000 - 1) + 1;
                    try {
                        uploadListToGenomeSpace(textArea.getText(), randomInt.toString());
                    }catch (JavaScriptException exception){
                        Window.alert(exception.getMessage());
                    }
        		}else{
        			Window.alert("You are trying to download an empty file.\n" +
                            "Please select at least one type of molecules AND one field for the download.");
        		}
        		presenter.moleculeDownloadStarted();
        	}
        });

        //Bringing together the two panels.
        FlowPanel controlArea = new FlowPanel();
        controlArea.insert(requiredType.asWidget(), 0);
        controlArea.insert(requiredFields.asWidget(), 1);
        ScrollPanel scrollPanel = new ScrollPanel(controlArea);

        this.addWest(scrollPanel, 200);
        this.addEast(buttonField, 100);

        //Preview
        this.textArea = new TextArea();
        this.textArea.setVisible(true);
        this.textArea.setTitle("Preview of what your download file will look like.");
        this.textArea.setText(resultToText());
        this.add(textArea);
        this.textArea.setStyleName("elv-PreviewPanel-Download");

        this.addStyleName("elv-Details-OverviewPanel");
    }
    

    /**
     * Uses files in resources/public to enable download.
     * @param text from preview
     */
    public static native void alertDownload(String text) /*-{
        $wnd.saveAs(
            new Blob(
                [text]
                , {type: "text/plain;charset=utf-8;"}
            )
            , "participatingMolecules.tsv"
        );
    }-*/;

    /**
     * Uses GenomeSpace JavaScript method to upload data.
     */
    public static native void uploadListToGenomeSpace(String list, String randomInt) /*-{    	                                                                                	    
		var blob = new Blob([list], {type: "text/plain"});
		var formData = new FormData();
		var fileName = "Reactome_" + randomInt + "_gene_list.txt";
		formData.append("webmasterfile", blob, fileName);
		$wnd.gsUploadByPost(formData);
	}-*/;
   
    /**
     * Converting ResultObject into text for preview according to checkboxes.
     * @return String for preview
     */
    private String resultToText() {
        String resultString = "";

        //Adding column names:
        if(typeTB.getValue()){
            resultString += "MoleculeType\t";
        }

        if(identifierTB.getValue()){
            resultString += "Identifier\t";
        }

        if(nameTB.getValue()){
            resultString += "MoleculeName\t";
        }

        //Adding line break in case there are column names
        if(resultString.length() > 0){
            resultString += "\n";
        }

        resultString += buildGroupString(chemTB, result.getChemicals(), PropertyType.CHEMICAL_COMPOUNDS.getTitle());
        resultString += buildGroupString(protTB, result.getProteins(), PropertyType.PROTEINS.getTitle());
        resultString += buildGroupString(sequTB, result.getSequences(), PropertyType.SEQUENCES.getTitle());
        resultString += buildGroupString(otheTB, result.getOthers(), PropertyType.OTHERS.getTitle());

        return resultString;
    }

    /**
     * Converting HashSet<Molecule> for one PropertyType into text taking into account checked boxes.
     * @param checkbox If not checked then this type of molecules does not need to be considered.
     * @param molecules List of molecules that has to be turned into a string.
     * @param string PropertyType as string (to be attached in front of every line)
     * @return String resultString
     */
    private String buildGroupString(CheckBox checkbox, HashSet<Molecule> molecules, String string){
        String resultString = "";
        if(checkbox != null && checkbox.getValue()){
            for(Molecule m : molecules){
                if(m.isToHighlight()){
                    if(this.typeTB.getValue()){
                        resultString += string + "\t";
                    }

                    if(this.identifierTB.getValue()){
                        resultString += m.getIdentifier() + "\t";
                    }

                    if(this.nameTB.getValue()){
                        resultString += m.getDisplayName() + "\t";
                    }

                    resultString += "\n";
                }
            }
        }
        return resultString;
    }

    /**
     * Update internal result and preview text.
     * @param result New result data
     */
    public void update(Result result){
        this.result = result;
        textArea.setText(resultToText());
        textArea.setStyleName("elv-PreviewPanel-Download");
    }
}


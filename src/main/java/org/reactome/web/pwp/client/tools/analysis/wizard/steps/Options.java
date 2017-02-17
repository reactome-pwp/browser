package org.reactome.web.pwp.client.tools.analysis.wizard.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.carrousel.client.CarrouselPanel;
import org.reactome.web.carrousel.client.Slide;
import org.reactome.web.pwp.client.tools.analysis.style.AnalysisStyleFactory;
import org.reactome.web.pwp.client.tools.analysis.wizard.AnalysisWizard;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardEventBus;
import org.reactome.web.pwp.client.tools.analysis.wizard.common.WizardSelection;
import org.reactome.web.pwp.client.tools.analysis.wizard.events.NextStepSelectedEvent;
import org.reactome.web.pwp.client.tools.analysis.wizard.handlers.NextStepSelectedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Options extends DockLayoutPanel implements ClickHandler, NextStepSelectedHandler {

    private WizardEventBus wizardEventBus;
    //wizardSelection is shared by all the steps in the wizard
    private WizardSelection wizardSelection;

    private CheckBox projection;
    private CheckBox interactors;

    public Options(WizardEventBus wizardEventBus, WizardSelection wizardSelection) {
        super(Style.Unit.PX);
        this.wizardEventBus = wizardEventBus;
        this.wizardSelection = wizardSelection;
        initHandlers();

        FlowPanel container = new FlowPanel();

        SimplePanel explanation = new SimplePanel();
        explanation.setStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisText());
        explanation.getElement().setInnerHTML("Step 2: Select your preferred options.");
        container.add(explanation);

        container.add(projection = new CheckBox("Project to human"));
        projection.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelCheckBox());
        projection.setValue(true);

        DisclosurePanel projectionInfo = new DisclosurePanel(RESOURCES.minus(), RESOURCES.plus(), "All non-human identifiers are converted to their human equivalents (expand for more info...)");
        projectionInfo.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosure());
        projectionInfo.add(getProjectionCarrousel());
        projectionInfo.getContent().addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosureContent());
        projectionInfo.setAnimationEnabled(true);
        container.add(projectionInfo);

        container.add(interactors = new CheckBox("Include interactors"));
        interactors.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelCheckBox());

        DisclosurePanel interactorsInfo = new DisclosurePanel(RESOURCES.minus(), RESOURCES.plus(), "IntAct interactors are used to increase the analysis background (expand for more info...)");
        interactorsInfo.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosure());
        interactorsInfo.add(getInteractorsCarrousel());
        interactorsInfo.getContent().addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosureContent());
        interactorsInfo.setAnimationEnabled(true);
        container.add(interactorsInfo);


        Button back = new Button("Back to your data", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Options.this.wizardEventBus.fireEventFromSource(new NextStepSelectedEvent(AnalysisWizard.Step.SAMPLE), Options.this);
            }
        });
        Button analyse = new Button("Analyse!", this);
        analyse.getElement().getStyle().setFloat(Style.Float.RIGHT);

        FlowPanel buttonsBar = new FlowPanel();
        buttonsBar.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelButtons());
        buttonsBar.add(back);
        buttonsBar.add(analyse);
        addSouth(buttonsBar, 100);

        add(new ScrollPanel(container));
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        wizardSelection.setProjectToHuman(projection.getValue());
        wizardSelection.setIncludeInteractors(interactors.getValue());

        wizardEventBus.fireEventFromSource(new NextStepSelectedEvent(AnalysisWizard.Step.ANALYSIS), Options.this);
    }

    @Override
    public void onNextStepSelected(NextStepSelectedEvent event) {
        if (!event.getStep().equals(AnalysisWizard.Step.OPTIONS)) return;

        //It's me!
    }


    private void initHandlers(){
        wizardEventBus.addHandler(NextStepSelectedEvent.TYPE, this);
    }

    private CarrouselPanel getInteractorsCarrousel(){
        List<Slide> slidesList = new LinkedList<>();
        slidesList.add(new Slide(RESOURCES.interactorsSlide01(),"Reactome can include interaction data from IntAct<br>when performing an analysis","white"));
        slidesList.add(new Slide(RESOURCES.interactorsSlide02(),"When interactors are found for the selected resource<br>proteins that have interactors are displayed like this","white"));
        slidesList.add(new Slide(RESOURCES.interactorsSlide03(),"Selecting the <b>\"include interactors\"</b> option will take<br>interactors into account when performing the analysis","white"));
        slidesList.add(new Slide(RESOURCES.interactorsSlide04(),"Only gene symbols, chemical names and UniProt or<br>ChEBI identifiers can be used to identify interactors","white"));
        slidesList.add(new Slide(RESOURCES.interactorsSlide05(),"Curated entities and interactors backgrounds may overlap (e.g. entities<br>can interact with themselves (dimers) or with other entities in the diagram)","white"));
        slidesList.add(new Slide(RESOURCES.interactorsSlide06(),"The analysis result uses the <b>union</b> of both backgrounds.<br>Entities in the overlapping area are only counted once","white"));

        CarrouselPanel carrouselPanel = new CarrouselPanel(slidesList, 500, 300, "azure");
        carrouselPanel.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        carrouselPanel.getElement().getStyle().setFloat(Style.Float.LEFT);
        return carrouselPanel;
    }

    private CarrouselPanel getProjectionCarrousel(){
        Slide slide1 = new Slide(RESOURCES.projectionSlide01(),"Every species has its own set of identifiers<br>(genes, proteins, chemicals, mRNA, etc...)","white");
        String infers = " <a style=\"color:white\" target=\"_blank\" href=\"//www.reactome.org/pages/documentation/electronically-inferred-events/\">infers</a> ";
        Slide slide2 = new Slide(RESOURCES.projectionSlide02(),"Reactome curates human pathways and" + infers + "their existence in other<br>species using ortholgy information from the ENSEMBL Compara database","white");
        Slide slide3 = new Slide(RESOURCES.projectionSlide03(),"When this option is selected, all non-human identifiers in your sample<br>are mapped to their human equivalents before the analysis is performed","white");
        List<Slide> slidesList = new LinkedList<>();
        slidesList.add(slide1);
        slidesList.add(slide2);
        slidesList.add(slide3);

        CarrouselPanel carrouselPanel = new CarrouselPanel(slidesList, 500, 300, "azure");
        carrouselPanel.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        return carrouselPanel;
    }


    public UserSampleResource RESOURCES = GWT.create(UserSampleResource.class);
    public interface UserSampleResource extends ClientBundle {

        @Source("projection/slide_01.png")
        ImageResource projectionSlide01();

        @Source("projection/slide_02.png")
        ImageResource projectionSlide02();

        @Source("projection/slide_03.png")
        ImageResource projectionSlide03();

        @Source("interactors/slide_01.png")
        ImageResource interactorsSlide01();

        @Source("interactors/slide_02.png")
        ImageResource interactorsSlide02();

        @Source("interactors/slide_03.png")
        ImageResource interactorsSlide03();

        @Source("interactors/slide_04.png")
        ImageResource interactorsSlide04();

        @Source("interactors/slide_05.png")
        ImageResource interactorsSlide05();

        @Source("interactors/slide_06.png")
        ImageResource interactorsSlide06();

        @Source("images/minus_blue.png")
        ImageResource minus();

        @Source("images/plus_blue.png")
        ImageResource plus();
    }
}

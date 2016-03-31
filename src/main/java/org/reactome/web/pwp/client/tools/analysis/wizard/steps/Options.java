package org.reactome.web.pwp.client.tools.analysis.wizard.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.controls.carousel.CarouselPanel;
import org.reactome.web.diagram.controls.carousel.Slide;
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
public class Options extends ScrollPanel implements ClickHandler, NextStepSelectedHandler {

    private WizardEventBus wizardEventBus;
    //wizardSelection is shared by all the steps in the wizard
    private WizardSelection wizardSelection;

    private CheckBox projection;
    private CheckBox interactors;

    public Options(WizardEventBus wizardEventBus, WizardSelection wizardSelection) {
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

        DisclosurePanel projectionInfo = new DisclosurePanel("All non-human identifiers are converted to their human equivalents (click here for more info...)");
        projectionInfo.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosure());
        projectionInfo.add(getProjectionCarousel());
        projectionInfo.getContent().addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosureContent());
        projectionInfo.setAnimationEnabled(true);
        container.add(projectionInfo);

        container.add(interactors = new CheckBox("Include interactors"));
        interactors.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelCheckBox());

        DisclosurePanel interactorsInfo = new DisclosurePanel("IntAct interactors are used to increase the analysis background (click here for more info...)");
        interactorsInfo.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelDisclosure());
        interactorsInfo.add(getInteractorsCarousel());
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
        container.add(buttonsBar);

        add(container);
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

    private CarouselPanel getInteractorsCarousel(){
        Slide slide1 = new Slide(RESOURCES.interactorsSlide01(),"Reactome includes interactors data from IntAct","white");
        Slide slide2 = new Slide(RESOURCES.interactorsSlide02(),"Every protein with interactors looks now like this","white");
        Slide slide3 = new Slide(RESOURCES.interactorsSlide03(),"Selecting this option will perform the analysis taking also into account the interactors from IntAct","white");
        List<Slide> slidesList = new LinkedList<>();
        slidesList.add(slide1);
        slidesList.add(slide2);
        slidesList.add(slide3);

        CarouselPanel carouselPanel = new CarouselPanel(slidesList, 335, 200, "azure");
        carouselPanel.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        carouselPanel.getElement().getStyle().setFloat(Style.Float.LEFT);
        return carouselPanel;
    }

    private CarouselPanel getProjectionCarousel(){
        Slide slide1 = new Slide(RESOURCES.projectionSlide01(),"Every species has its own set of proteins identifiers","white");
        Slide slide2 = new Slide(RESOURCES.projectionSlide02(),"Reactome mainly curates human proteins and infers to other species using ENSEMBL compara","white");
        Slide slide3 = new Slide(RESOURCES.projectionSlide03(),"Selecting this option, all non-human identifiers in your sample are mapped to their human equivalent","white");
        List<Slide> slidesList = new LinkedList<>();
        slidesList.add(slide1);
        slidesList.add(slide2);
        slidesList.add(slide3);

        CarouselPanel carouselPanel = new CarouselPanel(slidesList, 335, 200, "azure");
        carouselPanel.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        return carouselPanel;
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
    }
}

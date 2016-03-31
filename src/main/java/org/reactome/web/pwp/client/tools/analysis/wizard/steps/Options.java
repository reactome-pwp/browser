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
        //noinspection GWTStyleCheck
//        container.setStyleName("clearfix");
//        container.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanel());
        container.add(new Label("Step 2: Select the options"));

        container.add(projection = new CheckBox("Project to human"));
        projection.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelCheckBox());
        projection.setValue(true);

        container.add(interactors = new CheckBox("Include interactors"));
        interactors.addStyleName(AnalysisStyleFactory.getAnalysisStyle().optionsPanelCheckBox());

        container.add(getInteractorsCarousel());


        container.add(new Button("Back", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Options.this.wizardEventBus.fireEventFromSource(new NextStepSelectedEvent(AnalysisWizard.Step.SAMPLE), Options.this);
            }
        }));
        container.add(new Button("Analyse!", this));

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
        Slide slide1 = new Slide(RESOURCES.slide01(),"Slide 1: Caption for the first slide","transparent");
        Slide slide2 = new Slide(RESOURCES.slide02(),"Slide 2: Caption for the second slide","transparent");
        Slide slide3 = new Slide(RESOURCES.slide03(),"Slide 3: Caption for the third slide","transparent");
        List<Slide> slidesList = new LinkedList<>();
        slidesList.add(slide1);
        slidesList.add(slide2);
        slidesList.add(slide3);

        CarouselPanel carouselPanel = new CarouselPanel(slidesList, 300, 200, "azure");
        carouselPanel.setWidth(300 + "px");
        carouselPanel.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        return carouselPanel;
    }


    UserSampleResource RESOURCES = GWT.create(UserSampleResource.class);
    public interface UserSampleResource extends ClientBundle {

        @Source("interactors/slide_01.png")
        ImageResource slide01();

        @Source("interactors/slide_02.png")
        ImageResource slide02();

        @Source("interactors/slide_03.png")
        ImageResource slide03();
    }
}

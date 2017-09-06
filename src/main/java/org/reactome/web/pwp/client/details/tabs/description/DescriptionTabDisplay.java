package org.reactome.web.pwp.client.details.tabs.description;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.delegates.InstanceSelectedDelegate;
import org.reactome.web.pwp.client.details.delegates.InstanceSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.DescriptionPanel;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.util.Path;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DescriptionTabDisplay extends ResizeComposite implements DescriptionTab.Display, InstanceSelectedHandler {

    private DescriptionTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;
    private Map<DatabaseObject, DescriptionPanel> cache = new HashMap<>();

    public DescriptionTabDisplay() {
        this.title = getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.PX);
        initWidget(this.container);
        InstanceSelectedDelegate.get().setInstanceSelectedHandler(this);
        setInitialState();
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return DetailsTabType.DESCRIPTION;
    }

    @Override
    public Widget getTitleContainer() {
        return title;
    }

    @Override
    public void setInitialState() {
        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(DescriptionTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(DatabaseObject databaseObject) {
        this.container.clear();
        if(cache.containsKey(databaseObject)){
            this.container.add(cache.get(databaseObject));
        }else{
            DescriptionPanel descriptionPanel = new DescriptionPanel(databaseObject);
            cache.put(databaseObject, descriptionPanel);
            this.container.add(descriptionPanel);
        }
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading download panel, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    @Override
    public void showErrorMessage(String message) {
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.warning());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.container.clear();
        this.container.add(panel);
    }

    @Override
    public void eventSelected(Path path, Pathway pathway, Event event) {
        presenter.selectEvent(path, pathway, event);
    }

    @Override
    public void instanceSelected(DatabaseObject databaseObject) {
        presenter.selectObject(databaseObject);
    }
}

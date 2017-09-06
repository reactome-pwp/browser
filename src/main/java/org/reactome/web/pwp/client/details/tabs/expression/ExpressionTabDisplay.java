package org.reactome.web.pwp.client.details.tabs.expression;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.classes.ReferenceEntity;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import uk.ac.ebi.pwp.widgets.gxa.ui.GXAViewer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionTabDisplay extends ResizeComposite implements ExpressionTab.Display {

    private ExpressionTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;
    private Map<DatabaseObject, GXAViewer> cache = new HashMap<>();

    public ExpressionTabDisplay() {
        this.title = this.getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);
        initWidget(this.container);
        setInitialState();
    }

    public DetailsTabType getDetailTabType() {
        return DetailsTabType.EXPRESSION;
    }

    @Override
    public Widget getTitleContainer() {
        return this.title;
    }

    @Override
    public void setInitialState() {
        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(ExpressionTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(DatabaseObject databaseObject) {
        GXAViewer gxaViewer = new GXAViewer();
        cache.put(databaseObject, gxaViewer);
        String reactomeID = databaseObject.getReactomeIdentifier().split("\\.")[0];
        gxaViewer.setReactomeID(reactomeID);

        this.container.clear();
        this.container.add(gxaViewer);
    }

    @Override
    public void showProteins(DatabaseObject databaseObject) {
        if (this.cache.containsKey(databaseObject)) {
            this.container.clear();
            this.container.add(this.cache.get(databaseObject));
        }else {
            this.showLoadingMessage();
            presenter.getReferenceSequences(databaseObject);
        }
    }

    @Override
    public void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceEntity> referenceSequenceList) {
        GXAViewer gxaViewer = new GXAViewer();
        this.cache.put(databaseObject, gxaViewer);
        List<String> uniProtIDs = new LinkedList<>();
        for (ReferenceEntity referenceSequence : referenceSequenceList) {
            if (referenceSequence.getIdentifier() == null) continue;
            uniProtIDs.add(referenceSequence.getIdentifier());
        }
        if (uniProtIDs.isEmpty()) {
            gxaViewer.setEmpty();
        } else {
            gxaViewer.setUniProtIDs(uniProtIDs);
        }

        this.container.clear();
        this.container.add(gxaViewer);
    }

    @Override
    public void showPathway(final Pathway pathway) {
        if (this.cache.containsKey(pathway)) {
            this.container.clear();
            this.container.add(this.cache.get(pathway));
        }else {
            pathway.load(new ContentClientHandler.ObjectLoaded() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    showDetails(databaseObject);
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    Console.error(pathway.getDisplayName() + " details could not be retrieved from the server.", ExpressionTabDisplay.this);
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    Console.error(pathway.getDisplayName() + " details could not be retrieved from the server.", ExpressionTabDisplay.this);
                }
            });
        }
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading the data required to show the gene expression data. Please wait...");
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
}

package org.reactome.web.pwp.client.hierarchy.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.analysis.model.EntityStatistics;
import org.reactome.web.pwp.client.common.analysis.model.PathwaySummary;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOutEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOverHandler;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.util.Path;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItem extends TreeItem implements HasHandlers, MouseOverHandler, MouseOutHandler {

    private HandlerManager handlerManager = new HandlerManager(this);

    private boolean childrenLoaded = false;
    private FlowPanel textContainer;
    private InlineLabel analysisData;

    public HierarchyItem(Species species, Event event) {
        super();
        setUserObject(event);
        init(species, event);
        initHandlers();
    }

    public HandlerRegistration addHierarchyItemMouseOverHandler(HierarchyItemMouseOverHandler handler){
        return handlerManager.addHandler(HierarchyItemMouseOverEvent.TYPE, handler);
    }

    public HandlerRegistration addHierarchyItemMouseOutHandler(HierarchyItemMouseOutHandler handler){
        return handlerManager.addHandler(HierarchyItemMouseOutEvent.TYPE, handler);
    }

    private void init(Species species, Event event){
        FlowPanel itemContent = new FlowPanel();
        itemContent.setStyleName(RESOURCES.getCSS().hierarchyItem());

        itemContent.add(new Image(event.getImageResource()));

        ImageResource status = event.getStatusIcon();
        if(status!=null){
            Image statusIcon = new Image(status);
//            statusIcon.getElement().getStyle().setMarginLeft(5, Unit.PX);
            statusIcon.setTitle(event.getReleaseStatus().name());
            itemContent.add(statusIcon);
        }

        ImageResource inferred = event.getInferredIcon();
        if(inferred!=null){
            Image inferredIcon = new Image(inferred);
            if(species.getDbId().equals(Token.DEFAULT_SPECIES_ID)){
                inferredIcon.setTitle("Inferred from a non-human event");
            }else{
                inferredIcon.setTitle("Inferred from human event");
            }
            itemContent.add(inferredIcon);
        }

        ImageResource disease = event.getDiseaseIcon();
        if(event.isInDisease()){
            Image diseaseIcon = new Image(disease);
            diseaseIcon.setTitle("Is a disease");
            itemContent.add(diseaseIcon);
        }

        textContainer = new FlowPanel();
        textContainer.setStyleName(RESOURCES.getCSS().hierarchyTextContainer());

        InlineLabel label = new InlineLabel(event.getDisplayName());
        label.setTitle(event.getDisplayName());
        textContainer.add(label);

        analysisData = new InlineLabel("");
        Style ads = analysisData.getElement().getStyle();
        ads.setFontWeight(Style.FontWeight.BOLD);
        ads.setMarginLeft(6, Unit.PX);
        ads.setFontSize(11, Unit.PX);
        textContainer.add(analysisData);

        itemContent.add(textContainer);

        setWidget(itemContent);

        if(event instanceof Pathway){
            FlowPanel loaderMsg = new FlowPanel();
            loaderMsg.add(new Image(CommonImages.INSTANCE.loader()));
            InlineLabel loadingLabel = new InlineLabel("Loading...");
            loadingLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
            loaderMsg.add(loadingLabel);
            addItem(loaderMsg); // Add a place holder so that this item can be opened.
        }
    }

    private void initHandlers(){
        Widget widget = getWidget();
        widget.sinkEvents(com.google.gwt.user.client.Event.ONMOUSEOVER);
        widget.sinkEvents(com.google.gwt.user.client.Event.ONMOUSEOUT);
        widget.addHandler(this, MouseOverEvent.getType());
        widget.addHandler(this, MouseOutEvent.getType());
    }

    public void clearAnalysisData(){
        analysisData.setText("");
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemHit());
        for (int i = 0; i < getChildCount(); i++) {
            TreeItem child = getChild(i);
            if(child instanceof HierarchyItem){
                ((HierarchyItem) child).clearAnalysisData();
            }
        }
    }

    public void clearHighlight(){
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemSelected());
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemHighlighted());
    }


    public boolean isChildrenLoaded() {
        return childrenLoaded;
    }

    public Event getEvent() {
        return (Event) getUserObject();
    }

    public HierarchyItem getParentWithDiagram(){
        if(this.hasDiagram()){
            return this;
        }else{
            HierarchyItem parent = (HierarchyItem) this.getParentItem();
            if(parent!=null){
                return parent.getParentWithDiagram();
            }
            return null;
        }
    }

    public boolean hasDiagram(){
        DatabaseObject databaseObject = getEvent();
        Pathway pathway = (databaseObject instanceof Pathway) ? (Pathway) databaseObject : null;
        return ( pathway==null ) ? false : pathway.getHasDiagram();
    }

    public void highlightPath(){
        if(isSelected()){
            textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemSelected());
        }else{
            textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemHighlighted());
        }
        if(getParentItem()!=null){
            ((HierarchyItem) getParentItem()).highlightPath();
        }
    }

    public void setChildrenLoaded(boolean childrenLoaded) {
        this.childrenLoaded = childrenLoaded;
    }

    public Path getPath(){
        if(getParentItem()==null) {
            return new Path();
        }else{
            return ((HierarchyItem) getParentItem()).getPathToItem();
        }
    }

    private Path getPathToItem(){
        if(getParentItem()==null){
            return new Path(this.getEvent());
        }else{
            Path path = ((HierarchyItem) this.getParentItem()).getPathToItem();
            if(this.getEvent() instanceof Pathway){
                path.add(this.getEvent());
            }
            return path;
        }
    }

    public void highlightHitEvent(){
        textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemHit());
    }

    public void showAnalysisData(PathwaySummary pathwaySummary){
        StringBuilder sb = new StringBuilder();
        EntityStatistics entityStatistics = pathwaySummary.getEntities();
        String found = NumberFormat.getDecimalFormat().format(entityStatistics.getFound());
        String total = NumberFormat.getDecimalFormat().format(entityStatistics.getTotal());
        sb.append("(").append(found).append("/").append(total).append(") ");
        NumberFormat nf = NumberFormat.getFormat("#.##E0");
        sb.append("FDR: ").append(nf.format(entityStatistics.getFdr()));
        this.analysisData.setText(sb.toString());
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        fireEvent(new HierarchyItemMouseOverEvent(this));
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        fireEvent(new HierarchyItemMouseOutEvent());
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }


    public static final ObjectInfoResources RESOURCES;
    static {
        RESOURCES = GWT.create(ObjectInfoResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface ObjectInfoResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SuggestionPanelCSS.CSS)
        SuggestionPanelCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("diagram-ObjectInfoPanel")
    public interface SuggestionPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/hierarchy/widget/HierarchyItem.css";

        String hierarchyItem();

        String hierarchyTextContainer();

        String hierarchyItemSelected();

        String hierarchyItemHighlighted();

        String hierarchyItemHit();
    }

}
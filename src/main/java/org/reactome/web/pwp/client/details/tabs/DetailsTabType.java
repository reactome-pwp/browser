package org.reactome.web.pwp.client.details.tabs;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.DetailsDisplay;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum DetailsTabType {

    /* NOTE: If a new tab is added, make sure GAModule correspondence method is updated */
    DESCRIPTION("DD", "Description"),
    PARTICIPATING_MOLECULES("MT", "Molecules"),
    STRUCTURES("ST", "Structures"),
    EXPRESSION("EX", "Expression"),
    ANALYSIS("AN", "Analysis"),
    PARTICIPATING_PROCESSES("PT", "Processes"),
    DOWNLOADS("DT", "Downloads");

    private String code;
    private String title;

    DetailsTabType(String code, String title){
        this.code = code;
        this.title = title;
    }

    public static DetailsTabType getByCode(String code){
        for (DetailsTabType tabType : values()) {
            if(tabType.getCode().equals(code))
                return tabType;
        }
        return DESCRIPTION;
    }

    public String getCode() {
        return code;
    }

    public static DetailsTabType getDefault(){
        return DESCRIPTION;
    }

    public static int getDefaultIndex(){
        return getIndex(getDefault());
    }

    public static int getIndex(DetailsTabType type){
        int index = 0;
        for (DetailsTabType tabType : values()) {
            if(tabType.equals(type))
                return index;
            index++;
        }
        return DetailsTabType.getDefaultIndex();
    }

    public DetailsTabTitle getTitle() {
        return new DetailsTabTitle(this.getIcon(), this.title);
    }

    public Widget getInitialStatePanel(){
        FlowPanel fp = new FlowPanel();
        fp.setStyleName(DetailsDisplay.RESOURCES.getCSS().initialStateMessage());
        Image img = new Image(this.getMainImage());
        fp.add(img);

        SimplePanel sp = new SimplePanel();
        sp.add(new InlineLabel(this.getExplanation().getText()));
        fp.add(sp);

        return new ScrollPanel(fp);
    }

    private ImageResource getIcon() {
        switch (this){
            case DESCRIPTION: return DetailsTabResources.INSTANCE.descriptionIcon();
            case PARTICIPATING_MOLECULES: return DetailsTabResources.INSTANCE.moleculesIcon();
            case STRUCTURES: return DetailsTabResources.INSTANCE.structuresIcon();
            case EXPRESSION: return DetailsTabResources.INSTANCE.expressionIcon();
            case ANALYSIS: return DetailsTabResources.INSTANCE.analysisIcon();
            case DOWNLOADS: return DetailsTabResources.INSTANCE.downloadsIcon();
            default: return CommonImages.INSTANCE.exclamation();
        }
    }

    private ImageResource getMainImage(){
        switch (this){
            case DESCRIPTION: return DetailsTabResources.INSTANCE.description();
            case PARTICIPATING_MOLECULES: return DetailsTabResources.INSTANCE.molecules();
            case STRUCTURES: return DetailsTabResources.INSTANCE.structures();
            case EXPRESSION: return DetailsTabResources.INSTANCE.expression();
            case ANALYSIS: return DetailsTabResources.INSTANCE.analysis();
            case DOWNLOADS: return DetailsTabResources.INSTANCE.downloads();
            default: return CommonImages.INSTANCE.exclamation();
        }
    }

    private TextResource getExplanation(){
        switch (this){
            case DESCRIPTION: return DetailsTabResources.INSTANCE.descriptionTxt();
            case PARTICIPATING_MOLECULES: return DetailsTabResources.INSTANCE.moleculesTxt();
            case STRUCTURES: return DetailsTabResources.INSTANCE.structuresTxt();
            case EXPRESSION: return DetailsTabResources.INSTANCE.expressionTxt();
            case ANALYSIS: return DetailsTabResources.INSTANCE.analysisTxt();
            case DOWNLOADS: return DetailsTabResources.INSTANCE.downloadsTxt();
            default: throw new RuntimeException("No explanation found for " +  this);
        }
    }

    public interface DetailsTabResources extends ClientBundle {

        DetailsTabResources INSTANCE = GWT.create(DetailsTabResources.class);

        @Source("images/analysis.png")
        ImageResource analysis();

        @Source("images/analysis_icon.png")
        ImageResource analysisIcon();

        @Source("text/analysis.txt")
        TextResource analysisTxt();

        @Source("images/description.png")
        ImageResource description();

        @Source("images/description_icon.png")
        ImageResource descriptionIcon();

        @Source("text/description.txt")
        TextResource descriptionTxt();

        @Source("images/downloads.png")
        ImageResource downloads();

        @Source("images/downloads_icon.png")
        ImageResource downloadsIcon();

        @Source("text/downloads.txt")
        TextResource downloadsTxt();

        @Source("images/expression.png")
        ImageResource expression();

        @Source("images/expression_icon.png")
        ImageResource expressionIcon();

        @Source("text/expression.txt")
        TextResource expressionTxt();

        @Source("images/molecules.png")
        ImageResource molecules();

        @Source("images/molecules_icon.png")
        ImageResource moleculesIcon();

        @Source("text/molecules.txt")
        TextResource moleculesTxt();

        @Source("images/structures.png")
        ImageResource structures();

        @Source("images/structures_icon.png")
        ImageResource structuresIcon();

        @Source("text/structures.txt")
        TextResource structuresTxt();
    }
}
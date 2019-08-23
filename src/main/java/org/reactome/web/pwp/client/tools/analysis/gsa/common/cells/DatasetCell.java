package org.reactome.web.pwp.client.tools.analysis.gsa.common.cells;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.AnnotationProperty;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.dataset.GSADataset;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.DatasetTypesPalette;
import org.reactome.web.pwp.client.tools.analysis.gsa.style.GSAStyleFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders the items in the annotated datasets' list
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DatasetCell extends AbstractCell<GSADataset> {

    private static SafeHtml editIcon;
    private static SafeHtml deleteIcon;

    interface Templates extends SafeHtmlTemplates {

        @Template("" +
                "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; display: flex;\">" +
                    "<div title=\"{0}\" style=\"{5}; height:18px; width:18px; margin:2px 10px 0 5px; border-radius:10px; cursor: default;\"></div>" +
                    "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:medium; margin:0 10px 0 5px; width:250px; line-height: 25px; height:25px; cursor:default;\">{1}</div>" +
                    "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small; margin:0 5px 0 0; width:150px; line-height: 25px; height:25px; cursor:default;\">{2}</div>" +
                    "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small; margin:0 5px 0 0; width:180px; line-height: 25px; height:25px; cursor:default;\">{3}</div>" +
                    "<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small; margin:0 10px 0 0; width:180px; line-height: 25px; height:25px; cursor:default;\">{4}</div>" +
                    "<a><div class=\"editIcon\" title=\"Edit annotation\" style=\"margin:2px 25px 0 0; border: 1px solid #86b3cd; border-radius:12px; background-color: white; padding: 2px 3px; cursor: pointer;\">{6}</div></a>" +
                    "<a><div class=\"deleteIcon\" title=\"Remove dataset\" style=\"margin:2px 10px 0 0; border: 1px solid #86b3cd; border-radius:12px; background-color: white; padding: 2px 5px; cursor: pointer;\">{7}</div></a>" +
                "</div>")
        SafeHtml cell(String type, SafeHtml name, SafeHtml comparisonFactor, SafeHtml groups, SafeHtml covariates, SafeStyles colour, SafeHtml editImage, SafeHtml deleteImage);
    }

    private static Templates templates = GWT.create(Templates.class);

    public DatasetCell() {
        super();

        Image img = new Image(GSAStyleFactory.RESOURCES.editIcon());
        img.setStyleName(GSAStyleFactory.RESOURCES.getCSS().itemIcon());
        DatasetCell.editIcon = SafeHtmlUtils.fromTrustedString(img.toString());

        Image delImg = new Image(GSAStyleFactory.RESOURCES.deleteIcon());
        delImg.setStyleName(GSAStyleFactory.RESOURCES.getCSS().itemIcon());
        DatasetCell.deleteIcon = SafeHtmlUtils.fromTrustedString(delImg.toString());
    }

    @Override
    public void render(Cell.Context context, GSADataset value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }
        SafeStyles colour = SafeStylesUtils.forTrustedBackgroundColor(DatasetTypesPalette.get().colourFromType(value.getType()));

        SafeHtml name = SafeHtmlUtils.fromTrustedString(value.getName());
        SafeHtml comparisonFactor = SafeHtmlUtils.fromTrustedString(value.getAnnotations().getSelectedComparisonFactor());
        SafeHtml groups = SafeHtmlUtils.fromTrustedString(value.getAnnotations().getGroupOne() + " vs " + value.getAnnotations().getGroupTwo());

        List<AnnotationProperty> covariatesList = value.getAnnotations().getCovariates();
        SafeHtml covariates = SafeHtmlUtils.fromTrustedString("-");
        if (covariatesList!=null) {
            String cov = covariatesList.stream().map(c -> c.getName()).collect(Collectors.joining(", "));
            if (cov != null && !cov.isEmpty()) {
                cov = "[" + cov + "]";
            }
            covariates = SafeHtmlUtils.fromTrustedString(cov);
        }
        sb.append(templates.cell(value.getTypeName(), name, comparisonFactor, groups, covariates, colour, editIcon, deleteIcon));
    }
}

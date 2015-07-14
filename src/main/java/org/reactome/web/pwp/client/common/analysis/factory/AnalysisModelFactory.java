package org.reactome.web.pwp.client.common.analysis.factory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.reactome.web.pwp.client.common.analysis.model.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisModelFactory {

    @SuppressWarnings("UnusedDeclaration")
    interface AnalysisAutoBeanFactory extends AutoBeanFactory {
        AutoBean<AnalysisResult> analysisResult();
        AutoBean<AnalysisSummary> analysisSummary();
        AutoBean<ResourceSummary> resourceSummary();
        AutoBean<PathwaySummary> pathwaySummary();
        AutoBean<SpeciesSummary> getSpeciesSummary();
        AutoBean<EntityStatistics> entityStatistics();
        AutoBean<ReactionStatistics> reactionStatistics();
        AutoBean<PathwayIdentifiers> pathwayIdentifiers();
        AutoBean<PathwayIdentifier> pathwayIdentifier();
        AutoBean<IdentifierMap> identifierMap();
        AutoBean<IdentifierSummary> identifierSummary();
        AutoBean<ExpressionSummary> expressionBoundaries();
    }

    public static <T> T getModelObject(Class<T> cls, String json) throws AnalysisModelException {
        try{
            AutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
            AutoBean<T> bean = AutoBeanCodex.decode(factory, cls, json);
            return bean.as();
        }catch (Throwable e){
            throw new AnalysisModelException("Error mapping json string for [" + cls + "]: " + json, e);
        }
    }

    public static List<PathwaySummary> getPathwaySummaryList(String json)
            throws AnalysisModelException, NullPointerException, IllegalArgumentException, JSONException {
        List<PathwaySummary> rtn = new LinkedList<PathwaySummary>();
        JSONArray array = JSONParser.parseStrict(json).isArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.get(i).isObject();
            rtn.add(getModelObject(PathwaySummary.class, jsonObject.toString()));
        }
        return rtn;
    }
}

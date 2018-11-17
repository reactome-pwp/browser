package org.reactome.web.pwp.client.tools.analysis.tissues.client.model.factory;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.ExperimentError;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.ExperimentSummary;
import org.reactome.web.pwp.client.tools.analysis.tissues.client.model.SummariesResult;


/**
 @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ExperimentDigesterFactory {

    public static <T> T getModelObject(Class<T> cls, String json) throws ExperimentDigesterException {
        try{
            AutoBeanFactory factory = GWT.create(ModelAutoBeanFactory.class);
            AutoBean<T> bean = AutoBeanCodex.decode(factory, cls, json);
            return bean.as();
        }catch (Throwable e){
            throw new ExperimentDigesterException("Error mapping json string for [" + cls + "]: " + json, e);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    interface ModelAutoBeanFactory extends AutoBeanFactory {
        AutoBean<SummariesResult> summariesResult();
        AutoBean<ExperimentSummary> experimentSummary();
        AutoBean<ExperimentError> error();
    }
}

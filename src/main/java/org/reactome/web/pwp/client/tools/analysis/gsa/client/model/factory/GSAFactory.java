package org.reactome.web.pwp.client.tools.analysis.gsa.client.model.factory;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.*;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GSAFactory {

    public static <T> T getModelObject(Class<T> cls, String json) throws GSAException {
        try{
            AutoBeanFactory factory = GWT.create(ModelAutoBeanFactory.class);
            AutoBean<T> bean = AutoBeanCodex.decode(factory, cls, json);
            return bean.as();
        } catch (Throwable e){
            throw new GSAException("Error mapping json string for [" + cls + "]: " + json, e);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    interface ModelAutoBeanFactory extends AutoBeanFactory {
        AutoBean<Method> method();
        AutoBean<Parameter> parameter();
        AutoBean<MethodsResult> methods();

        AutoBean<DatasetType> datasetType();
        AutoBean<DatasetTypesResult> types();

        AutoBean<ExampleDataset> example();
        AutoBean<ExampleDatasetsResult> exampleDatasets();
        AutoBean<ExampleParameter> exampleParameter();
        AutoBean<ExampleDatasetSummary> exampleSummary();
        AutoBean<ExampleMetadata> metadata();

        AutoBean<UploadResult> uploadResult();

        AutoBean<Status> status();

        AutoBean<Link> link();
        AutoBean<ResultLinks> result();

        AutoBean<GSAError> error();
    }
}

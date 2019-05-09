package org.reactome.web.pwp.client.tools.analysis.gsa.client;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.DatasetType;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.GSAError;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.Method;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface GSAClientHandler {

    void onError(GSAError error);
    void onException(String msg);

    interface GSAMethodsHandler extends GSAClientHandler {
        void onMethodsSuccess(List<Method> methods);
    }

    interface GSADatasetTypesHandler extends GSAClientHandler {
        void onTypesSuccess(List<DatasetType> types);
    }

    interface GSAUploadHandler extends GSAClientHandler {
        void onUploadSuccess();
    }
}

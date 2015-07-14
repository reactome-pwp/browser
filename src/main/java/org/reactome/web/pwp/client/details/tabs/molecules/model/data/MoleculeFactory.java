package org.reactome.web.pwp.client.details.tabs.molecules.model.data;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.model.factory.SchemaClass;
import org.reactome.web.pwp.model.handlers.DatabaseObjectLoadedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MoleculeFactory {

    public static void load(final Molecule molecule, final DatabaseObjectLoadedHandler handler) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/ReferenceEntity/" + molecule.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    Molecule molecule;
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                                SchemaClass schemaClass = DatabaseObjectUtils.getSchemaClass(json);
                                molecule = new Molecule(schemaClass);
                                molecule.load(json);
                            } catch (Exception ex) {
                                handler.onDatabaseObjectError(ex);
                                return;
                            }
                            handler.onDatabaseObjectLoaded(molecule);
                            return;
                    }
                    handler.onDatabaseObjectError(new Exception(response.getStatusText()));
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onDatabaseObjectError(exception);
                }
            });
        } catch (RequestException ex) {
            handler.onDatabaseObjectError(ex);
        }
    }
}

package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.ExternalDatasource;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects and presents all the parameters for a given method,
 * as they are retrieved from the server
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ParametersPanel extends FlowPanel {

    public ParametersPanel(final Method method) {
        prepareParameters(method.getParameters());
    }

    public ParametersPanel(final ExternalDatasource externalDatasource) {
        prepareParameters(externalDatasource.getParameters());
    }

    public void prepareParameters(List<Parameter> parameters) {
        for (Parameter par : parameters) {
            Console.info("PPREPAREPARAMETERS: " + par.getName());
            if(par.getScope() != null && par.getScope().equalsIgnoreCase("common")) continue;

            AbstractParameterWidget widget = null;
            switch(par.getType()) {
                case "string":
                    widget = par.getValues() == null ? new TextBoxParameter(par) : new DropDownParameter(par);
                    break;
                case "int":
                    widget = new IntParameter(par);
                    break;
                case "float":
                    widget = new FloatParameter(par);
                    break;
                case "bool":
                    widget = new BooleanParameter(par);
                    break;
                default:
                    Console.error("Unknown parameter type [" + par.getType() + "]");
                    break;
            }
            if(widget != null) {
                add(widget);
            }
        }

        addDomHandler(e -> {
            e.preventDefault();
            e.stopPropagation();
        }, ClickEvent.getType());
    }

    public boolean validateAllParameters() {
        boolean rtn = true;
        for (Widget widget : getChildren()) {
            AbstractParameterWidget parameterWidget = (AbstractParameterWidget) widget;
            parameterWidget.showValidationError(false);
            Console.info("Validating KEY: " + parameterWidget.getName());
            Console.info("Validating VALUE: " + parameterWidget.getValue());
            if (!parameterWidget.validate()) {
                Console.info("Parameter validation error: " + parameterWidget.getName());
                parameterWidget.showValidationError(true);
                rtn = false;
            }
        }
        return rtn;
    }

    public Map<String, String> getParameterValues() {
        Map<String, String> rtn = new HashMap<>();
        for (Widget widget : getChildren()) {
            AbstractParameterWidget<?> parameterWidget = (AbstractParameterWidget<?>) widget;
            rtn.put(parameterWidget.getName(), parameterWidget.getValue());
            Console.info(parameterWidget.getName());
        }

        return rtn;
    }
}

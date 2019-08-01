package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Method;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ParametersPanel extends FlowPanel {
    private Method method;

    public ParametersPanel(final Method method) {
        this.method = method;

        for (Parameter par : method.getParameters()) {
            if(par.getScope().equalsIgnoreCase("common")) continue;

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
            if (!parameterWidget.validate()) {
                Console.info("Parameter validation error: " + parameterWidget.getName());
                rtn = false;
            }
        }
        return rtn;
    }

    public Map<String, String> getParameterValues() {
        Map<String, String> rtn = new HashMap<>();
        for (Widget widget : getChildren()) {
            AbstractParameterWidget parameterWidget = (AbstractParameterWidget) widget;
            rtn.put(parameterWidget.getName(), parameterWidget.getValue());
        }

        return rtn;
    }


}

package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;
import org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.FlipSwitch;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class BooleanParameter extends AbstractParameterWidget<Boolean> implements FlipSwitch.Handler {
    private FlipSwitch input;

    public BooleanParameter(Parameter parameter) {
        super(parameter);
        initUI();
    }

    @Override
    public String getValue() {
        return input.isChecked().toString();
    }

    @Override
    public void setValue(String value) {
        this.value = Boolean.parseBoolean(value);
        input.setChecked(this.value, false);
    }

    @Override
    protected void setDefault() {
        if (parameter.getDefault() != null) {
            setValue(parameter.getDefault());
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

    private void initUI() {
        input = new FlipSwitch();
        input.setHandler(this);
        input.addStyleName(AbstractParameterWidget.RESOURCES.getCSS().flipSwitch());
        setDefault();

        add(input);
    }

    @Override
    public void onValueChange(boolean value) {
        this.value = value;
        if (handler != null) {
            handler.onParameterChange(value);
        }
    }
}
package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.user.client.ui.TextBox;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FloatParameter extends AbstractParameterWidget<Float> {
    private TextBox input;

    public FloatParameter(Parameter parameter) {
        super(parameter);
        initUI();
    }

    @Override
    public String getValue() {
        return input.getValue();
    }

    @Override
    public void setValue(String value) {
        this.value = Float.parseFloat(value);
        input.setText(value);
    }

    @Override
    protected void setDefault() {
        input.setText(parameter.getDefault());
    }

    @Override
    public boolean validate() {
        try {
            Float.parseFloat(input.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void initUI() {
        input = new TextBox();
        setDefault();

        add(input);
    }
}

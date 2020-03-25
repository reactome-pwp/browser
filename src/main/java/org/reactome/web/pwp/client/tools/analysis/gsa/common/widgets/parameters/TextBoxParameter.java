package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.user.client.ui.TextBox;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class TextBoxParameter extends AbstractParameterWidget<String> {
    private TextBox input;

    public TextBoxParameter(Parameter parameter) {
        super(parameter);
        initUI();
    }

    @Override
    public String getValue() {
        return input.getValue();
    }

    @Override
    public void setValue(String value) {
        Console.info("SET VALUE: " + value);
        this.value = value;
        input.setText(value);
    }

    @Override
    protected void setDefault() {
        input.setText(parameter.getDefault());
    }

    @Override
    public boolean validate() {
        return true;
    }

    private void initUI() {
        input = new TextBox();
        setDefault();

        includeWidget(input);
    }
}

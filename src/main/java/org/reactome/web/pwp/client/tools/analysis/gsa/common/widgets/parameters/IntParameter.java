package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.TextBox;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class IntParameter extends AbstractParameterWidget<Integer> {
    private static RegExp regExp = RegExp.compile("^\\d+$");

    private TextBox input;

    public IntParameter(Parameter parameter) {
        super(parameter);
        initUI();
    }

    @Override
    public String getValue() {
        return input.getValue();
    }

    @Override
    public void setValue(String value) {
        this.value = Integer.parseInt(value);
        input.setText(value);
    }

    @Override
    protected void setDefault() {
        input.setText(parameter.getDefault());
    }

    @Override
    public boolean validate() {
        return regExp.test(input.getText().trim());
    }

    private void initUI() {
        input = new TextBox();
        input.addKeyUpHandler(e -> {
            if (validationIcon != null && validationIcon.isVisible()) {
                showValidationError(false);
            }
        });
        setDefault();

        includeWidget(input);
        addValidationWidget(HINT_INTEGER);
    }
}

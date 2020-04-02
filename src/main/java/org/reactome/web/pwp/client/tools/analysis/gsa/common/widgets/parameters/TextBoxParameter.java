package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.user.client.ui.TextBox;
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
        this.value = value;
        input.setText(value);
    }

    @Override
    protected void setDefault() {
        input.setText(parameter.getDefault());
    }

    @Override
    public boolean validate() {
        if (!parameter.isRequired()) return true;
        return input.getText() != null && !input.getText().trim().equalsIgnoreCase("");
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
        addValidationWidget(HINT_REQUIRED);

    }
}

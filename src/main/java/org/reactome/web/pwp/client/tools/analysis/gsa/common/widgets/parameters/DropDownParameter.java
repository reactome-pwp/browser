package org.reactome.web.pwp.client.tools.analysis.gsa.common.widgets.parameters;

import com.google.gwt.user.client.ui.ListBox;
import org.reactome.web.pwp.client.tools.analysis.gsa.client.model.raw.Parameter;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DropDownParameter extends AbstractParameterWidget<String> {
    private ListBox input;

    public DropDownParameter(Parameter parameter) {
        super(parameter);
        initUI();
    }

    @Override
    public String getValue() {
        return input.getValue(input.getSelectedIndex());
    }

    @Override
    protected void setDefault() {
        if (parameter.getDefault() != null) {
            input.setSelectedIndex(getItemIndex(parameter.getDefault()));
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

    private void initUI() {
        input = new ListBox();
        input.setMultipleSelect(false);
        for (String value : parameter.getValues()) {
            input.addItem(value);
        }
        setDefault();
        add(input);
    }

    private int getItemIndex(String item) {
        int indexToFind = -1;
        for (int i = 0; i < input.getItemCount(); i++) {
            if (input.getItemText(i).equals(item)) {
                indexToFind = i;
                break;
            }
        }
        return indexToFind;
    }
}

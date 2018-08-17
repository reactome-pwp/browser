package org.reactome.web.pwp.client.tools.analysis.tissues.listselector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import org.reactome.web.diagram.common.IconButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ListSelector<T> extends FlowPanel implements ClickHandler {
    private List<T> listItems;
    private List<T> selectedItems;

    private FlowPanel header;
    private FlowPanel container;

    private FlowPanel leftListWrapper;
    private FlowPanel rightListWrapper;

    private ListBox leftListBox;
    private ListBox rightListBox;

    private FlowPanel buttonsPanel;
    private Button addBtn;
    private Button addAllBtn;
    private Button removeBtn;
    private Button removeAllBtn;

    private Label headerLabel;

    private Handler handler;
    public interface Handler<T> {
        void onSelectedListChanged(List<T> selectedItems);
    }

    public ListSelector(String headerTitle, Handler handler) {
        this.handler = handler;
        selectedItems = new ArrayList<>();
        setStyleName(RESOURCES.getCSS().panel());

        headerLabel = new Label(headerTitle);

        header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().header());
        header.add(headerLabel);

        leftListBox = getListBox(RESOURCES.getCSS().listBox());
        rightListBox = getListBox(RESOURCES.getCSS().listBox());

        leftListWrapper = getListWrapper("Available Tissues:", leftListBox);
        rightListWrapper = getListWrapper("Selected Tissues:", rightListBox);

        addBtn = new IconButton("Add", RESOURCES.addIcon());
        addBtn.setStyleName(RESOURCES.getCSS().addBtn());
        addBtn.addClickHandler(this);

        addAllBtn = new IconButton("Add all", RESOURCES.addIcon());
        addAllBtn.setStyleName(RESOURCES.getCSS().addBtn());
        addAllBtn.addClickHandler(this);

        removeBtn = new IconButton("Remove", RESOURCES.addIcon());
        removeBtn.setStyleName(RESOURCES.getCSS().addBtn());
        removeBtn.addClickHandler(this);

        removeAllBtn = new IconButton("Remove all", RESOURCES.addIcon());
        removeAllBtn.setStyleName(RESOURCES.getCSS().addBtn());
        removeAllBtn.addClickHandler(this);

        buttonsPanel = new FlowPanel();
        buttonsPanel.setStyleName(RESOURCES.getCSS().buttonsPanel());
        buttonsPanel.add(addAllBtn);
        buttonsPanel.add(addBtn);
        buttonsPanel.add(removeBtn);
        buttonsPanel.add(removeAllBtn);

        container = new FlowPanel();
        container.setStyleName(RESOURCES.getCSS().container());
        container.add(leftListWrapper);
        container.add(buttonsPanel);
        container.add(rightListWrapper);

        add(header);
        add(container);
    }

    public void setHeaderTitle(String headerTitle) {
        headerLabel.setText(headerTitle);
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    public void setAvailableListItems(List<T> listItems) {
        this.listItems = listItems;
        clearSelectedItems();
        if (listItems == null) {
            setVisible(false);
        } else {
            leftListBox.clear();
            listItems.forEach(item -> leftListBox.addItem(item.toString()));
            setVisible(true);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        Button button = (Button) event.getSource();
        if (button.equals(addAllBtn)) {
            clearSelectedItems();
            listItems.forEach(item -> addSelectedItem(item));
            handler.onSelectedListChanged(selectedItems);
        } else if (button.equals(removeAllBtn)) {
            clearSelectedItems();
            handler.onSelectedListChanged(selectedItems);
        } else if (button.equals(addBtn)) {
            getSelectedLeftListItems().forEach(item -> addSelectedItem(item));
            handler.onSelectedListChanged(selectedItems);
        } else if (button.equals(removeBtn)) {
            getSelectedRightListItems().forEach(item -> removeSelectedItem(item));
            updateRightListFromSelected();
            handler.onSelectedListChanged(selectedItems);
        }
    }

    private void addSelectedItem(T T) {
        if (!selectedItems.contains(T)) {
            selectedItems.add(T);
            rightListBox.addItem(T.toString());
        }
    }

    private void removeSelectedItem(String value) {
        Optional<T> item = selectedItems.stream().filter(v -> v.equals(value)).findAny();
        item.ifPresent(t -> selectedItems.remove(t));
    }

    private void clearSelectedItems() {
        rightListBox.clear();
        selectedItems.clear();
    }

    private void updateRightListFromSelected() {
        rightListBox.clear();
        selectedItems.forEach(item -> rightListBox.addItem(item.toString()));
    }

    private ListBox getListBox(String style) {
        ListBox rtn = new ListBox();
        rtn.setStyleName(style);
        rtn.setMultipleSelect(true);
        rtn.setVisibleItemCount(10);

        return rtn;
    }

    private FlowPanel getListWrapper(String title, ListBox wrappedListBox) {
        Label titleLB = new Label(title);
        titleLB.setStyleName(RESOURCES.getCSS().listWrapperLabel());

        FlowPanel rtn = new FlowPanel();
        rtn.setStyleName(RESOURCES.getCSS().listWrapper());
        rtn.add(titleLB);
        rtn.add(wrappedListBox);
        return rtn;
    }

    private List<T> getSelectedLeftListItems() {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < leftListBox.getItemCount(); i++) {
            if (leftListBox.isItemSelected(i)) {
                selectedItems.add(listItems.get(i));
            }
        }
        return selectedItems;
    }

    private List<String> getSelectedRightListItems() {
        List<String> selectedItems = new ArrayList<>();
        for (int i = 0; i < rightListBox.getItemCount(); i++) {
            if (rightListBox.isItemSelected(i)) {
                selectedItems.add(rightListBox.getValue(i));
            }
        }
        return selectedItems;
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ListSelectorCSS.CSS)
        ListSelectorCSS getCSS();

        @Source("images/ok.png")
        ImageResource addIcon();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-ListSelector")
    public interface ListSelectorCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/tools/analysis/tissues/ListSelector.css";

        String panel();

        String header();

        String container();

        String listWrapper();

        String listWrapperLabel();

        String listBox();

        String buttonsPanel();

        String addBtn();

    }

}

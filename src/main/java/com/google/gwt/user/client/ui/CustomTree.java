package com.google.gwt.user.client.ui;

/**
 * The selection is never updated when opening or closing
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CustomTree extends Tree {

    /**
     * Ensures that the item is visible, opening its parents
     * and scrolling the tree as necessary.
     */
    public void ensureItemVisible(TreeItem item) {
        if(item==null) return;
        TreeItem parent = item.getParentItem();
        while (parent != null) {
            parent.setState(true);
            parent = parent.getParentItem();
        }
        item.getElement().scrollIntoView();
    }

//    /**
//     * Selects the item and ensures that all its parents are opened and
//     * the container is scrolled to show it
//     *
//     * @param item the tree item to be selected
//     * @param fireEvents whether the action has to be reported with an event or not
//     */
//    @Override
//    public void setSelectedItem(TreeItem item, boolean fireEvents) {
//        this.ensureItemVisible(item);
//        super.setSelectedItem(item, fireEvents);
//    }

    @Override
    void maybeUpdateSelection(TreeItem itemThatChangedState, boolean isItemOpening) {
        //overriding this method and doing nothing here works for our use case
    }
}

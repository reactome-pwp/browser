package org.reactome.web.pwp.client.details.tabs.analysis.widgets.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * A custom Pager that maintains a set page size and displays page numbers
 * and total pages more elegantly. SimplePager will ensure <code>pageSize</code>
 * rows are always rendered even if the "last" page has less than
 * <code>pageSize</code> rows remain.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CustomPager extends SimplePager {

    //Page size is normally derived from the visibleRange
    private int pageSize = 10;

    public CustomPager() {
        super(SimplePager.TextLocation.CENTER,
                CUSTOM_IMAGES, true, 500, false);
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        super.setPageSize( pageSize );
    }

    //This is a work around because super.getPage() does not work as expected
    @Override
    public int getPage() {
        return this.getPageStart() / this.getPageSize();
    }

    // We want pageSize to remain constant
    @Override
    public int getPageSize() {
        return pageSize;
    }

    // Page forward by an exact size rather than the number of visible
    // rows as is in the norm in the underlying implementation
    @Override
    public void nextPage() {
        if ( getDisplay() != null ) {
            Range range = getDisplay().getVisibleRange();
            setPageStart( range.getStart()
                    + getPageSize() );
        }
    }

    // Page back by an exact size rather than the number of visible rows
    // as is in the norm in the underlying implementation
    @Override
    public void previousPage() {
        if ( getDisplay() != null ) {
            Range range = getDisplay().getVisibleRange();
            setPageStart( range.getStart()
                    - getPageSize() );
        }
    }

    // Override so the last page is shown with a number of rows less
    // than the pageSize rather than always showing the pageSize number
    // of rows and possibly repeating rows on the last and penultimate
    // page
    @Override
    public void setPageStart(int index) {
        if ( getDisplay() != null ) {
            Range range = getDisplay().getVisibleRange();
            int displayPageSize = getPageSize();
            if ( isRangeLimited()
                    && getDisplay().isRowCountExact() ) {
                displayPageSize = Math.min( getPageSize(),
                        getDisplay().getRowCount()
                                - index );
            }
            index = Math.max( 0,
                    index );
            if ( index != range.getStart() ) {
                getDisplay().setVisibleRange( index,
                        displayPageSize );
            }
        }
    }

    // Override to display "0 of 0" when there are no records (otherwise
    // you get "1-1 of 0") and "1 of 1" when there is only one record
    // (otherwise you get "1-1 of 1"). Not internationalised (but
    // neither is SimplePager)
    protected String createText() {
        NumberFormat formatter = NumberFormat.getFormat( "#,###" );
        HasRows display = getDisplay();
        Range range = display.getVisibleRange();
        int pageStart = range.getStart() + 1;
        int pageSize = range.getLength();
        int dataSize = display.getRowCount();
        int endIndex = Math.min( dataSize,
                pageStart
                        + pageSize
                        - 1 );
        endIndex = Math.max( pageStart,
                endIndex );
        boolean exact = display.isRowCountExact();
        if ( dataSize == 0 ) {
            return "0 of 0";
        } else if ( pageStart == endIndex ) {
            return formatter.format( pageStart )
                    + " of "
                    + formatter.format( dataSize );
        }
        return formatter.format( pageStart )
                + "-"
                + formatter.format( endIndex )
                + (exact ? " of " : " of over ")
                + formatter.format( dataSize );
    }


    private static CustomPagerResources CUSTOM_IMAGES;
    static {
        CUSTOM_IMAGES = GWT.create(CustomPagerResources.class);
    }

    public interface CustomPagerResources extends SimplePager.Resources {
        // Here you can @Override all the methods and place @Source annotation to tell
        // SimplePager what image should be loaded as a replacement

        /**
         * The image used to skip ahead multiple pages.
         */
        @Override
        @Source( "../images/fastForward.png" )
        ImageResource simplePagerFastForward();

        /**
         * The disabled "fast forward" image.
         */
        @Override
        @Source( "../images/fastForwardDisabled.png" )
        ImageResource simplePagerFastForwardDisabled();

        /**
         * The image used to go to the first page.
         */
        @Override
        @Source( "../images/firstPage.png" )
        ImageResource simplePagerFirstPage();

        /**
         * The disabled first page image.
         */
        @Override
        @Source( "../images/firstPageDisabled.png" )
        ImageResource simplePagerFirstPageDisabled();

        /**
         * The image used to go to the last page.
         */
        @Override
        @Source( "../images/lastPage.png" )
        ImageResource simplePagerLastPage();

        /**
         * The disabled last page image.
         */
        @Override
        @Source( "../images/lastPageDisabled.png" )
        ImageResource simplePagerLastPageDisabled();

        /**
         * The image used to go to the next page.
         */
        @Override
        @Source( "../images/nextPage.png" )
        ImageResource simplePagerNextPage();

        /**
         * The disabled next page image.
         */
        @Override
        @Source( "../images/nextPageDisabled.png" )
        ImageResource simplePagerNextPageDisabled();

        /**
         * The image used to go to the previous page.
         */
        @Override
        @Source( "../images/previousPage.png" )
        ImageResource simplePagerPreviousPage();

        /**
         * The disabled previous page image.
         */
        @Override
        @Source( "../images/previousPageDisabled.png" )
        ImageResource simplePagerPreviousPageDisabled();

        @Source("CustomPager.css")
        Style simplePagerStyle();
    }

    /**
     * Styles used by this widget.
     */
    public interface Style extends SimplePager.Style {

        /**
         * Applied to buttons.
         */
        String button();

        /**
         * Applied to disabled buttons.
         */
        String disabledButton();

        /**
         * Applied to the details text.
         */
        String pageDetails();
    }

}

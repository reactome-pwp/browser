package org.reactome.web.pwp.client.details.tabs.analysis.widgets.filtering.size;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Bin {
    private int index;
    private int value;
    private int normalisedValue;
    private int start;
    private int end;

    private int x;
    private int y;
    private int w;
    private int h;

    public Bin(int index, int value, int normalisedValue, int start, int end, int x, int y, int w, int h) {
        this.index = index;
        this.value = value;
        this.normalisedValue = normalisedValue;
        this.start = start;
        this.end = end;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int index() {
        return index;
    }

    public int value() {
        return value;
    }

    public int normalisedValue() {
        return normalisedValue;
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int w() {
        return w;
    }

    public int h() {
        return h;
    }
}

/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package joptsimple.internal;

import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Columns;
import joptsimple.internal.Row;
import joptsimple.internal.Strings;

public class Rows {
    private final int overallWidth;
    private final int columnSeparatorWidth;
    private final List<Row> rows = new ArrayList<Row>();
    private int widthOfWidestOption;
    private int widthOfWidestDescription;

    public Rows(int overallWidth, int columnSeparatorWidth) {
        this.overallWidth = overallWidth;
        this.columnSeparatorWidth = columnSeparatorWidth;
    }

    public void add(String option, String description) {
        this.add(new Row(option, description));
    }

    private void add(Row row) {
        this.rows.add(row);
        this.widthOfWidestOption = Math.max(this.widthOfWidestOption, row.option.length());
        this.widthOfWidestDescription = Math.max(this.widthOfWidestDescription, row.description.length());
    }

    public void reset() {
        this.rows.clear();
        this.widthOfWidestOption = 0;
        this.widthOfWidestDescription = 0;
    }

    public void fitToWidth() {
        Columns columns = new Columns(this.optionWidth(), this.descriptionWidth());
        ArrayList<Row> fitted = new ArrayList<Row>();
        for (Row each : this.rows) {
            fitted.addAll(columns.fit(each));
        }
        this.reset();
        for (Row each : fitted) {
            this.add(each);
        }
    }

    public String render() {
        StringBuilder buffer = new StringBuilder();
        for (Row each : this.rows) {
            this.pad(buffer, each.option, this.optionWidth()).append(Strings.repeat(' ', this.columnSeparatorWidth));
            this.pad(buffer, each.description, this.descriptionWidth()).append(Strings.LINE_SEPARATOR);
        }
        return buffer.toString();
    }

    private int optionWidth() {
        return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestOption);
    }

    private int descriptionWidth() {
        return Math.min(this.overallWidth - this.optionWidth() - this.columnSeparatorWidth, this.widthOfWidestDescription);
    }

    private StringBuilder pad(StringBuilder buffer, String s, int length) {
        buffer.append(s).append(Strings.repeat(' ', length - s.length()));
        return buffer;
    }
}


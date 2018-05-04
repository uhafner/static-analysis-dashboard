package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class LineChartDataSet {
    private final List<Integer> data = new ArrayList<>();
    private final String backgroundColor;
    private final String borderColor;
    private final boolean fill;
    private final String label;

    public LineChartDataSet(final List<Integer> values, final String label, final boolean isFilled,
            final String backgroundColor, final String borderColor) {
        data.addAll(values);
        this.label = label;
        fill = isFilled;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
    }
    }


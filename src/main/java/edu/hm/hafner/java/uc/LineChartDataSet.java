package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Model of a data set for line charts using Chart.js.
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "unused", "PMD.SingularField"}) // Will be converted to Json
@SuppressFBWarnings("URF")
public class LineChartDataSet {
    private final List<Integer> data = new ArrayList<>();
    private final String backgroundColor;
    private final String borderColor;
    private final boolean fill;
    private final String label;

    /**
     * Creates a new data set for a line chart.
     *
     * @param values
     *         the values to show in the chart
     * @param label
     *         the label for the data set
     * @param isFilled
     *         determines, whether the chart should be filled or not
     * @param backgroundColor
     *         the background color of the chart
     * @param borderColor
     *         the border color of the chart
     */
    public LineChartDataSet(final List<Integer> values, final String label, final boolean isFilled,
            final String backgroundColor, final String borderColor) {
        data.addAll(values);
        this.label = label;
        fill = isFilled;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
    }
}


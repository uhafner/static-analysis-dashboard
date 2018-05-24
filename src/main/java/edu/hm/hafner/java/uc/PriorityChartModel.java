package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.list.MutableList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Model for a chart that shows the distribution of issues by priority.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "unused"}) // Will be converted to Json
@SuppressFBWarnings("URF")
public class PriorityChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    /**
     * Creates the model for the severity chart. This model maps a severity to a list of integer values that represent
     * the number of issues for the selected severity for each of the recorded builds. Note that the size and the order
     * of the list is not checked and must be the same for each of the data sets.
     *
     * @param values
     *         a list of integer values for each build number
     */
    public PriorityChartModel(final Map<String, MutableList<Integer>> values) {
        datasets.add(new LineChartDataSet(values.get("low"), "Low Priority", true,
                "#b8daff", "#a8caef"));
        datasets.add(new LineChartDataSet(values.get("normal"), "Normal Priority", true,
                "#ffeeba", "#efdeaa"));
        datasets.add(new LineChartDataSet(values.get("high"), "High Priority", true,
                "#f5c6cb", "#e5b6bb"));
    }
}

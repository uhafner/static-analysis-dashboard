package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.list.MutableList;

/**
 * Model for a chart that shows the distribution of issues by priority.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class PriorityChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    public PriorityChartModel(final Map<String, MutableList<Integer>> values) {
        datasets.add(new LineChartDataSet(values.get("low"), "Low Priority", true,
                "#b8daff", "#a8caef"));
        datasets.add(new LineChartDataSet(values.get("normal"), "Normal Priority", true,
                "#ffeeba", "#efdeaa"));
        datasets.add(new LineChartDataSet(values.get("high"), "High Priority", true,
                "#f5c6cb", "#e5b6bb"));
    }
}

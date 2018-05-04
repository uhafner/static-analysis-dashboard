package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.list.MutableList;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class OriginChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    public OriginChartModel(final Map<String, MutableList<Integer>> values) {
        datasets.add(new LineChartDataSet(values.get("checkstyle"), "CheckStyle", false,
                "#b8daff", "#a8caef"));
        datasets.add(new LineChartDataSet(values.get("pmd"), "PMD", false,
                "#f5c6cb", "#e5b6bb"));
    }
}

package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.api.list.FixedSizeList;
import org.eclipse.collections.api.map.FixedSizeMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class LineChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    public LineChartModel(final Map<String, List<Integer>> values) {
        List<String> builds = new ArrayList<>(values.keySet());
        Collections.sort(builds);
        List<Integer> high = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();
        List<Integer> low = new ArrayList<>();

        FixedSizeMap<String, FixedSizeList<Integer>> dummy = Maps.fixedSize
                .of("1", Lists.fixedSize.of(10, 20, 30),
                        "2", Lists.fixedSize.of(20, 40, 60),
                        "3", Lists.fixedSize.of(60, 20, 30));
        for (String build : dummy.keySet()) {
            labels.add(build);
            high.add(dummy.get(build).get(0));
            normal.add(dummy.get(build).get(1));
            low.add(dummy.get(build).get(2));
        }
        datasets.add(new LineChartDataSet(high, "High Priority", true,
                "#f5c6cb", "#e5b6bb"));
        datasets.add(new LineChartDataSet(normal, "Normal Priority", true,
                "#ffeeba", "#efdeaa"));
        datasets.add(new LineChartDataSet(low, "Low Priority", true,
                "#b8daff", "#a8caef"));
    }
}

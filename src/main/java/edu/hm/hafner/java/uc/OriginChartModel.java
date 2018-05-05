package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.collections.api.list.MutableList;

/**
 * Model for a chart that shows the number of issues per tool (i.e. origin).
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class OriginChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    public OriginChartModel(final Map<String, MutableList<Integer>> values) {
        Palette[] colors = Palette.values();
        int colorIndex = 0;
        for (Entry<String, MutableList<Integer>> dataSet : values.entrySet()) {
            Palette color = colors[colorIndex];
            datasets.add(new LineChartDataSet(dataSet.getValue(), dataSet.getKey(),
                    false, Palette.toWebColor(color), Palette.toWebColor(color.brighter())));
            colorIndex++;
            colorIndex %= colors.length;
        }
    }
}

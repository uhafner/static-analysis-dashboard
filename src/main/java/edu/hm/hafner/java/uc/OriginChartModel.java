package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.collections.api.list.MutableList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Model for a chart that shows the number of issues per tool (i.e. origin).
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "unused"}) // Will be converted to Json
@SuppressFBWarnings("URF")
public class OriginChartModel {
    private final List<String> labels = new ArrayList<>();
    private final List<LineChartDataSet> datasets = new ArrayList<>();

    /**
     * Creates the model for the origin chart. This model maps a origin (the tool ID) to a list of integer values that
     * represent the number of issues for the selected tool for each of the recorded builds. Note that the size and
     * the order of the list is not checked and must be the same for each of the data sets.
     *
     * @param values
     *         a list of integer values for each build number
     */
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

package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.hm.hafner.analysis.Issues;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Model that provides the sizes of a set of {@link Issues}.
 *
 * @author Ullrich Hafner
 * @see <a href="http://www.chartjs.org/docs/latest/charts/bar.html#dataset-properties">Bar Chart Dataset</a>
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
@SuppressFBWarnings("URF")
public class IssuePropertyDistribution {
    private final List<String> labels = new ArrayList<>();
    private final List<IssuesSizeList> datasets = new ArrayList<>();

    /**
     * Creates a new instance of {@link IssuePropertyDistribution}.
     *
     * @param counts
     *         a mapping of properties to number of issues
     */
    public IssuePropertyDistribution(final Map<String, Integer> counts) {
        List<Integer> values = createValuesAndSetLabels(counts);
        datasets.add(new IssuesSizeList(values));
    }

    /**
     * Creates a new instance of {@link IssuePropertyDistribution}.
     *
     * @param counts
     *         a mapping of properties to number of issues
     * @param backgroundColors
     *         a sequence of background colors to use for the data sets
     * @param borderColors
     *         a sequence of border colors to use for the data sets
     */
    public IssuePropertyDistribution(final Map<String, Integer> counts, final List<String> backgroundColors,
            final List<String> borderColors) {
        List<Integer> values = createValuesAndSetLabels(counts);
        datasets.add(new IssuesSizeList(values, backgroundColors, borderColors));
    }

    private List<Integer> createValuesAndSetLabels(final Map<String, Integer> counts) {
        List<Integer> values = new ArrayList<>();
        for (Entry<String, Integer> entry : counts.entrySet()) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }
        return values;
    }
}

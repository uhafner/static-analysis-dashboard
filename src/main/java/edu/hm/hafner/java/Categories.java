package edu.hm.hafner.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class Categories {
    private final List<String> labels = new ArrayList<>();
    private final List<IssuesCounter> datasets = new ArrayList<>();

    public Categories(final List<String> labels, final List<IssuesCounter> datasets) {
        this.labels.addAll(labels);
        this.datasets.addAll(datasets);
    }

    public Categories(final Map<String, Integer> counts) {
        List<Integer> values = new ArrayList<>();
        for (Entry<String, Integer> entry : counts.entrySet()) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }
        datasets.add(new IssuesCounter(values));
    }
}

package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides a list of integers that represent the individual sizes of several set of issues.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class IssuesSizeList {
    private final List<Integer> data = new ArrayList<>();
    private final List<String> backgroundColor = new ArrayList<>();
    private final List<String> borderColor = new ArrayList<>();

    /**
     * Creates a new instance of {@link IssuesSizeList}.
     *
     * @param values
     *         the elements of the list
     */
    public IssuesSizeList(final Collection<Integer> values) {
        data.addAll(values);
        for (int i = 0; i < values.size(); i++) {
            backgroundColor.add(i % 2 == 0 ? "#b9b8b6" : "#d9d8d6");
            borderColor.add(i % 2 == 0 ? "#a9a8a6" : "#c9c8c6");
        }
    }

    /**
     * Creates a new instance of {@link IssuesSizeList}.
     *
     * @param values
     *         the elements of the list
     * @param backgroundColors
     *         a sequence of background colors to use for the data sets
     * @param borderColors
     *         a sequence of border colors to use for the data sets
     */
    public IssuesSizeList(final List<Integer> values, final List<String> backgroundColors,
            final List<String> borderColors) {
        data.addAll(values);
        backgroundColor.addAll(backgroundColors);
        borderColor.addAll(borderColors);
    }
}

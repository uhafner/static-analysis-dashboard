package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a list of integers that represent the individual sizes of several set of issues.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class IssuesSizeList {
    private final List<Integer> data = new ArrayList<>();

    /**
     * Creates a new instance of {@link IssuesSizeList}.
     *
     * @param data
     *         the elements of the list
     */
    public IssuesSizeList(final List<Integer> data) {
        this.data.addAll(data);
    }
}

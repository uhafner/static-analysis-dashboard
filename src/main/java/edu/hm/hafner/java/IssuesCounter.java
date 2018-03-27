package edu.hm.hafner.java;

import java.util.ArrayList;
import java.util.List;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class IssuesCounter {
    private final List<Integer> data = new ArrayList<>();

    public IssuesCounter(final List<Integer> data) {
        this.data.addAll(data);
    }
}

package edu.hm.hafner.java.uc;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issues;

/**
 * Static analysis tool that can report {@link Issues}.
 *
 * @author Ullrich Hafner
 */
public class AnalysisTool {
    private final String id;
    private final String name;
    private final AbstractParser<?> parser;

    public AnalysisTool(final String id, final String name, final AbstractParser<?> parser) {
        this.id = id;
        this.name = name;
        this.parser = parser;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AbstractParser<?> getParser() {
        return parser;
    }
}

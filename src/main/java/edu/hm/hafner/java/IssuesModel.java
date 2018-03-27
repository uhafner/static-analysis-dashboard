package edu.hm.hafner.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class IssuesModel {
    public Categories createIssues() {
        PmdParser parser = new PmdParser();
        try (InputStreamReader reader = new InputStreamReader(getTestReport())) {
            Issues<Issue> issues = parser.parse(reader);
            Map<String, Integer> counts = issues.getPropertyCount(Issue::getCategory);
            return new Categories(counts);
        }
        catch (IOException exception) {
            throw new IllegalArgumentException("Can't read test resource.", exception);
        }
    }

    private InputStream getTestReport() {
        return IssuesModel.class.getResourceAsStream("/test/pmd.xml");
    }
}

package edu.hm.hafner.java.uc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * Provides services for {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesService {
    private static final Issues<Issue> TEST_DATA = createTestData();

    /**
     * Returns the number of issues per category.
     *
     * @return number of issues per category
     */
    public IssuePropertyDistribution createDistributionByCategory() {
        Map<String, Integer> counts = TEST_DATA.getPropertyCount(Issue::getCategory);

        return new IssuePropertyDistribution(counts);
    }

    /**
     * Returns the number of issues per type.
     *
     * @return number of issues per type
     */
    public IssuePropertyDistribution createDistributionByType() {
        Map<String, Integer> counts = TEST_DATA.getPropertyCount(Issue::getType);

        return new IssuePropertyDistribution(counts);
    }

    private static Issues<Issue> createTestData() {
        PmdParser parser = new PmdParser();
        try (InputStreamReader reader = new InputStreamReader(getTestReport())) {
            return parser.parse(reader);
        }
        catch (IOException ignored) {
            return new Issues<>();
        }
    }

    private static InputStream getTestReport() {
        return IssuesService.class.getResourceAsStream("/test/pmd.xml");
    }
}

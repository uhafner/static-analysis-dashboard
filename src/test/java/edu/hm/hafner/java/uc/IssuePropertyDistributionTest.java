package edu.hm.hafner.java.uc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link IssuePropertyDistribution}.
 *
 * @author Ullrich Hafner
 */
class IssuePropertyDistributionTest {
    /**
     * Verifies that the Json structure for the bar chart is correctly created.
     */
    @Test
    void shouldCreateJsonForCategoriesBarChart() {
        IssuePropertyDistribution issuePropertyDistribution = createIssues();
        Gson gson = new Gson();
        String value = gson.toJson(issuePropertyDistribution);

        assertThat(value).isEqualTo("{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

    private IssuePropertyDistribution createIssues() {
        PmdParser parser = new PmdParser();
        try (InputStreamReader reader = new InputStreamReader(getTestReport())) {
            Issues<Issue> issues = parser.parse(reader);
            Map<String, Integer> counts = issues.getPropertyCount(Issue::getCategory);
            return new IssuePropertyDistribution(counts);
        }
        catch (IOException exception) {
            throw new IllegalArgumentException("Can't read test resource.", exception);
        }
    }

    private InputStream getTestReport() {
        return IssuesService.class.getResourceAsStream("/test/pmd.xml");
    }
}
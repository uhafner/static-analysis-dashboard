package edu.hm.hafner.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link Categories}.
 *
 * @author Ullrich Hafner
 */
class CategoriesTest {
    /**
     * FIXME: write comment.
     */
    @Test
    void should() {
        Categories categories = createIssues();
        Gson gson = new Gson();
        String value = gson.toJson(categories);

        assertThat(value).isEqualTo("{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

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
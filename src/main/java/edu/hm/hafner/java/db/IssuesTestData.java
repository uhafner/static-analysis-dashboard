package edu.hm.hafner.java.db;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * Populates the DB with test data.
 */
@Component
public class IssuesTestData {
    private EntityService entityService;

    @Autowired
    public void setEntityService(final EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * Populates the DB with a PMD file.
     */
    @PostConstruct
    public void storeTestData() {
        entityService.insert(createTestData());
    }

    /**
     * Creates a set of issues. Reads the issues from a predefined PMD file.
     *
     * @return the issues
     */
    public Issues<Issue> createTestData() {
        PmdParser parser = new PmdParser();
        try (InputStreamReader reader = new InputStreamReader(getTestReport())) {
            Issues<Issue> issues = parser.parse(reader);
            issues.setId("id-is-not-available-yet");
            return issues;
        }
        catch (IOException ignored) {
            return new Issues<>();
        }
    }

    private InputStream getTestReport() {
        return getClass().getResourceAsStream("/test/pmd.xml");
    }
}
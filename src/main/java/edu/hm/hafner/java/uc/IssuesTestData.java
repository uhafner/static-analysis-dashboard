package edu.hm.hafner.java.uc;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import edu.hm.hafner.java.db.EntityService;

/**
 * Populates the database with test data.
 */
@Component
public class IssuesTestData {
    private static final String TEST_PMD_FILE = "/test/pmd.xml";
    /** Reference value of the test report. */
    public static final String NO_REFERENCE = "N/A";

    private final EntityService entityService;

    /**
     * Creates a new instance of {@link IssuesTestData}.
     *
     * @param entityService
     *         the entity service to use to store the issues
     */
    @Autowired
    public IssuesTestData(final EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * Populates the database with issues from a dummy PMD file.
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
    public Issues<?> createTestData() {
        return createTestData(TEST_PMD_FILE);
    }

    /**
     * Creates a set of issues. Reads the issues from the specified PMD file.
     *
     * @param reportFileName
     *         file name of the PMD report
     *
     * @return the issues
     */
    public Issues<?> createTestData(final String reportFileName) {
        AnalysisTool pmd = new AnalysisTool("pmd", "Pmd", new PmdParser());

        return readReport(pmd, getTestReport(reportFileName));
    }

    private Issues<?> readReport(final AnalysisTool parser, final InputStream report) {
        try (InputStreamReader reader = new InputStreamReader(report, StandardCharsets.UTF_8)) {
            Issues<?> issues = parser.getParser().parse(reader);
            issues.setReference(NO_REFERENCE);
            issues.setOrigin(parser.getId());
            return issues;
        }
        catch (IOException ignored) {
            return new Issues<>();
        }
    }

    private InputStream getTestReport(final String fileName) {
        return IssuesTestData.class.getResourceAsStream(fileName);
    }
}

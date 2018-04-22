package edu.hm.hafner.java.uc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.input.BOMInputStream;
import org.eclipse.collections.impl.factory.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import edu.hm.hafner.java.db.IssuesEntityService;
import edu.hm.hafner.util.NoSuchElementException;

/**
 * Provides services for {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesService {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesEntityService issuesEntityService;

    @Autowired
    public IssuesService(final IssuesEntityService issuesEntityService) {
        this.issuesEntityService = issuesEntityService;
    }

    /**
     * Returns the number of issues per category.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return number of issues per category
     */
    public IssuePropertyDistribution createDistributionByCategory(final String origin, final String reference) {
        Issues<?> issues = issuesEntityService.findByPrimaryKey(origin, reference);
        Map<String, Integer> counts = issues.getPropertyCount(Issue::getCategory);

        return new IssuePropertyDistribution(counts);
    }

    /**
     * Returns the number of issues per type.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return number of issues per type
     */
    public IssuePropertyDistribution createDistributionByType(final String origin, final String reference) {
        Issues<?> issues = issuesEntityService.findByPrimaryKey(origin, reference);
        Map<String, Integer> counts = issues.getPropertyCount(Issue::getType);

        return new IssuePropertyDistribution(counts);
    }

    /**
     * Returns the number of issues per type.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return number of issues per type
     */
    public IssuePropertyDistribution createDistributionByPriority(final String origin, final String reference) {
        Issues<?> issues = issuesEntityService.findByPrimaryKey(origin, reference);
        Map<String, Integer> counts = Maps.fixedSize.of("High", issues.getHighPrioritySize(),
                "Normal", issues.getNormalPrioritySize(),
                "Low", issues.getLowPrioritySize());

        List<String> backgroundColors = Arrays.asList("#d24939", "#f7f1da", "#80afbf");
        List<String> borderColors = Arrays.asList("#c23929", "#e7e1ca", "#709faf");
        return new IssuePropertyDistribution(counts, backgroundColors, borderColors);
    }

    /**
     * Returns the available static analysis tools.
     *
     * @return all tools
     */
    public List<AnalysisTool> findAllTools() {
        List<AnalysisTool> tools = new ArrayList<>();
        tools.add(new AnalysisTool("checkstyle", "CheckStyle", new CheckStyleParser()));
        tools.add(new AnalysisTool("pmd", "PMD", new PmdParser()));
        return tools;
    }

    /**
     * Parses the specified file with the parser with the given ID.
     *
     * @param id
     *         id of the static analysis tool
     * @param file
     *         the file to parse
     *
     * @return the issues of the specified report
     */
    public Issues<Issue> parse(final String id, final InputStream file) {
        Optional<AnalysisTool> analysisTool = findAllTools().stream()
                .filter(tool -> tool.getId().equals(id))
                .findFirst();
        if (analysisTool.isPresent()) {
            AnalysisTool tool = analysisTool.get();
            Issues<?> issues = parse(tool.getParser(), file);
            issues.setOrigin(tool.getId());
            return issuesEntityService.save(issues);
        }
        else {
            throw new NoSuchElementException("No such tool found with id %s.", id);
        }
    }

    private Issues<?> parse(final AbstractParser<?> parser, final InputStream file) {
        // FIXME: this should be part of analysis-model
        return parser.parse(new InputStreamReader(new BOMInputStream(file), StandardCharsets.UTF_8));
    }

    /**
     * Creates a table with the statistics of all issues. Each row shows the statistics of one uploaded report.
     *
     * @return a statistics table
     */
    public IssuesTable createIssuesStatistics() {
        Set<Issues<Issue>> reports = issuesEntityService.findAll();
        IssuesTable statistics = new IssuesTable();
        for (Issues<Issue> report : reports) {
            statistics.addRow(report);
        }
        return statistics;
    }

    /**
     * Finds the set of issues with the specified ID.
     *
     * @param origin
     *         of the desired issues
     * @param reference
     *         of the desired issues
     *
     * @return the issues
     * @throws NoSuchElementException
     *         if the set of issues with the specified ID has not been found
     */
    public Issues<Issue> findIssues(final String origin, final String reference) {
        return issuesEntityService.findByPrimaryKey(origin, reference);
    }
}

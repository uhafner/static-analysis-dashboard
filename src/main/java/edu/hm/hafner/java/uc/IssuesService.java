package edu.hm.hafner.java.uc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.input.BOMInputStream;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import edu.hm.hafner.java.db.IssuesEntityService;
import edu.hm.hafner.java.db.IssuesTestData;
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

    /**
     * Creates a new instance of {@link IssuesService}.
     *
     * @param issuesEntityService
     *         service to access the DB layer
     */
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
     * @param reference
     *         the reference for this report
     *
     * @return the issues of the specified report
     */
    public Issues<Issue> parse(final String id, final InputStream file, final String reference) {
        Optional<AnalysisTool> analysisTool = findAllTools().stream()
                .filter(tool -> tool.getId().equals(id))
                .findFirst();
        if (analysisTool.isPresent()) {
            AnalysisTool tool = analysisTool.get();
            Issues<?> issues = parse(tool.getParser(), file);
            issues.setOrigin(tool.getId());
            issues.setReference(reference);
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

    /**
     * Creates a mapping for a property to a value for each build.
     *
     * @param mapper
     *         the mapper to use
     *
     * @return the mapping
     */
    public Map<String, MutableList<Integer>> createPriorityMap(
            final Function<List<Issues<Issue>>, MutableMap<String, Integer>> mapper) {
        List<String> references = issuesEntityService.findAllReferences();
        Map<String, MutableList<Integer>> result = new HashMap<>();
        for (String reference : references) {
            if (!IssuesTestData.NO_REFERENCE.equalsIgnoreCase(reference)) {
                List<Issues<Issue>> issues = issuesEntityService.findByReference(reference);
                MutableMap<String, Integer> dataSet = mapper.apply(issues);
                for (Entry<String, Integer> entry : dataSet.entrySet()) {
                    MutableList<Integer> values = result.getOrDefault(entry.getKey(), Lists.mutable.empty());
                    values.add(entry.getValue());
                    result.putIfAbsent(entry.getKey(), values);
                }
            }
        }

        return result;
    }

    /**
     * Creates a model for a chart that shows the number of issues per tool (i.e. origin).
     *
     * @return the model that can be used in the data property of a line chart
     */
    // TODO: part of UI?
    public OriginChartModel createOriginAggregation() {
        return new OriginChartModel(createPriorityMap(this::createOrigins));
    }

    /**
     * Creates a model for a chart that shows the distribution of issues by priority.
     *
     * @return the model that can be used in the data property of a line chart
     */
    public PriorityChartModel createPriorityAggregation() {
        return new PriorityChartModel(createPriorityMap(this::createPriorities));
    }

    MutableMap<String, Integer> createPriorities(final List<Issues<Issue>> allReports) {
        MutableMap<String, Integer> priorities = Maps.mutable.empty();
        for (Issues<Issue> issues : allReports) {
            priorities.merge("high", issues.getHighPrioritySize(), Integer::sum);
            priorities.merge("normal", issues.getNormalPrioritySize(), Integer::sum);
            priorities.merge("low", issues.getLowPrioritySize(), Integer::sum);
        }
        return priorities;
    }

    MutableMap<String, Integer> createOrigins(final List<Issues<Issue>> allReports) {
        List<AnalysisTool> tools = findAllTools();
        MutableMap<String, Integer> origins = Maps.mutable.empty();
        for (Issues<Issue> issues : allReports) {
            for (AnalysisTool tool : tools) {
                if (tool.getId().equals(issues.getOrigin())) {
                    origins.put(tool.getName(), issues.size());
                }
            }
        }
        return origins;
    }
}

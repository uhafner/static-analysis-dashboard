package edu.hm.hafner.java.uc;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.eclipse.collections.api.factory.set.FixedSizeSetFactory;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.java.db.EntityService;
import edu.hm.hafner.java.db.IssuesEntityService;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesService}.
 *
 * @author Ullrich Hafner
 */
class IssuesServiceTest {
    private static final String ORIGIN = "dummy-origin";
    private static final String REFERENCE = "dummy-reference";

    @Test
    void shouldFindAllTools() {
        // Given
        IssuesService service = createService();

        // When
        List<AnalysisTool> tools = service.findAllTools();

        // Then
        assertThat(tools).hasSize(2);
        assertThat(tools.get(0).getName()).isEqualTo("CheckStyle");
    }

    @Test
    void shouldCreateIssuesTableWithOneRow() {
        // Given
        IssuesTestData data = new IssuesTestData(mock(EntityService.class));
        IssuesEntityService entityService = mock(IssuesEntityService.class);

        Issues<?> testData = data.createTestData();
        Issues<Issue> issues = new Issues<>();
        issues.addAll(testData);
        Issues<Issue> filtered = issues.filter(issue -> "Error Prone".equals(issue.getCategory()));
        FixedSizeSetFactory sets = Sets.fixedSize;
        IssuesService service = new IssuesService(entityService);

        // Given
        when(entityService.findAll()).thenReturn(sets.empty());
        // When
        IssuesTable empty = service.createIssuesStatistics();
        // Then
        assertThat(asJson(empty)).isEqualTo("{\"data\":[]}");

        // Given
        when(entityService.findAll()).thenReturn(sets.of(issues));
        // When
        IssuesTable oneRow = service.createIssuesStatistics();
        // Then
        assertThat(asJson(oneRow)).isEqualTo("{\"data\":[[\"pmd\",\"N/A\",\"109\",\"12\",\"97\",\"0\"]]}");

        // Given
        when(entityService.findAll()).thenReturn(sets.of(issues, filtered));
        // When
        IssuesTable twoRows = service.createIssuesStatistics();
        // Then
        assertThat(asJson(twoRows)).isEqualTo("{\"data\":[[\"pmd\",\"N/A\",\"109\",\"12\",\"97\",\"0\"],"
                + "[\"pmd\",\"N/A\",\"12\",\"4\",\"8\",\"0\"]]}");
    }

    @Test @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    void shouldCreateCategoryDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByCategory(ORIGIN, REFERENCE);

        // Then
        assertThatJsonContainsElements(distribution,
                asList("Design", "Documentation", "Best Practices", "Performance", "Code Style", "Error Prone"),
                asList(15, 3, 20, 6, 53, 12));
    }

    @Test @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    void shouldCreateTypeDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByType("dummy-id", "");

        // Then
        assertThatJsonContainsElements(distribution,
                asList("OptimizableToArrayCall", "LooseCoupling", "MethodArgumentCouldBeFinal",
                        "UncommentedEmptyMethodBody", "ConfusingTernary", "MissingSerialVersionUID",
                        "GuardLogStatement", "UnusedFormalParameter", "LoggerIsNotStaticFinal", "AssignmentInOperand",
                        "ImmutableField", "CompareObjectsWithEquals", "UnnecessaryConstructor", "CyclomaticComplexity",
                        "UnusedPrivateMethod", "ConsecutiveLiteralAppends", "CallSuperInConstructor",
                        "UnusedPrivateField", "AppendCharacterWithChar", "ExcessivePublicCount", "NPathComplexity",
                        "ExcessiveImports", "AvoidDeeplyNestedIfStmts", "AccessorClassGeneration",
                        "UncommentedEmptyConstructor"),
                asList(1, 1, 13, 2, 9, 4, 8, 2, 4, 1, 2, 3, 13, 3, 3, 4, 18, 1, 1, 2, 3, 4, 1, 5, 1));
    }

    private void assertThatJsonContainsElements(final IssuePropertyDistribution distribution,
            final List<String> expectedLabels, final List<Integer> expectedSizes) {
        DocumentContext documentContext = asJsonContext(distribution);

        DataSetAssertions assertions = new DataSetAssertions();
        assertions.assertThatChartModelIsCorrect(expectedLabels, expectedSizes, documentContext);
    }

    private DocumentContext asJsonContext(final IssuePropertyDistribution distribution) {
        return JsonPath.parse(asJson(distribution));
    }

    @Test @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    void shouldCreatePriorityDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByPriority("dummy-id", "");

        // Then
        assertThatJsonContainsElements(distribution,
                asList("High", "Normal", "Low"),
                asList(12, 97, 0));
    }

    @Test
    void shouldParsePmdFile() {
        // Given
        IssuesService service = createService();

        // When
        Issues<Issue> issues = service.parse("pmd", readPmdFile(), REFERENCE);

        // Then
        assertThat(issues).hasSize(109);
    }

    private InputStream readPmdFile() {
        return IssuesServiceTest.class.getResourceAsStream("/test/pmd.xml");
    }

    @Test
    void shouldThrowExceptionIfParserDoesNotExist() {
        // Given
        IssuesService service = createService();

        // When
        String notExistingId = "notExistingId";
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> service.parse(notExistingId, readPmdFile(), REFERENCE))
                .withMessageContaining(notExistingId);
    }

    @Test
    void shouldDelegateToDbLayerWhenFindingIssues() {
        // Given
        IssuesEntityService entityService = mock(IssuesEntityService.class);
        IssuesService service = new IssuesService(entityService);

        // When
        service.findIssues(ORIGIN, REFERENCE);

        // Then
        verify(entityService).findByPrimaryKey(ORIGIN, REFERENCE);
    }

    private String asJson(final Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private IssuesService createService() {
        IssuesTestData data = new IssuesTestData(mock(EntityService.class));
        IssuesEntityService entityService = mock(IssuesEntityService.class);

        Issues<?> testData = data.createTestData();
        Issues<Issue> issues = new Issues<>();
        issues.addAll(testData);
        when(entityService.findByPrimaryKey(anyString(), anyString())).thenReturn(issues);
        when(entityService.findAll()).thenReturn(Collections.singleton(issues));
        when(entityService.save(any())).thenReturn(issues);

        return new IssuesService(entityService);
    }

    // FIXME: Issues should be called Report
    @Test
    void shouldCreateMappingOfPriorities() {
        // Given
        IssuesEntityService entityService = mock(IssuesEntityService.class);
        when(entityService.findAllReferences()).thenReturn(Lists.fixedSize.of("1", "2"));
        Issues<Issue> issuesBuild1 = createReport(1, 2, 3);
        when(entityService.findByReference("1")).thenReturn(Lists.fixedSize.of(issuesBuild1));
        Issues<Issue> issuesBuild2 = createReport(6, 5, 4);
        when(entityService.findByReference("2")).thenReturn(Lists.fixedSize.of(issuesBuild2));
        IssuesService service = new IssuesService(entityService);

        // When
        Map<String, MutableList<Integer>> priorityMap = service.createPriorityMap(service::createPriorities);

        // Then
        assertThat(priorityMap).containsKeys("high", "normal", "low");
        assertThat(priorityMap).containsEntry("high", Lists.mutable.of(1, 6));
        assertThat(priorityMap).containsEntry("normal", Lists.mutable.of(2, 5));
        assertThat(priorityMap).containsEntry("low", Lists.mutable.of(3, 4));
    }

    @SuppressWarnings("unchecked")
    private Issues<Issue> createReport(final int highPrioritySize, final int normalPrioritySize,
            final int lowPrioritySize) {
        Issues<Issue> report = mock(Issues.class);
        when(report.getHighPrioritySize()).thenReturn(highPrioritySize);
        when(report.getNormalPrioritySize()).thenReturn(normalPrioritySize);
        when(report.getLowPrioritySize()).thenReturn(lowPrioritySize);
        return report;
    }
}


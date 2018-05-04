package edu.hm.hafner.java.uc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.collections.api.factory.set.FixedSizeSetFactory;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.java.db.EntityService;
import edu.hm.hafner.java.db.IssuesEntityService;
import edu.hm.hafner.java.db.IssuesTestData;
import static java.util.Arrays.*;
import net.minidev.json.JSONArray;
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
        assertThat(asJson(oneRow)).isEqualTo("{\"data\":[[\"pmd\",\"-\",\"109\",\"12\",\"97\",\"0\"]]}");

        // Given
        when(entityService.findAll()).thenReturn(sets.of(issues, filtered));
        // When
        IssuesTable twoRows = service.createIssuesStatistics();
        // Then
        assertThat(asJson(twoRows)).isEqualTo("{\"data\":[[\"pmd\",\"-\",\"109\",\"12\",\"97\",\"0\"],"
                + "[\"pmd\",\"-\",\"12\",\"4\",\"8\",\"0\"]]}");
    }

    @Test
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

    @Test
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

        JSONArray actualLabels = documentContext.read("$.labels[*]", JSONArray.class);
        assertThat(actualLabels).containsExactlyElementsOf(expectedLabels);

        assertThat(documentContext.read("$.datasets[*]", JSONArray.class)).hasSize(1);

        JSONArray actualSizes = documentContext.read("$.datasets[0].data", JSONArray.class);
        assertThat(actualSizes).containsExactlyElementsOf(expectedSizes);

        JSONArray backgroundColors = documentContext.read("$.datasets[0].backgroundColor[*]", JSONArray.class);
        assertThat(backgroundColors).hasSize(expectedSizes.size());

        JSONArray borderColors = documentContext.read("$.datasets[0].borderColor[*]", JSONArray.class);
        assertThat(borderColors).hasSize(expectedSizes.size());
    }

    private DocumentContext asJsonContext(final IssuePropertyDistribution distribution) {
        return JsonPath.parse(asJson(distribution));
    }

    @Test
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
        Issues<Issue> issues = service.parse("pmd", IssuesServiceTest.class.getResourceAsStream("/test/pmd.xml"),
                REFERENCE);

        // Then
        assertThat(issues).hasSize(109);
    }

    @Test
    void shouldThrowExceptionIfParserDoesNotExist() {
        // Given
        IssuesService service = createService();

        // When
        String notExistingId = "notExistingId";
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
                service.parse(notExistingId, IssuesServiceTest.class.getResourceAsStream("/test/pmd.xml"), REFERENCE))
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
}


package edu.hm.hafner.java.uc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.collections.api.factory.set.FixedSizeSetFactory;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.java.db.EntityService;
import edu.hm.hafner.java.db.IssuesEntityService;
import edu.hm.hafner.java.db.IssuesTestData;
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
        assertThat(toJson(empty)).isEqualTo("{\"data\":[]}");

        // Given
        when(entityService.findAll()).thenReturn(sets.of(issues));
        // When
        IssuesTable oneRow = service.createIssuesStatistics();
        // Then
        assertThat(toJson(oneRow)).isEqualTo("{\"data\":[[\"pmd\",\"-\",\"109\",\"12\",\"97\",\"0\"]]}");

        // Given
        when(entityService.findAll()).thenReturn(sets.of(issues, filtered));
        // When
        IssuesTable twoRows = service.createIssuesStatistics();
        // Then
        assertThat(toJson(twoRows)).isEqualTo("{\"data\":[[\"pmd\",\"-\",\"109\",\"12\",\"97\",\"0\"],"
                + "[\"pmd\",\"-\",\"12\",\"4\",\"8\",\"0\"]]}");
    }

    @Test
    void shouldCreateCategoryDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByCategory(ORIGIN, REFERENCE);

        // Then
        assertThat(toJson(distribution)).isEqualTo(
                "{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

    @Test
    void shouldCreateTypeDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByType("dummy-id", "");

        // Then
        assertThat(toJson(distribution)).isEqualTo(
                "{\"labels\":[\"OptimizableToArrayCall\",\"LooseCoupling\",\"MethodArgumentCouldBeFinal\",\"UncommentedEmptyMethodBody\",\"ConfusingTernary\",\"MissingSerialVersionUID\",\"GuardLogStatement\",\"UnusedFormalParameter\",\"LoggerIsNotStaticFinal\",\"AssignmentInOperand\",\"ImmutableField\",\"CompareObjectsWithEquals\",\"UnnecessaryConstructor\",\"CyclomaticComplexity\",\"UnusedPrivateMethod\",\"ConsecutiveLiteralAppends\",\"CallSuperInConstructor\",\"UnusedPrivateField\",\"AppendCharacterWithChar\",\"ExcessivePublicCount\",\"NPathComplexity\",\"ExcessiveImports\",\"AvoidDeeplyNestedIfStmts\",\"AccessorClassGeneration\",\"UncommentedEmptyConstructor\"],\"datasets\":[{\"data\":[1,1,13,2,9,4,8,2,4,1,2,3,13,3,3,4,18,1,1,2,3,4,1,5,1]}]}");
    }

    @Test
    void shouldParsePmdFile() {
        // Given
        IssuesService service = createService();

        // When
        Issues<Issue> issues = service.parse("pmd", IssuesServiceTest.class.getResourceAsStream("/test/pmd.xml"));

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
                service.parse(notExistingId, IssuesServiceTest.class.getResourceAsStream("/test/pmd.xml")))
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

    private String toJson(final Object object) {
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
        when(entityService.save(issues)).thenReturn(issues);

        return new IssuesService(entityService);
    }
}


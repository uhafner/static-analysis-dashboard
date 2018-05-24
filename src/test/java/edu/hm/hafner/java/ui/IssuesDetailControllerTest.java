package edu.hm.hafner.java.ui;

import java.util.List;

import org.eclipse.collections.impl.factory.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.JsonPath;

import edu.hm.hafner.java.uc.DataSetAssertions;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesDetailController}.
 *
 * @author Ullrich Hafner
 */
class IssuesDetailControllerTest {
    private static final String LABEL = "label";
    private static final String ORIGIN_CATEGORY = "origin-category";
    private static final String REFERENCE_CATEGORY = "reference-category";
    private static final String ORIGIN_TYPE = "origin-type";
    private static final String REFERENCE_TYPE = "reference-type";

    private static final IssuePropertyDistribution SINGLETON_DISTRIBUTION
            = new IssuePropertyDistribution(Maps.fixedSize.of(LABEL, 1));

    @Test
    void shouldReturnCategoriesAsJson() {
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        when(issuesService.createDistributionByCategory(ORIGIN_CATEGORY, REFERENCE_CATEGORY))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseIsEqualTo(controller.getCategories(ORIGIN_CATEGORY, REFERENCE_CATEGORY),
                singletonList("label"), singletonList(1));
    }

    @Test
    void shouldReturnTypesAsJson() {
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        when(issuesService.createDistributionByType(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseIsEqualTo(controller.getTypes(ORIGIN_TYPE, REFERENCE_TYPE),
                singletonList("label"), singletonList(1));
    }

    private void assertThatResponseIsEqualTo(final ResponseEntity<String> response,
            final List<String> expectedLabels, final List<Integer> expectedSizes) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DataSetAssertions assertions = new DataSetAssertions();
        assertions.assertThatChartModelIsCorrect(expectedLabels, expectedSizes, JsonPath.parse(response.getBody()));
    }

    @Test
    void shouldReturnPrioritiesAsJson() {
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        when(issuesService.createDistributionByPriority(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseIsEqualTo(controller.getPriorities(ORIGIN_TYPE, REFERENCE_TYPE),
                singletonList("label"), singletonList(1));
    }

}
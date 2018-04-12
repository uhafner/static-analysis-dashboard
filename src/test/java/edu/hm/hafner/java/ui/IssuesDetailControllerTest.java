package edu.hm.hafner.java.ui;

import java.util.HashMap;

import org.eclipse.collections.impl.factory.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
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

    private static final IssuePropertyDistribution EMPTY_DISTRIBUTION
            = new IssuePropertyDistribution(new HashMap<>());
    private static final IssuePropertyDistribution SINGLETON_DISTRIBUTION
            = new IssuePropertyDistribution(Maps.fixedSize.of(LABEL, 1));

    @Test
    void shouldReturnJsonOfPropertiesDistribution() {
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        when(issuesService.createDistributionByCategory(ORIGIN_CATEGORY, REFERENCE_CATEGORY))
                .thenReturn(EMPTY_DISTRIBUTION);
        assertThatResponseIsEmpty(controller.getCategories(ORIGIN_CATEGORY, REFERENCE_CATEGORY));

        when(issuesService.createDistributionByType(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(EMPTY_DISTRIBUTION);
        assertThatResponseIsEmpty(controller.getTypes(ORIGIN_TYPE, REFERENCE_TYPE));

        when(issuesService.createDistributionByCategory(ORIGIN_CATEGORY, REFERENCE_CATEGORY))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseContainsOneElement(controller.getCategories(ORIGIN_CATEGORY, REFERENCE_CATEGORY));

        when(issuesService.createDistributionByType(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseContainsOneElement(controller.getTypes(ORIGIN_TYPE, REFERENCE_TYPE));
    }

    private void assertThatResponseContainsOneElement(final ResponseEntity<?> categories) {
        assertThatResponseIsEqualTo(categories, "{\"labels\":[\"label\"],\"datasets\":[{\"data\":[1]}]}");
    }

    private void assertThatResponseIsEmpty(final ResponseEntity<?> empty) {
        assertThatResponseIsEqualTo(empty, "{\"labels\":[],\"datasets\":[{\"data\":[]}]}");
    }

    private void assertThatResponseIsEqualTo(final ResponseEntity<?> empty, final String s) {
        assertThat(empty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(empty.getBody()).isEqualTo(s);
    }
}
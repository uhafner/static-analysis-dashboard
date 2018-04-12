package edu.hm.hafner.java.db;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesEntityService}.
 *
 * @author Ullrich Hafner
 */
class IssuesEntityServiceTest {
    @Test
    void shouldReturnIssuesOrThrowException() {
        EntityService entityService = mock(EntityService.class);
        IssuesEntityService service = new IssuesEntityService(entityService);
        Issues<Issue> issues = new Issues<>();

        when(entityService.select("origin", "reference")).thenReturn(Optional.of(issues));
        assertThat(service.findByPrimaryKey("origin", "reference")).isSameAs(issues);

        when(entityService.select(anyString(), anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByPrimaryKey("wrong", "key"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("wrong")
                .hasMessageContaining("key");
    }
}
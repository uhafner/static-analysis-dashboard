package edu.hm.hafner.java.persistence;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EntityServiceTest {

    private static final EntityMapper MAPPER = new EntityMapper();

    @Test
    @Disabled
    void first() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, rangesRepository, MAPPER);

        IssuesEntity entity = new IssuesEntity();
        when(issuesRepository.findById("id")).thenReturn(Optional.of(entity));

        Optional<Issues<Issue>> o = sut.select("id");
        assertThat(o.isPresent()).isTrue();



        verify(issuesRepository).findById("id");
        verify(issuesRepository).findById("id2");
    }
}
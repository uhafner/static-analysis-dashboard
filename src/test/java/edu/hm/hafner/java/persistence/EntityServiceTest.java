package edu.hm.hafner.java.persistence;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EntityServiceTest {

    private static final EntityMapper MAPPER = new EntityMapper();

    private static final LineRangeList RANGES = new LineRangeList();

    static {
        RANGES.add(new LineRange(1, 2));
        RANGES.add(new LineRange(4, 6));
    }

    @Test
    void selectNotExistingIssueShouldReturnAnEmptyOptional() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, rangesRepository, MAPPER);

        Optional<Issue> issue = sut.select(UUID.fromString("ce856855-b91d-4ae7-b77a-7a30a699291e"));

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssueShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, rangesRepository, MAPPER);

        UUID id = UUID.fromString("ce856855-b91d-4ae7-b77a-7a30a699291e");
        IssueEntity entity = new IssueEntity();
        entity.setId(id);
        entity.setLineRanges(Arrays.asList(new LineRangeEntity()));

        when(issueRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Issue> issue = sut.select(id);

        assertThat(issue.isPresent()).isTrue();
        assertThat(issue.get().getId()).isEqualTo(id);
    }

    @Test
    void updateNotExistingIssueShouldReturnAnEmptyOptional() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, rangesRepository, MAPPER);

        Issue issue = new IssueBuilder().build();

        Optional<Issue> result = sut.update(issue);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void updateExistingIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, rangesRepository, MAPPER);

        String newFilename = "new-filename";

        Issue issue = new IssueBuilder().setLineRanges(RANGES).build();
        issue.setFileName(newFilename);
        IssueEntity entity = new IssueEntity();
        entity.setId(issue.getId());
        entity.setLineRanges(Arrays.asList(new LineRangeEntity(1, 2)));

        when(issueRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Optional<Issue> result = sut.update(issue);

        assertThat(result.isPresent()).isTrue();
        assertThat(entity.getFileName()).isEqualTo(newFilename);
        verify(rangesRepository, times(1)).findById("1-2");
        verify(rangesRepository, times(1)).findById("4-6");
        verify(rangesRepository, times(1)).save(new LineRangeEntity(4, 6));
    }
}
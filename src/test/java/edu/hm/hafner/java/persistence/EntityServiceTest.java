package edu.hm.hafner.java.persistence;

import java.util.Arrays;
<<<<<<< HEAD
import java.util.NoSuchElementException;
=======
>>>>>>> 4771fe98beaf6e59fd249bf25ec5dc2c688d41a7
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
<<<<<<< HEAD
import static org.assertj.core.api.Assertions.*;
=======
import static org.assertj.core.api.Assertions.assertThat;
>>>>>>> 4771fe98beaf6e59fd249bf25ec5dc2c688d41a7
import static org.mockito.Mockito.*;

class EntityServiceTest {

    private static final EntityMapper MAPPER = new EntityMapper();

    private static final UUID EXAMPLE_UUID = UUID.fromString("ce856855-b91d-4ae7-b77a-7a30a699291e");
    private static final String EXAMPLE_ID = "id";

    private static final Issues<Issue> ISSUES = new Issues<>();
    private static final Issue FIRST_ISSUE = new IssueBuilder().setLineStart(1).build();
    private static final Issue SECOND_ISSUE = new IssueBuilder().setLineStart(2).build();

    private static final LineRangeList RANGES = new LineRangeList();

    static {
        ISSUES.setId(EXAMPLE_ID);
        ISSUES.add(FIRST_ISSUE, SECOND_ISSUE);

        RANGES.add(new LineRange(1, 2));
        RANGES.add(new LineRange(4, 6));
    }

    @Test
    void selectNotExistingIssueShouldReturnAnEmptyOptional() {
        EntityService sut = new EntityService(mock(IssueRepository.class), mock(IssuesRepository.class), mock(LineRangeRepository.class), MAPPER);

        Optional<Issue> issue = sut.select(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssueShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        EntityService sut = new EntityService(issueRepository, mock(IssuesRepository.class), mock(LineRangeRepository.class), MAPPER);

        IssueEntity entity = new IssueEntity();
        entity.setId(EXAMPLE_UUID);
        entity.setLineRanges(Arrays.asList(new LineRangeEntity()));
        when(issueRepository.findById(EXAMPLE_UUID)).thenReturn(Optional.of(entity));

        Optional<Issue> issue = sut.select(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isTrue();
        assertThat(issue.get().getId()).isEqualTo(EXAMPLE_UUID);
    }

    @Test
    void updateNotExistingIssueShouldReturnAnEmptyOptional() {
        EntityService sut = new EntityService(mock(IssueRepository.class), mock(IssuesRepository.class), mock(LineRangeRepository.class), MAPPER);
        Issue issue = new IssueBuilder().build();

        Optional<Issue> result = sut.update(issue);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void updateExistingIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, mock(IssuesRepository.class), rangesRepository, MAPPER);

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

    @Test
    void insertNotExistingIssueShouldReturnAnEmptyOptional() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = new EntityService(issueRepository, mock(IssuesRepository.class), rangesRepository, MAPPER);
        Issue issue = new IssueBuilder().setLineRanges(RANGES).build();

        when(rangesRepository.findById("1-2")).thenReturn(Optional.of(new LineRangeEntity(1, 2)));

        Issue result = sut.insert(issue);

        assertThat(result).isEqualTo(issue);
        verify(issueRepository, times(1)).save(MAPPER.map(issue));
        verify(rangesRepository, times(1)).findById("1-2");
        verify(rangesRepository, times(1)).findById("4-6");
        verify(rangesRepository, times(1)).save(new LineRangeEntity(4, 6));
    }

    @Test
    void selectNotExistingIssuesShouldThrowException() {
        EntityService sut = new EntityService(mock(IssueRepository.class), mock(IssuesRepository.class), mock(LineRangeRepository.class), MAPPER);

        assertThatThrownBy(() -> sut.select(EXAMPLE_ID)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void selectExistingIssuesShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, mock(LineRangeRepository.class), MAPPER);
        when(issuesRepository.findById(EXAMPLE_ID)).thenReturn(Optional.of(MAPPER.map(ISSUES)));

        Issues<Issue> optionalResult = sut.select(EXAMPLE_ID);

        assertThat(optionalResult.getId()).isEqualTo(EXAMPLE_ID);
        assertThat(optionalResult.iterator()).containsExactly(FIRST_ISSUE, SECOND_ISSUE);
    }

    @Test
    void updateNotExistingIssuesShouldReturnAnEmptyOptional() {
        EntityService sut = new EntityService(mock(IssueRepository.class), mock(IssuesRepository.class), mock(LineRangeRepository.class), MAPPER);

        Optional<Issues<Issue>> result = sut.update(ISSUES);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void updateExistingIssues() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, mock(LineRangeRepository.class), MAPPER);
        when(issuesRepository.findById(EXAMPLE_ID)).thenReturn(Optional.of(MAPPER.map(ISSUES)));
        when(issueRepository.findById(FIRST_ISSUE.getId())).thenReturn(Optional.of(MAPPER.map(FIRST_ISSUE)));

        sut.update(ISSUES);

        verify(issuesRepository, times(1)).findById(EXAMPLE_ID);
        verify(issueRepository, times(1)).findById(FIRST_ISSUE.getId());
        verify(issueRepository, times(1)).findById(SECOND_ISSUE.getId());
        verify(issueRepository, times(1)).save(MAPPER.map(SECOND_ISSUE));
        verifyNoMoreInteractions(issuesRepository);
        verifyNoMoreInteractions(issueRepository);
    }

    @Test
    void insertNotExistingIssues() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        EntityService sut = new EntityService(issueRepository, issuesRepository, mock(LineRangeRepository.class), MAPPER);
        when(issueRepository.findById(FIRST_ISSUE.getId())).thenReturn(Optional.of(MAPPER.map(FIRST_ISSUE)));

        sut.insert(ISSUES);

        verify(issuesRepository, times(1)).save(MAPPER.map(ISSUES));
        verify(issueRepository, times(1)).findById(FIRST_ISSUE.getId());
        verify(issueRepository, times(1)).findById(SECOND_ISSUE.getId());
        verify(issueRepository, times(1)).save(MAPPER.map(SECOND_ISSUE));
        verifyNoMoreInteractions(issuesRepository);
        verifyNoMoreInteractions(issueRepository);
    }
}
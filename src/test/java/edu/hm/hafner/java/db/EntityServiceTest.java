package edu.hm.hafner.java.db;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link EntityService}.
 *
 * @author Michael Schmid
 */
class EntityServiceTest {
    private static final EntityMapper MAPPER = new EntityMapper();

    private static final UUID EXAMPLE_UUID = UUID.fromString("ce856855-b91d-4ae7-b77a-7a30a699291e");
    private static final String EXAMPLE_ORIGIN = "origin";
    private static final String EXAMPLE_REFERENCE = "1";

    private static final Issues<Issue> ISSUES = new Issues<>();
    private static final Issue FIRST_ISSUE = new IssueBuilder().setLineStart(1).build();
    private static final Issue SECOND_ISSUE = new IssueBuilder().setLineStart(2).build();

    private static final LineRangeList RANGES = new LineRangeList();

    static {
        ISSUES.setOrigin(EXAMPLE_ORIGIN);
        ISSUES.setReference(EXAMPLE_REFERENCE);
        ISSUES.add(FIRST_ISSUE, SECOND_ISSUE);

        RANGES.add(new LineRange(1, 2));
        RANGES.add(new LineRange(4, 6));
    }

    @Test
    void selectNotExistingIssueShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class), mock(LineRangeRepository.class));

        Optional<Issue> issue = sut.select(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssueShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        EntityService sut = createEntityService(issueRepository, mock(LineRangeRepository.class));

        IssueEntity entity = new IssueEntity();
        entity.setId(EXAMPLE_UUID);
        entity.setLineRanges(Collections.singletonList(new LineRangeEntity()));
        when(issueRepository.findById(EXAMPLE_UUID)).thenReturn(Optional.of(entity));

        Optional<Issue> issue = sut.select(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isTrue();
        assertThat(issue.get().getId()).isEqualTo(EXAMPLE_UUID);
    }

    @Test
    void updateNotExistingIssueShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class), mock(LineRangeRepository.class));
        Issue issue = new IssueBuilder().build();

        Optional<Issue> result = sut.update(issue);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void updateExistingIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = createEntityService(issueRepository, rangesRepository);

        String newFilename = "new-filename";

        Issue issue = new IssueBuilder().setLineRanges(RANGES).build();
        issue.setFileName(newFilename);
        IssueEntity entity = new IssueEntity();
        entity.setId(issue.getId());
        entity.setLineRanges(Collections.singletonList(new LineRangeEntity(1, 2)));

        when(issueRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Optional<Issue> result = sut.update(issue);

        assertThat(result.isPresent()).isTrue();
        assertThat(entity.getFileName()).isEqualTo(newFilename);
        verify(rangesRepository, times(1)).findById("1-2");
        verify(rangesRepository, times(1)).findById("4-6");
        verify(rangesRepository, times(1)).save(new LineRangeEntity(4, 6));
    }

    @Test
    void insertIssueWithLineRanges() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        LineRangeRepository rangesRepository = mock(LineRangeRepository.class);
        EntityService sut = createEntityService(issueRepository, rangesRepository);
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
    void selectNotExistingIssueaShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class), mock(LineRangeRepository.class));

        Optional<Issues<Issue>> issue = sut.select(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE);

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssuesShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        EntityService sut = createEntityService(issueRepository, issuesRepository);
        when(issuesRepository.findById(new IssuesEntityId(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE))).thenReturn(Optional.of(MAPPER.map(ISSUES)));

        Optional<Issues<Issue>> optionalResult = sut.select(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE);

        assertThat(optionalResult.isPresent()).isTrue();
        assertThat(optionalResult.get().getOrigin()).isEqualTo(EXAMPLE_ORIGIN);
        assertThat(optionalResult.get().getReference()).isEqualTo(EXAMPLE_REFERENCE);
        assertThat(optionalResult.get().iterator()).containsExactly(FIRST_ISSUE, SECOND_ISSUE);
    }

    @Test
    void updateNotExistingIssuesShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class), mock(LineRangeRepository.class));

        Optional<Issues<Issue>> result = sut.update(ISSUES);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void updateExistingIssues() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        IssuesRepository issuesRepository = mock(IssuesRepository.class);
        EntityService sut = createEntityService(issueRepository, issuesRepository);
        when(issuesRepository.findById(new IssuesEntityId(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE))).thenReturn(Optional.of(MAPPER.map(ISSUES)));
        when(issueRepository.findById(FIRST_ISSUE.getId())).thenReturn(Optional.of(MAPPER.map(FIRST_ISSUE)));

        sut.update(ISSUES);

        verify(issuesRepository, times(1)).findById(new IssuesEntityId(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE));
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
        EntityService sut = createEntityService(issueRepository, issuesRepository);
        when(issueRepository.findById(FIRST_ISSUE.getId())).thenReturn(Optional.of(MAPPER.map(FIRST_ISSUE)));

        sut.insert(ISSUES);

        verify(issuesRepository, times(1)).save(MAPPER.map(ISSUES));
        verify(issueRepository, times(1)).findById(FIRST_ISSUE.getId());
        verify(issueRepository, times(1)).findById(SECOND_ISSUE.getId());
        verify(issueRepository, times(1)).save(MAPPER.map(SECOND_ISSUE));
        verifyNoMoreInteractions(issuesRepository);
        verifyNoMoreInteractions(issueRepository);
    }

    private EntityService createEntityService(final IssueRepository issueRepository,
            final LineRangeRepository rangesRepository) {
        return new EntityService(issueRepository, mock(IssuesRepository.class), rangesRepository,
                MAPPER, mock(EntityManager.class));
    }

    private EntityService createEntityService(final IssueRepository issueRepository,
            final IssuesRepository issuesRepository) {
        return new EntityService(issueRepository, issuesRepository, mock(LineRangeRepository.class),
                MAPPER, mock(EntityManager.class));
    }
}
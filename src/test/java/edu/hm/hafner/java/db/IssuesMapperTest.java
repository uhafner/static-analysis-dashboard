package edu.hm.hafner.java.db;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

/**
 * Tests the class {@link EntityMapper} for {@link Issues} instances.
 *
 * @author Michael Schmid
 */
class IssuesMapperTest {
    private static final Issue HIGH = new IssueBuilder().setMessage("issue-1")
            .setFileName("file-1")
            .setPriority(Priority.HIGH)
            .build();
    private static final Issue NORMAL_1 = new IssueBuilder().setMessage("issue-2")
            .setFileName("file-1")
            .setPriority(Priority.NORMAL)
            .build();
    private static final Issue NORMAL_2 = new IssueBuilder().setMessage("issue-3")
            .setFileName("file-1")
            .setPriority(Priority.NORMAL)
            .build();
    private static final Issue LOW_2_A = new IssueBuilder().setMessage("issue-4")
            .setFileName("file-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue LOW_2_B = new IssueBuilder().setMessage("issue-5")
            .setFileName("file-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue LOW_FILE_3 = new IssueBuilder().setMessage("issue-6")
            .setFileName("file-3")
            .setPriority(Priority.LOW)
            .build();

    private static final String ID = "id";

    private static final Issues<Issue> ISSUES = new Issues<>();

    static {
        ISSUES.add(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        ISSUES.setOrigin(ID);
        ISSUES.logInfo("Hello");
        ISSUES.logInfo("World!");
        ISSUES.logError("Boom!");
    }



    @Test
    void mapIssuesToIssuesEntity() {
        EntityMapper mapper = new EntityMapper();

        IssuesEntity result = mapper.map(ISSUES);

        SoftAssertions softly = new SoftAssertions();
        assertIssuesAndEntityEqual(softly, result, ISSUES);
        softly.assertAll();
    }

    @Test
    void issuesRoundTrip() {
        EntityMapper mapper = new EntityMapper();

        IssuesEntity entity = mapper.map(ISSUES);

        SoftAssertions softly = new SoftAssertions();
        assertIssuesAndEntityEqual(softly, entity, ISSUES);

        Issues<Issue> result = mapper.map(entity);

        assertRoundTrip(softly, result, ISSUES);
        softly.assertAll();
    }

    private void assertIssuesAndEntityEqual(final SoftAssertions softly, final IssuesEntity entity, final Issues<Issue> issues) {
        softly.assertThat(entity.getOrigin()).isEqualTo(issues.getOrigin());
        softly.assertThat(entity.getReference()).isEqualTo(issues.getReference());
        softly.assertThat(entity.getErrorMessages()).isEqualTo(issues.getErrorMessages());
        softly.assertThat(entity.getInfoMessages()).isEqualTo(issues.getInfoMessages());
        softly.assertThat(entity.getDuplicatesSize()).isEqualTo(issues.getDuplicatesSize());
        softly.assertThat(entity.getElements().size()).isEqualTo((int)issues.stream().count());
    }

    private void assertRoundTrip(final SoftAssertions softly, final Issues<Issue> result, final Issues<Issue> expected) {
        softly.assertThat(result.getOrigin()).isEqualTo(expected.getOrigin());
        softly.assertThat(result.getReference()).isEqualTo(expected.getReference());
        softly.assertThat(result.getErrorMessages()).isEqualTo(expected.getErrorMessages());
        softly.assertThat(result.getInfoMessages()).isEqualTo(expected.getInfoMessages());
        softly.assertThat(result.getDuplicatesSize()).isEqualTo(expected.getDuplicatesSize());
        softly.assertThat(result.getSizeOf(Priority.LOW)).isEqualTo(expected.getSizeOf(Priority.LOW));
        softly.assertThat(result.getSizeOf(Priority.NORMAL)).isEqualTo(expected.getSizeOf(Priority.NORMAL));
        softly.assertThat(result.getSizeOf(Priority.HIGH)).isEqualTo(expected.getSizeOf(Priority.HIGH));
        softly.assertThat(result.stream().count()).isEqualTo((int)expected.stream().count());
        softly.assertThat(result.iterator()).containsAll(expected::iterator);
    }
}
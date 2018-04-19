package edu.hm.hafner.java.db;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;

/**
 * Tests the class {@link EntityMapper} for {@link Issue} instances.
 *
 * @author Michael Schmid
 */
class IssueMapperTest {
    @Test
    void mapIssueToIssueEntity() {
        EntityMapper mapper = new EntityMapper();

        Issue issue = new IssueBuilder().build();

        IssueEntity entity = mapper.map(issue);

        SoftAssertions softly = new SoftAssertions();
        assertIssueAndEntityEqual(softly, entity, issue);
        softly.assertAll();
    }

    @Test
    void issueRoundTrip() {
        EntityMapper mapper = new EntityMapper();
        Issue issue = new IssueBuilder().build();

        IssueEntity entity = mapper.map(issue);

        SoftAssertions softly = new SoftAssertions();
        assertIssueAndEntityEqual(softly, entity, issue);

        Issue result = mapper.map(entity);

        assertRoundTrip(softly, result, issue);
        softly.assertAll();
    }

    @Test
    void issueRoundTripWithLineRangeList() {
        EntityMapper mapper = new EntityMapper();
        IssueBuilder builder = new IssueBuilder();

        builder.setLineStart(1).setLineEnd(2);
        LineRangeList lineRanges = new LineRangeList();
        lineRanges.add(new LineRange(3, 4));
        lineRanges.add(new LineRange(5, 6));
        builder.setLineRanges(lineRanges);

        Issue issue = builder.build();
        IssueEntity entity = mapper.map(issue);

        SoftAssertions softly = new SoftAssertions();
        assertIssueAndEntityEqual(softly, entity, issue);

        Issue result = mapper.map(entity);

        assertRoundTrip(softly, result, issue);
        softly.assertAll();
    }

    private void assertRoundTrip(final SoftAssertions softly, final Issue result, final Issue expected) {
        softly.assertThat(result.getCategory()).isEqualTo(expected.getCategory());
        softly.assertThat(result.getType()).isEqualTo(expected.getType());
        softly.assertThat(result.getPriority()).isEqualTo(expected.getPriority());
        softly.assertThat(result.getMessage()).isEqualTo(expected.getMessage());
        softly.assertThat(result.getLineStart()).isEqualTo(expected.getLineStart());
        softly.assertThat(result.getLineEnd()).isEqualTo(expected.getLineEnd());
        softly.assertThat(result.getColumnStart()).isEqualTo(expected.getColumnStart());
        softly.assertThat(result.getColumnEnd()).isEqualTo(expected.getColumnEnd());
        softly.assertThat(result.getLineRanges()).isEqualTo(expected.getLineRanges());
        softly.assertThat(result.getId()).isEqualTo(expected.getId());
        softly.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
        softly.assertThat(result.getReference()).isEqualTo(expected.getReference());
        softly.assertThat(result.getOrigin()).isEqualTo(expected.getOrigin());
        softly.assertThat(result.getModuleName()).isEqualTo(expected.getModuleName());
        softly.assertThat(result.getPackageName()).isEqualTo(expected.getPackageName());
        softly.assertThat(result.getFileName()).isEqualTo(expected.getFileName());
        softly.assertThat(result.getFingerprint()).isEqualTo(expected.getFingerprint());
    }

    private void assertIssueAndEntityEqual(final SoftAssertions softly, final IssueEntity entity, final Issue issue) {
        softly.assertThat(entity.getCategory()).isEqualTo(issue.getCategory());
        softly.assertThat(entity.getType()).isEqualTo(issue.getType());
        softly.assertThat(entity.getPriority()).isEqualTo(issue.getPriority());
        softly.assertThat(entity.getMessage()).isEqualTo(issue.getMessage());
        softly.assertThat(entity.getLineStart()).isEqualTo(issue.getLineStart());
        softly.assertThat(entity.getLineEnd()).isEqualTo(issue.getLineEnd());
        softly.assertThat(entity.getColumnStart()).isEqualTo(issue.getColumnStart());
        softly.assertThat(entity.getColumnEnd()).isEqualTo(issue.getColumnEnd());
        softly.assertThat(entity.getLineRanges().size()).isEqualTo(issue.getLineRanges().size());
        softly.assertThat(entity.getId()).isEqualTo(issue.getId());
        softly.assertThat(entity.getDescription()).isEqualTo(issue.getDescription());
        softly.assertThat(entity.getReference()).isEqualTo(issue.getReference());
        softly.assertThat(entity.getOrigin()).isEqualTo(issue.getOrigin());
        softly.assertThat(entity.getModuleName()).isEqualTo(issue.getModuleName());
        softly.assertThat(entity.getPackageName()).isEqualTo(issue.getPackageName());
        softly.assertThat(entity.getFileName()).isEqualTo(issue.getFileName());
        softly.assertThat(entity.getFingerprint()).isEqualTo(issue.getFingerprint());
    }

}
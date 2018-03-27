package edu.hm.hafner.java.persistence;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void mapIssuesToIssuesEntity() {
        EntityMapper mapper = new EntityMapper();

        Issues<Issue> issues = new Issues<>();
        issues.add(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        issues.setId(ID);
        issues.logInfo("Hello");
        issues.logInfo("World!");
        issues.logError("Boom!");

        IssuesEntity result = mapper.map(issues);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getId()).isEqualTo(issues.getId());
        softly.assertThat(result.getErrorMessages()).isEqualTo(issues.getErrorMessages());
        softly.assertThat(result.getInfoMessages()).isEqualTo(issues.getInfoMessages());
        softly.assertThat(result.getSizeOfDuplicates()).isEqualTo(issues.getDuplicatesSize());
        softly.assertThat(result.getElements().size()).isEqualTo((int)issues.stream().count());
        softly.assertAll();
    }




}
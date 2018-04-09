package edu.hm.hafner.java.persistence;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.Priority;

@Component
public class DataInitializer {
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
    private final EntityService service;
    @Autowired
    public DataInitializer(final EntityService service) {
        this.service = service;
    }

    @PostConstruct
    public void initData() {
        LineRangeList ranges = new LineRangeList();
        ranges.add(new LineRange(2, 6));
        ranges.add(new LineRange(30, 40));
        Issue rangesIssue = new IssueBuilder().setLineRanges(ranges).build();

        Issues<Issue> issues = new Issues<>();
        issues.add(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        issues.add(rangesIssue);
        issues.setId(ID);
        issues.logInfo("Hello");
        issues.logInfo("World!");
        issues.logError("Boom!");

        service.insert(issues);

        Set<Issue> issueFromDatabase = service.selectAllIssue();
        Set<Issues<Issue>> issuesFromDatabase = service.selectAllIssues();

        System.out.println("------------------------------------");

        Issue work = issueFromDatabase.iterator().next();
        System.out.println(work);
        work.setFileName("FILE");
        Optional<Issue> result = service.update(work);
        if (result.isPresent()) {
            System.out.println(result.get());
        }

        Issues<Issue> workIssues = issuesFromDatabase.iterator().next();
        Issue newIssue = new IssueBuilder().build();
        workIssues.add(newIssue);
        service.update(workIssues);

        System.out.println("------------------------------------");
    }
}

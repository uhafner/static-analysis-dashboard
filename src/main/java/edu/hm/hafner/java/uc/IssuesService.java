package edu.hm.hafner.java.uc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.java.db.IssuesTableGateway;

/**
 * Provides services for {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesService {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private IssuesTableGateway issuesTableGateway;

    @Autowired
    public void setIssuesTableGateway(final IssuesTableGateway issuesTableGateway) {
        this.issuesTableGateway = issuesTableGateway;
    }

    /**
     * Returns the number of issues per category.
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return number of issues per category
     */
    public IssuePropertyDistribution createDistributionByCategory(final String id) {
        Issues<Issue> issues = issuesTableGateway.findByPrimaryKey(id);
        Map<String, Integer> counts = issues.getPropertyCount(Issue::getCategory);

        return new IssuePropertyDistribution(counts);
    }

    /**
     * Returns the number of issues per type.
     *
     * @param id
     *         the ID of the issues instance to show the details for
     *
     * @return number of issues per type
     */
    public IssuePropertyDistribution createDistributionByType(final String id) {
        Issues<Issue> issues = issuesTableGateway.findByPrimaryKey(id);
        Map<String, Integer> counts = issues.getPropertyCount(Issue::getType);

        return new IssuePropertyDistribution(counts);
    }
}

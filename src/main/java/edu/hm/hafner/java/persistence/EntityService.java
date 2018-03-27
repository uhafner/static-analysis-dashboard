package edu.hm.hafner.java.persistence;

import javax.transaction.Transactional;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

@Service
@Transactional
public class EntityService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private EntityMapper mapper;

    public Issue create(final Issue issue) {
        IssueEntity entity = getMapper().map(issue);
        issueRepository.save(entity);
        return getMapper().map(entity);
    }

    public Issues create(final Issues issues) {
        IssuesEntity entity = getMapper().map(issues);
        ((Stream<Issue>)issues.stream()).forEach(issue -> create(issue));
        issuesRepository.save(entity);
        //return getMapper().map(entity);
        return issues;
    }

    public EntityMapper getMapper() {
        return mapper;
    }
}

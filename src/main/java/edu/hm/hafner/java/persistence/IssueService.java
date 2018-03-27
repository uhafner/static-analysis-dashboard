package edu.hm.hafner.java.persistence;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;

@Service
@Transactional
public class IssueService {

    @Autowired
    private IssueRepository repository;

    @Autowired
    private IssueMapper mapper;

    public Issue create(final Issue issue) {
        IssueEntity entity = getMapper().map(issue);
        repository.save(entity);
        return getMapper().map(entity);
    }

    public IssueMapper getMapper() {
        return mapper;
    }
}

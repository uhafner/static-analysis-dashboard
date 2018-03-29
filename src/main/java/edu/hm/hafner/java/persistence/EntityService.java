package edu.hm.hafner.java.persistence;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import static java.util.stream.Collectors.toSet;

@Service
@Transactional
public class EntityService {

    private final IssueRepository issueRepository;
    private final IssuesRepository issuesRepository;
    private final LineRangeRepository rangesRepository;
    private final EntityMapper mapper;

    @Autowired
    public EntityService(final IssueRepository issueRepository, final IssuesRepository issuesRepository, final LineRangeRepository rangesRepository, final EntityMapper mapper) {
        this.issueRepository = issueRepository;
        this.issuesRepository = issuesRepository;
        this.rangesRepository = rangesRepository;
        this.mapper = mapper;
    }

    public Issue insert(final Issue issue) {
        IssueEntity entity = getMapper().map(issue);
        entity.getLineRanges().stream().filter(range -> !getRangesRepository().findById(range.getId()).isPresent()).forEach(getRangesRepository()::save);
        getIssueRepository().save(entity);
        return getMapper().map(entity);
    }

    public Issues<Issue> insert(final Issues<Issue> issues) {
        IssuesEntity entity = getMapper().map(issues);
        issues.stream().forEach(this::insert);
        getIssuesRepository().save(entity);
        return getMapper().map(entity);
    }

    public Set<Issue> selectAllIssue() {
        return getIssueRepository().findAll().stream().map(getMapper()::map).collect(toSet());
    }

    public Set<Issues<Issue>> selectAllIssues() {
        return getIssuesRepository().findAll().stream().map(getMapper()::map).collect(toSet());
    }

    public Optional<Issue> select(final UUID id) {
        return getIssueRepository().findById(id).map(getMapper()::map);
    }

    public Optional<Issues<Issue>> select(final String id) {
        return getIssuesRepository().findById(id).map(getMapper()::map);
    }

    public Optional<Issue> update(final Issue issue) {
        Optional<IssueEntity> optionalEntity = getIssueRepository().findById(issue.getId());
        Optional<Issue> result = Optional.empty();

        if(optionalEntity.isPresent()) {
            optionalEntity.get().getLineRanges().stream().filter(range -> !getRangesRepository().findById(range.getId()).isPresent()).forEach(getRangesRepository()::save);
            IssueEntity entity = getMapper().map(issue, optionalEntity.get());
            result = Optional.of(getMapper().map(entity));
        }
        return result;
    }

    public Optional<Issues<Issue>> update(final Issues<Issue> issues) {
        Optional<IssuesEntity> optionalEntity = getIssuesRepository().findById(issues.getId());
        Optional<Issues<Issue>> result = Optional.empty();

        if(optionalEntity.isPresent()) {
            issues.stream().filter(issue -> !select(issue.getId()).isPresent()).forEach(this::insert);
            IssuesEntity entity = getMapper().map(issues, optionalEntity.get());
            result = Optional.of(getMapper().map(entity));
        }
        return result;
    }



    public EntityMapper getMapper() {
        return mapper;
    }

    public IssueRepository getIssueRepository() {
        return issueRepository;
    }

    public IssuesRepository getIssuesRepository() {
        return issuesRepository;
    }

    public LineRangeRepository getRangesRepository() {
        return rangesRepository;
    }
}

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

/**
 * Service to interact with a database to store and load Issue objects and Issues objects.
 *
 * @author Michael Schmid
 */
@Service
@Transactional
public class EntityService {

    /**
     * Repository to store and load Issue objects.
     */
    private final IssueRepository issueRepository;

    /**
     * Repository to store and load Issues objects.
     */
    private final IssuesRepository issuesRepository;

    /**
     * Repository to store and load LineRange objects.
     */
    private final LineRangeRepository rangesRepository;

    /**
     * Mapper to convert dto-object to entity-object and reverse.
     */
    private final EntityMapper mapper;

    @Autowired
    public EntityService(final IssueRepository issueRepository, final IssuesRepository issuesRepository, final LineRangeRepository rangesRepository, final EntityMapper mapper) {
        this.issueRepository = issueRepository;
        this.issuesRepository = issuesRepository;
        this.rangesRepository = rangesRepository;
        this.mapper = mapper;
    }

    /**
     * Insert a Issue object to database. If the id of the issue is still in the database an exception occurs.
     * Inserts all related LineRanges if they are not present in the database. Returns a new object with the values of the database.
     *
     * @param issue to insert into the database
     * @return new instance of the issue with the values of the database
     */
    public Issue insert(final Issue issue) {
        IssueEntity entity = getMapper().map(issue);
        entity.getLineRanges().stream().filter(range -> !getRangesRepository().findById(range.getId()).isPresent()).forEach(getRangesRepository()::save);
        getIssueRepository().save(entity);
        return getMapper().map(entity);
    }

    /**
     * Insert a Issues object to database. If the id of the issues is still in the database an exception occurs.
     * Inserts all related Issue entities if they are not present in the database. Returns a new object with the values of the database.
     *
     * @param issues to insert into the database
     * @return new instance of the issues with the values of the database
     */
    public Issues<Issue> insert(final Issues<Issue> issues) {
        IssuesEntity entity = getMapper().map(issues);
        issues.stream().filter(issue -> !getIssueRepository().findById(issue.getId()).isPresent()).forEach(this::insert);
        getIssuesRepository().save(entity);
        return getMapper().map(entity);
    }

    /**
     * Select all issue entities stored in the database.
     * @return set of all issue entities in the database.
     */
    public Set<Issue> selectAllIssue() {
        return getIssueRepository().findAll().stream().map(getMapper()::map).collect(toSet());
    }

    /**
     * Select all issues entities stored in the database.
     * @return set of all issues entities in the database.
     */
    public Set<Issues<Issue>> selectAllIssues() {
        return getIssuesRepository().findAll().stream().map(getMapper()::map).collect(toSet());
    }

    /**
     * Select a single issue identified by the id.
     * @param id of the desired issue
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> select(final UUID id) {
        return getIssueRepository().findById(id).map(getMapper()::map);
    }

    /**
     * Select a single issues identified by the id.
     * @param id of the desired issues
     * @return Optional a new the issues if it is present in the database else an empty optional.
     */
    public Optional<Issues<Issue>> select(final String id) {
        return getIssuesRepository().findById(id).map(getMapper()::map);
    }

    /**
     * Update the issue in the database. If the issue is not present in the database an empty optional will be returned and nothing else happens.
     * Inserts all related LineRanges if they are not present in the database.
     *
     * @param issue to update in the database.
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> update(final Issue issue) {
        Optional<IssueEntity> optionalEntity = getIssueRepository().findById(issue.getId());
        Optional<Issue> result = Optional.empty();

        if(optionalEntity.isPresent()) {
            getMapper().map(issue).getLineRanges().stream().filter(range -> !getRangesRepository().findById(range.getId()).isPresent()).forEach(getRangesRepository()::save);
            IssueEntity entity = getMapper().map(issue, optionalEntity.get());
            result = Optional.of(getMapper().map(entity));
        }
        return result;
    }

    /**
     * Update the issues in the database. If the issues entity is not present in the database an empty optional will be returned and nothing else happens.
     * Inserts all related issue entities if they are not present in the database.
     *
     * @param issues to update in the database.
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
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



    private EntityMapper getMapper() {
        return mapper;
    }

    private IssueRepository getIssueRepository() {
        return issueRepository;
    }

    private IssuesRepository getIssuesRepository() {
        return issuesRepository;
    }

    private LineRangeRepository getRangesRepository() {
        return rangesRepository;
    }
}

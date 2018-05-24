package edu.hm.hafner.java.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import static java.util.stream.Collectors.*;

/**
 * Service to interact with a database to store and load Issue objects and Issues objects.
 *
 * @author Michael Schmid
 */
@Service
@Transactional
public class EntityService {
    /** Repository to store and load Issue objects. */
    private final IssueRepository issueRepository;
    /** Repository to store and load Issues objects. */
    private final IssuesRepository issuesRepository;
    /** Repository to store and load LineRange objects. */
    private final LineRangeRepository rangesRepository;
    /** Mapper to convert dto-object to entity-object and vice versa. */
    private final EntityMapper mapper;

    @PersistenceContext
    private final EntityManager manager;

    /**
     * Creates a new instance of {@link EntityService}.
     *
     * @param issueRepository
     *         JPA repository to store and load {@link Issue} objects
     * @param issuesRepository
     *         JPA repository to store and load {@link Issues} objects
     * @param rangesRepository
     *         JPA repository to store and load {@link LineRange} objects
     * @param mapper
     *         OR mapper convert dto-object to entity-object and vice versa
     * @param manager
     *         entity manager to use to build custom queries
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public EntityService(final IssueRepository issueRepository, final IssuesRepository issuesRepository,
            final LineRangeRepository rangesRepository, final EntityMapper mapper,
            final EntityManager manager) {
        this.issueRepository = issueRepository;
        this.issuesRepository = issuesRepository;
        this.rangesRepository = rangesRepository;
        this.mapper = mapper;
        this.manager = manager;
    }

    /**
     * Insert a Issue object to database. If the id of the issue is still in the database an exception occurs. Inserts
     * all related LineRanges if they are not present in the database. Returns a new object with the values of the
     * database.
     *
     * @param issue
     *         to insert into the database
     *
     * @return new instance of the issue with the values of the database
     */
    public Issue insert(final Issue issue) {
        IssueEntity entity = mapper.map(issue);
        entity.getLineRanges()
                .stream()
                .filter(range -> !rangesRepository.findById(range.getId()).isPresent())
                .forEach(rangesRepository::save);
        issueRepository.save(entity);
        return mapper.map(entity);
    }

    /**
     * Insert a Issues object to database. If the id of the issues is still in the database an exception occurs. Inserts
     * all related Issue entities if they are not present in the database. Returns a new object with the values of the
     * database.
     *
     * @param issues
     *         to insert into the database
     *
     * @return new instance of the issues with the values of the database
     */
    public Issues<Issue> insert(final Issues<?> issues) {
        IssuesEntity entity = mapper.map(issues);
        issues.stream()
                .filter(issue -> !issueRepository.findById(issue.getId()).isPresent())
                .forEach(this::insert);
        issuesRepository.save(entity);
        return mapper.map(entity);
    }

    /**
     * Select all issue entities stored in the database.
     *
     * @return set of all issue entities in the database.
     */
    public Set<Issue> selectAllIssue() {
        return issueRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /**
     * Select all issues entities stored in the database.
     *
     * @return set of all issues entities in the database.
     */
    public Set<Issues<Issue>> selectAllIssues() {
        return issuesRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /**
     * Select a single issue identified by the id.
     *
     * @param id
     *         of the desired issue
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> select(final UUID id) {
        return issueRepository.findById(id).map(mapper::map);
    }

    /**
     * Select a single issues identified by the id.
     *
     * @param origin
     *         of the desired issues
     * @param reference
     *         of the desired issues
     *
     * @return Optional a new the issues if it is present in the database else an empty optional.
     */
    public Optional<Issues<Issue>> select(final String origin, final String reference) {
        return issuesRepository.findById(new IssuesEntityId(origin, reference)).map(mapper::map);
    }

    /**
     * Selects all issues with the specified reference. The matching issues will be ordered by origin.
     *
     * @param reference
     *         reference of the desired issues
     *
     * @return the matching ordered list of issues
     */
    public List<Issues<Issue>> selectByReference(final String reference) {
        return issuesRepository.findByIdReferenceOrderByIdOrigin(reference).stream().map(mapper::map).collect(toList());

    }

    /**
     * Selects all issues with the specified origin. The matching issues will be ordered by reference.
     *
     * @param origin
     *         origin of the desired issues
     *
     * @return the matching ordered list of issues
     */
    public List<Issues<Issue>> selectByOrigin(final String origin) {
        return issuesRepository.findByIdOriginOrderByIdReference(origin).stream().map(mapper::map).collect(toList());
    }

    /**
     * Update the issue in the database. If the issue is not present in the database an empty optional will be returned
     * and nothing else happens. Inserts all related LineRanges if they are not present in the database.
     *
     * @param issue
     *         to update in the database.
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> update(final Issue issue) {
        Optional<IssueEntity> optionalEntity = issueRepository.findById(issue.getId());
        Optional<Issue> result = Optional.empty();

        if (optionalEntity.isPresent()) {
            mapper.map(issue)
                    .getLineRanges()
                    .stream()
                    .filter(range -> !rangesRepository.findById(range.getId()).isPresent())
                    .forEach(rangesRepository::save);
            IssueEntity entity = mapper.map(issue, optionalEntity.get());
            result = Optional.of(mapper.map(entity));
        }
        return result;
    }

    /**
     * Update the issues in the database. If the issues entity is not present in the database an empty optional will be
     * returned and nothing else happens. Inserts all related issue entities if they are not present in the database.
     *
     * @param issues
     *         to update in the database.
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issues<Issue>> update(final Issues<?> issues) {
        Optional<IssuesEntity> optionalEntity = issuesRepository.findById(new IssuesEntityId(issues));
        Optional<Issues<Issue>> result = Optional.empty();

        if (optionalEntity.isPresent()) {
            issues.stream().filter(issue -> !select(issue.getId()).isPresent()).forEach(this::insert);
            IssuesEntity entity = mapper.map(issues, optionalEntity.get());
            result = Optional.of(mapper.map(entity));
        }
        return result;
    }

    /**
     * Returns a list of the references of all persisted reports.
     *
     * @return list of references
     */
    public List<String> findAllReferences() {
        TypedQuery<String> query = manager.createQuery(
                "SELECT i.id.reference FROM IssuesEntity AS i", String.class);
        return query.getResultList();
    }
}
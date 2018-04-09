package edu.hm.hafner.java.persistence;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import static java.util.stream.Collectors.toList;

/**
 * Mapper to map issue and issues dtos to issue and issues entities which could be stored in a database.
 *
 * @author Michael Schmid
 */
@Component
public class EntityMapper {

    /**
     * Special rules for the ModelMapper to map Issue to IssuesEntity.
     */
    private static final PropertyMap<Issue, IssueEntity> ISSUE_PROPERTY_MAP = new PropertyMap<Issue, IssueEntity>() {
        @Override
        protected void configure() {
            skip().setLineRanges(null);
        }
    };

    /**
     * Special rules for the ModelMapper to map IssueEntity to IssueBuilder.
     */
    private static final PropertyMap<IssueEntity, IssueBuilder> ISSUE_ENTITY_PROPERTY_MAP = new PropertyMap<IssueEntity, IssueBuilder>() {
        @Override
        protected void configure() {
            skip().setLineRanges(new LineRangeList());
        }
    };

    /**
     * Special rules for the ModelMapper to map Issues to IssuesEntity.
     */
    private static final PropertyMap<Issues<Issue>, IssuesEntity> ISSUES_PROPERTY_MAP = new PropertyMap<Issues<Issue>, IssuesEntity>() {
        @Override
        protected void configure() {
            map().setErrorMessages(source.getErrorMessages().castToList());
            map().setInfoMessages(source.getInfoMessages().castToList());
            map().setSizeOfDuplicates(source.getDuplicatesSize());
            skip().setElements(null);
        }
    };

    /**
     * Mapper which overtakes the trivial mapping from a getter to the proper setter.
     */
    private final ModelMapper mapper;


    public EntityMapper() {
        mapper = new ModelMapper();
        mapper.addMappings(ISSUE_PROPERTY_MAP);
        mapper.addMappings(ISSUE_ENTITY_PROPERTY_MAP);
        mapper.addMappings(ISSUES_PROPERTY_MAP);
        mapper.typeMap(IssuesEntity.class, Issues.class);
        mapper.typeMap(LineRange.class, LineRangeEntity.class);
        mapper.validate();
    }

    /**
     * Map the issue to a new entity object.
     * @param issue to map to a entity
     * @return a new entity object with the values of the issue.
     */
    public IssueEntity map(final Issue issue) {
        return map(issue, new IssueEntity());
    }

    /**
     * Map the issue to the entity object. This is used to update existing entities in the database.
     * @param issue to map to a entity
     * @param entity which should get the values of issue.
     * @return the entity of the parameters
     */
    public IssueEntity map(final Issue issue, final IssueEntity entity) {
        getMapper().map(issue, entity);
        entity.setLineRanges(issue.getLineRanges().stream().map(lineRange -> getMapper().map(lineRange, LineRangeEntity.class)).collect(toList()));
        return entity;
    }

    /**
     * Map the entity to a new dto object.
     * @param entity to map to a dto
     * @return a new issue object with the values of the entity.
     */
    public Issue map(final IssueEntity entity) {
        IssueBuilder builder = getMapper().map(entity, IssueBuilder.class);
        LineRangeList ranges = new LineRangeList();
        ranges.addAll(entity.getLineRanges().stream().map(lineRangeEntity -> new LineRange(lineRangeEntity.getStart(), lineRangeEntity.getEnd())).collect(toList()));
        builder.setLineRanges(ranges);

        Issue result = builder.build();
        setIssueId(result, entity.getId());
        return result;
    }

    /**
     * Map the issues dto to a new entity object.
     * @param issues to map to a entity
     * @return a new entity object with the values of the issues.
     */
    public IssuesEntity map(final Issues<Issue> issues) {
        return map(issues, new IssuesEntity());
    }

    /**
     * Map the issues dto to the entity object. This is used to update existing entities in the database.
     * @param issues to map to a entity
     * @param entity which should get the values of issues.
     * @return the entity of the parameters
     */
    public IssuesEntity map(final Issues<Issue> issues, final IssuesEntity entity) {
        getMapper().map(issues, entity);
        List<IssueEntity> issuesSet = issues.stream().map(this::map).collect(toList());
        entity.setElements(issuesSet);
        return entity;
    }

    /**
     * Map the issues entity to a new issues dto object.
     * @param entity to map to a dto
     * @return a new issues object with the values of the entity.
     */
    public Issues<Issue> map(final IssuesEntity entity) {
        Issues<Issue> result = getMapper().map(entity, Issues.class);
        entity.getInfoMessages().forEach(result::logInfo);
        entity.getErrorMessages().forEach(result::logError);
        entity.getElements().stream().map(this::map).forEach(result::add);
        return result;
    }

    private ModelMapper getMapper() {
        return mapper;
    }

    /**
     * Set the private final id of a issue object by using reflexions.
     * @param result issue which should get the id
     * @param id to set
     */
    private void setIssueId(final Issue result, final UUID id) {
        Field idField = null;
        try {
            idField = result.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(result, id);
        }
        catch (NoSuchFieldException|IllegalAccessException exception) {
            throw new RuntimeException("The implementation of Issue has changed. The Mapper can't map Issue anymore.", exception);
        }
    }
}

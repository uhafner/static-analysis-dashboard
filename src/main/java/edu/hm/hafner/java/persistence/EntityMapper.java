package edu.hm.hafner.java.persistence;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRangeList;
import static java.util.stream.Collectors.toSet;

@Component
public class EntityMapper {

    private static final PropertyMap<Issues, IssuesEntity> ISSUES_PROPERTY_MAP = new PropertyMap<Issues, IssuesEntity>() {
        @Override
        protected void configure() {
            map().setErrorMessages(source.getErrorMessages().castToList());
            map().setInfoMessages(source.getInfoMessages().castToList());
            map().setSizeOfDuplicates(source.getDuplicatesSize());
            skip().setElements(null);
        }
    };

    private final ModelMapper mapper;



    public EntityMapper() {
        mapper = new ModelMapper();
        mapper.typeMap(Issue.class, IssueEntity.class);
        mapper.typeMap(IssueEntity.class, Issue.class);
        mapper.addMappings(ISSUES_PROPERTY_MAP);
        mapper.validate();

        Function<IssueEntity, Issue> f = this::map;
    }

    public IssueEntity map(final Issue issue) {
        return getMapper().map(issue, IssueEntity.class);
    }

    public Issue map(final IssueEntity entity) {
        IssueBuilder builder = getMapper().map(entity, IssueBuilder.class);

        if(entity.getLineRanges() instanceof LineRangeList) {
            builder.setLineRanges((LineRangeList) entity.getLineRanges());
        }

        Issue result = builder.build();
        setIssueId(result, entity.getId());
        return result;
    }

    public IssuesEntity map(final Issues issues) {
        IssuesEntity result = getMapper().map(issues, IssuesEntity.class);
        Set<IssueEntity> issuesSet = ((Stream<Issue>)issues.stream()).map(this::map).collect(toSet());
        result.setElements(issuesSet);
        return result;
    }

    private ModelMapper getMapper() {
        return mapper;
    }

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

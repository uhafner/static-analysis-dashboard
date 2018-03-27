package edu.hm.hafner.java.persistence;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LineRangeList;

@Component
public class IssueMapper {

    private final ModelMapper mapper;

    public IssueMapper() {
        mapper = new ModelMapper();
        mapper.typeMap(Issue.class, IssueEntity.class);
        mapper.typeMap(IssueEntity.class, Issue.class);
        mapper.validate();
    }

    public IssueEntity map(final Issue issue) {
        return getMapper().map(issue, IssueEntity.class);
    }

    public Issue map(final IssueEntity entity) {
        IssueBuilder builder = getMapper().map(entity, IssueBuilder.class);

        if(entity.getLineRanges() instanceof LineRangeList) {
            builder.setLineRanges((LineRangeList) entity.getLineRanges());
        }

        return builder.build();
    }

    private ModelMapper getMapper() {
        return mapper;
    }
}

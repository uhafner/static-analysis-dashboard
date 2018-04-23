package edu.hm.hafner.java.db;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

import edu.hm.hafner.analysis.Priority;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity of a Issue object to store in a database.
 *
 * @author Michael Schmid
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@Entity
@Table(name = "issue")
@Data
@EqualsAndHashCode(exclude = {"id", "fingerprint"})
public class IssueEntity {
    private String category;
    private String type;
    private Priority priority;
    @Column(length=1024)
    private String message;

    private int lineStart;
    private int lineEnd;
    private int columnStart;
    private int columnEnd;

    @ElementCollection(targetClass = LineRangeEntity.class)
    @OrderColumn
    private List<LineRangeEntity> lineRanges;
    @Id
    private UUID id;
    private String description;
    private String reference;       // not part of equals
    private String origin;
    private String moduleName;
    private String packageName;
    private String fileName;
    private String fingerprint;     // not part of equals
}

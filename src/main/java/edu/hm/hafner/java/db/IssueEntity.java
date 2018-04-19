package edu.hm.hafner.java.db;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.hm.hafner.analysis.Priority;

/**
 * Entity of a Issue object to store in a database.
 *
 * @author Michael Schmid
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "PMD.TooManyFields"})
@Entity
@Table(name = "issue")
public class IssueEntity {
    private String category;
    private String type;
    private Priority priority;
    @Column(length = 1024)
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

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(final int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(final int lineEnd) {
        this.lineEnd = lineEnd;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public void setColumnStart(final int columnStart) {
        this.columnStart = columnStart;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    public void setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
    }

    public List<LineRangeEntity> getLineRanges() {
        return lineRanges;
    }

    public void setLineRanges(final List<LineRangeEntity> lineRanges) {
        this.lineRanges = lineRanges;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueEntity entity = (IssueEntity) o;
        return lineStart == entity.lineStart
                && lineEnd == entity.lineEnd
                && columnStart == entity.columnStart
                && columnEnd == entity.columnEnd
                && Objects.equals(category, entity.category)
                && Objects.equals(type, entity.type)
                && priority == entity.priority
                && Objects.equals(message, entity.message)
                && Objects.equals(lineRanges, entity.lineRanges)
                && Objects.equals(id, entity.id)
                && Objects.equals(description, entity.description)
                && Objects.equals(origin, entity.origin)
                && Objects.equals(moduleName, entity.moduleName)
                && Objects.equals(packageName, entity.packageName)
                && Objects.equals(fileName, entity.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, type, priority, message, lineStart, lineEnd, columnStart, columnEnd, lineRanges,
                id, description, reference, origin, moduleName, packageName, fileName, fingerprint);
    }
}

package edu.hm.hafner.java.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

import edu.hm.hafner.analysis.Priority;

@Entity
public class IssueEntity {
    private String category; // almost final
    private String type;     // almost final
    private Priority priority;
    private String message;

    private int lineStart;     // fixed
    private int lineEnd;       // fixed
    private int columnStart;   // fixed
    private int columnEnd;     // fixed
    private Serializable lineRanges; // fixed

    @Id
    private UUID id; // fixed

    private String description;

    private String reference;       // mutable, not part of equals
    private String origin;          // mutable
    private String moduleName;      // mutable
    private String packageName; // mutable
    private String fileName;    // mutable

    private String fingerprint;     // mutable, not part of equals


    public IssueEntity() {
        // JPA
    }

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

    public Serializable getLineRanges() {
        return lineRanges;
    }

    public void setLineRanges(final Serializable lineRanges) {
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
}

package edu.hm.hafner.java.db;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity of a Issues object to store in a database.
 *
 * @author Michael Schmid
 */
@Entity
@Table(name = "issues")
public class IssuesEntity {
    /** Set of issue objects related to this issues object. */
    @OneToMany
    @OrderColumn
    private List<IssueEntity> elements = new ArrayList<>();

    /** List of info messages stored as a table of strings. */
    @ElementCollection(targetClass = String.class)
    @OrderColumn
    private List<String> infoMessages = new ArrayList<>();

    /** List of error messages stored as a table of strings. */
    @ElementCollection(targetClass = String.class)
    @OrderColumn
    private List<String> errorMessages = new ArrayList<>();

    private int duplicatesSize = 0;

    @EmbeddedId
    private final IssuesEntityId id = new IssuesEntityId();

    public List<IssueEntity> getElements() {
        return elements;
    }

    public void setElements(final List<IssueEntity> elements) {
        this.elements = elements;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(final List<String> infoMessages) {
        this.infoMessages = infoMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(final List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public int getDuplicatesSize() {
        return duplicatesSize;
    }

    public void setDuplicatesSize(final int duplicatesSize) {
        this.duplicatesSize = duplicatesSize;
    }

    public String getOrigin() {
        return getId().getOrigin();
    }

    /**
     * Sets the origin for this issues instance. Since the origin is part of the ID, actually the ID will be updated.
     *
     * @param origin
     *         the origin
     */
    public void setOrigin(final String origin) {
        getId().setOrigin(origin);
    }

    public String getReference() {
        return getId().getReference();
    }

    /**
     * Sets the reference for this issues instance. Since the reference is part of the ID, actually the ID will be
     * updated.
     *
     * @param reference
     *         the origin
     */
    public void setReference(final String reference) {
        getId().setReference(reference);
    }

    public IssuesEntityId getId() {
        return id;
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IssuesEntity that = (IssuesEntity) obj;
        return duplicatesSize == that.duplicatesSize
                && Objects.equals(elements, that.elements)
                && Objects.equals(infoMessages, that.infoMessages)
                && Objects.equals(errorMessages, that.errorMessages)
                && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements, infoMessages, errorMessages, duplicatesSize, id);
    }
}

package edu.hm.hafner.java.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Entity of a LineRange object to store in a database. Entity of a LineRange object to store in a database.
 *
 * @author Michael Schmid
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@Entity
@Table(name = "linerange")
public final class LineRangeEntity {
    /** Id of the entity build of start and end of the range (start-end). */
    @Id
    private String id;

    /** Line number of the start of the range. */
    private int start;

    /** Line number of the end of the range. */
    private int end;

    /**
     * The no-argument constructor is required to use JPA.
     */
    public LineRangeEntity() {
        // JPA
    }

    public LineRangeEntity(final int start, final int end) {
        this.start = start;
        this.end = end;
        id = calculateId();
    }

    public int getStart() {
        return start;
    }

    /**
     * Sets the first line of this range.
     *
     * @param start the first line
     */
    public void setStart(final int start) {
        this.start = start;
        setId(calculateId());
    }

    public int getEnd() {
        return end;
    }

    /**
     * Sets the last line of this range.
     *
     * @param end the last line
     */
    public void setEnd(final int end) {
        this.end = end;
        setId(calculateId());
    }

    public String getId() {
        return id;
    }

    private void setId(final String id) {
        this.id = id;
    }

    /**
     * Calculate the id of a LineRangeEntity by concatenate the start, a minus and the end (start-end).
     *
     * @return if of the LineRangeEntity
     */
    private String calculateId() {
        return getStart() + "-" + getEnd();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineRangeEntity that = (LineRangeEntity) o;
        return start == that.start && end == that.end && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end);
    }
}

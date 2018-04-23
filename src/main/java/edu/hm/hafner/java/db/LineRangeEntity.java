package edu.hm.hafner.java.db;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Entity of a LineRange object to store in a database. Entity of a LineRange object to store in a database.
 *
 * @author Michael Schmid
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@Entity
@Table(name = "lineRange")
@NoArgsConstructor
@EqualsAndHashCode
public class LineRangeEntity {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LineRangeEntityId implements Serializable {
        /** Line number of the start of the range. */
        private int start;

        /** Line number of the end of the range. */
        private int end;
    }

    /** Id of the entity build of start and end of the range (start-end). */
    @Id
    @EmbeddedId
    @Getter
    @Delegate
    private final LineRangeEntityId id = new LineRangeEntityId();

    public LineRangeEntity(final int start, final int end) {
        id.setStart(start);
        id.setEnd(end);
    }
}

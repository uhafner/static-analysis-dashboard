package edu.hm.hafner.java.db;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.hm.hafner.analysis.Issues;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

/**
 * Entity of a Issues object to store in a database.
 *
 * @author Michael Schmid
 */
@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
public class IssuesEntity {

    @Embeddable
    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    static class IssuesEntityId implements Serializable {
        private String origin = "-";
        private String reference = "-";

        public IssuesEntityId(final Issues issues) {
            this(issues.getOrigin(), issues.getReference());
        }
    }

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
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    @Delegate
    private IssuesEntityId id = new IssuesEntityId();
}

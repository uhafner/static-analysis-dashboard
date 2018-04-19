package edu.hm.hafner.java.db;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

import edu.hm.hafner.analysis.Issues;

/**
 * Composite key of IssuesEntity contains origin and reference to identify an entity.
 *
 * @author Michael Schmid
 */
@Embeddable
public class IssuesEntityId implements Serializable {
    private static final long serialVersionUID = 6578603589347674507L;

    private String origin;
    private String reference;

    public IssuesEntityId() {
        this("-", "-");
    }

    public IssuesEntityId(final String origin, final String reference) {
        this.origin = origin;
        this.reference = reference;
    }

    public IssuesEntityId(final Issues<?> issues) {
        this(issues.getOrigin(), issues.getReference());
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuesEntityId that = (IssuesEntityId) o;
        return Objects.equals(origin, that.origin)
                && Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {

        return Objects.hash(origin, reference);
    }
}

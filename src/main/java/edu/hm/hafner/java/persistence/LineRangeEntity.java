package edu.hm.hafner.java.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class LineRangeEntity {
    @Id
    private String id;
    private int start;
    private int end;

    public LineRangeEntity() {
        // JPA
    }

    public LineRangeEntity(int start, int end) {
        this.start = start;
        this.end = end;
        id = calculateId();
    }

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
        setId(calculateId());
    }

    public int getEnd() {
        return end;
    }

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
        return start == that.start &&
                end == that.end &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, start, end);
    }
}

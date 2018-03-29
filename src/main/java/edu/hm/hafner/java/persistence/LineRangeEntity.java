package edu.hm.hafner.java.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LineRangeEntity {
    @Id
    private String id;
    private int start;
    private int end;

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
}

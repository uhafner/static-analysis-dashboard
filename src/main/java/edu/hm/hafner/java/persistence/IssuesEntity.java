package edu.hm.hafner.java.persistence;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.hm.hafner.analysis.Priority;

@Entity
@Table(name="issues")
public class IssuesEntity {

    @OneToMany//(mappedBy="issues")
    private Set<IssueEntity> elements = new LinkedHashSet<>();
    @ElementCollection(targetClass=String.class)
    private List<String> infoMessages = new ArrayList<>();
    @ElementCollection(targetClass=String.class)
    private List<String> errorMessages = new ArrayList<>();

    private int sizeOfDuplicates = 0;
    @Id
    private String id;

    public IssuesEntity() {
        // JPA
    }

    public Set<IssueEntity> getElements() {
        return elements;
    }

    public void setElements(final Set<IssueEntity> elements) {
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

    public int getSizeOfDuplicates() {
        return sizeOfDuplicates;
    }

    public void setSizeOfDuplicates(final int sizeOfDuplicates) {
        this.sizeOfDuplicates = sizeOfDuplicates;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}

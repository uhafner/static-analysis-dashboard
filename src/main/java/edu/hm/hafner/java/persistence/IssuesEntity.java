package edu.hm.hafner.java.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.hm.hafner.analysis.Priority;

//@Entity
public class IssuesEntity {

    @OneToMany
    private Set<IssuesEntity> elements = new LinkedHashSet<>();
    private int[] sizeOfPriority = new int[Priority.values().length];
    private List<String> infoMessages = new ArrayList<>();
    private List<String> errorMessages = new ArrayList<>();

    private int sizeOfDuplicates = 0;
    @Id
    private String id;

    public IssuesEntity() {
        // JPA
    }

    public Set<IssuesEntity> getElements() {
        return elements;
    }

    public void setElements(final Set<IssuesEntity> elements) {
        this.elements = elements;
    }

    public int[] getSizeOfPriority() {
        return sizeOfPriority;
    }

    public void setSizeOfPriority(final int[] sizeOfPriority) {
        this.sizeOfPriority = sizeOfPriority;
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

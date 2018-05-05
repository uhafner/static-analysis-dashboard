package edu.hm.hafner.java.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access IssuesEntities.
 *
 * @author Michael Schmid
 */
public interface IssuesRepository extends JpaRepository<IssuesEntity, IssuesEntityId> {
    /**
     * Selects all issues with the specified reference. The matching issues will be ordered by origin.
     *
     * @param reference
     *         reference of the desired issues
     *
     * @return the matching ordered list of issues
     */
    List<IssuesEntity> findByIdReferenceOrderByIdOrigin(String reference);

    /**
     * Selects all issues with the specified origin. The matching issues will be ordered by reference.
     *
     * @param origin
     *         origin of the desired issues
     *
     * @return the matching ordered list of issues
     */
    List<IssuesEntity> findByIdOriginOrderByIdReference(String origin);
}

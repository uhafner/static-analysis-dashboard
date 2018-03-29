package edu.hm.hafner.java.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access IssueEntities.
 *
 * @author Michael Schmid
 */
public interface IssueRepository extends JpaRepository<IssueEntity, UUID> {
}

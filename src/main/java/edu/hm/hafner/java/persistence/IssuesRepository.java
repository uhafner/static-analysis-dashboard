package edu.hm.hafner.java.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access IssuesEntities.
 *
 * @author Michael Schmid
 */
public interface IssuesRepository extends JpaRepository<IssuesEntity, String> {
}

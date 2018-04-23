package edu.hm.hafner.java.db;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hm.hafner.java.db.LineRangeEntity.LineRangeEntityId;

/**
 * JPA repository to access LineRangeEntities.
 *
 * @author Michael Schmid
 */
public interface LineRangeRepository extends JpaRepository<LineRangeEntity, LineRangeEntityId> {
}

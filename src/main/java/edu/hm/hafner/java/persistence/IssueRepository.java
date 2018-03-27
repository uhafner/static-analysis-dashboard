package edu.hm.hafner.java.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity, UUID> {
}

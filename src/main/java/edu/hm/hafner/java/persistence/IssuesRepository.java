package edu.hm.hafner.java.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuesRepository extends JpaRepository<IssuesEntity, String> {
}

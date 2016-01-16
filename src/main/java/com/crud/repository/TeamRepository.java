package com.crud.repository;

import com.crud.domain.Team;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team,Long> {

}

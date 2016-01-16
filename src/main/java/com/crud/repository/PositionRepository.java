package com.crud.repository;

import com.crud.domain.Position;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Position entity.
 */
public interface PositionRepository extends JpaRepository<Position,Long> {

}

package com.crud.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crud.domain.Position;
import com.crud.repository.PositionRepository;
import com.crud.repository.search.PositionSearchRepository;
import com.crud.web.rest.util.HeaderUtil;
import com.crud.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Position.
 */
@RestController
@RequestMapping("/api")
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);
        
    @Inject
    private PositionRepository positionRepository;
    
    @Inject
    private PositionSearchRepository positionSearchRepository;
    
    /**
     * POST  /positions -> Create a new position.
     */
    @RequestMapping(value = "/positions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> createPosition(@RequestBody Position position) throws URISyntaxException {
        log.debug("REST request to save Position : {}", position);
        if (position.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("position", "idexists", "A new position cannot already have an ID")).body(null);
        }
        Position result = positionRepository.save(position);
        positionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("position", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /positions -> Updates an existing position.
     */
    @RequestMapping(value = "/positions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> updatePosition(@RequestBody Position position) throws URISyntaxException {
        log.debug("REST request to update Position : {}", position);
        if (position.getId() == null) {
            return createPosition(position);
        }
        Position result = positionRepository.save(position);
        positionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("position", position.getId().toString()))
            .body(result);
    }

    /**
     * GET  /positions -> get all the positions.
     */
    @RequestMapping(value = "/positions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Position>> getAllPositions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Positions");
        Page<Position> page = positionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/positions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /positions/:id -> get the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> getPosition(@PathVariable Long id) {
        log.debug("REST request to get Position : {}", id);
        Position position = positionRepository.findOne(id);
        return Optional.ofNullable(position)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /positions/:id -> delete the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        log.debug("REST request to delete Position : {}", id);
        positionRepository.delete(id);
        positionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("position", id.toString())).build();
    }

    /**
     * SEARCH  /_search/positions/:query -> search for the position corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/positions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Position> searchPositions(@PathVariable String query) {
        log.debug("REST request to search Positions for query {}", query);
        return StreamSupport
            .stream(positionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

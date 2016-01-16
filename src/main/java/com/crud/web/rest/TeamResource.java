package com.crud.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crud.domain.Team;
import com.crud.repository.TeamRepository;
import com.crud.repository.search.TeamSearchRepository;
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
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);
        
    @Inject
    private TeamRepository teamRepository;
    
    @Inject
    private TeamSearchRepository teamSearchRepository;
    
    /**
     * POST  /teams -> Create a new team.
     */
    @RequestMapping(value = "/teams",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Team> createTeam(@RequestBody Team team) throws URISyntaxException {
        log.debug("REST request to save Team : {}", team);
        if (team.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("team", "idexists", "A new team cannot already have an ID")).body(null);
        }
        Team result = teamRepository.save(team);
        teamSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("team", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teams -> Updates an existing team.
     */
    @RequestMapping(value = "/teams",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Team> updateTeam(@RequestBody Team team) throws URISyntaxException {
        log.debug("REST request to update Team : {}", team);
        if (team.getId() == null) {
            return createTeam(team);
        }
        Team result = teamRepository.save(team);
        teamSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("team", team.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teams -> get all the teams.
     */
    @RequestMapping(value = "/teams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Team>> getAllTeams(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Teams");
        Page<Team> page = teamRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /teams/:id -> get the "id" team.
     */
    @RequestMapping(value = "/teams/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        return Optional.ofNullable(team)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /teams/:id -> delete the "id" team.
     */
    @RequestMapping(value = "/teams/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamRepository.delete(id);
        teamSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("team", id.toString())).build();
    }

    /**
     * SEARCH  /_search/teams/:query -> search for the team corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/teams/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Team> searchTeams(@PathVariable String query) {
        log.debug("REST request to search Teams for query {}", query);
        return StreamSupport
            .stream(teamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

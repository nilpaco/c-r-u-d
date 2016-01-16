package com.crud.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crud.domain.Player;
import com.crud.repository.PlayerRepository;
import com.crud.repository.search.PlayerSearchRepository;
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
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);
        
    @Inject
    private PlayerRepository playerRepository;
    
    @Inject
    private PlayerSearchRepository playerSearchRepository;
    
    /**
     * POST  /players -> Create a new player.
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to save Player : {}", player);
        if (player.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("player", "idexists", "A new player cannot already have an ID")).body(null);
        }
        Player result = playerRepository.save(player);
        playerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("player", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /players -> Updates an existing player.
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to update Player : {}", player);
        if (player.getId() == null) {
            return createPlayer(player);
        }
        Player result = playerRepository.save(player);
        playerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("player", player.getId().toString()))
            .body(result);
    }

    /**
     * GET  /players -> get all the players.
     */
    @RequestMapping(value = "/players",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Player>> getAllPlayers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Players");
        Page<Player> page = playerRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/players");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /players/:id -> get the "id" player.
     */
    @RequestMapping(value = "/players/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        Player player = playerRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(player)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /players/:id -> delete the "id" player.
     */
    @RequestMapping(value = "/players/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerRepository.delete(id);
        playerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("player", id.toString())).build();
    }

    /**
     * SEARCH  /_search/players/:query -> search for the player corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/players/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Player> searchPlayers(@PathVariable String query) {
        log.debug("REST request to search Players for query {}", query);
        return StreamSupport
            .stream(playerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

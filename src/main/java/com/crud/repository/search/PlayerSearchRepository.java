package com.crud.repository.search;

import com.crud.domain.Player;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Player entity.
 */
public interface PlayerSearchRepository extends ElasticsearchRepository<Player, Long> {
}

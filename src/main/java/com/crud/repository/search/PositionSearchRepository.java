package com.crud.repository.search;

import com.crud.domain.Position;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Position entity.
 */
public interface PositionSearchRepository extends ElasticsearchRepository<Position, Long> {
}

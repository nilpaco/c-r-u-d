package com.crud.web.rest;

import com.crud.Application;
import com.crud.domain.Position;
import com.crud.repository.PositionRepository;
import com.crud.repository.search.PositionSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PositionResource REST controller.
 *
 * @see PositionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PositionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PositionRepository positionRepository;

    @Inject
    private PositionSearchRepository positionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPositionMockMvc;

    private Position position;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionResource positionResource = new PositionResource();
        ReflectionTestUtils.setField(positionResource, "positionSearchRepository", positionSearchRepository);
        ReflectionTestUtils.setField(positionResource, "positionRepository", positionRepository);
        this.restPositionMockMvc = MockMvcBuilders.standaloneSetup(positionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        position = new Position();
        position.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // Create the Position

        restPositionMockMvc.perform(post("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeCreate + 1);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positions
        restPositionMockMvc.perform(get("/api/positions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

		int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position
        position.setName(UPDATED_NAME);

        restPositionMockMvc.perform(put("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

		int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Get the position
        restPositionMockMvc.perform(delete("/api/positions/{id}", position.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeDelete - 1);
    }
}

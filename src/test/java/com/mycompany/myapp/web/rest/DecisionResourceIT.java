package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Decision;
import com.mycompany.myapp.domain.enumeration.DecisionType;
import com.mycompany.myapp.repository.DecisionRepository;
import com.mycompany.myapp.repository.search.DecisionSearchRepository;
import com.mycompany.myapp.service.dto.DecisionDTO;
import com.mycompany.myapp.service.mapper.DecisionMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DecisionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DecisionResourceIT {

    private static final DecisionType DEFAULT_DECISION = DecisionType.JE_DECIDE_D_ETRE_MEMBRE;
    private static final DecisionType UPDATED_DECISION = DecisionType.INDECIS;

    private static final String ENTITY_API_URL = "/api/decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/decisions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private DecisionMapper decisionMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DecisionSearchRepositoryMockConfiguration
     */
    @Autowired
    private DecisionSearchRepository mockDecisionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionMockMvc;

    private Decision decision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createEntity(EntityManager em) {
        Decision decision = new Decision().decision(DEFAULT_DECISION);
        return decision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createUpdatedEntity(EntityManager em) {
        Decision decision = new Decision().decision(UPDATED_DECISION);
        return decision;
    }

    @BeforeEach
    public void initTest() {
        decision = createEntity(em);
    }

    @Test
    @Transactional
    void createDecision() throws Exception {
        int databaseSizeBeforeCreate = decisionRepository.findAll().size();
        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);
        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(decisionDTO)))
            .andExpect(status().isCreated());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeCreate + 1);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getDecision()).isEqualTo(DEFAULT_DECISION);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(1)).save(testDecision);
    }

    @Test
    @Transactional
    void createDecisionWithExistingId() throws Exception {
        // Create the Decision with an existing ID
        decision.setId(1L);
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        int databaseSizeBeforeCreate = decisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(decisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void checkDecisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionRepository.findAll().size();
        // set the field null
        decision.setDecision(null);

        // Create the Decision, which fails.
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(decisionDTO)))
            .andExpect(status().isBadRequest());

        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecisions() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())));
    }

    @Test
    @Transactional
    void getDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get the decision
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL_ID, decision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decision.getId().intValue()))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDecision() throws Exception {
        // Get the decision
        restDecisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision
        Decision updatedDecision = decisionRepository.findById(decision.getId()).get();
        // Disconnect from session so that the updates on updatedDecision are not directly saved in db
        em.detach(updatedDecision);
        updatedDecision.decision(UPDATED_DECISION);
        DecisionDTO decisionDTO = decisionMapper.toDto(updatedDecision);

        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getDecision()).isEqualTo(UPDATED_DECISION);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository).save(testDecision);
    }

    @Test
    @Transactional
    void putNonExistingDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(decisionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void partialUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        partialUpdatedDecision.decision(UPDATED_DECISION);

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getDecision()).isEqualTo(UPDATED_DECISION);
    }

    @Test
    @Transactional
    void fullUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        partialUpdatedDecision.decision(UPDATED_DECISION);

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getDecision()).isEqualTo(UPDATED_DECISION);
    }

    @Test
    @Transactional
    void patchNonExistingDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decisionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(0)).save(decision);
    }

    @Test
    @Transactional
    void deleteDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeDelete = decisionRepository.findAll().size();

        // Delete the decision
        restDecisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, decision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Decision in Elasticsearch
        verify(mockDecisionSearchRepository, times(1)).deleteById(decision.getId());
    }

    @Test
    @Transactional
    void searchDecision() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        decisionRepository.saveAndFlush(decision);
        when(mockDecisionSearchRepository.search("id:" + decision.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(decision), PageRequest.of(0, 1), 1));

        // Search the decision
        restDecisionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + decision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())));
    }
}

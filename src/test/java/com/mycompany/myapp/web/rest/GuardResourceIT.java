package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Guard;
import com.mycompany.myapp.repository.GuardRepository;
import com.mycompany.myapp.repository.search.GuardSearchRepository;
import com.mycompany.myapp.service.dto.GuardDTO;
import com.mycompany.myapp.service.mapper.GuardMapper;
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
 * Integration tests for the {@link GuardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GuardResourceIT {

    private static final String ENTITY_API_URL = "/api/guards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/guards";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuardRepository guardRepository;

    @Autowired
    private GuardMapper guardMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.GuardSearchRepositoryMockConfiguration
     */
    @Autowired
    private GuardSearchRepository mockGuardSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuardMockMvc;

    private Guard guard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guard createEntity(EntityManager em) {
        Guard guard = new Guard();
        return guard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guard createUpdatedEntity(EntityManager em) {
        Guard guard = new Guard();
        return guard;
    }

    @BeforeEach
    public void initTest() {
        guard = createEntity(em);
    }

    @Test
    @Transactional
    void createGuard() throws Exception {
        int databaseSizeBeforeCreate = guardRepository.findAll().size();
        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);
        restGuardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardDTO)))
            .andExpect(status().isCreated());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeCreate + 1);
        Guard testGuard = guardList.get(guardList.size() - 1);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(1)).save(testGuard);
    }

    @Test
    @Transactional
    void createGuardWithExistingId() throws Exception {
        // Create the Guard with an existing ID
        guard.setId(1L);
        GuardDTO guardDTO = guardMapper.toDto(guard);

        int databaseSizeBeforeCreate = guardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeCreate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void getAllGuards() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        // Get all the guardList
        restGuardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guard.getId().intValue())));
    }

    @Test
    @Transactional
    void getGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        // Get the guard
        restGuardMockMvc
            .perform(get(ENTITY_API_URL_ID, guard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guard.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingGuard() throws Exception {
        // Get the guard
        restGuardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeUpdate = guardRepository.findAll().size();

        // Update the guard
        Guard updatedGuard = guardRepository.findById(guard.getId()).get();
        // Disconnect from session so that the updates on updatedGuard are not directly saved in db
        em.detach(updatedGuard);
        GuardDTO guardDTO = guardMapper.toDto(updatedGuard);

        restGuardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);
        Guard testGuard = guardList.get(guardList.size() - 1);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository).save(testGuard);
    }

    @Test
    @Transactional
    void putNonExistingGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void partialUpdateGuardWithPatch() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeUpdate = guardRepository.findAll().size();

        // Update the guard using partial update
        Guard partialUpdatedGuard = new Guard();
        partialUpdatedGuard.setId(guard.getId());

        restGuardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuard))
            )
            .andExpect(status().isOk());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);
        Guard testGuard = guardList.get(guardList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateGuardWithPatch() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeUpdate = guardRepository.findAll().size();

        // Update the guard using partial update
        Guard partialUpdatedGuard = new Guard();
        partialUpdatedGuard.setId(guard.getId());

        restGuardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuard))
            )
            .andExpect(status().isOk());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);
        Guard testGuard = guardList.get(guardList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuard() throws Exception {
        int databaseSizeBeforeUpdate = guardRepository.findAll().size();
        guard.setId(count.incrementAndGet());

        // Create the Guard
        GuardDTO guardDTO = guardMapper.toDto(guard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(guardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guard in the database
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(0)).save(guard);
    }

    @Test
    @Transactional
    void deleteGuard() throws Exception {
        // Initialize the database
        guardRepository.saveAndFlush(guard);

        int databaseSizeBeforeDelete = guardRepository.findAll().size();

        // Delete the guard
        restGuardMockMvc
            .perform(delete(ENTITY_API_URL_ID, guard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guard> guardList = guardRepository.findAll();
        assertThat(guardList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Guard in Elasticsearch
        verify(mockGuardSearchRepository, times(1)).deleteById(guard.getId());
    }

    @Test
    @Transactional
    void searchGuard() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        guardRepository.saveAndFlush(guard);
        when(mockGuardSearchRepository.search("id:" + guard.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(guard), PageRequest.of(0, 1), 1));

        // Search the guard
        restGuardMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + guard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guard.getId().intValue())));
    }
}

package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Communaute;
import com.mycompany.myapp.repository.CommunauteRepository;
import com.mycompany.myapp.repository.search.CommunauteSearchRepository;
import com.mycompany.myapp.service.dto.CommunauteDTO;
import com.mycompany.myapp.service.mapper.CommunauteMapper;
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
 * Integration tests for the {@link CommunauteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommunauteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communautes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/communautes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunauteRepository communauteRepository;

    @Autowired
    private CommunauteMapper communauteMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CommunauteSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommunauteSearchRepository mockCommunauteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunauteMockMvc;

    private Communaute communaute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Communaute createEntity(EntityManager em) {
        Communaute communaute = new Communaute().nom(DEFAULT_NOM);
        return communaute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Communaute createUpdatedEntity(EntityManager em) {
        Communaute communaute = new Communaute().nom(UPDATED_NOM);
        return communaute;
    }

    @BeforeEach
    public void initTest() {
        communaute = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunaute() throws Exception {
        int databaseSizeBeforeCreate = communauteRepository.findAll().size();
        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);
        restCommunauteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communauteDTO)))
            .andExpect(status().isCreated());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeCreate + 1);
        Communaute testCommunaute = communauteList.get(communauteList.size() - 1);
        assertThat(testCommunaute.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(1)).save(testCommunaute);
    }

    @Test
    @Transactional
    void createCommunauteWithExistingId() throws Exception {
        // Create the Communaute with an existing ID
        communaute.setId(1L);
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        int databaseSizeBeforeCreate = communauteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunauteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communauteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = communauteRepository.findAll().size();
        // set the field null
        communaute.setNom(null);

        // Create the Communaute, which fails.
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        restCommunauteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communauteDTO)))
            .andExpect(status().isBadRequest());

        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommunautes() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        // Get all the communauteList
        restCommunauteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communaute.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getCommunaute() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        // Get the communaute
        restCommunauteMockMvc
            .perform(get(ENTITY_API_URL_ID, communaute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communaute.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingCommunaute() throws Exception {
        // Get the communaute
        restCommunauteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunaute() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();

        // Update the communaute
        Communaute updatedCommunaute = communauteRepository.findById(communaute.getId()).get();
        // Disconnect from session so that the updates on updatedCommunaute are not directly saved in db
        em.detach(updatedCommunaute);
        updatedCommunaute.nom(UPDATED_NOM);
        CommunauteDTO communauteDTO = communauteMapper.toDto(updatedCommunaute);

        restCommunauteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communauteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);
        Communaute testCommunaute = communauteList.get(communauteList.size() - 1);
        assertThat(testCommunaute.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository).save(testCommunaute);
    }

    @Test
    @Transactional
    void putNonExistingCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communauteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communauteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void partialUpdateCommunauteWithPatch() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();

        // Update the communaute using partial update
        Communaute partialUpdatedCommunaute = new Communaute();
        partialUpdatedCommunaute.setId(communaute.getId());

        partialUpdatedCommunaute.nom(UPDATED_NOM);

        restCommunauteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunaute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunaute))
            )
            .andExpect(status().isOk());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);
        Communaute testCommunaute = communauteList.get(communauteList.size() - 1);
        assertThat(testCommunaute.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCommunauteWithPatch() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();

        // Update the communaute using partial update
        Communaute partialUpdatedCommunaute = new Communaute();
        partialUpdatedCommunaute.setId(communaute.getId());

        partialUpdatedCommunaute.nom(UPDATED_NOM);

        restCommunauteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunaute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunaute))
            )
            .andExpect(status().isOk());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);
        Communaute testCommunaute = communauteList.get(communauteList.size() - 1);
        assertThat(testCommunaute.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communauteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunaute() throws Exception {
        int databaseSizeBeforeUpdate = communauteRepository.findAll().size();
        communaute.setId(count.incrementAndGet());

        // Create the Communaute
        CommunauteDTO communauteDTO = communauteMapper.toDto(communaute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunauteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(communauteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Communaute in the database
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(0)).save(communaute);
    }

    @Test
    @Transactional
    void deleteCommunaute() throws Exception {
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);

        int databaseSizeBeforeDelete = communauteRepository.findAll().size();

        // Delete the communaute
        restCommunauteMockMvc
            .perform(delete(ENTITY_API_URL_ID, communaute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Communaute> communauteList = communauteRepository.findAll();
        assertThat(communauteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Communaute in Elasticsearch
        verify(mockCommunauteSearchRepository, times(1)).deleteById(communaute.getId());
    }

    @Test
    @Transactional
    void searchCommunaute() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        communauteRepository.saveAndFlush(communaute);
        when(mockCommunauteSearchRepository.search("id:" + communaute.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(communaute), PageRequest.of(0, 1), 1));

        // Search the communaute
        restCommunauteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communaute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communaute.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
}

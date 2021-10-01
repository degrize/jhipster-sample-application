package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Quartier;
import com.mycompany.myapp.repository.QuartierRepository;
import com.mycompany.myapp.repository.search.QuartierSearchRepository;
import com.mycompany.myapp.service.QuartierService;
import com.mycompany.myapp.service.dto.QuartierDTO;
import com.mycompany.myapp.service.mapper.QuartierMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link QuartierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuartierResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quartiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/quartiers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuartierRepository quartierRepository;

    @Mock
    private QuartierRepository quartierRepositoryMock;

    @Autowired
    private QuartierMapper quartierMapper;

    @Mock
    private QuartierService quartierServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.QuartierSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuartierSearchRepository mockQuartierSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuartierMockMvc;

    private Quartier quartier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createEntity(EntityManager em) {
        Quartier quartier = new Quartier().nom(DEFAULT_NOM);
        return quartier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quartier createUpdatedEntity(EntityManager em) {
        Quartier quartier = new Quartier().nom(UPDATED_NOM);
        return quartier;
    }

    @BeforeEach
    public void initTest() {
        quartier = createEntity(em);
    }

    @Test
    @Transactional
    void createQuartier() throws Exception {
        int databaseSizeBeforeCreate = quartierRepository.findAll().size();
        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);
        restQuartierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quartierDTO)))
            .andExpect(status().isCreated());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate + 1);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(1)).save(testQuartier);
    }

    @Test
    @Transactional
    void createQuartierWithExistingId() throws Exception {
        // Create the Quartier with an existing ID
        quartier.setId(1L);
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        int databaseSizeBeforeCreate = quartierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuartierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quartierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeCreate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartierRepository.findAll().size();
        // set the field null
        quartier.setNom(null);

        // Create the Quartier, which fails.
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        restQuartierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quartierDTO)))
            .andExpect(status().isBadRequest());

        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuartiers() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        // Get all the quartierList
        restQuartierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuartiersWithEagerRelationshipsIsEnabled() throws Exception {
        when(quartierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuartierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quartierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuartiersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quartierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuartierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quartierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        // Get the quartier
        restQuartierMockMvc
            .perform(get(ENTITY_API_URL_ID, quartier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quartier.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingQuartier() throws Exception {
        // Get the quartier
        restQuartierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier
        Quartier updatedQuartier = quartierRepository.findById(quartier.getId()).get();
        // Disconnect from session so that the updates on updatedQuartier are not directly saved in db
        em.detach(updatedQuartier);
        updatedQuartier.nom(UPDATED_NOM);
        QuartierDTO quartierDTO = quartierMapper.toDto(updatedQuartier);

        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quartierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository).save(testQuartier);
    }

    @Test
    @Transactional
    void putNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quartierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quartierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void partialUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nom(UPDATED_NOM);

        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateQuartierWithPatch() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();

        // Update the quartier using partial update
        Quartier partialUpdatedQuartier = new Quartier();
        partialUpdatedQuartier.setId(quartier.getId());

        partialUpdatedQuartier.nom(UPDATED_NOM);

        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuartier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuartier))
            )
            .andExpect(status().isOk());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);
        Quartier testQuartier = quartierList.get(quartierList.size() - 1);
        assertThat(testQuartier.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quartierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuartier() throws Exception {
        int databaseSizeBeforeUpdate = quartierRepository.findAll().size();
        quartier.setId(count.incrementAndGet());

        // Create the Quartier
        QuartierDTO quartierDTO = quartierMapper.toDto(quartier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quartierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quartier in the database
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(0)).save(quartier);
    }

    @Test
    @Transactional
    void deleteQuartier() throws Exception {
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);

        int databaseSizeBeforeDelete = quartierRepository.findAll().size();

        // Delete the quartier
        restQuartierMockMvc
            .perform(delete(ENTITY_API_URL_ID, quartier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quartier> quartierList = quartierRepository.findAll();
        assertThat(quartierList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Quartier in Elasticsearch
        verify(mockQuartierSearchRepository, times(1)).deleteById(quartier.getId());
    }

    @Test
    @Transactional
    void searchQuartier() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        quartierRepository.saveAndFlush(quartier);
        when(mockQuartierSearchRepository.search("id:" + quartier.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(quartier), PageRequest.of(0, 1), 1));

        // Search the quartier
        restQuartierMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + quartier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
}

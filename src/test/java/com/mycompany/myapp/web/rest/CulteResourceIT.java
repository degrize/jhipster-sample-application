package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Culte;
import com.mycompany.myapp.repository.CulteRepository;
import com.mycompany.myapp.repository.search.CulteSearchRepository;
import com.mycompany.myapp.service.CulteService;
import com.mycompany.myapp.service.dto.CulteDTO;
import com.mycompany.myapp.service.mapper.CulteMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CulteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CulteResourceIT {

    private static final String DEFAULT_THEME = "AAAAAAAAAA";
    private static final String UPDATED_THEME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/cultes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/cultes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CulteRepository culteRepository;

    @Mock
    private CulteRepository culteRepositoryMock;

    @Autowired
    private CulteMapper culteMapper;

    @Mock
    private CulteService culteServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CulteSearchRepositoryMockConfiguration
     */
    @Autowired
    private CulteSearchRepository mockCulteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCulteMockMvc;

    private Culte culte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Culte createEntity(EntityManager em) {
        Culte culte = new Culte().theme(DEFAULT_THEME).date(DEFAULT_DATE);
        return culte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Culte createUpdatedEntity(EntityManager em) {
        Culte culte = new Culte().theme(UPDATED_THEME).date(UPDATED_DATE);
        return culte;
    }

    @BeforeEach
    public void initTest() {
        culte = createEntity(em);
    }

    @Test
    @Transactional
    void createCulte() throws Exception {
        int databaseSizeBeforeCreate = culteRepository.findAll().size();
        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);
        restCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isCreated());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeCreate + 1);
        Culte testCulte = culteList.get(culteList.size() - 1);
        assertThat(testCulte.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testCulte.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(1)).save(testCulte);
    }

    @Test
    @Transactional
    void createCulteWithExistingId() throws Exception {
        // Create the Culte with an existing ID
        culte.setId(1L);
        CulteDTO culteDTO = culteMapper.toDto(culte);

        int databaseSizeBeforeCreate = culteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void checkThemeIsRequired() throws Exception {
        int databaseSizeBeforeTest = culteRepository.findAll().size();
        // set the field null
        culte.setTheme(null);

        // Create the Culte, which fails.
        CulteDTO culteDTO = culteMapper.toDto(culte);

        restCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isBadRequest());

        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = culteRepository.findAll().size();
        // set the field null
        culte.setDate(null);

        // Create the Culte, which fails.
        CulteDTO culteDTO = culteMapper.toDto(culte);

        restCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isBadRequest());

        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCultes() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        // Get all the culteList
        restCulteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(culte.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCultesWithEagerRelationshipsIsEnabled() throws Exception {
        when(culteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCulteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(culteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCultesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(culteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCulteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(culteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCulte() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        // Get the culte
        restCulteMockMvc
            .perform(get(ENTITY_API_URL_ID, culte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(culte.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCulte() throws Exception {
        // Get the culte
        restCulteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCulte() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        int databaseSizeBeforeUpdate = culteRepository.findAll().size();

        // Update the culte
        Culte updatedCulte = culteRepository.findById(culte.getId()).get();
        // Disconnect from session so that the updates on updatedCulte are not directly saved in db
        em.detach(updatedCulte);
        updatedCulte.theme(UPDATED_THEME).date(UPDATED_DATE);
        CulteDTO culteDTO = culteMapper.toDto(updatedCulte);

        restCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, culteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(culteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);
        Culte testCulte = culteList.get(culteList.size() - 1);
        assertThat(testCulte.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testCulte.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository).save(testCulte);
    }

    @Test
    @Transactional
    void putNonExistingCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, culteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(culteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void putWithIdMismatchCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(culteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void partialUpdateCulteWithPatch() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        int databaseSizeBeforeUpdate = culteRepository.findAll().size();

        // Update the culte using partial update
        Culte partialUpdatedCulte = new Culte();
        partialUpdatedCulte.setId(culte.getId());

        partialUpdatedCulte.theme(UPDATED_THEME);

        restCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCulte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCulte))
            )
            .andExpect(status().isOk());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);
        Culte testCulte = culteList.get(culteList.size() - 1);
        assertThat(testCulte.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testCulte.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCulteWithPatch() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        int databaseSizeBeforeUpdate = culteRepository.findAll().size();

        // Update the culte using partial update
        Culte partialUpdatedCulte = new Culte();
        partialUpdatedCulte.setId(culte.getId());

        partialUpdatedCulte.theme(UPDATED_THEME).date(UPDATED_DATE);

        restCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCulte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCulte))
            )
            .andExpect(status().isOk());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);
        Culte testCulte = culteList.get(culteList.size() - 1);
        assertThat(testCulte.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testCulte.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, culteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(culteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(culteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCulte() throws Exception {
        int databaseSizeBeforeUpdate = culteRepository.findAll().size();
        culte.setId(count.incrementAndGet());

        // Create the Culte
        CulteDTO culteDTO = culteMapper.toDto(culte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCulteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(culteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Culte in the database
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(0)).save(culte);
    }

    @Test
    @Transactional
    void deleteCulte() throws Exception {
        // Initialize the database
        culteRepository.saveAndFlush(culte);

        int databaseSizeBeforeDelete = culteRepository.findAll().size();

        // Delete the culte
        restCulteMockMvc
            .perform(delete(ENTITY_API_URL_ID, culte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Culte> culteList = culteRepository.findAll();
        assertThat(culteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Culte in Elasticsearch
        verify(mockCulteSearchRepository, times(1)).deleteById(culte.getId());
    }

    @Test
    @Transactional
    void searchCulte() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        culteRepository.saveAndFlush(culte);
        when(mockCulteSearchRepository.search("id:" + culte.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(culte), PageRequest.of(0, 1), 1));

        // Search the culte
        restCulteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + culte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(culte.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}

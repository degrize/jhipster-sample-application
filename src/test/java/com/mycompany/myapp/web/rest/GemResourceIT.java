package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gem;
import com.mycompany.myapp.repository.GemRepository;
import com.mycompany.myapp.repository.search.GemSearchRepository;
import com.mycompany.myapp.service.GemService;
import com.mycompany.myapp.service.dto.GemDTO;
import com.mycompany.myapp.service.mapper.GemMapper;
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
 * Integration tests for the {@link GemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GemResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/gems";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GemRepository gemRepository;

    @Mock
    private GemRepository gemRepositoryMock;

    @Autowired
    private GemMapper gemMapper;

    @Mock
    private GemService gemServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.GemSearchRepositoryMockConfiguration
     */
    @Autowired
    private GemSearchRepository mockGemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGemMockMvc;

    private Gem gem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gem createEntity(EntityManager em) {
        Gem gem = new Gem().nom(DEFAULT_NOM).annee(DEFAULT_ANNEE);
        return gem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gem createUpdatedEntity(EntityManager em) {
        Gem gem = new Gem().nom(UPDATED_NOM).annee(UPDATED_ANNEE);
        return gem;
    }

    @BeforeEach
    public void initTest() {
        gem = createEntity(em);
    }

    @Test
    @Transactional
    void createGem() throws Exception {
        int databaseSizeBeforeCreate = gemRepository.findAll().size();
        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);
        restGemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isCreated());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeCreate + 1);
        Gem testGem = gemList.get(gemList.size() - 1);
        assertThat(testGem.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testGem.getAnnee()).isEqualTo(DEFAULT_ANNEE);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(1)).save(testGem);
    }

    @Test
    @Transactional
    void createGemWithExistingId() throws Exception {
        // Create the Gem with an existing ID
        gem.setId(1L);
        GemDTO gemDTO = gemMapper.toDto(gem);

        int databaseSizeBeforeCreate = gemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeCreate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = gemRepository.findAll().size();
        // set the field null
        gem.setNom(null);

        // Create the Gem, which fails.
        GemDTO gemDTO = gemMapper.toDto(gem);

        restGemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isBadRequest());

        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gemRepository.findAll().size();
        // set the field null
        gem.setAnnee(null);

        // Create the Gem, which fails.
        GemDTO gemDTO = gemMapper.toDto(gem);

        restGemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isBadRequest());

        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGems() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        // Get all the gemList
        restGemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gem.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGem() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        // Get the gem
        restGemMockMvc
            .perform(get(ENTITY_API_URL_ID, gem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gem.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE));
    }

    @Test
    @Transactional
    void getNonExistingGem() throws Exception {
        // Get the gem
        restGemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGem() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        int databaseSizeBeforeUpdate = gemRepository.findAll().size();

        // Update the gem
        Gem updatedGem = gemRepository.findById(gem.getId()).get();
        // Disconnect from session so that the updates on updatedGem are not directly saved in db
        em.detach(updatedGem);
        updatedGem.nom(UPDATED_NOM).annee(UPDATED_ANNEE);
        GemDTO gemDTO = gemMapper.toDto(updatedGem);

        restGemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);
        Gem testGem = gemList.get(gemList.size() - 1);
        assertThat(testGem.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGem.getAnnee()).isEqualTo(UPDATED_ANNEE);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository).save(testGem);
    }

    @Test
    @Transactional
    void putNonExistingGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void putWithIdMismatchGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void partialUpdateGemWithPatch() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        int databaseSizeBeforeUpdate = gemRepository.findAll().size();

        // Update the gem using partial update
        Gem partialUpdatedGem = new Gem();
        partialUpdatedGem.setId(gem.getId());

        partialUpdatedGem.nom(UPDATED_NOM).annee(UPDATED_ANNEE);

        restGemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGem))
            )
            .andExpect(status().isOk());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);
        Gem testGem = gemList.get(gemList.size() - 1);
        assertThat(testGem.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGem.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateGemWithPatch() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        int databaseSizeBeforeUpdate = gemRepository.findAll().size();

        // Update the gem using partial update
        Gem partialUpdatedGem = new Gem();
        partialUpdatedGem.setId(gem.getId());

        partialUpdatedGem.nom(UPDATED_NOM).annee(UPDATED_ANNEE);

        restGemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGem))
            )
            .andExpect(status().isOk());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);
        Gem testGem = gemList.get(gemList.size() - 1);
        assertThat(testGem.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGem.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGem() throws Exception {
        int databaseSizeBeforeUpdate = gemRepository.findAll().size();
        gem.setId(count.incrementAndGet());

        // Create the Gem
        GemDTO gemDTO = gemMapper.toDto(gem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gem in the database
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(0)).save(gem);
    }

    @Test
    @Transactional
    void deleteGem() throws Exception {
        // Initialize the database
        gemRepository.saveAndFlush(gem);

        int databaseSizeBeforeDelete = gemRepository.findAll().size();

        // Delete the gem
        restGemMockMvc.perform(delete(ENTITY_API_URL_ID, gem.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gem> gemList = gemRepository.findAll();
        assertThat(gemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Gem in Elasticsearch
        verify(mockGemSearchRepository, times(1)).deleteById(gem.getId());
    }

    @Test
    @Transactional
    void searchGem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        gemRepository.saveAndFlush(gem);
        when(mockGemSearchRepository.search("id:" + gem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gem), PageRequest.of(0, 1), 1));

        // Search the gem
        restGemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + gem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gem.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }
}

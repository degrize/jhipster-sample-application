package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Besoin;
import com.mycompany.myapp.domain.enumeration.BesoinType;
import com.mycompany.myapp.repository.BesoinRepository;
import com.mycompany.myapp.repository.search.BesoinSearchRepository;
import com.mycompany.myapp.service.dto.BesoinDTO;
import com.mycompany.myapp.service.mapper.BesoinMapper;
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
 * Integration tests for the {@link BesoinResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BesoinResourceIT {

    private static final BesoinType DEFAULT_BESOIN = BesoinType.DELIVRANCE;
    private static final BesoinType UPDATED_BESOIN = BesoinType.PRIERE;

    private static final String ENTITY_API_URL = "/api/besoins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/besoins";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BesoinRepository besoinRepository;

    @Autowired
    private BesoinMapper besoinMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.BesoinSearchRepositoryMockConfiguration
     */
    @Autowired
    private BesoinSearchRepository mockBesoinSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBesoinMockMvc;

    private Besoin besoin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Besoin createEntity(EntityManager em) {
        Besoin besoin = new Besoin().besoin(DEFAULT_BESOIN);
        return besoin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Besoin createUpdatedEntity(EntityManager em) {
        Besoin besoin = new Besoin().besoin(UPDATED_BESOIN);
        return besoin;
    }

    @BeforeEach
    public void initTest() {
        besoin = createEntity(em);
    }

    @Test
    @Transactional
    void createBesoin() throws Exception {
        int databaseSizeBeforeCreate = besoinRepository.findAll().size();
        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);
        restBesoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinDTO)))
            .andExpect(status().isCreated());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeCreate + 1);
        Besoin testBesoin = besoinList.get(besoinList.size() - 1);
        assertThat(testBesoin.getBesoin()).isEqualTo(DEFAULT_BESOIN);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(1)).save(testBesoin);
    }

    @Test
    @Transactional
    void createBesoinWithExistingId() throws Exception {
        // Create the Besoin with an existing ID
        besoin.setId(1L);
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        int databaseSizeBeforeCreate = besoinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBesoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeCreate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void checkBesoinIsRequired() throws Exception {
        int databaseSizeBeforeTest = besoinRepository.findAll().size();
        // set the field null
        besoin.setBesoin(null);

        // Create the Besoin, which fails.
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        restBesoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinDTO)))
            .andExpect(status().isBadRequest());

        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBesoins() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        // Get all the besoinList
        restBesoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(besoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].besoin").value(hasItem(DEFAULT_BESOIN.toString())));
    }

    @Test
    @Transactional
    void getBesoin() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        // Get the besoin
        restBesoinMockMvc
            .perform(get(ENTITY_API_URL_ID, besoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(besoin.getId().intValue()))
            .andExpect(jsonPath("$.besoin").value(DEFAULT_BESOIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBesoin() throws Exception {
        // Get the besoin
        restBesoinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBesoin() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();

        // Update the besoin
        Besoin updatedBesoin = besoinRepository.findById(besoin.getId()).get();
        // Disconnect from session so that the updates on updatedBesoin are not directly saved in db
        em.detach(updatedBesoin);
        updatedBesoin.besoin(UPDATED_BESOIN);
        BesoinDTO besoinDTO = besoinMapper.toDto(updatedBesoin);

        restBesoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, besoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isOk());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);
        Besoin testBesoin = besoinList.get(besoinList.size() - 1);
        assertThat(testBesoin.getBesoin()).isEqualTo(UPDATED_BESOIN);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository).save(testBesoin);
    }

    @Test
    @Transactional
    void putNonExistingBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, besoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void putWithIdMismatchBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void partialUpdateBesoinWithPatch() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();

        // Update the besoin using partial update
        Besoin partialUpdatedBesoin = new Besoin();
        partialUpdatedBesoin.setId(besoin.getId());

        restBesoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBesoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBesoin))
            )
            .andExpect(status().isOk());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);
        Besoin testBesoin = besoinList.get(besoinList.size() - 1);
        assertThat(testBesoin.getBesoin()).isEqualTo(DEFAULT_BESOIN);
    }

    @Test
    @Transactional
    void fullUpdateBesoinWithPatch() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();

        // Update the besoin using partial update
        Besoin partialUpdatedBesoin = new Besoin();
        partialUpdatedBesoin.setId(besoin.getId());

        partialUpdatedBesoin.besoin(UPDATED_BESOIN);

        restBesoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBesoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBesoin))
            )
            .andExpect(status().isOk());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);
        Besoin testBesoin = besoinList.get(besoinList.size() - 1);
        assertThat(testBesoin.getBesoin()).isEqualTo(UPDATED_BESOIN);
    }

    @Test
    @Transactional
    void patchNonExistingBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, besoinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBesoin() throws Exception {
        int databaseSizeBeforeUpdate = besoinRepository.findAll().size();
        besoin.setId(count.incrementAndGet());

        // Create the Besoin
        BesoinDTO besoinDTO = besoinMapper.toDto(besoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(besoinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Besoin in the database
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(0)).save(besoin);
    }

    @Test
    @Transactional
    void deleteBesoin() throws Exception {
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);

        int databaseSizeBeforeDelete = besoinRepository.findAll().size();

        // Delete the besoin
        restBesoinMockMvc
            .perform(delete(ENTITY_API_URL_ID, besoin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Besoin> besoinList = besoinRepository.findAll();
        assertThat(besoinList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Besoin in Elasticsearch
        verify(mockBesoinSearchRepository, times(1)).deleteById(besoin.getId());
    }

    @Test
    @Transactional
    void searchBesoin() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        besoinRepository.saveAndFlush(besoin);
        when(mockBesoinSearchRepository.search("id:" + besoin.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(besoin), PageRequest.of(0, 1), 1));

        // Search the besoin
        restBesoinMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + besoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(besoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].besoin").value(hasItem(DEFAULT_BESOIN.toString())));
    }
}

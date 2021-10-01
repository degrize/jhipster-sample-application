package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FrereQuiInvite;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.repository.FrereQuiInviteRepository;
import com.mycompany.myapp.repository.search.FrereQuiInviteSearchRepository;
import com.mycompany.myapp.service.FrereQuiInviteService;
import com.mycompany.myapp.service.dto.FrereQuiInviteDTO;
import com.mycompany.myapp.service.mapper.FrereQuiInviteMapper;
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
 * Integration tests for the {@link FrereQuiInviteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FrereQuiInviteResourceIT {

    private static final String DEFAULT_NOM_COMPLET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.F;
    private static final Sexe UPDATED_SEXE = Sexe.M;

    private static final String ENTITY_API_URL = "/api/frere-qui-invites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/frere-qui-invites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrereQuiInviteRepository frereQuiInviteRepository;

    @Mock
    private FrereQuiInviteRepository frereQuiInviteRepositoryMock;

    @Autowired
    private FrereQuiInviteMapper frereQuiInviteMapper;

    @Mock
    private FrereQuiInviteService frereQuiInviteServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.FrereQuiInviteSearchRepositoryMockConfiguration
     */
    @Autowired
    private FrereQuiInviteSearchRepository mockFrereQuiInviteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrereQuiInviteMockMvc;

    private FrereQuiInvite frereQuiInvite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrereQuiInvite createEntity(EntityManager em) {
        FrereQuiInvite frereQuiInvite = new FrereQuiInvite().nomComplet(DEFAULT_NOM_COMPLET).contact(DEFAULT_CONTACT).sexe(DEFAULT_SEXE);
        return frereQuiInvite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrereQuiInvite createUpdatedEntity(EntityManager em) {
        FrereQuiInvite frereQuiInvite = new FrereQuiInvite().nomComplet(UPDATED_NOM_COMPLET).contact(UPDATED_CONTACT).sexe(UPDATED_SEXE);
        return frereQuiInvite;
    }

    @BeforeEach
    public void initTest() {
        frereQuiInvite = createEntity(em);
    }

    @Test
    @Transactional
    void createFrereQuiInvite() throws Exception {
        int databaseSizeBeforeCreate = frereQuiInviteRepository.findAll().size();
        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);
        restFrereQuiInviteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeCreate + 1);
        FrereQuiInvite testFrereQuiInvite = frereQuiInviteList.get(frereQuiInviteList.size() - 1);
        assertThat(testFrereQuiInvite.getNomComplet()).isEqualTo(DEFAULT_NOM_COMPLET);
        assertThat(testFrereQuiInvite.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testFrereQuiInvite.getSexe()).isEqualTo(DEFAULT_SEXE);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(1)).save(testFrereQuiInvite);
    }

    @Test
    @Transactional
    void createFrereQuiInviteWithExistingId() throws Exception {
        // Create the FrereQuiInvite with an existing ID
        frereQuiInvite.setId(1L);
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        int databaseSizeBeforeCreate = frereQuiInviteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrereQuiInviteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeCreate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void checkNomCompletIsRequired() throws Exception {
        int databaseSizeBeforeTest = frereQuiInviteRepository.findAll().size();
        // set the field null
        frereQuiInvite.setNomComplet(null);

        // Create the FrereQuiInvite, which fails.
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        restFrereQuiInviteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFrereQuiInvites() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        // Get all the frereQuiInviteList
        restFrereQuiInviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frereQuiInvite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFrereQuiInvitesWithEagerRelationshipsIsEnabled() throws Exception {
        when(frereQuiInviteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFrereQuiInviteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(frereQuiInviteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFrereQuiInvitesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(frereQuiInviteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFrereQuiInviteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(frereQuiInviteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFrereQuiInvite() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        // Get the frereQuiInvite
        restFrereQuiInviteMockMvc
            .perform(get(ENTITY_API_URL_ID, frereQuiInvite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frereQuiInvite.getId().intValue()))
            .andExpect(jsonPath("$.nomComplet").value(DEFAULT_NOM_COMPLET))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFrereQuiInvite() throws Exception {
        // Get the frereQuiInvite
        restFrereQuiInviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFrereQuiInvite() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();

        // Update the frereQuiInvite
        FrereQuiInvite updatedFrereQuiInvite = frereQuiInviteRepository.findById(frereQuiInvite.getId()).get();
        // Disconnect from session so that the updates on updatedFrereQuiInvite are not directly saved in db
        em.detach(updatedFrereQuiInvite);
        updatedFrereQuiInvite.nomComplet(UPDATED_NOM_COMPLET).contact(UPDATED_CONTACT).sexe(UPDATED_SEXE);
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(updatedFrereQuiInvite);

        restFrereQuiInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frereQuiInviteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isOk());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);
        FrereQuiInvite testFrereQuiInvite = frereQuiInviteList.get(frereQuiInviteList.size() - 1);
        assertThat(testFrereQuiInvite.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testFrereQuiInvite.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testFrereQuiInvite.getSexe()).isEqualTo(UPDATED_SEXE);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository).save(testFrereQuiInvite);
    }

    @Test
    @Transactional
    void putNonExistingFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frereQuiInviteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void partialUpdateFrereQuiInviteWithPatch() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();

        // Update the frereQuiInvite using partial update
        FrereQuiInvite partialUpdatedFrereQuiInvite = new FrereQuiInvite();
        partialUpdatedFrereQuiInvite.setId(frereQuiInvite.getId());

        partialUpdatedFrereQuiInvite.nomComplet(UPDATED_NOM_COMPLET).contact(UPDATED_CONTACT).sexe(UPDATED_SEXE);

        restFrereQuiInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrereQuiInvite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrereQuiInvite))
            )
            .andExpect(status().isOk());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);
        FrereQuiInvite testFrereQuiInvite = frereQuiInviteList.get(frereQuiInviteList.size() - 1);
        assertThat(testFrereQuiInvite.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testFrereQuiInvite.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testFrereQuiInvite.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    void fullUpdateFrereQuiInviteWithPatch() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();

        // Update the frereQuiInvite using partial update
        FrereQuiInvite partialUpdatedFrereQuiInvite = new FrereQuiInvite();
        partialUpdatedFrereQuiInvite.setId(frereQuiInvite.getId());

        partialUpdatedFrereQuiInvite.nomComplet(UPDATED_NOM_COMPLET).contact(UPDATED_CONTACT).sexe(UPDATED_SEXE);

        restFrereQuiInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrereQuiInvite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrereQuiInvite))
            )
            .andExpect(status().isOk());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);
        FrereQuiInvite testFrereQuiInvite = frereQuiInviteList.get(frereQuiInviteList.size() - 1);
        assertThat(testFrereQuiInvite.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testFrereQuiInvite.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testFrereQuiInvite.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    void patchNonExistingFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frereQuiInviteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrereQuiInvite() throws Exception {
        int databaseSizeBeforeUpdate = frereQuiInviteRepository.findAll().size();
        frereQuiInvite.setId(count.incrementAndGet());

        // Create the FrereQuiInvite
        FrereQuiInviteDTO frereQuiInviteDTO = frereQuiInviteMapper.toDto(frereQuiInvite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrereQuiInviteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frereQuiInviteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrereQuiInvite in the database
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(0)).save(frereQuiInvite);
    }

    @Test
    @Transactional
    void deleteFrereQuiInvite() throws Exception {
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);

        int databaseSizeBeforeDelete = frereQuiInviteRepository.findAll().size();

        // Delete the frereQuiInvite
        restFrereQuiInviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, frereQuiInvite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FrereQuiInvite> frereQuiInviteList = frereQuiInviteRepository.findAll();
        assertThat(frereQuiInviteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FrereQuiInvite in Elasticsearch
        verify(mockFrereQuiInviteSearchRepository, times(1)).deleteById(frereQuiInvite.getId());
    }

    @Test
    @Transactional
    void searchFrereQuiInvite() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        frereQuiInviteRepository.saveAndFlush(frereQuiInvite);
        when(mockFrereQuiInviteSearchRepository.search("id:" + frereQuiInvite.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(frereQuiInvite), PageRequest.of(0, 1), 1));

        // Search the frereQuiInvite
        restFrereQuiInviteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + frereQuiInvite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frereQuiInvite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }
}

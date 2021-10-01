package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Departement;
import com.mycompany.myapp.repository.DepartementRepository;
import com.mycompany.myapp.repository.search.DepartementSearchRepository;
import com.mycompany.myapp.service.DepartementService;
import com.mycompany.myapp.service.dto.DepartementDTO;
import com.mycompany.myapp.service.mapper.DepartementMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepartementResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESPONSABLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_VIDEO_INTRODUCTION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO_INTRODUCTION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTACT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COULEUR_1 = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR_1 = "BBBBBBBBBB";

    private static final String DEFAULT_COULEUR_2 = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/departements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartementRepository departementRepository;

    @Mock
    private DepartementRepository departementRepositoryMock;

    @Autowired
    private DepartementMapper departementMapper;

    @Mock
    private DepartementService departementServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DepartementSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartementSearchRepository mockDepartementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement()
            .nom(DEFAULT_NOM)
            .shortName(DEFAULT_SHORT_NAME)
            .nomResponsable(DEFAULT_NOM_RESPONSABLE)
            .videoIntroduction(DEFAULT_VIDEO_INTRODUCTION)
            .videoIntroductionContentType(DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE)
            .contactResponsable(DEFAULT_CONTACT_RESPONSABLE)
            .description(DEFAULT_DESCRIPTION)
            .couleur1(DEFAULT_COULEUR_1)
            .couleur2(DEFAULT_COULEUR_2);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement()
            .nom(UPDATED_NOM)
            .shortName(UPDATED_SHORT_NAME)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .videoIntroduction(UPDATED_VIDEO_INTRODUCTION)
            .videoIntroductionContentType(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE)
            .contactResponsable(UPDATED_CONTACT_RESPONSABLE)
            .description(UPDATED_DESCRIPTION)
            .couleur1(UPDATED_COULEUR_1)
            .couleur2(UPDATED_COULEUR_2);
        return departement;
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();
        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);
        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDepartement.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testDepartement.getNomResponsable()).isEqualTo(DEFAULT_NOM_RESPONSABLE);
        assertThat(testDepartement.getVideoIntroduction()).isEqualTo(DEFAULT_VIDEO_INTRODUCTION);
        assertThat(testDepartement.getVideoIntroductionContentType()).isEqualTo(DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE);
        assertThat(testDepartement.getContactResponsable()).isEqualTo(DEFAULT_CONTACT_RESPONSABLE);
        assertThat(testDepartement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartement.getCouleur1()).isEqualTo(DEFAULT_COULEUR_1);
        assertThat(testDepartement.getCouleur2()).isEqualTo(DEFAULT_COULEUR_2);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(1)).save(testDepartement);
    }

    @Test
    @Transactional
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = departementRepository.findAll().size();
        // set the field null
        departement.setNom(null);

        // Create the Departement, which fails.
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departementRepository.findAll().size();
        // set the field null
        departement.setShortName(null);

        // Create the Departement, which fails.
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].nomResponsable").value(hasItem(DEFAULT_NOM_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].videoIntroductionContentType").value(hasItem(DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoIntroduction").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_INTRODUCTION))))
            .andExpect(jsonPath("$.[*].contactResponsable").value(hasItem(DEFAULT_CONTACT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couleur1").value(hasItem(DEFAULT_COULEUR_1)))
            .andExpect(jsonPath("$.[*].couleur2").value(hasItem(DEFAULT_COULEUR_2)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL_ID, departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.nomResponsable").value(DEFAULT_NOM_RESPONSABLE))
            .andExpect(jsonPath("$.videoIntroductionContentType").value(DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE))
            .andExpect(jsonPath("$.videoIntroduction").value(Base64Utils.encodeToString(DEFAULT_VIDEO_INTRODUCTION)))
            .andExpect(jsonPath("$.contactResponsable").value(DEFAULT_CONTACT_RESPONSABLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.couleur1").value(DEFAULT_COULEUR_1))
            .andExpect(jsonPath("$.couleur2").value(DEFAULT_COULEUR_2));
    }

    @Test
    @Transactional
    void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).get();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement
            .nom(UPDATED_NOM)
            .shortName(UPDATED_SHORT_NAME)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .videoIntroduction(UPDATED_VIDEO_INTRODUCTION)
            .videoIntroductionContentType(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE)
            .contactResponsable(UPDATED_CONTACT_RESPONSABLE)
            .description(UPDATED_DESCRIPTION)
            .couleur1(UPDATED_COULEUR_1)
            .couleur2(UPDATED_COULEUR_2);
        DepartementDTO departementDTO = departementMapper.toDto(updatedDepartement);

        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepartement.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDepartement.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testDepartement.getVideoIntroduction()).isEqualTo(UPDATED_VIDEO_INTRODUCTION);
        assertThat(testDepartement.getVideoIntroductionContentType()).isEqualTo(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE);
        assertThat(testDepartement.getContactResponsable()).isEqualTo(UPDATED_CONTACT_RESPONSABLE);
        assertThat(testDepartement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartement.getCouleur1()).isEqualTo(UPDATED_COULEUR_1);
        assertThat(testDepartement.getCouleur2()).isEqualTo(UPDATED_COULEUR_2);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository).save(testDepartement);
    }

    @Test
    @Transactional
    void putNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement
            .nom(UPDATED_NOM)
            .shortName(UPDATED_SHORT_NAME)
            .videoIntroduction(UPDATED_VIDEO_INTRODUCTION)
            .videoIntroductionContentType(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE)
            .contactResponsable(UPDATED_CONTACT_RESPONSABLE)
            .couleur2(UPDATED_COULEUR_2);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepartement.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDepartement.getNomResponsable()).isEqualTo(DEFAULT_NOM_RESPONSABLE);
        assertThat(testDepartement.getVideoIntroduction()).isEqualTo(UPDATED_VIDEO_INTRODUCTION);
        assertThat(testDepartement.getVideoIntroductionContentType()).isEqualTo(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE);
        assertThat(testDepartement.getContactResponsable()).isEqualTo(UPDATED_CONTACT_RESPONSABLE);
        assertThat(testDepartement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartement.getCouleur1()).isEqualTo(DEFAULT_COULEUR_1);
        assertThat(testDepartement.getCouleur2()).isEqualTo(UPDATED_COULEUR_2);
    }

    @Test
    @Transactional
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement
            .nom(UPDATED_NOM)
            .shortName(UPDATED_SHORT_NAME)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .videoIntroduction(UPDATED_VIDEO_INTRODUCTION)
            .videoIntroductionContentType(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE)
            .contactResponsable(UPDATED_CONTACT_RESPONSABLE)
            .description(UPDATED_DESCRIPTION)
            .couleur1(UPDATED_COULEUR_1)
            .couleur2(UPDATED_COULEUR_2);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepartement.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDepartement.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testDepartement.getVideoIntroduction()).isEqualTo(UPDATED_VIDEO_INTRODUCTION);
        assertThat(testDepartement.getVideoIntroductionContentType()).isEqualTo(UPDATED_VIDEO_INTRODUCTION_CONTENT_TYPE);
        assertThat(testDepartement.getContactResponsable()).isEqualTo(UPDATED_CONTACT_RESPONSABLE);
        assertThat(testDepartement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartement.getCouleur1()).isEqualTo(UPDATED_COULEUR_1);
        assertThat(testDepartement.getCouleur2()).isEqualTo(UPDATED_COULEUR_2);
    }

    @Test
    @Transactional
    void patchNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(0)).save(departement);
    }

    @Test
    @Transactional
    void deleteDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeDelete = departementRepository.findAll().size();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, departement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Departement in Elasticsearch
        verify(mockDepartementSearchRepository, times(1)).deleteById(departement.getId());
    }

    @Test
    @Transactional
    void searchDepartement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        departementRepository.saveAndFlush(departement);
        when(mockDepartementSearchRepository.search("id:" + departement.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(departement), PageRequest.of(0, 1), 1));

        // Search the departement
        restDepartementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].nomResponsable").value(hasItem(DEFAULT_NOM_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].videoIntroductionContentType").value(hasItem(DEFAULT_VIDEO_INTRODUCTION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoIntroduction").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_INTRODUCTION))))
            .andExpect(jsonPath("$.[*].contactResponsable").value(hasItem(DEFAULT_CONTACT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couleur1").value(hasItem(DEFAULT_COULEUR_1)))
            .andExpect(jsonPath("$.[*].couleur2").value(hasItem(DEFAULT_COULEUR_2)));
    }
}

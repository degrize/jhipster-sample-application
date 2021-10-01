package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Nouveau;
import com.mycompany.myapp.domain.enumeration.CanalInvitation;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.SituationMatrimoniale;
import com.mycompany.myapp.repository.NouveauRepository;
import com.mycompany.myapp.repository.search.NouveauSearchRepository;
import com.mycompany.myapp.service.NouveauService;
import com.mycompany.myapp.service.dto.NouveauDTO;
import com.mycompany.myapp.service.mapper.NouveauMapper;
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
 * Integration tests for the {@link NouveauResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NouveauResourceIT {

    private static final String DEFAULT_NOM_COMPLET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_TRANCHE_AGE = "AAAAAAAAAA";
    private static final String UPDATED_TRANCHE_AGE = "BBBBBBBBBB";

    private static final SituationMatrimoniale DEFAULT_SITUATION_MATRIMONIALE = SituationMatrimoniale.SEUL;
    private static final SituationMatrimoniale UPDATED_SITUATION_MATRIMONIALE = SituationMatrimoniale.FIANCE;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_IMPRESSIONS_DU_CULTE = "AAAAAAAAAA";
    private static final String UPDATED_IMPRESSIONS_DU_CULTE = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.F;
    private static final Sexe UPDATED_SEXE = Sexe.M;

    private static final CanalInvitation DEFAULT_INVITE_PAR = CanalInvitation.RADIO;
    private static final CanalInvitation UPDATED_INVITE_PAR = CanalInvitation.INTERNET;

    private static final String ENTITY_API_URL = "/api/nouveaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nouveaus";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NouveauRepository nouveauRepository;

    @Mock
    private NouveauRepository nouveauRepositoryMock;

    @Autowired
    private NouveauMapper nouveauMapper;

    @Mock
    private NouveauService nouveauServiceMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.NouveauSearchRepositoryMockConfiguration
     */
    @Autowired
    private NouveauSearchRepository mockNouveauSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNouveauMockMvc;

    private Nouveau nouveau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nouveau createEntity(EntityManager em) {
        Nouveau nouveau = new Nouveau()
            .nomComplet(DEFAULT_NOM_COMPLET)
            .contact(DEFAULT_CONTACT)
            .trancheAge(DEFAULT_TRANCHE_AGE)
            .situationMatrimoniale(DEFAULT_SITUATION_MATRIMONIALE)
            .date(DEFAULT_DATE)
            .impressionsDuCulte(DEFAULT_IMPRESSIONS_DU_CULTE)
            .sexe(DEFAULT_SEXE)
            .invitePar(DEFAULT_INVITE_PAR);
        return nouveau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nouveau createUpdatedEntity(EntityManager em) {
        Nouveau nouveau = new Nouveau()
            .nomComplet(UPDATED_NOM_COMPLET)
            .contact(UPDATED_CONTACT)
            .trancheAge(UPDATED_TRANCHE_AGE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .date(UPDATED_DATE)
            .impressionsDuCulte(UPDATED_IMPRESSIONS_DU_CULTE)
            .sexe(UPDATED_SEXE)
            .invitePar(UPDATED_INVITE_PAR);
        return nouveau;
    }

    @BeforeEach
    public void initTest() {
        nouveau = createEntity(em);
    }

    @Test
    @Transactional
    void createNouveau() throws Exception {
        int databaseSizeBeforeCreate = nouveauRepository.findAll().size();
        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);
        restNouveauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nouveauDTO)))
            .andExpect(status().isCreated());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeCreate + 1);
        Nouveau testNouveau = nouveauList.get(nouveauList.size() - 1);
        assertThat(testNouveau.getNomComplet()).isEqualTo(DEFAULT_NOM_COMPLET);
        assertThat(testNouveau.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testNouveau.getTrancheAge()).isEqualTo(DEFAULT_TRANCHE_AGE);
        assertThat(testNouveau.getSituationMatrimoniale()).isEqualTo(DEFAULT_SITUATION_MATRIMONIALE);
        assertThat(testNouveau.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testNouveau.getImpressionsDuCulte()).isEqualTo(DEFAULT_IMPRESSIONS_DU_CULTE);
        assertThat(testNouveau.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testNouveau.getInvitePar()).isEqualTo(DEFAULT_INVITE_PAR);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(1)).save(testNouveau);
    }

    @Test
    @Transactional
    void createNouveauWithExistingId() throws Exception {
        // Create the Nouveau with an existing ID
        nouveau.setId(1L);
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        int databaseSizeBeforeCreate = nouveauRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNouveauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nouveauDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeCreate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void checkNomCompletIsRequired() throws Exception {
        int databaseSizeBeforeTest = nouveauRepository.findAll().size();
        // set the field null
        nouveau.setNomComplet(null);

        // Create the Nouveau, which fails.
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        restNouveauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nouveauDTO)))
            .andExpect(status().isBadRequest());

        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNouveaus() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        // Get all the nouveauList
        restNouveauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nouveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].trancheAge").value(hasItem(DEFAULT_TRANCHE_AGE)))
            .andExpect(jsonPath("$.[*].situationMatrimoniale").value(hasItem(DEFAULT_SITUATION_MATRIMONIALE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].impressionsDuCulte").value(hasItem(DEFAULT_IMPRESSIONS_DU_CULTE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].invitePar").value(hasItem(DEFAULT_INVITE_PAR.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNouveausWithEagerRelationshipsIsEnabled() throws Exception {
        when(nouveauServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNouveauMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nouveauServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNouveausWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nouveauServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNouveauMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nouveauServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getNouveau() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        // Get the nouveau
        restNouveauMockMvc
            .perform(get(ENTITY_API_URL_ID, nouveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nouveau.getId().intValue()))
            .andExpect(jsonPath("$.nomComplet").value(DEFAULT_NOM_COMPLET))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.trancheAge").value(DEFAULT_TRANCHE_AGE))
            .andExpect(jsonPath("$.situationMatrimoniale").value(DEFAULT_SITUATION_MATRIMONIALE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.impressionsDuCulte").value(DEFAULT_IMPRESSIONS_DU_CULTE))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.invitePar").value(DEFAULT_INVITE_PAR.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNouveau() throws Exception {
        // Get the nouveau
        restNouveauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNouveau() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();

        // Update the nouveau
        Nouveau updatedNouveau = nouveauRepository.findById(nouveau.getId()).get();
        // Disconnect from session so that the updates on updatedNouveau are not directly saved in db
        em.detach(updatedNouveau);
        updatedNouveau
            .nomComplet(UPDATED_NOM_COMPLET)
            .contact(UPDATED_CONTACT)
            .trancheAge(UPDATED_TRANCHE_AGE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .date(UPDATED_DATE)
            .impressionsDuCulte(UPDATED_IMPRESSIONS_DU_CULTE)
            .sexe(UPDATED_SEXE)
            .invitePar(UPDATED_INVITE_PAR);
        NouveauDTO nouveauDTO = nouveauMapper.toDto(updatedNouveau);

        restNouveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nouveauDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);
        Nouveau testNouveau = nouveauList.get(nouveauList.size() - 1);
        assertThat(testNouveau.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testNouveau.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testNouveau.getTrancheAge()).isEqualTo(UPDATED_TRANCHE_AGE);
        assertThat(testNouveau.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testNouveau.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNouveau.getImpressionsDuCulte()).isEqualTo(UPDATED_IMPRESSIONS_DU_CULTE);
        assertThat(testNouveau.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testNouveau.getInvitePar()).isEqualTo(UPDATED_INVITE_PAR);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository).save(testNouveau);
    }

    @Test
    @Transactional
    void putNonExistingNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nouveauDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void putWithIdMismatchNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nouveauDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void partialUpdateNouveauWithPatch() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();

        // Update the nouveau using partial update
        Nouveau partialUpdatedNouveau = new Nouveau();
        partialUpdatedNouveau.setId(nouveau.getId());

        partialUpdatedNouveau
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .date(UPDATED_DATE)
            .impressionsDuCulte(UPDATED_IMPRESSIONS_DU_CULTE)
            .sexe(UPDATED_SEXE)
            .invitePar(UPDATED_INVITE_PAR);

        restNouveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNouveau.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNouveau))
            )
            .andExpect(status().isOk());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);
        Nouveau testNouveau = nouveauList.get(nouveauList.size() - 1);
        assertThat(testNouveau.getNomComplet()).isEqualTo(DEFAULT_NOM_COMPLET);
        assertThat(testNouveau.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testNouveau.getTrancheAge()).isEqualTo(DEFAULT_TRANCHE_AGE);
        assertThat(testNouveau.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testNouveau.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNouveau.getImpressionsDuCulte()).isEqualTo(UPDATED_IMPRESSIONS_DU_CULTE);
        assertThat(testNouveau.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testNouveau.getInvitePar()).isEqualTo(UPDATED_INVITE_PAR);
    }

    @Test
    @Transactional
    void fullUpdateNouveauWithPatch() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();

        // Update the nouveau using partial update
        Nouveau partialUpdatedNouveau = new Nouveau();
        partialUpdatedNouveau.setId(nouveau.getId());

        partialUpdatedNouveau
            .nomComplet(UPDATED_NOM_COMPLET)
            .contact(UPDATED_CONTACT)
            .trancheAge(UPDATED_TRANCHE_AGE)
            .situationMatrimoniale(UPDATED_SITUATION_MATRIMONIALE)
            .date(UPDATED_DATE)
            .impressionsDuCulte(UPDATED_IMPRESSIONS_DU_CULTE)
            .sexe(UPDATED_SEXE)
            .invitePar(UPDATED_INVITE_PAR);

        restNouveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNouveau.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNouveau))
            )
            .andExpect(status().isOk());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);
        Nouveau testNouveau = nouveauList.get(nouveauList.size() - 1);
        assertThat(testNouveau.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testNouveau.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testNouveau.getTrancheAge()).isEqualTo(UPDATED_TRANCHE_AGE);
        assertThat(testNouveau.getSituationMatrimoniale()).isEqualTo(UPDATED_SITUATION_MATRIMONIALE);
        assertThat(testNouveau.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNouveau.getImpressionsDuCulte()).isEqualTo(UPDATED_IMPRESSIONS_DU_CULTE);
        assertThat(testNouveau.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testNouveau.getInvitePar()).isEqualTo(UPDATED_INVITE_PAR);
    }

    @Test
    @Transactional
    void patchNonExistingNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nouveauDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNouveau() throws Exception {
        int databaseSizeBeforeUpdate = nouveauRepository.findAll().size();
        nouveau.setId(count.incrementAndGet());

        // Create the Nouveau
        NouveauDTO nouveauDTO = nouveauMapper.toDto(nouveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNouveauMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nouveauDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nouveau in the database
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(0)).save(nouveau);
    }

    @Test
    @Transactional
    void deleteNouveau() throws Exception {
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);

        int databaseSizeBeforeDelete = nouveauRepository.findAll().size();

        // Delete the nouveau
        restNouveauMockMvc
            .perform(delete(ENTITY_API_URL_ID, nouveau.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nouveau> nouveauList = nouveauRepository.findAll();
        assertThat(nouveauList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Nouveau in Elasticsearch
        verify(mockNouveauSearchRepository, times(1)).deleteById(nouveau.getId());
    }

    @Test
    @Transactional
    void searchNouveau() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nouveauRepository.saveAndFlush(nouveau);
        when(mockNouveauSearchRepository.search("id:" + nouveau.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nouveau), PageRequest.of(0, 1), 1));

        // Search the nouveau
        restNouveauMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nouveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nouveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].trancheAge").value(hasItem(DEFAULT_TRANCHE_AGE)))
            .andExpect(jsonPath("$.[*].situationMatrimoniale").value(hasItem(DEFAULT_SITUATION_MATRIMONIALE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].impressionsDuCulte").value(hasItem(DEFAULT_IMPRESSIONS_DU_CULTE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].invitePar").value(hasItem(DEFAULT_INVITE_PAR.toString())));
    }
}

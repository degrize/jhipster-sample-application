package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ImageCulte;
import com.mycompany.myapp.repository.ImageCulteRepository;
import com.mycompany.myapp.repository.search.ImageCulteSearchRepository;
import com.mycompany.myapp.service.dto.ImageCulteDTO;
import com.mycompany.myapp.service.mapper.ImageCulteMapper;
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
 * Integration tests for the {@link ImageCulteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImageCulteResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/image-cultes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/image-cultes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageCulteRepository imageCulteRepository;

    @Autowired
    private ImageCulteMapper imageCulteMapper;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ImageCulteSearchRepositoryMockConfiguration
     */
    @Autowired
    private ImageCulteSearchRepository mockImageCulteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageCulteMockMvc;

    private ImageCulte imageCulte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageCulte createEntity(EntityManager em) {
        ImageCulte imageCulte = new ImageCulte().titre(DEFAULT_TITRE).image(DEFAULT_IMAGE).imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return imageCulte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageCulte createUpdatedEntity(EntityManager em) {
        ImageCulte imageCulte = new ImageCulte().titre(UPDATED_TITRE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return imageCulte;
    }

    @BeforeEach
    public void initTest() {
        imageCulte = createEntity(em);
    }

    @Test
    @Transactional
    void createImageCulte() throws Exception {
        int databaseSizeBeforeCreate = imageCulteRepository.findAll().size();
        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);
        restImageCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageCulteDTO)))
            .andExpect(status().isCreated());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeCreate + 1);
        ImageCulte testImageCulte = imageCulteList.get(imageCulteList.size() - 1);
        assertThat(testImageCulte.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testImageCulte.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImageCulte.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(1)).save(testImageCulte);
    }

    @Test
    @Transactional
    void createImageCulteWithExistingId() throws Exception {
        // Create the ImageCulte with an existing ID
        imageCulte.setId(1L);
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        int databaseSizeBeforeCreate = imageCulteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageCulteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeCreate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageCulteRepository.findAll().size();
        // set the field null
        imageCulte.setTitre(null);

        // Create the ImageCulte, which fails.
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        restImageCulteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageCulteDTO)))
            .andExpect(status().isBadRequest());

        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImageCultes() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        // Get all the imageCulteList
        restImageCulteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageCulte.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getImageCulte() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        // Get the imageCulte
        restImageCulteMockMvc
            .perform(get(ENTITY_API_URL_ID, imageCulte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageCulte.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingImageCulte() throws Exception {
        // Get the imageCulte
        restImageCulteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageCulte() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();

        // Update the imageCulte
        ImageCulte updatedImageCulte = imageCulteRepository.findById(imageCulte.getId()).get();
        // Disconnect from session so that the updates on updatedImageCulte are not directly saved in db
        em.detach(updatedImageCulte);
        updatedImageCulte.titre(UPDATED_TITRE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(updatedImageCulte);

        restImageCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageCulteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);
        ImageCulte testImageCulte = imageCulteList.get(imageCulteList.size() - 1);
        assertThat(testImageCulte.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testImageCulte.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImageCulte.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository).save(testImageCulte);
    }

    @Test
    @Transactional
    void putNonExistingImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageCulteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageCulteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void partialUpdateImageCulteWithPatch() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();

        // Update the imageCulte using partial update
        ImageCulte partialUpdatedImageCulte = new ImageCulte();
        partialUpdatedImageCulte.setId(imageCulte.getId());

        partialUpdatedImageCulte.titre(UPDATED_TITRE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restImageCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageCulte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageCulte))
            )
            .andExpect(status().isOk());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);
        ImageCulte testImageCulte = imageCulteList.get(imageCulteList.size() - 1);
        assertThat(testImageCulte.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testImageCulte.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImageCulte.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateImageCulteWithPatch() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();

        // Update the imageCulte using partial update
        ImageCulte partialUpdatedImageCulte = new ImageCulte();
        partialUpdatedImageCulte.setId(imageCulte.getId());

        partialUpdatedImageCulte.titre(UPDATED_TITRE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restImageCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageCulte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageCulte))
            )
            .andExpect(status().isOk());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);
        ImageCulte testImageCulte = imageCulteList.get(imageCulteList.size() - 1);
        assertThat(testImageCulte.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testImageCulte.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImageCulte.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageCulteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageCulte() throws Exception {
        int databaseSizeBeforeUpdate = imageCulteRepository.findAll().size();
        imageCulte.setId(count.incrementAndGet());

        // Create the ImageCulte
        ImageCulteDTO imageCulteDTO = imageCulteMapper.toDto(imageCulte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageCulteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageCulteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageCulte in the database
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(0)).save(imageCulte);
    }

    @Test
    @Transactional
    void deleteImageCulte() throws Exception {
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);

        int databaseSizeBeforeDelete = imageCulteRepository.findAll().size();

        // Delete the imageCulte
        restImageCulteMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageCulte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageCulte> imageCulteList = imageCulteRepository.findAll();
        assertThat(imageCulteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ImageCulte in Elasticsearch
        verify(mockImageCulteSearchRepository, times(1)).deleteById(imageCulte.getId());
    }

    @Test
    @Transactional
    void searchImageCulte() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        imageCulteRepository.saveAndFlush(imageCulte);
        when(mockImageCulteSearchRepository.search("id:" + imageCulte.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(imageCulte), PageRequest.of(0, 1), 1));

        // Search the imageCulte
        restImageCulteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageCulte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageCulte.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}

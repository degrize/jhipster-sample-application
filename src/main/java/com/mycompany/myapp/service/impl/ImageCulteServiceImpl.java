package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ImageCulte;
import com.mycompany.myapp.repository.ImageCulteRepository;
import com.mycompany.myapp.repository.search.ImageCulteSearchRepository;
import com.mycompany.myapp.service.ImageCulteService;
import com.mycompany.myapp.service.dto.ImageCulteDTO;
import com.mycompany.myapp.service.mapper.ImageCulteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImageCulte}.
 */
@Service
@Transactional
public class ImageCulteServiceImpl implements ImageCulteService {

    private final Logger log = LoggerFactory.getLogger(ImageCulteServiceImpl.class);

    private final ImageCulteRepository imageCulteRepository;

    private final ImageCulteMapper imageCulteMapper;

    private final ImageCulteSearchRepository imageCulteSearchRepository;

    public ImageCulteServiceImpl(
        ImageCulteRepository imageCulteRepository,
        ImageCulteMapper imageCulteMapper,
        ImageCulteSearchRepository imageCulteSearchRepository
    ) {
        this.imageCulteRepository = imageCulteRepository;
        this.imageCulteMapper = imageCulteMapper;
        this.imageCulteSearchRepository = imageCulteSearchRepository;
    }

    @Override
    public ImageCulteDTO save(ImageCulteDTO imageCulteDTO) {
        log.debug("Request to save ImageCulte : {}", imageCulteDTO);
        ImageCulte imageCulte = imageCulteMapper.toEntity(imageCulteDTO);
        imageCulte = imageCulteRepository.save(imageCulte);
        ImageCulteDTO result = imageCulteMapper.toDto(imageCulte);
        imageCulteSearchRepository.save(imageCulte);
        return result;
    }

    @Override
    public Optional<ImageCulteDTO> partialUpdate(ImageCulteDTO imageCulteDTO) {
        log.debug("Request to partially update ImageCulte : {}", imageCulteDTO);

        return imageCulteRepository
            .findById(imageCulteDTO.getId())
            .map(existingImageCulte -> {
                imageCulteMapper.partialUpdate(existingImageCulte, imageCulteDTO);

                return existingImageCulte;
            })
            .map(imageCulteRepository::save)
            .map(savedImageCulte -> {
                imageCulteSearchRepository.save(savedImageCulte);

                return savedImageCulte;
            })
            .map(imageCulteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageCulteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ImageCultes");
        return imageCulteRepository.findAll(pageable).map(imageCulteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageCulteDTO> findOne(Long id) {
        log.debug("Request to get ImageCulte : {}", id);
        return imageCulteRepository.findById(id).map(imageCulteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ImageCulte : {}", id);
        imageCulteRepository.deleteById(id);
        imageCulteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageCulteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ImageCultes for query {}", query);
        return imageCulteSearchRepository.search(query, pageable).map(imageCulteMapper::toDto);
    }
}

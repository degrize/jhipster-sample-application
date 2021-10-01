package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Gem;
import com.mycompany.myapp.repository.GemRepository;
import com.mycompany.myapp.repository.search.GemSearchRepository;
import com.mycompany.myapp.service.GemService;
import com.mycompany.myapp.service.dto.GemDTO;
import com.mycompany.myapp.service.mapper.GemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gem}.
 */
@Service
@Transactional
public class GemServiceImpl implements GemService {

    private final Logger log = LoggerFactory.getLogger(GemServiceImpl.class);

    private final GemRepository gemRepository;

    private final GemMapper gemMapper;

    private final GemSearchRepository gemSearchRepository;

    public GemServiceImpl(GemRepository gemRepository, GemMapper gemMapper, GemSearchRepository gemSearchRepository) {
        this.gemRepository = gemRepository;
        this.gemMapper = gemMapper;
        this.gemSearchRepository = gemSearchRepository;
    }

    @Override
    public GemDTO save(GemDTO gemDTO) {
        log.debug("Request to save Gem : {}", gemDTO);
        Gem gem = gemMapper.toEntity(gemDTO);
        gem = gemRepository.save(gem);
        GemDTO result = gemMapper.toDto(gem);
        gemSearchRepository.save(gem);
        return result;
    }

    @Override
    public Optional<GemDTO> partialUpdate(GemDTO gemDTO) {
        log.debug("Request to partially update Gem : {}", gemDTO);

        return gemRepository
            .findById(gemDTO.getId())
            .map(existingGem -> {
                gemMapper.partialUpdate(existingGem, gemDTO);

                return existingGem;
            })
            .map(gemRepository::save)
            .map(savedGem -> {
                gemSearchRepository.save(savedGem);

                return savedGem;
            })
            .map(gemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Gems");
        return gemRepository.findAll(pageable).map(gemMapper::toDto);
    }

    public Page<GemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gemRepository.findAllWithEagerRelationships(pageable).map(gemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GemDTO> findOne(Long id) {
        log.debug("Request to get Gem : {}", id);
        return gemRepository.findOneWithEagerRelationships(id).map(gemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gem : {}", id);
        gemRepository.deleteById(id);
        gemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Gems for query {}", query);
        return gemSearchRepository.search(query, pageable).map(gemMapper::toDto);
    }
}

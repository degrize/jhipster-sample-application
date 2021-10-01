package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Culte;
import com.mycompany.myapp.repository.CulteRepository;
import com.mycompany.myapp.repository.search.CulteSearchRepository;
import com.mycompany.myapp.service.CulteService;
import com.mycompany.myapp.service.dto.CulteDTO;
import com.mycompany.myapp.service.mapper.CulteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Culte}.
 */
@Service
@Transactional
public class CulteServiceImpl implements CulteService {

    private final Logger log = LoggerFactory.getLogger(CulteServiceImpl.class);

    private final CulteRepository culteRepository;

    private final CulteMapper culteMapper;

    private final CulteSearchRepository culteSearchRepository;

    public CulteServiceImpl(CulteRepository culteRepository, CulteMapper culteMapper, CulteSearchRepository culteSearchRepository) {
        this.culteRepository = culteRepository;
        this.culteMapper = culteMapper;
        this.culteSearchRepository = culteSearchRepository;
    }

    @Override
    public CulteDTO save(CulteDTO culteDTO) {
        log.debug("Request to save Culte : {}", culteDTO);
        Culte culte = culteMapper.toEntity(culteDTO);
        culte = culteRepository.save(culte);
        CulteDTO result = culteMapper.toDto(culte);
        culteSearchRepository.save(culte);
        return result;
    }

    @Override
    public Optional<CulteDTO> partialUpdate(CulteDTO culteDTO) {
        log.debug("Request to partially update Culte : {}", culteDTO);

        return culteRepository
            .findById(culteDTO.getId())
            .map(existingCulte -> {
                culteMapper.partialUpdate(existingCulte, culteDTO);

                return existingCulte;
            })
            .map(culteRepository::save)
            .map(savedCulte -> {
                culteSearchRepository.save(savedCulte);

                return savedCulte;
            })
            .map(culteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CulteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cultes");
        return culteRepository.findAll(pageable).map(culteMapper::toDto);
    }

    public Page<CulteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return culteRepository.findAllWithEagerRelationships(pageable).map(culteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CulteDTO> findOne(Long id) {
        log.debug("Request to get Culte : {}", id);
        return culteRepository.findOneWithEagerRelationships(id).map(culteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Culte : {}", id);
        culteRepository.deleteById(id);
        culteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CulteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cultes for query {}", query);
        return culteSearchRepository.search(query, pageable).map(culteMapper::toDto);
    }
}

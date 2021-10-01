package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Quartier;
import com.mycompany.myapp.repository.QuartierRepository;
import com.mycompany.myapp.repository.search.QuartierSearchRepository;
import com.mycompany.myapp.service.QuartierService;
import com.mycompany.myapp.service.dto.QuartierDTO;
import com.mycompany.myapp.service.mapper.QuartierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Quartier}.
 */
@Service
@Transactional
public class QuartierServiceImpl implements QuartierService {

    private final Logger log = LoggerFactory.getLogger(QuartierServiceImpl.class);

    private final QuartierRepository quartierRepository;

    private final QuartierMapper quartierMapper;

    private final QuartierSearchRepository quartierSearchRepository;

    public QuartierServiceImpl(
        QuartierRepository quartierRepository,
        QuartierMapper quartierMapper,
        QuartierSearchRepository quartierSearchRepository
    ) {
        this.quartierRepository = quartierRepository;
        this.quartierMapper = quartierMapper;
        this.quartierSearchRepository = quartierSearchRepository;
    }

    @Override
    public QuartierDTO save(QuartierDTO quartierDTO) {
        log.debug("Request to save Quartier : {}", quartierDTO);
        Quartier quartier = quartierMapper.toEntity(quartierDTO);
        quartier = quartierRepository.save(quartier);
        QuartierDTO result = quartierMapper.toDto(quartier);
        quartierSearchRepository.save(quartier);
        return result;
    }

    @Override
    public Optional<QuartierDTO> partialUpdate(QuartierDTO quartierDTO) {
        log.debug("Request to partially update Quartier : {}", quartierDTO);

        return quartierRepository
            .findById(quartierDTO.getId())
            .map(existingQuartier -> {
                quartierMapper.partialUpdate(existingQuartier, quartierDTO);

                return existingQuartier;
            })
            .map(quartierRepository::save)
            .map(savedQuartier -> {
                quartierSearchRepository.save(savedQuartier);

                return savedQuartier;
            })
            .map(quartierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuartierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quartiers");
        return quartierRepository.findAll(pageable).map(quartierMapper::toDto);
    }

    public Page<QuartierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return quartierRepository.findAllWithEagerRelationships(pageable).map(quartierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuartierDTO> findOne(Long id) {
        log.debug("Request to get Quartier : {}", id);
        return quartierRepository.findOneWithEagerRelationships(id).map(quartierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Quartier : {}", id);
        quartierRepository.deleteById(id);
        quartierSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuartierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Quartiers for query {}", query);
        return quartierSearchRepository.search(query, pageable).map(quartierMapper::toDto);
    }
}

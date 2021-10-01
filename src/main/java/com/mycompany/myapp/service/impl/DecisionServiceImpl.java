package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Decision;
import com.mycompany.myapp.repository.DecisionRepository;
import com.mycompany.myapp.repository.search.DecisionSearchRepository;
import com.mycompany.myapp.service.DecisionService;
import com.mycompany.myapp.service.dto.DecisionDTO;
import com.mycompany.myapp.service.mapper.DecisionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Decision}.
 */
@Service
@Transactional
public class DecisionServiceImpl implements DecisionService {

    private final Logger log = LoggerFactory.getLogger(DecisionServiceImpl.class);

    private final DecisionRepository decisionRepository;

    private final DecisionMapper decisionMapper;

    private final DecisionSearchRepository decisionSearchRepository;

    public DecisionServiceImpl(
        DecisionRepository decisionRepository,
        DecisionMapper decisionMapper,
        DecisionSearchRepository decisionSearchRepository
    ) {
        this.decisionRepository = decisionRepository;
        this.decisionMapper = decisionMapper;
        this.decisionSearchRepository = decisionSearchRepository;
    }

    @Override
    public DecisionDTO save(DecisionDTO decisionDTO) {
        log.debug("Request to save Decision : {}", decisionDTO);
        Decision decision = decisionMapper.toEntity(decisionDTO);
        decision = decisionRepository.save(decision);
        DecisionDTO result = decisionMapper.toDto(decision);
        decisionSearchRepository.save(decision);
        return result;
    }

    @Override
    public Optional<DecisionDTO> partialUpdate(DecisionDTO decisionDTO) {
        log.debug("Request to partially update Decision : {}", decisionDTO);

        return decisionRepository
            .findById(decisionDTO.getId())
            .map(existingDecision -> {
                decisionMapper.partialUpdate(existingDecision, decisionDTO);

                return existingDecision;
            })
            .map(decisionRepository::save)
            .map(savedDecision -> {
                decisionSearchRepository.save(savedDecision);

                return savedDecision;
            })
            .map(decisionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DecisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Decisions");
        return decisionRepository.findAll(pageable).map(decisionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DecisionDTO> findOne(Long id) {
        log.debug("Request to get Decision : {}", id);
        return decisionRepository.findById(id).map(decisionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Decision : {}", id);
        decisionRepository.deleteById(id);
        decisionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DecisionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Decisions for query {}", query);
        return decisionSearchRepository.search(query, pageable).map(decisionMapper::toDto);
    }
}

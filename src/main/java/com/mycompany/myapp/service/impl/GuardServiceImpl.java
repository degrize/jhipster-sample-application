package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Guard;
import com.mycompany.myapp.repository.GuardRepository;
import com.mycompany.myapp.repository.search.GuardSearchRepository;
import com.mycompany.myapp.service.GuardService;
import com.mycompany.myapp.service.dto.GuardDTO;
import com.mycompany.myapp.service.mapper.GuardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Guard}.
 */
@Service
@Transactional
public class GuardServiceImpl implements GuardService {

    private final Logger log = LoggerFactory.getLogger(GuardServiceImpl.class);

    private final GuardRepository guardRepository;

    private final GuardMapper guardMapper;

    private final GuardSearchRepository guardSearchRepository;

    public GuardServiceImpl(GuardRepository guardRepository, GuardMapper guardMapper, GuardSearchRepository guardSearchRepository) {
        this.guardRepository = guardRepository;
        this.guardMapper = guardMapper;
        this.guardSearchRepository = guardSearchRepository;
    }

    @Override
    public GuardDTO save(GuardDTO guardDTO) {
        log.debug("Request to save Guard : {}", guardDTO);
        Guard guard = guardMapper.toEntity(guardDTO);
        guard = guardRepository.save(guard);
        GuardDTO result = guardMapper.toDto(guard);
        guardSearchRepository.save(guard);
        return result;
    }

    @Override
    public Optional<GuardDTO> partialUpdate(GuardDTO guardDTO) {
        log.debug("Request to partially update Guard : {}", guardDTO);

        return guardRepository
            .findById(guardDTO.getId())
            .map(existingGuard -> {
                guardMapper.partialUpdate(existingGuard, guardDTO);

                return existingGuard;
            })
            .map(guardRepository::save)
            .map(savedGuard -> {
                guardSearchRepository.save(savedGuard);

                return savedGuard;
            })
            .map(guardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Guards");
        return guardRepository.findAll(pageable).map(guardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuardDTO> findOne(Long id) {
        log.debug("Request to get Guard : {}", id);
        return guardRepository.findById(id).map(guardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guard : {}", id);
        guardRepository.deleteById(id);
        guardSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Guards for query {}", query);
        return guardSearchRepository.search(query, pageable).map(guardMapper::toDto);
    }
}

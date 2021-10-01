package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.FrereQuiInvite;
import com.mycompany.myapp.repository.FrereQuiInviteRepository;
import com.mycompany.myapp.repository.search.FrereQuiInviteSearchRepository;
import com.mycompany.myapp.service.FrereQuiInviteService;
import com.mycompany.myapp.service.dto.FrereQuiInviteDTO;
import com.mycompany.myapp.service.mapper.FrereQuiInviteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FrereQuiInvite}.
 */
@Service
@Transactional
public class FrereQuiInviteServiceImpl implements FrereQuiInviteService {

    private final Logger log = LoggerFactory.getLogger(FrereQuiInviteServiceImpl.class);

    private final FrereQuiInviteRepository frereQuiInviteRepository;

    private final FrereQuiInviteMapper frereQuiInviteMapper;

    private final FrereQuiInviteSearchRepository frereQuiInviteSearchRepository;

    public FrereQuiInviteServiceImpl(
        FrereQuiInviteRepository frereQuiInviteRepository,
        FrereQuiInviteMapper frereQuiInviteMapper,
        FrereQuiInviteSearchRepository frereQuiInviteSearchRepository
    ) {
        this.frereQuiInviteRepository = frereQuiInviteRepository;
        this.frereQuiInviteMapper = frereQuiInviteMapper;
        this.frereQuiInviteSearchRepository = frereQuiInviteSearchRepository;
    }

    @Override
    public FrereQuiInviteDTO save(FrereQuiInviteDTO frereQuiInviteDTO) {
        log.debug("Request to save FrereQuiInvite : {}", frereQuiInviteDTO);
        FrereQuiInvite frereQuiInvite = frereQuiInviteMapper.toEntity(frereQuiInviteDTO);
        frereQuiInvite = frereQuiInviteRepository.save(frereQuiInvite);
        FrereQuiInviteDTO result = frereQuiInviteMapper.toDto(frereQuiInvite);
        frereQuiInviteSearchRepository.save(frereQuiInvite);
        return result;
    }

    @Override
    public Optional<FrereQuiInviteDTO> partialUpdate(FrereQuiInviteDTO frereQuiInviteDTO) {
        log.debug("Request to partially update FrereQuiInvite : {}", frereQuiInviteDTO);

        return frereQuiInviteRepository
            .findById(frereQuiInviteDTO.getId())
            .map(existingFrereQuiInvite -> {
                frereQuiInviteMapper.partialUpdate(existingFrereQuiInvite, frereQuiInviteDTO);

                return existingFrereQuiInvite;
            })
            .map(frereQuiInviteRepository::save)
            .map(savedFrereQuiInvite -> {
                frereQuiInviteSearchRepository.save(savedFrereQuiInvite);

                return savedFrereQuiInvite;
            })
            .map(frereQuiInviteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrereQuiInviteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrereQuiInvites");
        return frereQuiInviteRepository.findAll(pageable).map(frereQuiInviteMapper::toDto);
    }

    public Page<FrereQuiInviteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return frereQuiInviteRepository.findAllWithEagerRelationships(pageable).map(frereQuiInviteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrereQuiInviteDTO> findOne(Long id) {
        log.debug("Request to get FrereQuiInvite : {}", id);
        return frereQuiInviteRepository.findOneWithEagerRelationships(id).map(frereQuiInviteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrereQuiInvite : {}", id);
        frereQuiInviteRepository.deleteById(id);
        frereQuiInviteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrereQuiInviteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FrereQuiInvites for query {}", query);
        return frereQuiInviteSearchRepository.search(query, pageable).map(frereQuiInviteMapper::toDto);
    }
}

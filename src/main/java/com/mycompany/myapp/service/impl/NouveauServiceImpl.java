package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Nouveau;
import com.mycompany.myapp.repository.NouveauRepository;
import com.mycompany.myapp.repository.search.NouveauSearchRepository;
import com.mycompany.myapp.service.NouveauService;
import com.mycompany.myapp.service.dto.NouveauDTO;
import com.mycompany.myapp.service.mapper.NouveauMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Nouveau}.
 */
@Service
@Transactional
public class NouveauServiceImpl implements NouveauService {

    private final Logger log = LoggerFactory.getLogger(NouveauServiceImpl.class);

    private final NouveauRepository nouveauRepository;

    private final NouveauMapper nouveauMapper;

    private final NouveauSearchRepository nouveauSearchRepository;

    public NouveauServiceImpl(
        NouveauRepository nouveauRepository,
        NouveauMapper nouveauMapper,
        NouveauSearchRepository nouveauSearchRepository
    ) {
        this.nouveauRepository = nouveauRepository;
        this.nouveauMapper = nouveauMapper;
        this.nouveauSearchRepository = nouveauSearchRepository;
    }

    @Override
    public NouveauDTO save(NouveauDTO nouveauDTO) {
        log.debug("Request to save Nouveau : {}", nouveauDTO);
        Nouveau nouveau = nouveauMapper.toEntity(nouveauDTO);
        nouveau = nouveauRepository.save(nouveau);
        NouveauDTO result = nouveauMapper.toDto(nouveau);
        nouveauSearchRepository.save(nouveau);
        return result;
    }

    @Override
    public Optional<NouveauDTO> partialUpdate(NouveauDTO nouveauDTO) {
        log.debug("Request to partially update Nouveau : {}", nouveauDTO);

        return nouveauRepository
            .findById(nouveauDTO.getId())
            .map(existingNouveau -> {
                nouveauMapper.partialUpdate(existingNouveau, nouveauDTO);

                return existingNouveau;
            })
            .map(nouveauRepository::save)
            .map(savedNouveau -> {
                nouveauSearchRepository.save(savedNouveau);

                return savedNouveau;
            })
            .map(nouveauMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NouveauDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Nouveaus");
        return nouveauRepository.findAll(pageable).map(nouveauMapper::toDto);
    }

    public Page<NouveauDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nouveauRepository.findAllWithEagerRelationships(pageable).map(nouveauMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NouveauDTO> findOne(Long id) {
        log.debug("Request to get Nouveau : {}", id);
        return nouveauRepository.findOneWithEagerRelationships(id).map(nouveauMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Nouveau : {}", id);
        nouveauRepository.deleteById(id);
        nouveauSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NouveauDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Nouveaus for query {}", query);
        return nouveauSearchRepository.search(query, pageable).map(nouveauMapper::toDto);
    }
}

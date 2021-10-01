package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Besoin;
import com.mycompany.myapp.repository.BesoinRepository;
import com.mycompany.myapp.repository.search.BesoinSearchRepository;
import com.mycompany.myapp.service.BesoinService;
import com.mycompany.myapp.service.dto.BesoinDTO;
import com.mycompany.myapp.service.mapper.BesoinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Besoin}.
 */
@Service
@Transactional
public class BesoinServiceImpl implements BesoinService {

    private final Logger log = LoggerFactory.getLogger(BesoinServiceImpl.class);

    private final BesoinRepository besoinRepository;

    private final BesoinMapper besoinMapper;

    private final BesoinSearchRepository besoinSearchRepository;

    public BesoinServiceImpl(BesoinRepository besoinRepository, BesoinMapper besoinMapper, BesoinSearchRepository besoinSearchRepository) {
        this.besoinRepository = besoinRepository;
        this.besoinMapper = besoinMapper;
        this.besoinSearchRepository = besoinSearchRepository;
    }

    @Override
    public BesoinDTO save(BesoinDTO besoinDTO) {
        log.debug("Request to save Besoin : {}", besoinDTO);
        Besoin besoin = besoinMapper.toEntity(besoinDTO);
        besoin = besoinRepository.save(besoin);
        BesoinDTO result = besoinMapper.toDto(besoin);
        besoinSearchRepository.save(besoin);
        return result;
    }

    @Override
    public Optional<BesoinDTO> partialUpdate(BesoinDTO besoinDTO) {
        log.debug("Request to partially update Besoin : {}", besoinDTO);

        return besoinRepository
            .findById(besoinDTO.getId())
            .map(existingBesoin -> {
                besoinMapper.partialUpdate(existingBesoin, besoinDTO);

                return existingBesoin;
            })
            .map(besoinRepository::save)
            .map(savedBesoin -> {
                besoinSearchRepository.save(savedBesoin);

                return savedBesoin;
            })
            .map(besoinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BesoinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Besoins");
        return besoinRepository.findAll(pageable).map(besoinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BesoinDTO> findOne(Long id) {
        log.debug("Request to get Besoin : {}", id);
        return besoinRepository.findById(id).map(besoinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Besoin : {}", id);
        besoinRepository.deleteById(id);
        besoinSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BesoinDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Besoins for query {}", query);
        return besoinSearchRepository.search(query, pageable).map(besoinMapper::toDto);
    }
}

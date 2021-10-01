package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Communaute;
import com.mycompany.myapp.repository.CommunauteRepository;
import com.mycompany.myapp.repository.search.CommunauteSearchRepository;
import com.mycompany.myapp.service.CommunauteService;
import com.mycompany.myapp.service.dto.CommunauteDTO;
import com.mycompany.myapp.service.mapper.CommunauteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Communaute}.
 */
@Service
@Transactional
public class CommunauteServiceImpl implements CommunauteService {

    private final Logger log = LoggerFactory.getLogger(CommunauteServiceImpl.class);

    private final CommunauteRepository communauteRepository;

    private final CommunauteMapper communauteMapper;

    private final CommunauteSearchRepository communauteSearchRepository;

    public CommunauteServiceImpl(
        CommunauteRepository communauteRepository,
        CommunauteMapper communauteMapper,
        CommunauteSearchRepository communauteSearchRepository
    ) {
        this.communauteRepository = communauteRepository;
        this.communauteMapper = communauteMapper;
        this.communauteSearchRepository = communauteSearchRepository;
    }

    @Override
    public CommunauteDTO save(CommunauteDTO communauteDTO) {
        log.debug("Request to save Communaute : {}", communauteDTO);
        Communaute communaute = communauteMapper.toEntity(communauteDTO);
        communaute = communauteRepository.save(communaute);
        CommunauteDTO result = communauteMapper.toDto(communaute);
        communauteSearchRepository.save(communaute);
        return result;
    }

    @Override
    public Optional<CommunauteDTO> partialUpdate(CommunauteDTO communauteDTO) {
        log.debug("Request to partially update Communaute : {}", communauteDTO);

        return communauteRepository
            .findById(communauteDTO.getId())
            .map(existingCommunaute -> {
                communauteMapper.partialUpdate(existingCommunaute, communauteDTO);

                return existingCommunaute;
            })
            .map(communauteRepository::save)
            .map(savedCommunaute -> {
                communauteSearchRepository.save(savedCommunaute);

                return savedCommunaute;
            })
            .map(communauteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunauteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communautes");
        return communauteRepository.findAll(pageable).map(communauteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunauteDTO> findOne(Long id) {
        log.debug("Request to get Communaute : {}", id);
        return communauteRepository.findById(id).map(communauteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Communaute : {}", id);
        communauteRepository.deleteById(id);
        communauteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunauteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Communautes for query {}", query);
        return communauteSearchRepository.search(query, pageable).map(communauteMapper::toDto);
    }
}

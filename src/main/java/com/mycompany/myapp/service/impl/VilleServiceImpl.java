package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Ville;
import com.mycompany.myapp.repository.VilleRepository;
import com.mycompany.myapp.repository.search.VilleSearchRepository;
import com.mycompany.myapp.service.VilleService;
import com.mycompany.myapp.service.dto.VilleDTO;
import com.mycompany.myapp.service.mapper.VilleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ville}.
 */
@Service
@Transactional
public class VilleServiceImpl implements VilleService {

    private final Logger log = LoggerFactory.getLogger(VilleServiceImpl.class);

    private final VilleRepository villeRepository;

    private final VilleMapper villeMapper;

    private final VilleSearchRepository villeSearchRepository;

    public VilleServiceImpl(VilleRepository villeRepository, VilleMapper villeMapper, VilleSearchRepository villeSearchRepository) {
        this.villeRepository = villeRepository;
        this.villeMapper = villeMapper;
        this.villeSearchRepository = villeSearchRepository;
    }

    @Override
    public VilleDTO save(VilleDTO villeDTO) {
        log.debug("Request to save Ville : {}", villeDTO);
        Ville ville = villeMapper.toEntity(villeDTO);
        ville = villeRepository.save(ville);
        VilleDTO result = villeMapper.toDto(ville);
        villeSearchRepository.save(ville);
        return result;
    }

    @Override
    public Optional<VilleDTO> partialUpdate(VilleDTO villeDTO) {
        log.debug("Request to partially update Ville : {}", villeDTO);

        return villeRepository
            .findById(villeDTO.getId())
            .map(existingVille -> {
                villeMapper.partialUpdate(existingVille, villeDTO);

                return existingVille;
            })
            .map(villeRepository::save)
            .map(savedVille -> {
                villeSearchRepository.save(savedVille);

                return savedVille;
            })
            .map(villeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VilleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Villes");
        return villeRepository.findAll(pageable).map(villeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VilleDTO> findOne(Long id) {
        log.debug("Request to get Ville : {}", id);
        return villeRepository.findById(id).map(villeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ville : {}", id);
        villeRepository.deleteById(id);
        villeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VilleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Villes for query {}", query);
        return villeSearchRepository.search(query, pageable).map(villeMapper::toDto);
    }
}

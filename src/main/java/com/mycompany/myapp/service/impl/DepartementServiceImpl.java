package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Departement;
import com.mycompany.myapp.repository.DepartementRepository;
import com.mycompany.myapp.repository.search.DepartementSearchRepository;
import com.mycompany.myapp.service.DepartementService;
import com.mycompany.myapp.service.dto.DepartementDTO;
import com.mycompany.myapp.service.mapper.DepartementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    private final DepartementMapper departementMapper;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementServiceImpl(
        DepartementRepository departementRepository,
        DepartementMapper departementMapper,
        DepartementSearchRepository departementSearchRepository
    ) {
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
        this.departementSearchRepository = departementSearchRepository;
    }

    @Override
    public DepartementDTO save(DepartementDTO departementDTO) {
        log.debug("Request to save Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        DepartementDTO result = departementMapper.toDto(departement);
        departementSearchRepository.save(departement);
        return result;
    }

    @Override
    public Optional<DepartementDTO> partialUpdate(DepartementDTO departementDTO) {
        log.debug("Request to partially update Departement : {}", departementDTO);

        return departementRepository
            .findById(departementDTO.getId())
            .map(existingDepartement -> {
                departementMapper.partialUpdate(existingDepartement, departementDTO);

                return existingDepartement;
            })
            .map(departementRepository::save)
            .map(savedDepartement -> {
                departementSearchRepository.save(savedDepartement);

                return savedDepartement;
            })
            .map(departementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable).map(departementMapper::toDto);
    }

    public Page<DepartementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return departementRepository.findAllWithEagerRelationships(pageable).map(departementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOneWithEagerRelationships(id).map(departementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        departementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Departements for query {}", query);
        return departementSearchRepository.search(query, pageable).map(departementMapper::toDto);
    }
}

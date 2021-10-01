package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DepartementDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring", uses = { ImageCulteMapper.class })
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    @Mapping(target = "imageCultes", source = "imageCultes", qualifiedByName = "titreSet")
    DepartementDTO toDto(Departement s);

    @Mapping(target = "removeImageCulte", ignore = true)
    Departement toEntity(DepartementDTO departementDTO);

    @Named("nomSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    Set<DepartementDTO> toDtoNomSet(Set<Departement> departement);

    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    DepartementDTO toDtoNom(Departement departement);
}

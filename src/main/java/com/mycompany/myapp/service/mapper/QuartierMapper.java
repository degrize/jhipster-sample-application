package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.QuartierDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quartier} and its DTO {@link QuartierDTO}.
 */
@Mapper(componentModel = "spring", uses = { VilleMapper.class })
public interface QuartierMapper extends EntityMapper<QuartierDTO, Quartier> {
    @Mapping(target = "villes", source = "villes", qualifiedByName = "nomSet")
    QuartierDTO toDto(Quartier s);

    @Mapping(target = "removeVille", ignore = true)
    Quartier toEntity(QuartierDTO quartierDTO);

    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    QuartierDTO toDtoNom(Quartier quartier);
}

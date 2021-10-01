package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CommunauteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Communaute} and its DTO {@link CommunauteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommunauteMapper extends EntityMapper<CommunauteDTO, Communaute> {
    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    CommunauteDTO toDtoNom(Communaute communaute);
}

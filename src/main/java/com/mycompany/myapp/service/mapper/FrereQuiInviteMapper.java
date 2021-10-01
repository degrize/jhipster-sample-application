package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FrereQuiInviteDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FrereQuiInvite} and its DTO {@link FrereQuiInviteDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuartierMapper.class, DepartementMapper.class })
public interface FrereQuiInviteMapper extends EntityMapper<FrereQuiInviteDTO, FrereQuiInvite> {
    @Mapping(target = "quartier", source = "quartier", qualifiedByName = "nom")
    @Mapping(target = "departements", source = "departements", qualifiedByName = "nomSet")
    FrereQuiInviteDTO toDto(FrereQuiInvite s);

    @Mapping(target = "removeDepartement", ignore = true)
    FrereQuiInvite toEntity(FrereQuiInviteDTO frereQuiInviteDTO);

    @Named("nomCompletSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomComplet", source = "nomComplet")
    Set<FrereQuiInviteDTO> toDtoNomCompletSet(Set<FrereQuiInvite> frereQuiInvite);
}

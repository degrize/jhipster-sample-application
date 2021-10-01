package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.GemDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gem} and its DTO {@link GemDTO}.
 */
@Mapper(componentModel = "spring", uses = { GuardMapper.class, DepartementMapper.class, FrereQuiInviteMapper.class })
public interface GemMapper extends EntityMapper<GemDTO, Gem> {
    @Mapping(target = "guard", source = "guard", qualifiedByName = "id")
    @Mapping(target = "departement", source = "departement", qualifiedByName = "nom")
    @Mapping(target = "frereQuiInvites", source = "frereQuiInvites", qualifiedByName = "nomCompletSet")
    GemDTO toDto(Gem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GemDTO toDtoId(Gem gem);

    @Mapping(target = "removeFrereQuiInvite", ignore = true)
    Gem toEntity(GemDTO gemDTO);
}

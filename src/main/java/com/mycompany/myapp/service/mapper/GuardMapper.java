package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.GuardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Guard} and its DTO {@link GuardDTO}.
 */
@Mapper(componentModel = "spring", uses = { GemMapper.class })
public interface GuardMapper extends EntityMapper<GuardDTO, Guard> {
    @Mapping(target = "guard", source = "guard", qualifiedByName = "id")
    GuardDTO toDto(Guard s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GuardDTO toDtoId(Guard guard);
}

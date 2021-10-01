package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BesoinDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Besoin} and its DTO {@link BesoinDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BesoinMapper extends EntityMapper<BesoinDTO, Besoin> {
    @Named("besoinSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "besoin", source = "besoin")
    Set<BesoinDTO> toDtoBesoinSet(Set<Besoin> besoin);
}

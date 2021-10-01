package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CulteDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Culte} and its DTO {@link CulteDTO}.
 */
@Mapper(componentModel = "spring", uses = { ImageCulteMapper.class })
public interface CulteMapper extends EntityMapper<CulteDTO, Culte> {
    @Mapping(target = "imageCultes", source = "imageCultes", qualifiedByName = "titreSet")
    CulteDTO toDto(Culte s);

    @Mapping(target = "removeImageCulte", ignore = true)
    Culte toEntity(CulteDTO culteDTO);

    @Named("theme")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "theme", source = "theme")
    CulteDTO toDtoTheme(Culte culte);
}

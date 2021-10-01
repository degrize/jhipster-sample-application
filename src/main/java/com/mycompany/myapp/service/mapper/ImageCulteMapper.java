package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ImageCulteDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageCulte} and its DTO {@link ImageCulteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageCulteMapper extends EntityMapper<ImageCulteDTO, ImageCulte> {
    @Named("titreSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titre", source = "titre")
    Set<ImageCulteDTO> toDtoTitreSet(Set<ImageCulte> imageCulte);
}

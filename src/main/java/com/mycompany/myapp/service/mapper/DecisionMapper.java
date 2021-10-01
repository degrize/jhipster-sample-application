package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DecisionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Decision} and its DTO {@link DecisionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DecisionMapper extends EntityMapper<DecisionDTO, Decision> {
    @Named("decisionSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "decision", source = "decision")
    Set<DecisionDTO> toDtoDecisionSet(Set<Decision> decision);
}

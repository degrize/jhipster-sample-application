package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.NouveauDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nouveau} and its DTO {@link NouveauDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        CommunauteMapper.class,
        VilleMapper.class,
        QuartierMapper.class,
        CulteMapper.class,
        DepartementMapper.class,
        FrereQuiInviteMapper.class,
        BesoinMapper.class,
        DecisionMapper.class,
    }
)
public interface NouveauMapper extends EntityMapper<NouveauDTO, Nouveau> {
    @Mapping(target = "communaute", source = "communaute", qualifiedByName = "nom")
    @Mapping(target = "ville", source = "ville", qualifiedByName = "nom")
    @Mapping(target = "quartier", source = "quartier", qualifiedByName = "nom")
    @Mapping(target = "culte", source = "culte", qualifiedByName = "theme")
    @Mapping(target = "departements", source = "departements", qualifiedByName = "nomSet")
    @Mapping(target = "frereQuiInvites", source = "frereQuiInvites", qualifiedByName = "nomCompletSet")
    @Mapping(target = "besoins", source = "besoins", qualifiedByName = "besoinSet")
    @Mapping(target = "decisions", source = "decisions", qualifiedByName = "decisionSet")
    NouveauDTO toDto(Nouveau s);

    @Mapping(target = "removeDepartement", ignore = true)
    @Mapping(target = "removeFrereQuiInvite", ignore = true)
    @Mapping(target = "removeBesoin", ignore = true)
    @Mapping(target = "removeDecision", ignore = true)
    Nouveau toEntity(NouveauDTO nouveauDTO);
}

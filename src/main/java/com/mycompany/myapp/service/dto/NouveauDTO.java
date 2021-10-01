package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.CanalInvitation;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.SituationMatrimoniale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Nouveau} entity.
 */
@ApiModel(description = "Nouveau entity.\n@author Youth Team.")
public class NouveauDTO implements Serializable {

    private Long id;

    /**
     * Le nom complet name.\n@author Youth Team.
     */
    @NotNull
    @ApiModelProperty(value = "Le nom complet name.\n@author Youth Team.", required = true)
    private String nomComplet;

    private String contact;

    private String trancheAge;

    private SituationMatrimoniale situationMatrimoniale;

    private LocalDate date;

    private String impressionsDuCulte;

    private Sexe sexe;

    private CanalInvitation invitePar;

    private CommunauteDTO communaute;

    private VilleDTO ville;

    private QuartierDTO quartier;

    private CulteDTO culte;

    private Set<DepartementDTO> departements = new HashSet<>();

    private Set<FrereQuiInviteDTO> frereQuiInvites = new HashSet<>();

    private Set<BesoinDTO> besoins = new HashSet<>();

    private Set<DecisionDTO> decisions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTrancheAge() {
        return trancheAge;
    }

    public void setTrancheAge(String trancheAge) {
        this.trancheAge = trancheAge;
    }

    public SituationMatrimoniale getSituationMatrimoniale() {
        return situationMatrimoniale;
    }

    public void setSituationMatrimoniale(SituationMatrimoniale situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImpressionsDuCulte() {
        return impressionsDuCulte;
    }

    public void setImpressionsDuCulte(String impressionsDuCulte) {
        this.impressionsDuCulte = impressionsDuCulte;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public CanalInvitation getInvitePar() {
        return invitePar;
    }

    public void setInvitePar(CanalInvitation invitePar) {
        this.invitePar = invitePar;
    }

    public CommunauteDTO getCommunaute() {
        return communaute;
    }

    public void setCommunaute(CommunauteDTO communaute) {
        this.communaute = communaute;
    }

    public VilleDTO getVille() {
        return ville;
    }

    public void setVille(VilleDTO ville) {
        this.ville = ville;
    }

    public QuartierDTO getQuartier() {
        return quartier;
    }

    public void setQuartier(QuartierDTO quartier) {
        this.quartier = quartier;
    }

    public CulteDTO getCulte() {
        return culte;
    }

    public void setCulte(CulteDTO culte) {
        this.culte = culte;
    }

    public Set<DepartementDTO> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<DepartementDTO> departements) {
        this.departements = departements;
    }

    public Set<FrereQuiInviteDTO> getFrereQuiInvites() {
        return frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInviteDTO> frereQuiInvites) {
        this.frereQuiInvites = frereQuiInvites;
    }

    public Set<BesoinDTO> getBesoins() {
        return besoins;
    }

    public void setBesoins(Set<BesoinDTO> besoins) {
        this.besoins = besoins;
    }

    public Set<DecisionDTO> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<DecisionDTO> decisions) {
        this.decisions = decisions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NouveauDTO)) {
            return false;
        }

        NouveauDTO nouveauDTO = (NouveauDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nouveauDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NouveauDTO{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", contact='" + getContact() + "'" +
            ", trancheAge='" + getTrancheAge() + "'" +
            ", situationMatrimoniale='" + getSituationMatrimoniale() + "'" +
            ", date='" + getDate() + "'" +
            ", impressionsDuCulte='" + getImpressionsDuCulte() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", invitePar='" + getInvitePar() + "'" +
            ", communaute=" + getCommunaute() +
            ", ville=" + getVille() +
            ", quartier=" + getQuartier() +
            ", culte=" + getCulte() +
            ", departements=" + getDepartements() +
            ", frereQuiInvites=" + getFrereQuiInvites() +
            ", besoins=" + getBesoins() +
            ", decisions=" + getDecisions() +
            "}";
    }
}

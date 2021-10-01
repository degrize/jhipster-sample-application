package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Gem} entity.
 */
public class GemDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String annee;

    private GuardDTO guard;

    private DepartementDTO departement;

    private Set<FrereQuiInviteDTO> frereQuiInvites = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public GuardDTO getGuard() {
        return guard;
    }

    public void setGuard(GuardDTO guard) {
        this.guard = guard;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
    }

    public Set<FrereQuiInviteDTO> getFrereQuiInvites() {
        return frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInviteDTO> frereQuiInvites) {
        this.frereQuiInvites = frereQuiInvites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GemDTO)) {
            return false;
        }

        GemDTO gemDTO = (GemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GemDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", annee='" + getAnnee() + "'" +
            ", guard=" + getGuard() +
            ", departement=" + getDepartement() +
            ", frereQuiInvites=" + getFrereQuiInvites() +
            "}";
    }
}

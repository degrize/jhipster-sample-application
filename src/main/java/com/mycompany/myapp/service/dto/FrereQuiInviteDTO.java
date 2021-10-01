package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Sexe;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.FrereQuiInvite} entity.
 */
public class FrereQuiInviteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomComplet;

    private String contact;

    private Sexe sexe;

    private QuartierDTO quartier;

    private Set<DepartementDTO> departements = new HashSet<>();

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

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public QuartierDTO getQuartier() {
        return quartier;
    }

    public void setQuartier(QuartierDTO quartier) {
        this.quartier = quartier;
    }

    public Set<DepartementDTO> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<DepartementDTO> departements) {
        this.departements = departements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrereQuiInviteDTO)) {
            return false;
        }

        FrereQuiInviteDTO frereQuiInviteDTO = (FrereQuiInviteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, frereQuiInviteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrereQuiInviteDTO{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", contact='" + getContact() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", quartier=" + getQuartier() +
            ", departements=" + getDepartements() +
            "}";
    }
}

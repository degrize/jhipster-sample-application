package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Departement} entity.
 */
@ApiModel(description = "Department entity.\n@author Youth Team.")
public class DepartementDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String shortName;

    private String nomResponsable;

    @Lob
    private byte[] videoIntroduction;

    private String videoIntroductionContentType;
    private String contactResponsable;

    private String description;

    private String couleur1;

    private String couleur2;

    private Set<ImageCulteDTO> imageCultes = new HashSet<>();

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public byte[] getVideoIntroduction() {
        return videoIntroduction;
    }

    public void setVideoIntroduction(byte[] videoIntroduction) {
        this.videoIntroduction = videoIntroduction;
    }

    public String getVideoIntroductionContentType() {
        return videoIntroductionContentType;
    }

    public void setVideoIntroductionContentType(String videoIntroductionContentType) {
        this.videoIntroductionContentType = videoIntroductionContentType;
    }

    public String getContactResponsable() {
        return contactResponsable;
    }

    public void setContactResponsable(String contactResponsable) {
        this.contactResponsable = contactResponsable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouleur1() {
        return couleur1;
    }

    public void setCouleur1(String couleur1) {
        this.couleur1 = couleur1;
    }

    public String getCouleur2() {
        return couleur2;
    }

    public void setCouleur2(String couleur2) {
        this.couleur2 = couleur2;
    }

    public Set<ImageCulteDTO> getImageCultes() {
        return imageCultes;
    }

    public void setImageCultes(Set<ImageCulteDTO> imageCultes) {
        this.imageCultes = imageCultes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", videoIntroduction='" + getVideoIntroduction() + "'" +
            ", contactResponsable='" + getContactResponsable() + "'" +
            ", description='" + getDescription() + "'" +
            ", couleur1='" + getCouleur1() + "'" +
            ", couleur2='" + getCouleur2() + "'" +
            ", imageCultes=" + getImageCultes() +
            "}";
    }
}

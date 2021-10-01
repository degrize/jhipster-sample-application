package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImageCulte.
 */
@Entity
@Table(name = "image_culte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imageculte")
public class ImageCulte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @NotNull
    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @ManyToMany(mappedBy = "imageCultes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageCultes", "nouveaus" }, allowSetters = true)
    private Set<Culte> cultes = new HashSet<>();

    @ManyToMany(mappedBy = "imageCultes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageCultes", "gems", "nouveaus", "frereQuiInvites" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageCulte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public ImageCulte titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ImageCulte image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ImageCulte imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Culte> getCultes() {
        return this.cultes;
    }

    public void setCultes(Set<Culte> cultes) {
        if (this.cultes != null) {
            this.cultes.forEach(i -> i.removeImageCulte(this));
        }
        if (cultes != null) {
            cultes.forEach(i -> i.addImageCulte(this));
        }
        this.cultes = cultes;
    }

    public ImageCulte cultes(Set<Culte> cultes) {
        this.setCultes(cultes);
        return this;
    }

    public ImageCulte addCulte(Culte culte) {
        this.cultes.add(culte);
        culte.getImageCultes().add(this);
        return this;
    }

    public ImageCulte removeCulte(Culte culte) {
        this.cultes.remove(culte);
        culte.getImageCultes().remove(this);
        return this;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        if (this.departements != null) {
            this.departements.forEach(i -> i.removeImageCulte(this));
        }
        if (departements != null) {
            departements.forEach(i -> i.addImageCulte(this));
        }
        this.departements = departements;
    }

    public ImageCulte departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public ImageCulte addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.getImageCultes().add(this);
        return this;
    }

    public ImageCulte removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.getImageCultes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageCulte)) {
            return false;
        }
        return id != null && id.equals(((ImageCulte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCulte{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}

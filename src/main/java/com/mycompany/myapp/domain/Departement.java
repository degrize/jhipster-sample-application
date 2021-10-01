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
 * Department entity.\n@author Youth Team.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "departement")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "nom_responsable")
    private String nomResponsable;

    @Lob
    @Column(name = "video_introduction")
    private byte[] videoIntroduction;

    @Column(name = "video_introduction_content_type")
    private String videoIntroductionContentType;

    @Column(name = "contact_responsable")
    private String contactResponsable;

    @Column(name = "description")
    private String description;

    @Column(name = "couleur_1")
    private String couleur1;

    @Column(name = "couleur_2")
    private String couleur2;

    @ManyToMany
    @JoinTable(
        name = "rel_departement__image_culte",
        joinColumns = @JoinColumn(name = "departement_id"),
        inverseJoinColumns = @JoinColumn(name = "image_culte_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cultes", "departements" }, allowSetters = true)
    private Set<ImageCulte> imageCultes = new HashSet<>();

    @OneToMany(mappedBy = "departement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guard", "departement", "frereQuiInvites", "gems" }, allowSetters = true)
    private Set<Gem> gems = new HashSet<>();

    @ManyToMany(mappedBy = "departements")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "communaute", "ville", "quartier", "culte", "departements", "frereQuiInvites", "besoins", "decisions" },
        allowSetters = true
    )
    private Set<Nouveau> nouveaus = new HashSet<>();

    @ManyToMany(mappedBy = "departements")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier", "departements", "nouveaus", "gems" }, allowSetters = true)
    private Set<FrereQuiInvite> frereQuiInvites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Departement nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Departement shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNomResponsable() {
        return this.nomResponsable;
    }

    public Departement nomResponsable(String nomResponsable) {
        this.setNomResponsable(nomResponsable);
        return this;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public byte[] getVideoIntroduction() {
        return this.videoIntroduction;
    }

    public Departement videoIntroduction(byte[] videoIntroduction) {
        this.setVideoIntroduction(videoIntroduction);
        return this;
    }

    public void setVideoIntroduction(byte[] videoIntroduction) {
        this.videoIntroduction = videoIntroduction;
    }

    public String getVideoIntroductionContentType() {
        return this.videoIntroductionContentType;
    }

    public Departement videoIntroductionContentType(String videoIntroductionContentType) {
        this.videoIntroductionContentType = videoIntroductionContentType;
        return this;
    }

    public void setVideoIntroductionContentType(String videoIntroductionContentType) {
        this.videoIntroductionContentType = videoIntroductionContentType;
    }

    public String getContactResponsable() {
        return this.contactResponsable;
    }

    public Departement contactResponsable(String contactResponsable) {
        this.setContactResponsable(contactResponsable);
        return this;
    }

    public void setContactResponsable(String contactResponsable) {
        this.contactResponsable = contactResponsable;
    }

    public String getDescription() {
        return this.description;
    }

    public Departement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouleur1() {
        return this.couleur1;
    }

    public Departement couleur1(String couleur1) {
        this.setCouleur1(couleur1);
        return this;
    }

    public void setCouleur1(String couleur1) {
        this.couleur1 = couleur1;
    }

    public String getCouleur2() {
        return this.couleur2;
    }

    public Departement couleur2(String couleur2) {
        this.setCouleur2(couleur2);
        return this;
    }

    public void setCouleur2(String couleur2) {
        this.couleur2 = couleur2;
    }

    public Set<ImageCulte> getImageCultes() {
        return this.imageCultes;
    }

    public void setImageCultes(Set<ImageCulte> imageCultes) {
        this.imageCultes = imageCultes;
    }

    public Departement imageCultes(Set<ImageCulte> imageCultes) {
        this.setImageCultes(imageCultes);
        return this;
    }

    public Departement addImageCulte(ImageCulte imageCulte) {
        this.imageCultes.add(imageCulte);
        imageCulte.getDepartements().add(this);
        return this;
    }

    public Departement removeImageCulte(ImageCulte imageCulte) {
        this.imageCultes.remove(imageCulte);
        imageCulte.getDepartements().remove(this);
        return this;
    }

    public Set<Gem> getGems() {
        return this.gems;
    }

    public void setGems(Set<Gem> gems) {
        if (this.gems != null) {
            this.gems.forEach(i -> i.setDepartement(null));
        }
        if (gems != null) {
            gems.forEach(i -> i.setDepartement(this));
        }
        this.gems = gems;
    }

    public Departement gems(Set<Gem> gems) {
        this.setGems(gems);
        return this;
    }

    public Departement addGem(Gem gem) {
        this.gems.add(gem);
        gem.setDepartement(this);
        return this;
    }

    public Departement removeGem(Gem gem) {
        this.gems.remove(gem);
        gem.setDepartement(null);
        return this;
    }

    public Set<Nouveau> getNouveaus() {
        return this.nouveaus;
    }

    public void setNouveaus(Set<Nouveau> nouveaus) {
        if (this.nouveaus != null) {
            this.nouveaus.forEach(i -> i.removeDepartement(this));
        }
        if (nouveaus != null) {
            nouveaus.forEach(i -> i.addDepartement(this));
        }
        this.nouveaus = nouveaus;
    }

    public Departement nouveaus(Set<Nouveau> nouveaus) {
        this.setNouveaus(nouveaus);
        return this;
    }

    public Departement addNouveau(Nouveau nouveau) {
        this.nouveaus.add(nouveau);
        nouveau.getDepartements().add(this);
        return this;
    }

    public Departement removeNouveau(Nouveau nouveau) {
        this.nouveaus.remove(nouveau);
        nouveau.getDepartements().remove(this);
        return this;
    }

    public Set<FrereQuiInvite> getFrereQuiInvites() {
        return this.frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        if (this.frereQuiInvites != null) {
            this.frereQuiInvites.forEach(i -> i.removeDepartement(this));
        }
        if (frereQuiInvites != null) {
            frereQuiInvites.forEach(i -> i.addDepartement(this));
        }
        this.frereQuiInvites = frereQuiInvites;
    }

    public Departement frereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.setFrereQuiInvites(frereQuiInvites);
        return this;
    }

    public Departement addFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.add(frereQuiInvite);
        frereQuiInvite.getDepartements().add(this);
        return this;
    }

    public Departement removeFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.remove(frereQuiInvite);
        frereQuiInvite.getDepartements().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", videoIntroduction='" + getVideoIntroduction() + "'" +
            ", videoIntroductionContentType='" + getVideoIntroductionContentType() + "'" +
            ", contactResponsable='" + getContactResponsable() + "'" +
            ", description='" + getDescription() + "'" +
            ", couleur1='" + getCouleur1() + "'" +
            ", couleur2='" + getCouleur2() + "'" +
            "}";
    }
}

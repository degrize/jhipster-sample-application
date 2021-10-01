package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Sexe;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FrereQuiInvite.
 */
@Entity
@Table(name = "frere_qui_invite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "frerequiinvite")
public class FrereQuiInvite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @Column(name = "contact")
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "villes", "frereQuiInvites" }, allowSetters = true)
    private Quartier quartier;

    @ManyToMany
    @JoinTable(
        name = "rel_frere_qui_invite__departement",
        joinColumns = @JoinColumn(name = "frere_qui_invite_id"),
        inverseJoinColumns = @JoinColumn(name = "departement_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageCultes", "gems", "nouveaus", "frereQuiInvites" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    @ManyToMany(mappedBy = "frereQuiInvites")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "communaute", "ville", "quartier", "culte", "departements", "frereQuiInvites", "besoins", "decisions" },
        allowSetters = true
    )
    private Set<Nouveau> nouveaus = new HashSet<>();

    @ManyToMany(mappedBy = "frereQuiInvites")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guard", "departement", "frereQuiInvites", "gems" }, allowSetters = true)
    private Set<Gem> gems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FrereQuiInvite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return this.nomComplet;
    }

    public FrereQuiInvite nomComplet(String nomComplet) {
        this.setNomComplet(nomComplet);
        return this;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getContact() {
        return this.contact;
    }

    public FrereQuiInvite contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public FrereQuiInvite sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public FrereQuiInvite quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    public FrereQuiInvite departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public FrereQuiInvite addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.getFrereQuiInvites().add(this);
        return this;
    }

    public FrereQuiInvite removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.getFrereQuiInvites().remove(this);
        return this;
    }

    public Set<Nouveau> getNouveaus() {
        return this.nouveaus;
    }

    public void setNouveaus(Set<Nouveau> nouveaus) {
        if (this.nouveaus != null) {
            this.nouveaus.forEach(i -> i.removeFrereQuiInvite(this));
        }
        if (nouveaus != null) {
            nouveaus.forEach(i -> i.addFrereQuiInvite(this));
        }
        this.nouveaus = nouveaus;
    }

    public FrereQuiInvite nouveaus(Set<Nouveau> nouveaus) {
        this.setNouveaus(nouveaus);
        return this;
    }

    public FrereQuiInvite addNouveau(Nouveau nouveau) {
        this.nouveaus.add(nouveau);
        nouveau.getFrereQuiInvites().add(this);
        return this;
    }

    public FrereQuiInvite removeNouveau(Nouveau nouveau) {
        this.nouveaus.remove(nouveau);
        nouveau.getFrereQuiInvites().remove(this);
        return this;
    }

    public Set<Gem> getGems() {
        return this.gems;
    }

    public void setGems(Set<Gem> gems) {
        if (this.gems != null) {
            this.gems.forEach(i -> i.removeFrereQuiInvite(this));
        }
        if (gems != null) {
            gems.forEach(i -> i.addFrereQuiInvite(this));
        }
        this.gems = gems;
    }

    public FrereQuiInvite gems(Set<Gem> gems) {
        this.setGems(gems);
        return this;
    }

    public FrereQuiInvite addGem(Gem gem) {
        this.gems.add(gem);
        gem.getFrereQuiInvites().add(this);
        return this;
    }

    public FrereQuiInvite removeGem(Gem gem) {
        this.gems.remove(gem);
        gem.getFrereQuiInvites().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrereQuiInvite)) {
            return false;
        }
        return id != null && id.equals(((FrereQuiInvite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrereQuiInvite{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", contact='" + getContact() + "'" +
            ", sexe='" + getSexe() + "'" +
            "}";
    }
}

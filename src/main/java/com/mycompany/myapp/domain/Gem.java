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
 * A Gem.
 */
@Entity
@Table(name = "gem")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gem")
public class Gem implements Serializable {

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
    @Column(name = "annee", nullable = false)
    private String annee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "guard", "gems" }, allowSetters = true)
    private Guard guard;

    @ManyToOne
    @JsonIgnoreProperties(value = { "imageCultes", "gems", "nouveaus", "frereQuiInvites" }, allowSetters = true)
    private Departement departement;

    @ManyToMany
    @JoinTable(
        name = "rel_gem__frere_qui_invite",
        joinColumns = @JoinColumn(name = "gem_id"),
        inverseJoinColumns = @JoinColumn(name = "frere_qui_invite_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier", "departements", "nouveaus", "gems" }, allowSetters = true)
    private Set<FrereQuiInvite> frereQuiInvites = new HashSet<>();

    @OneToMany(mappedBy = "guard")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guard", "gems" }, allowSetters = true)
    private Set<Guard> gems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Gem nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return this.annee;
    }

    public Gem annee(String annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public Guard getGuard() {
        return this.guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public Gem guard(Guard guard) {
        this.setGuard(guard);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Gem departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public Set<FrereQuiInvite> getFrereQuiInvites() {
        return this.frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.frereQuiInvites = frereQuiInvites;
    }

    public Gem frereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.setFrereQuiInvites(frereQuiInvites);
        return this;
    }

    public Gem addFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.add(frereQuiInvite);
        frereQuiInvite.getGems().add(this);
        return this;
    }

    public Gem removeFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.remove(frereQuiInvite);
        frereQuiInvite.getGems().remove(this);
        return this;
    }

    public Set<Guard> getGems() {
        return this.gems;
    }

    public void setGems(Set<Guard> guards) {
        if (this.gems != null) {
            this.gems.forEach(i -> i.setGuard(null));
        }
        if (guards != null) {
            guards.forEach(i -> i.setGuard(this));
        }
        this.gems = guards;
    }

    public Gem gems(Set<Guard> guards) {
        this.setGems(guards);
        return this;
    }

    public Gem addGem(Guard guard) {
        this.gems.add(guard);
        guard.setGuard(this);
        return this;
    }

    public Gem removeGem(Guard guard) {
        this.gems.remove(guard);
        guard.setGuard(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gem)) {
            return false;
        }
        return id != null && id.equals(((Gem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gem{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", annee='" + getAnnee() + "'" +
            "}";
    }
}

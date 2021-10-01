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
 * not an ignored comment
 */
@Entity
@Table(name = "quartier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "quartier")
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToMany
    @JoinTable(
        name = "rel_quartier__ville",
        joinColumns = @JoinColumn(name = "quartier_id"),
        inverseJoinColumns = @JoinColumn(name = "ville_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartiers" }, allowSetters = true)
    private Set<Ville> villes = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier", "departements", "nouveaus", "gems" }, allowSetters = true)
    private Set<FrereQuiInvite> frereQuiInvites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quartier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Quartier nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Ville> getVilles() {
        return this.villes;
    }

    public void setVilles(Set<Ville> villes) {
        this.villes = villes;
    }

    public Quartier villes(Set<Ville> villes) {
        this.setVilles(villes);
        return this;
    }

    public Quartier addVille(Ville ville) {
        this.villes.add(ville);
        ville.getQuartiers().add(this);
        return this;
    }

    public Quartier removeVille(Ville ville) {
        this.villes.remove(ville);
        ville.getQuartiers().remove(this);
        return this;
    }

    public Set<FrereQuiInvite> getFrereQuiInvites() {
        return this.frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        if (this.frereQuiInvites != null) {
            this.frereQuiInvites.forEach(i -> i.setQuartier(null));
        }
        if (frereQuiInvites != null) {
            frereQuiInvites.forEach(i -> i.setQuartier(this));
        }
        this.frereQuiInvites = frereQuiInvites;
    }

    public Quartier frereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.setFrereQuiInvites(frereQuiInvites);
        return this;
    }

    public Quartier addFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.add(frereQuiInvite);
        frereQuiInvite.setQuartier(this);
        return this;
    }

    public Quartier removeFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.remove(frereQuiInvite);
        frereQuiInvite.setQuartier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quartier)) {
            return false;
        }
        return id != null && id.equals(((Quartier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quartier{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

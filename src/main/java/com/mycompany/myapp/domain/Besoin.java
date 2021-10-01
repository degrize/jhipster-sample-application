package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.BesoinType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Besoin.
 */
@Entity
@Table(name = "besoin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "besoin")
public class Besoin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "besoin", nullable = false)
    private BesoinType besoin;

    @ManyToMany(mappedBy = "besoins")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "communaute", "ville", "quartier", "culte", "departements", "frereQuiInvites", "besoins", "decisions" },
        allowSetters = true
    )
    private Set<Nouveau> nouveaus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Besoin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BesoinType getBesoin() {
        return this.besoin;
    }

    public Besoin besoin(BesoinType besoin) {
        this.setBesoin(besoin);
        return this;
    }

    public void setBesoin(BesoinType besoin) {
        this.besoin = besoin;
    }

    public Set<Nouveau> getNouveaus() {
        return this.nouveaus;
    }

    public void setNouveaus(Set<Nouveau> nouveaus) {
        if (this.nouveaus != null) {
            this.nouveaus.forEach(i -> i.removeBesoin(this));
        }
        if (nouveaus != null) {
            nouveaus.forEach(i -> i.addBesoin(this));
        }
        this.nouveaus = nouveaus;
    }

    public Besoin nouveaus(Set<Nouveau> nouveaus) {
        this.setNouveaus(nouveaus);
        return this;
    }

    public Besoin addNouveau(Nouveau nouveau) {
        this.nouveaus.add(nouveau);
        nouveau.getBesoins().add(this);
        return this;
    }

    public Besoin removeNouveau(Nouveau nouveau) {
        this.nouveaus.remove(nouveau);
        nouveau.getBesoins().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Besoin)) {
            return false;
        }
        return id != null && id.equals(((Besoin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Besoin{" +
            "id=" + getId() +
            ", besoin='" + getBesoin() + "'" +
            "}";
    }
}

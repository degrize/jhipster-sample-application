package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DecisionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Decision.
 */
@Entity
@Table(name = "decision")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "decision")
public class Decision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private DecisionType decision;

    @ManyToMany(mappedBy = "decisions")
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

    public Decision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DecisionType getDecision() {
        return this.decision;
    }

    public Decision decision(DecisionType decision) {
        this.setDecision(decision);
        return this;
    }

    public void setDecision(DecisionType decision) {
        this.decision = decision;
    }

    public Set<Nouveau> getNouveaus() {
        return this.nouveaus;
    }

    public void setNouveaus(Set<Nouveau> nouveaus) {
        if (this.nouveaus != null) {
            this.nouveaus.forEach(i -> i.removeDecision(this));
        }
        if (nouveaus != null) {
            nouveaus.forEach(i -> i.addDecision(this));
        }
        this.nouveaus = nouveaus;
    }

    public Decision nouveaus(Set<Nouveau> nouveaus) {
        this.setNouveaus(nouveaus);
        return this;
    }

    public Decision addNouveau(Nouveau nouveau) {
        this.nouveaus.add(nouveau);
        nouveau.getDecisions().add(this);
        return this;
    }

    public Decision removeNouveau(Nouveau nouveau) {
        this.nouveaus.remove(nouveau);
        nouveau.getDecisions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Decision)) {
            return false;
        }
        return id != null && id.equals(((Decision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Decision{" +
            "id=" + getId() +
            ", decision='" + getDecision() + "'" +
            "}";
    }
}

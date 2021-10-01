package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Guard.
 */
@Entity
@Table(name = "guard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "guard")
public class Guard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "guard", "departement", "frereQuiInvites", "gems" }, allowSetters = true)
    private Gem guard;

    @OneToMany(mappedBy = "guard")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guard", "departement", "frereQuiInvites", "gems" }, allowSetters = true)
    private Set<Gem> gems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Guard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gem getGuard() {
        return this.guard;
    }

    public void setGuard(Gem gem) {
        this.guard = gem;
    }

    public Guard guard(Gem gem) {
        this.setGuard(gem);
        return this;
    }

    public Set<Gem> getGems() {
        return this.gems;
    }

    public void setGems(Set<Gem> gems) {
        if (this.gems != null) {
            this.gems.forEach(i -> i.setGuard(null));
        }
        if (gems != null) {
            gems.forEach(i -> i.setGuard(this));
        }
        this.gems = gems;
    }

    public Guard gems(Set<Gem> gems) {
        this.setGems(gems);
        return this;
    }

    public Guard addGem(Gem gem) {
        this.gems.add(gem);
        gem.setGuard(this);
        return this;
    }

    public Guard removeGem(Gem gem) {
        this.gems.remove(gem);
        gem.setGuard(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Guard)) {
            return false;
        }
        return id != null && id.equals(((Guard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Guard{" +
            "id=" + getId() +
            "}";
    }
}

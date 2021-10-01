package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Culte.
 */
@Entity
@Table(name = "culte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "culte")
public class Culte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "theme", nullable = false)
    private String theme;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(
        name = "rel_culte__image_culte",
        joinColumns = @JoinColumn(name = "culte_id"),
        inverseJoinColumns = @JoinColumn(name = "image_culte_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cultes", "departements" }, allowSetters = true)
    private Set<ImageCulte> imageCultes = new HashSet<>();

    @OneToMany(mappedBy = "culte")
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

    public Culte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return this.theme;
    }

    public Culte theme(String theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Culte date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<ImageCulte> getImageCultes() {
        return this.imageCultes;
    }

    public void setImageCultes(Set<ImageCulte> imageCultes) {
        this.imageCultes = imageCultes;
    }

    public Culte imageCultes(Set<ImageCulte> imageCultes) {
        this.setImageCultes(imageCultes);
        return this;
    }

    public Culte addImageCulte(ImageCulte imageCulte) {
        this.imageCultes.add(imageCulte);
        imageCulte.getCultes().add(this);
        return this;
    }

    public Culte removeImageCulte(ImageCulte imageCulte) {
        this.imageCultes.remove(imageCulte);
        imageCulte.getCultes().remove(this);
        return this;
    }

    public Set<Nouveau> getNouveaus() {
        return this.nouveaus;
    }

    public void setNouveaus(Set<Nouveau> nouveaus) {
        if (this.nouveaus != null) {
            this.nouveaus.forEach(i -> i.setCulte(null));
        }
        if (nouveaus != null) {
            nouveaus.forEach(i -> i.setCulte(this));
        }
        this.nouveaus = nouveaus;
    }

    public Culte nouveaus(Set<Nouveau> nouveaus) {
        this.setNouveaus(nouveaus);
        return this;
    }

    public Culte addNouveau(Nouveau nouveau) {
        this.nouveaus.add(nouveau);
        nouveau.setCulte(this);
        return this;
    }

    public Culte removeNouveau(Nouveau nouveau) {
        this.nouveaus.remove(nouveau);
        nouveau.setCulte(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Culte)) {
            return false;
        }
        return id != null && id.equals(((Culte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Culte{" +
            "id=" + getId() +
            ", theme='" + getTheme() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}

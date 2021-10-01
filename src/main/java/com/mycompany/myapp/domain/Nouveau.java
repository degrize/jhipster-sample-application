package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CanalInvitation;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.SituationMatrimoniale;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Nouveau entity.\n@author Youth Team.
 */
@Entity
@Table(name = "nouveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nouveau")
public class Nouveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Le nom complet name.\n@author Youth Team.
     */
    @NotNull
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @Column(name = "contact")
    private String contact;

    @Column(name = "tranche_age")
    private String trancheAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation_matrimoniale")
    private SituationMatrimoniale situationMatrimoniale;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "impressions_du_culte")
    private String impressionsDuCulte;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @Enumerated(EnumType.STRING)
    @Column(name = "invite_par")
    private CanalInvitation invitePar;

    @ManyToOne
    private Communaute communaute;

    @ManyToOne
    @JsonIgnoreProperties(value = { "quartiers" }, allowSetters = true)
    private Ville ville;

    @ManyToOne
    @JsonIgnoreProperties(value = { "villes", "frereQuiInvites" }, allowSetters = true)
    private Quartier quartier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "imageCultes", "nouveaus" }, allowSetters = true)
    private Culte culte;

    @ManyToMany
    @JoinTable(
        name = "rel_nouveau__departement",
        joinColumns = @JoinColumn(name = "nouveau_id"),
        inverseJoinColumns = @JoinColumn(name = "departement_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageCultes", "gems", "nouveaus", "frereQuiInvites" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_nouveau__frere_qui_invite",
        joinColumns = @JoinColumn(name = "nouveau_id"),
        inverseJoinColumns = @JoinColumn(name = "frere_qui_invite_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier", "departements", "nouveaus", "gems" }, allowSetters = true)
    private Set<FrereQuiInvite> frereQuiInvites = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_nouveau__besoin",
        joinColumns = @JoinColumn(name = "nouveau_id"),
        inverseJoinColumns = @JoinColumn(name = "besoin_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nouveaus" }, allowSetters = true)
    private Set<Besoin> besoins = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_nouveau__decision",
        joinColumns = @JoinColumn(name = "nouveau_id"),
        inverseJoinColumns = @JoinColumn(name = "decision_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nouveaus" }, allowSetters = true)
    private Set<Decision> decisions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nouveau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return this.nomComplet;
    }

    public Nouveau nomComplet(String nomComplet) {
        this.setNomComplet(nomComplet);
        return this;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getContact() {
        return this.contact;
    }

    public Nouveau contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTrancheAge() {
        return this.trancheAge;
    }

    public Nouveau trancheAge(String trancheAge) {
        this.setTrancheAge(trancheAge);
        return this;
    }

    public void setTrancheAge(String trancheAge) {
        this.trancheAge = trancheAge;
    }

    public SituationMatrimoniale getSituationMatrimoniale() {
        return this.situationMatrimoniale;
    }

    public Nouveau situationMatrimoniale(SituationMatrimoniale situationMatrimoniale) {
        this.setSituationMatrimoniale(situationMatrimoniale);
        return this;
    }

    public void setSituationMatrimoniale(SituationMatrimoniale situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Nouveau date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImpressionsDuCulte() {
        return this.impressionsDuCulte;
    }

    public Nouveau impressionsDuCulte(String impressionsDuCulte) {
        this.setImpressionsDuCulte(impressionsDuCulte);
        return this;
    }

    public void setImpressionsDuCulte(String impressionsDuCulte) {
        this.impressionsDuCulte = impressionsDuCulte;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Nouveau sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public CanalInvitation getInvitePar() {
        return this.invitePar;
    }

    public Nouveau invitePar(CanalInvitation invitePar) {
        this.setInvitePar(invitePar);
        return this;
    }

    public void setInvitePar(CanalInvitation invitePar) {
        this.invitePar = invitePar;
    }

    public Communaute getCommunaute() {
        return this.communaute;
    }

    public void setCommunaute(Communaute communaute) {
        this.communaute = communaute;
    }

    public Nouveau communaute(Communaute communaute) {
        this.setCommunaute(communaute);
        return this;
    }

    public Ville getVille() {
        return this.ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Nouveau ville(Ville ville) {
        this.setVille(ville);
        return this;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Nouveau quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    public Culte getCulte() {
        return this.culte;
    }

    public void setCulte(Culte culte) {
        this.culte = culte;
    }

    public Nouveau culte(Culte culte) {
        this.setCulte(culte);
        return this;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    public Nouveau departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public Nouveau addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.getNouveaus().add(this);
        return this;
    }

    public Nouveau removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.getNouveaus().remove(this);
        return this;
    }

    public Set<FrereQuiInvite> getFrereQuiInvites() {
        return this.frereQuiInvites;
    }

    public void setFrereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.frereQuiInvites = frereQuiInvites;
    }

    public Nouveau frereQuiInvites(Set<FrereQuiInvite> frereQuiInvites) {
        this.setFrereQuiInvites(frereQuiInvites);
        return this;
    }

    public Nouveau addFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.add(frereQuiInvite);
        frereQuiInvite.getNouveaus().add(this);
        return this;
    }

    public Nouveau removeFrereQuiInvite(FrereQuiInvite frereQuiInvite) {
        this.frereQuiInvites.remove(frereQuiInvite);
        frereQuiInvite.getNouveaus().remove(this);
        return this;
    }

    public Set<Besoin> getBesoins() {
        return this.besoins;
    }

    public void setBesoins(Set<Besoin> besoins) {
        this.besoins = besoins;
    }

    public Nouveau besoins(Set<Besoin> besoins) {
        this.setBesoins(besoins);
        return this;
    }

    public Nouveau addBesoin(Besoin besoin) {
        this.besoins.add(besoin);
        besoin.getNouveaus().add(this);
        return this;
    }

    public Nouveau removeBesoin(Besoin besoin) {
        this.besoins.remove(besoin);
        besoin.getNouveaus().remove(this);
        return this;
    }

    public Set<Decision> getDecisions() {
        return this.decisions;
    }

    public void setDecisions(Set<Decision> decisions) {
        this.decisions = decisions;
    }

    public Nouveau decisions(Set<Decision> decisions) {
        this.setDecisions(decisions);
        return this;
    }

    public Nouveau addDecision(Decision decision) {
        this.decisions.add(decision);
        decision.getNouveaus().add(this);
        return this;
    }

    public Nouveau removeDecision(Decision decision) {
        this.decisions.remove(decision);
        decision.getNouveaus().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nouveau)) {
            return false;
        }
        return id != null && id.equals(((Nouveau) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nouveau{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", contact='" + getContact() + "'" +
            ", trancheAge='" + getTrancheAge() + "'" +
            ", situationMatrimoniale='" + getSituationMatrimoniale() + "'" +
            ", date='" + getDate() + "'" +
            ", impressionsDuCulte='" + getImpressionsDuCulte() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", invitePar='" + getInvitePar() + "'" +
            "}";
    }
}

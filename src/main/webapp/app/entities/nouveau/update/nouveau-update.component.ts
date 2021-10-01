import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INouveau, Nouveau } from '../nouveau.model';
import { NouveauService } from '../service/nouveau.service';
import { ICommunaute } from 'app/entities/communaute/communaute.model';
import { CommunauteService } from 'app/entities/communaute/service/communaute.service';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { ICulte } from 'app/entities/culte/culte.model';
import { CulteService } from 'app/entities/culte/service/culte.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';
import { FrereQuiInviteService } from 'app/entities/frere-qui-invite/service/frere-qui-invite.service';
import { IBesoin } from 'app/entities/besoin/besoin.model';
import { BesoinService } from 'app/entities/besoin/service/besoin.service';
import { IDecision } from 'app/entities/decision/decision.model';
import { DecisionService } from 'app/entities/decision/service/decision.service';

@Component({
  selector: 'jhi-nouveau-update',
  templateUrl: './nouveau-update.component.html',
})
export class NouveauUpdateComponent implements OnInit {
  isSaving = false;

  communautesSharedCollection: ICommunaute[] = [];
  villesSharedCollection: IVille[] = [];
  quartiersSharedCollection: IQuartier[] = [];
  cultesSharedCollection: ICulte[] = [];
  departementsSharedCollection: IDepartement[] = [];
  frereQuiInvitesSharedCollection: IFrereQuiInvite[] = [];
  besoinsSharedCollection: IBesoin[] = [];
  decisionsSharedCollection: IDecision[] = [];

  editForm = this.fb.group({
    id: [],
    nomComplet: [null, [Validators.required]],
    contact: [],
    trancheAge: [],
    situationMatrimoniale: [],
    date: [],
    impressionsDuCulte: [],
    sexe: [],
    invitePar: [],
    communaute: [],
    ville: [],
    quartier: [],
    culte: [],
    departements: [],
    frereQuiInvites: [],
    besoins: [],
    decisions: [],
  });

  constructor(
    protected nouveauService: NouveauService,
    protected communauteService: CommunauteService,
    protected villeService: VilleService,
    protected quartierService: QuartierService,
    protected culteService: CulteService,
    protected departementService: DepartementService,
    protected frereQuiInviteService: FrereQuiInviteService,
    protected besoinService: BesoinService,
    protected decisionService: DecisionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nouveau }) => {
      this.updateForm(nouveau);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nouveau = this.createFromForm();
    if (nouveau.id !== undefined) {
      this.subscribeToSaveResponse(this.nouveauService.update(nouveau));
    } else {
      this.subscribeToSaveResponse(this.nouveauService.create(nouveau));
    }
  }

  trackCommunauteById(index: number, item: ICommunaute): number {
    return item.id!;
  }

  trackVilleById(index: number, item: IVille): number {
    return item.id!;
  }

  trackQuartierById(index: number, item: IQuartier): number {
    return item.id!;
  }

  trackCulteById(index: number, item: ICulte): number {
    return item.id!;
  }

  trackDepartementById(index: number, item: IDepartement): number {
    return item.id!;
  }

  trackFrereQuiInviteById(index: number, item: IFrereQuiInvite): number {
    return item.id!;
  }

  trackBesoinById(index: number, item: IBesoin): number {
    return item.id!;
  }

  trackDecisionById(index: number, item: IDecision): number {
    return item.id!;
  }

  getSelectedDepartement(option: IDepartement, selectedVals?: IDepartement[]): IDepartement {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedFrereQuiInvite(option: IFrereQuiInvite, selectedVals?: IFrereQuiInvite[]): IFrereQuiInvite {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBesoin(option: IBesoin, selectedVals?: IBesoin[]): IBesoin {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedDecision(option: IDecision, selectedVals?: IDecision[]): IDecision {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INouveau>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(nouveau: INouveau): void {
    this.editForm.patchValue({
      id: nouveau.id,
      nomComplet: nouveau.nomComplet,
      contact: nouveau.contact,
      trancheAge: nouveau.trancheAge,
      situationMatrimoniale: nouveau.situationMatrimoniale,
      date: nouveau.date,
      impressionsDuCulte: nouveau.impressionsDuCulte,
      sexe: nouveau.sexe,
      invitePar: nouveau.invitePar,
      communaute: nouveau.communaute,
      ville: nouveau.ville,
      quartier: nouveau.quartier,
      culte: nouveau.culte,
      departements: nouveau.departements,
      frereQuiInvites: nouveau.frereQuiInvites,
      besoins: nouveau.besoins,
      decisions: nouveau.decisions,
    });

    this.communautesSharedCollection = this.communauteService.addCommunauteToCollectionIfMissing(
      this.communautesSharedCollection,
      nouveau.communaute
    );
    this.villesSharedCollection = this.villeService.addVilleToCollectionIfMissing(this.villesSharedCollection, nouveau.ville);
    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      nouveau.quartier
    );
    this.cultesSharedCollection = this.culteService.addCulteToCollectionIfMissing(this.cultesSharedCollection, nouveau.culte);
    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing(
      this.departementsSharedCollection,
      ...(nouveau.departements ?? [])
    );
    this.frereQuiInvitesSharedCollection = this.frereQuiInviteService.addFrereQuiInviteToCollectionIfMissing(
      this.frereQuiInvitesSharedCollection,
      ...(nouveau.frereQuiInvites ?? [])
    );
    this.besoinsSharedCollection = this.besoinService.addBesoinToCollectionIfMissing(
      this.besoinsSharedCollection,
      ...(nouveau.besoins ?? [])
    );
    this.decisionsSharedCollection = this.decisionService.addDecisionToCollectionIfMissing(
      this.decisionsSharedCollection,
      ...(nouveau.decisions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communauteService
      .query()
      .pipe(map((res: HttpResponse<ICommunaute[]>) => res.body ?? []))
      .pipe(
        map((communautes: ICommunaute[]) =>
          this.communauteService.addCommunauteToCollectionIfMissing(communautes, this.editForm.get('communaute')!.value)
        )
      )
      .subscribe((communautes: ICommunaute[]) => (this.communautesSharedCollection = communautes));

    this.villeService
      .query()
      .pipe(map((res: HttpResponse<IVille[]>) => res.body ?? []))
      .pipe(map((villes: IVille[]) => this.villeService.addVilleToCollectionIfMissing(villes, this.editForm.get('ville')!.value)))
      .subscribe((villes: IVille[]) => (this.villesSharedCollection = villes));

    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('quartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));

    this.culteService
      .query()
      .pipe(map((res: HttpResponse<ICulte[]>) => res.body ?? []))
      .pipe(map((cultes: ICulte[]) => this.culteService.addCulteToCollectionIfMissing(cultes, this.editForm.get('culte')!.value)))
      .subscribe((cultes: ICulte[]) => (this.cultesSharedCollection = cultes));

    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing(departements, ...(this.editForm.get('departements')!.value ?? []))
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));

    this.frereQuiInviteService
      .query()
      .pipe(map((res: HttpResponse<IFrereQuiInvite[]>) => res.body ?? []))
      .pipe(
        map((frereQuiInvites: IFrereQuiInvite[]) =>
          this.frereQuiInviteService.addFrereQuiInviteToCollectionIfMissing(
            frereQuiInvites,
            ...(this.editForm.get('frereQuiInvites')!.value ?? [])
          )
        )
      )
      .subscribe((frereQuiInvites: IFrereQuiInvite[]) => (this.frereQuiInvitesSharedCollection = frereQuiInvites));

    this.besoinService
      .query()
      .pipe(map((res: HttpResponse<IBesoin[]>) => res.body ?? []))
      .pipe(
        map((besoins: IBesoin[]) =>
          this.besoinService.addBesoinToCollectionIfMissing(besoins, ...(this.editForm.get('besoins')!.value ?? []))
        )
      )
      .subscribe((besoins: IBesoin[]) => (this.besoinsSharedCollection = besoins));

    this.decisionService
      .query()
      .pipe(map((res: HttpResponse<IDecision[]>) => res.body ?? []))
      .pipe(
        map((decisions: IDecision[]) =>
          this.decisionService.addDecisionToCollectionIfMissing(decisions, ...(this.editForm.get('decisions')!.value ?? []))
        )
      )
      .subscribe((decisions: IDecision[]) => (this.decisionsSharedCollection = decisions));
  }

  protected createFromForm(): INouveau {
    return {
      ...new Nouveau(),
      id: this.editForm.get(['id'])!.value,
      nomComplet: this.editForm.get(['nomComplet'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      trancheAge: this.editForm.get(['trancheAge'])!.value,
      situationMatrimoniale: this.editForm.get(['situationMatrimoniale'])!.value,
      date: this.editForm.get(['date'])!.value,
      impressionsDuCulte: this.editForm.get(['impressionsDuCulte'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      invitePar: this.editForm.get(['invitePar'])!.value,
      communaute: this.editForm.get(['communaute'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
      culte: this.editForm.get(['culte'])!.value,
      departements: this.editForm.get(['departements'])!.value,
      frereQuiInvites: this.editForm.get(['frereQuiInvites'])!.value,
      besoins: this.editForm.get(['besoins'])!.value,
      decisions: this.editForm.get(['decisions'])!.value,
    };
  }
}

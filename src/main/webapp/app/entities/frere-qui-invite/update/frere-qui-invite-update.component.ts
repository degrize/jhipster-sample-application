import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFrereQuiInvite, FrereQuiInvite } from '../frere-qui-invite.model';
import { FrereQuiInviteService } from '../service/frere-qui-invite.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

@Component({
  selector: 'jhi-frere-qui-invite-update',
  templateUrl: './frere-qui-invite-update.component.html',
})
export class FrereQuiInviteUpdateComponent implements OnInit {
  isSaving = false;

  quartiersSharedCollection: IQuartier[] = [];
  departementsSharedCollection: IDepartement[] = [];

  editForm = this.fb.group({
    id: [],
    nomComplet: [null, [Validators.required]],
    contact: [],
    sexe: [],
    quartier: [],
    departements: [],
  });

  constructor(
    protected frereQuiInviteService: FrereQuiInviteService,
    protected quartierService: QuartierService,
    protected departementService: DepartementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frereQuiInvite }) => {
      this.updateForm(frereQuiInvite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frereQuiInvite = this.createFromForm();
    if (frereQuiInvite.id !== undefined) {
      this.subscribeToSaveResponse(this.frereQuiInviteService.update(frereQuiInvite));
    } else {
      this.subscribeToSaveResponse(this.frereQuiInviteService.create(frereQuiInvite));
    }
  }

  trackQuartierById(index: number, item: IQuartier): number {
    return item.id!;
  }

  trackDepartementById(index: number, item: IDepartement): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrereQuiInvite>>): void {
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

  protected updateForm(frereQuiInvite: IFrereQuiInvite): void {
    this.editForm.patchValue({
      id: frereQuiInvite.id,
      nomComplet: frereQuiInvite.nomComplet,
      contact: frereQuiInvite.contact,
      sexe: frereQuiInvite.sexe,
      quartier: frereQuiInvite.quartier,
      departements: frereQuiInvite.departements,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      frereQuiInvite.quartier
    );
    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing(
      this.departementsSharedCollection,
      ...(frereQuiInvite.departements ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('quartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));

    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing(departements, ...(this.editForm.get('departements')!.value ?? []))
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }

  protected createFromForm(): IFrereQuiInvite {
    return {
      ...new FrereQuiInvite(),
      id: this.editForm.get(['id'])!.value,
      nomComplet: this.editForm.get(['nomComplet'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
      departements: this.editForm.get(['departements'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGem, Gem } from '../gem.model';
import { GemService } from '../service/gem.service';
import { IGuard } from 'app/entities/guard/guard.model';
import { GuardService } from 'app/entities/guard/service/guard.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';
import { FrereQuiInviteService } from 'app/entities/frere-qui-invite/service/frere-qui-invite.service';

@Component({
  selector: 'jhi-gem-update',
  templateUrl: './gem-update.component.html',
})
export class GemUpdateComponent implements OnInit {
  isSaving = false;

  guardsSharedCollection: IGuard[] = [];
  departementsSharedCollection: IDepartement[] = [];
  frereQuiInvitesSharedCollection: IFrereQuiInvite[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    annee: [null, [Validators.required]],
    guard: [],
    departement: [],
    frereQuiInvites: [],
  });

  constructor(
    protected gemService: GemService,
    protected guardService: GuardService,
    protected departementService: DepartementService,
    protected frereQuiInviteService: FrereQuiInviteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gem }) => {
      this.updateForm(gem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gem = this.createFromForm();
    if (gem.id !== undefined) {
      this.subscribeToSaveResponse(this.gemService.update(gem));
    } else {
      this.subscribeToSaveResponse(this.gemService.create(gem));
    }
  }

  trackGuardById(index: number, item: IGuard): number {
    return item.id!;
  }

  trackDepartementById(index: number, item: IDepartement): number {
    return item.id!;
  }

  trackFrereQuiInviteById(index: number, item: IFrereQuiInvite): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGem>>): void {
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

  protected updateForm(gem: IGem): void {
    this.editForm.patchValue({
      id: gem.id,
      nom: gem.nom,
      annee: gem.annee,
      guard: gem.guard,
      departement: gem.departement,
      frereQuiInvites: gem.frereQuiInvites,
    });

    this.guardsSharedCollection = this.guardService.addGuardToCollectionIfMissing(this.guardsSharedCollection, gem.guard);
    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing(
      this.departementsSharedCollection,
      gem.departement
    );
    this.frereQuiInvitesSharedCollection = this.frereQuiInviteService.addFrereQuiInviteToCollectionIfMissing(
      this.frereQuiInvitesSharedCollection,
      ...(gem.frereQuiInvites ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.guardService
      .query()
      .pipe(map((res: HttpResponse<IGuard[]>) => res.body ?? []))
      .pipe(map((guards: IGuard[]) => this.guardService.addGuardToCollectionIfMissing(guards, this.editForm.get('guard')!.value)))
      .subscribe((guards: IGuard[]) => (this.guardsSharedCollection = guards));

    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing(departements, this.editForm.get('departement')!.value)
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
  }

  protected createFromForm(): IGem {
    return {
      ...new Gem(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      guard: this.editForm.get(['guard'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      frereQuiInvites: this.editForm.get(['frereQuiInvites'])!.value,
    };
  }
}

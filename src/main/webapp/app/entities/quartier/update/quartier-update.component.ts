import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuartier, Quartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';

@Component({
  selector: 'jhi-quartier-update',
  templateUrl: './quartier-update.component.html',
})
export class QuartierUpdateComponent implements OnInit {
  isSaving = false;

  villesSharedCollection: IVille[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    villes: [],
  });

  constructor(
    protected quartierService: QuartierService,
    protected villeService: VilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quartier }) => {
      this.updateForm(quartier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quartier = this.createFromForm();
    if (quartier.id !== undefined) {
      this.subscribeToSaveResponse(this.quartierService.update(quartier));
    } else {
      this.subscribeToSaveResponse(this.quartierService.create(quartier));
    }
  }

  trackVilleById(index: number, item: IVille): number {
    return item.id!;
  }

  getSelectedVille(option: IVille, selectedVals?: IVille[]): IVille {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuartier>>): void {
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

  protected updateForm(quartier: IQuartier): void {
    this.editForm.patchValue({
      id: quartier.id,
      nom: quartier.nom,
      villes: quartier.villes,
    });

    this.villesSharedCollection = this.villeService.addVilleToCollectionIfMissing(this.villesSharedCollection, ...(quartier.villes ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.villeService
      .query()
      .pipe(map((res: HttpResponse<IVille[]>) => res.body ?? []))
      .pipe(
        map((villes: IVille[]) => this.villeService.addVilleToCollectionIfMissing(villes, ...(this.editForm.get('villes')!.value ?? [])))
      )
      .subscribe((villes: IVille[]) => (this.villesSharedCollection = villes));
  }

  protected createFromForm(): IQuartier {
    return {
      ...new Quartier(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      villes: this.editForm.get(['villes'])!.value,
    };
  }
}

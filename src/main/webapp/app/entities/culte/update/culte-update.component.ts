import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICulte, Culte } from '../culte.model';
import { CulteService } from '../service/culte.service';
import { IImageCulte } from 'app/entities/image-culte/image-culte.model';
import { ImageCulteService } from 'app/entities/image-culte/service/image-culte.service';

@Component({
  selector: 'jhi-culte-update',
  templateUrl: './culte-update.component.html',
})
export class CulteUpdateComponent implements OnInit {
  isSaving = false;

  imageCultesSharedCollection: IImageCulte[] = [];

  editForm = this.fb.group({
    id: [],
    theme: [null, [Validators.required]],
    date: [null, [Validators.required]],
    imageCultes: [],
  });

  constructor(
    protected culteService: CulteService,
    protected imageCulteService: ImageCulteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ culte }) => {
      this.updateForm(culte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const culte = this.createFromForm();
    if (culte.id !== undefined) {
      this.subscribeToSaveResponse(this.culteService.update(culte));
    } else {
      this.subscribeToSaveResponse(this.culteService.create(culte));
    }
  }

  trackImageCulteById(index: number, item: IImageCulte): number {
    return item.id!;
  }

  getSelectedImageCulte(option: IImageCulte, selectedVals?: IImageCulte[]): IImageCulte {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICulte>>): void {
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

  protected updateForm(culte: ICulte): void {
    this.editForm.patchValue({
      id: culte.id,
      theme: culte.theme,
      date: culte.date,
      imageCultes: culte.imageCultes,
    });

    this.imageCultesSharedCollection = this.imageCulteService.addImageCulteToCollectionIfMissing(
      this.imageCultesSharedCollection,
      ...(culte.imageCultes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imageCulteService
      .query()
      .pipe(map((res: HttpResponse<IImageCulte[]>) => res.body ?? []))
      .pipe(
        map((imageCultes: IImageCulte[]) =>
          this.imageCulteService.addImageCulteToCollectionIfMissing(imageCultes, ...(this.editForm.get('imageCultes')!.value ?? []))
        )
      )
      .subscribe((imageCultes: IImageCulte[]) => (this.imageCultesSharedCollection = imageCultes));
  }

  protected createFromForm(): ICulte {
    return {
      ...new Culte(),
      id: this.editForm.get(['id'])!.value,
      theme: this.editForm.get(['theme'])!.value,
      date: this.editForm.get(['date'])!.value,
      imageCultes: this.editForm.get(['imageCultes'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDepartement, Departement } from '../departement.model';
import { DepartementService } from '../service/departement.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IImageCulte } from 'app/entities/image-culte/image-culte.model';
import { ImageCulteService } from 'app/entities/image-culte/service/image-culte.service';

@Component({
  selector: 'jhi-departement-update',
  templateUrl: './departement-update.component.html',
})
export class DepartementUpdateComponent implements OnInit {
  isSaving = false;

  imageCultesSharedCollection: IImageCulte[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    shortName: [null, [Validators.required]],
    nomResponsable: [],
    videoIntroduction: [],
    videoIntroductionContentType: [],
    contactResponsable: [],
    description: [],
    couleur1: [],
    couleur2: [],
    imageCultes: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected departementService: DepartementService,
    protected imageCulteService: ImageCulteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departement }) => {
      this.updateForm(departement);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departement = this.createFromForm();
    if (departement.id !== undefined) {
      this.subscribeToSaveResponse(this.departementService.update(departement));
    } else {
      this.subscribeToSaveResponse(this.departementService.create(departement));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartement>>): void {
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

  protected updateForm(departement: IDepartement): void {
    this.editForm.patchValue({
      id: departement.id,
      nom: departement.nom,
      shortName: departement.shortName,
      nomResponsable: departement.nomResponsable,
      videoIntroduction: departement.videoIntroduction,
      videoIntroductionContentType: departement.videoIntroductionContentType,
      contactResponsable: departement.contactResponsable,
      description: departement.description,
      couleur1: departement.couleur1,
      couleur2: departement.couleur2,
      imageCultes: departement.imageCultes,
    });

    this.imageCultesSharedCollection = this.imageCulteService.addImageCulteToCollectionIfMissing(
      this.imageCultesSharedCollection,
      ...(departement.imageCultes ?? [])
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

  protected createFromForm(): IDepartement {
    return {
      ...new Departement(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      nomResponsable: this.editForm.get(['nomResponsable'])!.value,
      videoIntroductionContentType: this.editForm.get(['videoIntroductionContentType'])!.value,
      videoIntroduction: this.editForm.get(['videoIntroduction'])!.value,
      contactResponsable: this.editForm.get(['contactResponsable'])!.value,
      description: this.editForm.get(['description'])!.value,
      couleur1: this.editForm.get(['couleur1'])!.value,
      couleur2: this.editForm.get(['couleur2'])!.value,
      imageCultes: this.editForm.get(['imageCultes'])!.value,
    };
  }
}

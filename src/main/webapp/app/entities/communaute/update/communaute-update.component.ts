import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICommunaute, Communaute } from '../communaute.model';
import { CommunauteService } from '../service/communaute.service';

@Component({
  selector: 'jhi-communaute-update',
  templateUrl: './communaute-update.component.html',
})
export class CommunauteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected communauteService: CommunauteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communaute }) => {
      this.updateForm(communaute);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const communaute = this.createFromForm();
    if (communaute.id !== undefined) {
      this.subscribeToSaveResponse(this.communauteService.update(communaute));
    } else {
      this.subscribeToSaveResponse(this.communauteService.create(communaute));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunaute>>): void {
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

  protected updateForm(communaute: ICommunaute): void {
    this.editForm.patchValue({
      id: communaute.id,
      nom: communaute.nom,
    });
  }

  protected createFromForm(): ICommunaute {
    return {
      ...new Communaute(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}

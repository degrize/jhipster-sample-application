import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBesoin, Besoin } from '../besoin.model';
import { BesoinService } from '../service/besoin.service';

@Component({
  selector: 'jhi-besoin-update',
  templateUrl: './besoin-update.component.html',
})
export class BesoinUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    besoin: [null, [Validators.required]],
  });

  constructor(protected besoinService: BesoinService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ besoin }) => {
      this.updateForm(besoin);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const besoin = this.createFromForm();
    if (besoin.id !== undefined) {
      this.subscribeToSaveResponse(this.besoinService.update(besoin));
    } else {
      this.subscribeToSaveResponse(this.besoinService.create(besoin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBesoin>>): void {
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

  protected updateForm(besoin: IBesoin): void {
    this.editForm.patchValue({
      id: besoin.id,
      besoin: besoin.besoin,
    });
  }

  protected createFromForm(): IBesoin {
    return {
      ...new Besoin(),
      id: this.editForm.get(['id'])!.value,
      besoin: this.editForm.get(['besoin'])!.value,
    };
  }
}

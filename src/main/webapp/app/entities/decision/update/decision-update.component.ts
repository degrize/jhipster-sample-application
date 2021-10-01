import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDecision, Decision } from '../decision.model';
import { DecisionService } from '../service/decision.service';

@Component({
  selector: 'jhi-decision-update',
  templateUrl: './decision-update.component.html',
})
export class DecisionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    decision: [null, [Validators.required]],
  });

  constructor(protected decisionService: DecisionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decision }) => {
      this.updateForm(decision);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decision = this.createFromForm();
    if (decision.id !== undefined) {
      this.subscribeToSaveResponse(this.decisionService.update(decision));
    } else {
      this.subscribeToSaveResponse(this.decisionService.create(decision));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecision>>): void {
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

  protected updateForm(decision: IDecision): void {
    this.editForm.patchValue({
      id: decision.id,
      decision: decision.decision,
    });
  }

  protected createFromForm(): IDecision {
    return {
      ...new Decision(),
      id: this.editForm.get(['id'])!.value,
      decision: this.editForm.get(['decision'])!.value,
    };
  }
}

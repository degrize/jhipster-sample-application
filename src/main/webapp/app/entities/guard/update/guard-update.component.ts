import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGuard, Guard } from '../guard.model';
import { GuardService } from '../service/guard.service';
import { IGem } from 'app/entities/gem/gem.model';
import { GemService } from 'app/entities/gem/service/gem.service';

@Component({
  selector: 'jhi-guard-update',
  templateUrl: './guard-update.component.html',
})
export class GuardUpdateComponent implements OnInit {
  isSaving = false;

  gemsSharedCollection: IGem[] = [];

  editForm = this.fb.group({
    id: [],
    guard: [],
  });

  constructor(
    protected guardService: GuardService,
    protected gemService: GemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.updateForm(guard);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guard = this.createFromForm();
    if (guard.id !== undefined) {
      this.subscribeToSaveResponse(this.guardService.update(guard));
    } else {
      this.subscribeToSaveResponse(this.guardService.create(guard));
    }
  }

  trackGemById(index: number, item: IGem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuard>>): void {
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

  protected updateForm(guard: IGuard): void {
    this.editForm.patchValue({
      id: guard.id,
      guard: guard.guard,
    });

    this.gemsSharedCollection = this.gemService.addGemToCollectionIfMissing(this.gemsSharedCollection, guard.guard);
  }

  protected loadRelationshipsOptions(): void {
    this.gemService
      .query()
      .pipe(map((res: HttpResponse<IGem[]>) => res.body ?? []))
      .pipe(map((gems: IGem[]) => this.gemService.addGemToCollectionIfMissing(gems, this.editForm.get('guard')!.value)))
      .subscribe((gems: IGem[]) => (this.gemsSharedCollection = gems));
  }

  protected createFromForm(): IGuard {
    return {
      ...new Guard(),
      id: this.editForm.get(['id'])!.value,
      guard: this.editForm.get(['guard'])!.value,
    };
  }
}

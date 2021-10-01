import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuard } from '../guard.model';

@Component({
  selector: 'jhi-guard-detail',
  templateUrl: './guard-detail.component.html',
})
export class GuardDetailComponent implements OnInit {
  guard: IGuard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.guard = guard;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

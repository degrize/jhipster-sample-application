import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICulte } from '../culte.model';

@Component({
  selector: 'jhi-culte-detail',
  templateUrl: './culte-detail.component.html',
})
export class CulteDetailComponent implements OnInit {
  culte: ICulte | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ culte }) => {
      this.culte = culte;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBesoin } from '../besoin.model';

@Component({
  selector: 'jhi-besoin-detail',
  templateUrl: './besoin-detail.component.html',
})
export class BesoinDetailComponent implements OnInit {
  besoin: IBesoin | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ besoin }) => {
      this.besoin = besoin;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

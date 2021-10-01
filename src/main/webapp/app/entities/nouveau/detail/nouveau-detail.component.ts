import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INouveau } from '../nouveau.model';

@Component({
  selector: 'jhi-nouveau-detail',
  templateUrl: './nouveau-detail.component.html',
})
export class NouveauDetailComponent implements OnInit {
  nouveau: INouveau | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nouveau }) => {
      this.nouveau = nouveau;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

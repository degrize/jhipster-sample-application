import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGem } from '../gem.model';

@Component({
  selector: 'jhi-gem-detail',
  templateUrl: './gem-detail.component.html',
})
export class GemDetailComponent implements OnInit {
  gem: IGem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gem }) => {
      this.gem = gem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

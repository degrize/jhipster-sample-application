import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunaute } from '../communaute.model';

@Component({
  selector: 'jhi-communaute-detail',
  templateUrl: './communaute-detail.component.html',
})
export class CommunauteDetailComponent implements OnInit {
  communaute: ICommunaute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communaute }) => {
      this.communaute = communaute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrereQuiInvite } from '../frere-qui-invite.model';

@Component({
  selector: 'jhi-frere-qui-invite-detail',
  templateUrl: './frere-qui-invite-detail.component.html',
})
export class FrereQuiInviteDetailComponent implements OnInit {
  frereQuiInvite: IFrereQuiInvite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frereQuiInvite }) => {
      this.frereQuiInvite = frereQuiInvite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

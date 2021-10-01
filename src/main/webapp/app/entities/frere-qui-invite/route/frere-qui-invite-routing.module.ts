import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrereQuiInviteComponent } from '../list/frere-qui-invite.component';
import { FrereQuiInviteDetailComponent } from '../detail/frere-qui-invite-detail.component';
import { FrereQuiInviteUpdateComponent } from '../update/frere-qui-invite-update.component';
import { FrereQuiInviteRoutingResolveService } from './frere-qui-invite-routing-resolve.service';

const frereQuiInviteRoute: Routes = [
  {
    path: '',
    component: FrereQuiInviteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrereQuiInviteDetailComponent,
    resolve: {
      frereQuiInvite: FrereQuiInviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrereQuiInviteUpdateComponent,
    resolve: {
      frereQuiInvite: FrereQuiInviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrereQuiInviteUpdateComponent,
    resolve: {
      frereQuiInvite: FrereQuiInviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(frereQuiInviteRoute)],
  exports: [RouterModule],
})
export class FrereQuiInviteRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GuardComponent } from '../list/guard.component';
import { GuardDetailComponent } from '../detail/guard-detail.component';
import { GuardUpdateComponent } from '../update/guard-update.component';
import { GuardRoutingResolveService } from './guard-routing-resolve.service';

const guardRoute: Routes = [
  {
    path: '',
    component: GuardComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GuardDetailComponent,
    resolve: {
      guard: GuardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GuardUpdateComponent,
    resolve: {
      guard: GuardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GuardUpdateComponent,
    resolve: {
      guard: GuardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(guardRoute)],
  exports: [RouterModule],
})
export class GuardRoutingModule {}

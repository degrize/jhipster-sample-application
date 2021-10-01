import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CulteComponent } from '../list/culte.component';
import { CulteDetailComponent } from '../detail/culte-detail.component';
import { CulteUpdateComponent } from '../update/culte-update.component';
import { CulteRoutingResolveService } from './culte-routing-resolve.service';

const culteRoute: Routes = [
  {
    path: '',
    component: CulteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CulteDetailComponent,
    resolve: {
      culte: CulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CulteUpdateComponent,
    resolve: {
      culte: CulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CulteUpdateComponent,
    resolve: {
      culte: CulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(culteRoute)],
  exports: [RouterModule],
})
export class CulteRoutingModule {}

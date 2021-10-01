import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunauteComponent } from '../list/communaute.component';
import { CommunauteDetailComponent } from '../detail/communaute-detail.component';
import { CommunauteUpdateComponent } from '../update/communaute-update.component';
import { CommunauteRoutingResolveService } from './communaute-routing-resolve.service';

const communauteRoute: Routes = [
  {
    path: '',
    component: CommunauteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunauteDetailComponent,
    resolve: {
      communaute: CommunauteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunauteUpdateComponent,
    resolve: {
      communaute: CommunauteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunauteUpdateComponent,
    resolve: {
      communaute: CommunauteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communauteRoute)],
  exports: [RouterModule],
})
export class CommunauteRoutingModule {}

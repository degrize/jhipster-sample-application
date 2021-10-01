import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GemComponent } from '../list/gem.component';
import { GemDetailComponent } from '../detail/gem-detail.component';
import { GemUpdateComponent } from '../update/gem-update.component';
import { GemRoutingResolveService } from './gem-routing-resolve.service';

const gemRoute: Routes = [
  {
    path: '',
    component: GemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GemDetailComponent,
    resolve: {
      gem: GemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GemUpdateComponent,
    resolve: {
      gem: GemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GemUpdateComponent,
    resolve: {
      gem: GemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gemRoute)],
  exports: [RouterModule],
})
export class GemRoutingModule {}

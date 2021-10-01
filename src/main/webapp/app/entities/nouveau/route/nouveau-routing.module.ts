import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NouveauComponent } from '../list/nouveau.component';
import { NouveauDetailComponent } from '../detail/nouveau-detail.component';
import { NouveauUpdateComponent } from '../update/nouveau-update.component';
import { NouveauRoutingResolveService } from './nouveau-routing-resolve.service';

const nouveauRoute: Routes = [
  {
    path: '',
    component: NouveauComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NouveauDetailComponent,
    resolve: {
      nouveau: NouveauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NouveauUpdateComponent,
    resolve: {
      nouveau: NouveauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NouveauUpdateComponent,
    resolve: {
      nouveau: NouveauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nouveauRoute)],
  exports: [RouterModule],
})
export class NouveauRoutingModule {}

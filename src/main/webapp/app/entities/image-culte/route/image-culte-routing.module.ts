import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageCulteComponent } from '../list/image-culte.component';
import { ImageCulteDetailComponent } from '../detail/image-culte-detail.component';
import { ImageCulteUpdateComponent } from '../update/image-culte-update.component';
import { ImageCulteRoutingResolveService } from './image-culte-routing-resolve.service';

const imageCulteRoute: Routes = [
  {
    path: '',
    component: ImageCulteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageCulteDetailComponent,
    resolve: {
      imageCulte: ImageCulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageCulteUpdateComponent,
    resolve: {
      imageCulte: ImageCulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageCulteUpdateComponent,
    resolve: {
      imageCulte: ImageCulteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageCulteRoute)],
  exports: [RouterModule],
})
export class ImageCulteRoutingModule {}

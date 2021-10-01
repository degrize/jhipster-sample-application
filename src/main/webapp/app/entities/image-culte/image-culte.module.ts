import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImageCulteComponent } from './list/image-culte.component';
import { ImageCulteDetailComponent } from './detail/image-culte-detail.component';
import { ImageCulteUpdateComponent } from './update/image-culte-update.component';
import { ImageCulteDeleteDialogComponent } from './delete/image-culte-delete-dialog.component';
import { ImageCulteRoutingModule } from './route/image-culte-routing.module';

@NgModule({
  imports: [SharedModule, ImageCulteRoutingModule],
  declarations: [ImageCulteComponent, ImageCulteDetailComponent, ImageCulteUpdateComponent, ImageCulteDeleteDialogComponent],
  entryComponents: [ImageCulteDeleteDialogComponent],
})
export class ImageCulteModule {}

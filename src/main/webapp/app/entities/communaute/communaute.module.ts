import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommunauteComponent } from './list/communaute.component';
import { CommunauteDetailComponent } from './detail/communaute-detail.component';
import { CommunauteUpdateComponent } from './update/communaute-update.component';
import { CommunauteDeleteDialogComponent } from './delete/communaute-delete-dialog.component';
import { CommunauteRoutingModule } from './route/communaute-routing.module';

@NgModule({
  imports: [SharedModule, CommunauteRoutingModule],
  declarations: [CommunauteComponent, CommunauteDetailComponent, CommunauteUpdateComponent, CommunauteDeleteDialogComponent],
  entryComponents: [CommunauteDeleteDialogComponent],
})
export class CommunauteModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CulteComponent } from './list/culte.component';
import { CulteDetailComponent } from './detail/culte-detail.component';
import { CulteUpdateComponent } from './update/culte-update.component';
import { CulteDeleteDialogComponent } from './delete/culte-delete-dialog.component';
import { CulteRoutingModule } from './route/culte-routing.module';

@NgModule({
  imports: [SharedModule, CulteRoutingModule],
  declarations: [CulteComponent, CulteDetailComponent, CulteUpdateComponent, CulteDeleteDialogComponent],
  entryComponents: [CulteDeleteDialogComponent],
})
export class CulteModule {}

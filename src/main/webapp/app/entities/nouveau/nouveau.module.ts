import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NouveauComponent } from './list/nouveau.component';
import { NouveauDetailComponent } from './detail/nouveau-detail.component';
import { NouveauUpdateComponent } from './update/nouveau-update.component';
import { NouveauDeleteDialogComponent } from './delete/nouveau-delete-dialog.component';
import { NouveauRoutingModule } from './route/nouveau-routing.module';

@NgModule({
  imports: [SharedModule, NouveauRoutingModule],
  declarations: [NouveauComponent, NouveauDetailComponent, NouveauUpdateComponent, NouveauDeleteDialogComponent],
  entryComponents: [NouveauDeleteDialogComponent],
})
export class NouveauModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GemComponent } from './list/gem.component';
import { GemDetailComponent } from './detail/gem-detail.component';
import { GemUpdateComponent } from './update/gem-update.component';
import { GemDeleteDialogComponent } from './delete/gem-delete-dialog.component';
import { GemRoutingModule } from './route/gem-routing.module';

@NgModule({
  imports: [SharedModule, GemRoutingModule],
  declarations: [GemComponent, GemDetailComponent, GemUpdateComponent, GemDeleteDialogComponent],
  entryComponents: [GemDeleteDialogComponent],
})
export class GemModule {}

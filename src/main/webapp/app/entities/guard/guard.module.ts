import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GuardComponent } from './list/guard.component';
import { GuardDetailComponent } from './detail/guard-detail.component';
import { GuardUpdateComponent } from './update/guard-update.component';
import { GuardDeleteDialogComponent } from './delete/guard-delete-dialog.component';
import { GuardRoutingModule } from './route/guard-routing.module';

@NgModule({
  imports: [SharedModule, GuardRoutingModule],
  declarations: [GuardComponent, GuardDetailComponent, GuardUpdateComponent, GuardDeleteDialogComponent],
  entryComponents: [GuardDeleteDialogComponent],
})
export class GuardModule {}

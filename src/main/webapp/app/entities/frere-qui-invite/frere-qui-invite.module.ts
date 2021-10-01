import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FrereQuiInviteComponent } from './list/frere-qui-invite.component';
import { FrereQuiInviteDetailComponent } from './detail/frere-qui-invite-detail.component';
import { FrereQuiInviteUpdateComponent } from './update/frere-qui-invite-update.component';
import { FrereQuiInviteDeleteDialogComponent } from './delete/frere-qui-invite-delete-dialog.component';
import { FrereQuiInviteRoutingModule } from './route/frere-qui-invite-routing.module';

@NgModule({
  imports: [SharedModule, FrereQuiInviteRoutingModule],
  declarations: [
    FrereQuiInviteComponent,
    FrereQuiInviteDetailComponent,
    FrereQuiInviteUpdateComponent,
    FrereQuiInviteDeleteDialogComponent,
  ],
  entryComponents: [FrereQuiInviteDeleteDialogComponent],
})
export class FrereQuiInviteModule {}

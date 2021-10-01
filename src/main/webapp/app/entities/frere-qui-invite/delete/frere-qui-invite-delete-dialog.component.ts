import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrereQuiInvite } from '../frere-qui-invite.model';
import { FrereQuiInviteService } from '../service/frere-qui-invite.service';

@Component({
  templateUrl: './frere-qui-invite-delete-dialog.component.html',
})
export class FrereQuiInviteDeleteDialogComponent {
  frereQuiInvite?: IFrereQuiInvite;

  constructor(protected frereQuiInviteService: FrereQuiInviteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.frereQuiInviteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

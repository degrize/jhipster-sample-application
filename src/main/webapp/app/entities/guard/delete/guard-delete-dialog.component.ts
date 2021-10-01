import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGuard } from '../guard.model';
import { GuardService } from '../service/guard.service';

@Component({
  templateUrl: './guard-delete-dialog.component.html',
})
export class GuardDeleteDialogComponent {
  guard?: IGuard;

  constructor(protected guardService: GuardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.guardService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunaute } from '../communaute.model';
import { CommunauteService } from '../service/communaute.service';

@Component({
  templateUrl: './communaute-delete-dialog.component.html',
})
export class CommunauteDeleteDialogComponent {
  communaute?: ICommunaute;

  constructor(protected communauteService: CommunauteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communauteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
